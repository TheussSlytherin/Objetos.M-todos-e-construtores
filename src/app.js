const API_URL = "https://openlibrary.org/search.json";
const ACCOUNTS_KEY = "entrelinhas-accounts";
const SESSION_KEY = "entrelinhas-session";

const elements = {
  searchForm: document.querySelector("#search-form"),
  searchInput: document.querySelector("#search-input"),
  catalogTab: document.querySelector("#catalog-tab"),
  loansTab: document.querySelector("#loans-tab"),
  catalogView: document.querySelector("#catalog-view"),
  loansView: document.querySelector("#loans-view"),
  sectionTitle: document.querySelector("#section-title"),
  sectionEyebrow: document.querySelector("#section-eyebrow"),
  resultsLabel: document.querySelector("#results-label"),
  feedback: document.querySelector("#feedback"),
  feedbackText: document.querySelector("#feedback-text"),
  bookGrid: document.querySelector("#book-grid"),
  loansGrid: document.querySelector("#loans-grid"),
  loansEmpty: document.querySelector("#loans-empty"),
  loanCount: document.querySelector("#loan-count"),
  exploreButton: document.querySelector("#explore-button"),
  accountName: document.querySelector("#account-name"),
  accountButton: document.querySelector("#account-button"),
  logoutButton: document.querySelector("#logout-button"),
  authModal: document.querySelector("#auth-modal"),
  closeAuth: document.querySelector("#close-auth"),
  loginForm: document.querySelector("#login-form"),
  registerForm: document.querySelector("#register-form"),
  loginError: document.querySelector("#login-error"),
  registerError: document.querySelector("#register-error"),
  authTitle: document.querySelector("#auth-title"),
  authDescription: document.querySelector("#auth-description"),
  showRegister: document.querySelector("#show-register"),
  showLogin: document.querySelector("#show-login"),
  toast: document.querySelector("#toast")
};

let searchController;
let toastTimeout;
let currentQuery = "fiction";
let currentUser = null;
let authPrompt = "";
let lastFocusedElement = null;

function readAccounts() {
  const stored = localStorage.getItem(ACCOUNTS_KEY);
  if (!stored) return [];

  const accounts = JSON.parse(stored);
  if (!Array.isArray(accounts) || accounts.some((account) =>
    typeof account.id !== "string" ||
    typeof account.name !== "string" ||
    typeof account.email !== "string" ||
    typeof account.salt !== "string" ||
    typeof account.passwordHash !== "string"
  )) {
    throw new Error("Os dados das contas estão em um formato inválido.");
  }
  return accounts;
}

function saveAccounts(accounts) {
  localStorage.setItem(ACCOUNTS_KEY, JSON.stringify(accounts));
}

function restoreSession() {
  try {
    const userId = localStorage.getItem(SESSION_KEY);
    if (!userId) return null;
    return readAccounts().find((account) => account.id === userId) || null;
  } catch (error) {
    showToast(error.message || "Não foi possível carregar a conta neste navegador.", true);
    return null;
  }
}

function loansStorageKey() {
  if (!currentUser) throw new Error("Entre na sua conta para acessar os empréstimos.");
  return `entrelinhas-loans:${currentUser.id}`;
}

async function hashPassword(password, salt) {
  if (!globalThis.crypto?.subtle) {
    throw new Error("A autenticação exige uma conexão segura (HTTPS ou localhost).");
  }
  const key = await crypto.subtle.importKey("raw", new TextEncoder().encode(password), "PBKDF2", false, ["deriveBits"]);
  const bits = await crypto.subtle.deriveBits({
    name: "PBKDF2",
    salt: Uint8Array.from(atob(salt), (character) => character.charCodeAt(0)),
    iterations: 600000,
    hash: "SHA-256"
  }, key, 256);
  return btoa(String.fromCharCode(...new Uint8Array(bits)));
}

function createSalt() {
  const bytes = crypto.getRandomValues(new Uint8Array(16));
  return btoa(String.fromCharCode(...bytes));
}

function clearAuthErrors() {
  elements.loginError.hidden = true;
  elements.registerError.hidden = true;
  elements.loginError.textContent = "";
  elements.registerError.textContent = "";
}

function openAuth(mode = "login", message = "") {
  authPrompt = message;
  lastFocusedElement = document.activeElement;
  clearAuthErrors();
  showAuthForm(mode);
  elements.authModal.hidden = false;
  (mode === "register" ? elements.registerForm : elements.loginForm)
    .querySelector("input").focus();
}

function closeAuth() {
  elements.authModal.hidden = true;
  if (lastFocusedElement instanceof HTMLElement) lastFocusedElement.focus();
}

function showAuthForm(mode) {
  const registering = mode === "register";
  elements.loginForm.hidden = registering;
  elements.registerForm.hidden = !registering;
  elements.authTitle.textContent = registering ? "Crie sua conta" : "Bem-vindo de volta";
  elements.authDescription.textContent = authPrompt ||
    (registering ? "Cadastre-se para montar sua estante pessoal." : "Entre para acompanhar seus empréstimos.");
  clearAuthErrors();
}

function setCurrentUser(account) {
  localStorage.setItem(SESSION_KEY, account.id);
  currentUser = account;
  syncAccountUI();
  renderLoans();
  searchBooks(currentQuery);
}

function syncAccountUI() {
  const signedIn = Boolean(currentUser);
  elements.accountButton.hidden = signedIn;
  elements.logoutButton.hidden = !signedIn;
  elements.accountName.hidden = !signedIn;
  elements.accountName.textContent = signedIn ? `Olá, ${currentUser.name}` : "";
  updateLoanCount(getLoans());
}

function readLoans() {
  if (!currentUser) return [];
  const stored = localStorage.getItem(loansStorageKey());
  if (!stored) return [];

  const loans = JSON.parse(stored);
  if (!Array.isArray(loans)) throw new Error("Os empréstimos salvos estão em um formato inválido.");
  return loans;
}

function saveLoans(loans) {
  localStorage.setItem(loansStorageKey(), JSON.stringify(loans));
}

function getLoans() {
  try {
    return readLoans();
  } catch (error) {
    showToast(error.message || "Não foi possível acessar seus empréstimos.", true);
    return [];
  }
}

function setFeedback(message, state = "") {
  elements.feedback.className = `feedback is-visible${state ? ` ${state}` : ""}`;
  elements.feedbackText.textContent = message;
}

function clearFeedback() {
  elements.feedback.className = "feedback";
  elements.feedbackText.textContent = "";
}

function showToast(message, isError = false) {
  window.clearTimeout(toastTimeout);
  elements.toast.textContent = message;
  elements.toast.className = `toast is-visible${isError ? " is-error" : ""}`;
  toastTimeout = window.setTimeout(() => {
    elements.toast.className = "toast";
  }, 3000);
}

function setView(view) {
  const showingLoans = view === "loans";
  if (showingLoans && !currentUser) {
    openAuth("login", "Entre na sua conta para ver seus empréstimos.");
    return;
  }
  elements.catalogView.hidden = showingLoans;
  elements.loansView.hidden = !showingLoans;
  elements.catalogTab.classList.toggle("is-active", !showingLoans);
  elements.loansTab.classList.toggle("is-active", showingLoans);
  elements.catalogTab.toggleAttribute("aria-current", !showingLoans);
  elements.loansTab.toggleAttribute("aria-current", showingLoans);
  if (showingLoans) renderLoans();
}

function makeCover(book, title) {
  const wrapper = document.createElement("div");
  wrapper.className = "cover-wrap";
  if (book.cover_i) {
    const image = document.createElement("img");
    image.className = "book-cover";
    image.src = `https://covers.openlibrary.org/b/id/${encodeURIComponent(book.cover_i)}-M.jpg`;
    image.alt = `Capa de ${title}`;
    image.loading = "lazy";
    image.addEventListener("error", () => {
      image.remove();
      addCoverFallback(wrapper, title);
    }, { once: true });
    wrapper.append(image);
  } else {
    addCoverFallback(wrapper, title);
  }
  return wrapper;
}

function addCoverFallback(wrapper, title) {
  const fallback = document.createElement("div");
  fallback.className = "cover-fallback";
  fallback.textContent = title;
  wrapper.prepend(fallback);
}

function makeBookCard(book, isLoan = false) {
  const title = book.title || "Título não informado";
  const author = Array.isArray(book.author_name) && book.author_name.length
    ? book.author_name.slice(0, 2).join(", ")
    : (book.author || "Autor não informado");
  const id = book.key || book.id;
  const card = document.createElement("article");
  card.className = "book-card";
  card.append(makeCover(book, title));

  const info = document.createElement("div");
  info.className = "book-info";
  const heading = document.createElement("h3");
  heading.className = "book-title";
  heading.textContent = title;
  const byline = document.createElement("p");
  byline.className = "book-author";
  byline.textContent = author;
  const meta = document.createElement("div");
  meta.className = "book-meta";
  const year = document.createElement("span");
  year.className = "book-year";
  year.textContent = book.first_publish_year ? `Publicado em ${book.first_publish_year}` : "Ano não informado";
  const action = document.createElement("button");
  action.className = "action-button";
  action.type = "button";

  if (isLoan) {
    action.textContent = "Devolver";
    action.addEventListener("click", () => returnBook(id));
  } else {
    const borrowed = getLoans().some((loan) => loan.id === id);
    action.textContent = borrowed ? "Já emprestado" : currentUser ? "Pegar emprestado" : "Entrar para emprestar";
    action.disabled = borrowed || !id;
    action.addEventListener("click", () => {
      if (!currentUser) {
        openAuth("login", "Entre ou crie uma conta para pegar livros emprestados.");
        return;
      }
      borrowBook(book, id);
    });
  }

  meta.append(year, action);
  info.append(heading, byline, meta);
  card.append(info);
  return card;
}

async function searchBooks(query) {
  const normalizedQuery = query.trim();
  if (!normalizedQuery) {
    elements.searchInput.focus();
    return;
  }
  currentQuery = normalizedQuery;

  if (searchController) searchController.abort();
  searchController = new AbortController();
  elements.bookGrid.replaceChildren();
  elements.resultsLabel.textContent = "";
  elements.sectionTitle.textContent = `Resultados para “${normalizedQuery}”`;
  elements.sectionEyebrow.textContent = "CATÁLOGO OPEN LIBRARY";
  setFeedback("Buscando livros...", "is-loading");

  const params = new URLSearchParams({
    q: normalizedQuery,
    limit: "12",
    fields: "key,title,author_name,first_publish_year,cover_i"
  });

  try {
    const response = await fetch(`${API_URL}?${params}`, {
      headers: { Accept: "application/json" },
      signal: searchController.signal
    });
    if (!response.ok) throw new Error(`A Open Library respondeu com erro (${response.status}).`);

    const data = await response.json();
    const books = Array.isArray(data.docs) ? data.docs : [];
    clearFeedback();
    if (!books.length) {
      setFeedback("Nenhum livro encontrado. Tente outro título ou autor.");
      return;
    }

    const fragment = document.createDocumentFragment();
    books.forEach((book) => fragment.append(makeBookCard(book)));
    elements.bookGrid.append(fragment);
    elements.resultsLabel.textContent = `${books.length} livros encontrados`;
  } catch (error) {
    if (error.name === "AbortError") return;
    setFeedback(error.message || "Não foi possível buscar livros agora. Tente novamente.", "is-error");
  }
}

function updateLoanCount(loans) {
  elements.loanCount.textContent = String(loans.length);
}

function renderLoans() {
  const loans = getLoans();
  updateLoanCount(loans);
  elements.loansGrid.replaceChildren();
  elements.loansEmpty.hidden = loans.length > 0;
  if (!loans.length) return;

  const fragment = document.createDocumentFragment();
  loans.forEach((loan) => fragment.append(makeBookCard(loan, true)));
  elements.loansGrid.append(fragment);
}

function borrowBook(book, id) {
  if (!currentUser) {
    openAuth("login", "Entre ou crie uma conta para pegar livros emprestados.");
    return;
  }
  if (!id) {
    showToast("Este livro não possui identificador para empréstimo.", true);
    return;
  }
  try {
    const loans = readLoans();
    if (loans.some((loan) => loan.id === id)) {
      showToast("Este livro já está nos seus empréstimos.");
      return;
    }
    loans.push({
      id,
      key: book.key,
      title: book.title,
      author_name: book.author_name,
      author: book.author,
      first_publish_year: book.first_publish_year,
      cover_i: book.cover_i
    });
    saveLoans(loans);
    updateLoanCount(loans);
    searchBooks(currentQuery);
    showToast(`“${book.title}” foi adicionado aos seus empréstimos.`);
  } catch (error) {
    showToast(error.message || "Não foi possível salvar este empréstimo no navegador.", true);
  }
}

function returnBook(id) {
  try {
    const loans = readLoans();
    const updatedLoans = loans.filter((loan) => loan.id !== id);
    saveLoans(updatedLoans);
    renderLoans();
    showToast("Livro devolvido com sucesso.");
  } catch (error) {
    showToast(error.message || "Não foi possível registrar a devolução.", true);
  }
}

function showAuthError(element, message) {
  element.textContent = message;
  element.hidden = false;
}

elements.loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAuthErrors();
  const formData = new FormData(elements.loginForm);
  const email = String(formData.get("email")).trim().toLowerCase();
  const password = String(formData.get("password"));
  const submitButton = elements.loginForm.querySelector('button[type="submit"]');
  submitButton.disabled = true;
  submitButton.textContent = "Entrando...";

  try {
    const account = readAccounts().find((candidate) => candidate.email === email);
    if (!account || await hashPassword(password, account.salt) !== account.passwordHash) {
      showAuthError(elements.loginError, "E-mail ou senha inválidos.");
      return;
    }
    setCurrentUser(account);
    elements.loginForm.reset();
    closeAuth();
    showToast(`Bem-vindo(a), ${account.name}!`);
  } catch (error) {
    showAuthError(elements.loginError, error.message || "Não foi possível entrar. Tente novamente.");
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = "Entrar";
  }
});

elements.registerForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAuthErrors();
  const formData = new FormData(elements.registerForm);
  const name = String(formData.get("name")).trim();
  const email = String(formData.get("email")).trim().toLowerCase();
  const password = String(formData.get("password"));
  const submitButton = elements.registerForm.querySelector('button[type="submit"]');

  if (name.length < 2) {
    showAuthError(elements.registerError, "Informe um nome com pelo menos 2 caracteres.");
    return;
  }
  if (password.length < 8) {
    showAuthError(elements.registerError, "A senha precisa ter pelo menos 8 caracteres.");
    return;
  }

  submitButton.disabled = true;
  submitButton.textContent = "Criando conta...";
  try {
    const accounts = readAccounts();
    if (accounts.some((account) => account.email === email)) {
      showAuthError(elements.registerError, "Já existe uma conta com este e-mail. Faça login.");
      return;
    }
    const salt = createSalt();
    const account = {
      id: crypto.randomUUID(),
      name,
      email,
      salt,
      passwordHash: await hashPassword(password, salt)
    };
    accounts.push(account);
    saveAccounts(accounts);
    setCurrentUser(account);
    elements.registerForm.reset();
    closeAuth();
    showToast(`Conta criada. Boas-vindas, ${account.name}!`);
  } catch (error) {
    showAuthError(elements.registerError, error.message || "Não foi possível criar sua conta.");
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = "Criar conta";
  }
});

elements.searchForm.addEventListener("submit", (event) => {
  event.preventDefault();
  setView("catalog");
  searchBooks(elements.searchInput.value);
});
elements.catalogTab.addEventListener("click", () => setView("catalog"));
elements.loansTab.addEventListener("click", () => setView("loans"));
elements.exploreButton.addEventListener("click", () => setView("catalog"));
elements.accountButton.addEventListener("click", () => openAuth("login"));
elements.logoutButton.addEventListener("click", () => {
  localStorage.removeItem(SESSION_KEY);
  currentUser = null;
  if (!elements.loansView.hidden) setView("catalog");
  syncAccountUI();
  searchBooks(currentQuery);
  showToast("Você saiu da sua conta.");
});
elements.closeAuth.addEventListener("click", closeAuth);
elements.showRegister.addEventListener("click", () => showAuthForm("register"));
elements.showLogin.addEventListener("click", () => showAuthForm("login"));
elements.authModal.addEventListener("click", (event) => {
  if (event.target === elements.authModal) closeAuth();
});
document.addEventListener("keydown", (event) => {
  if (event.key === "Escape" && !elements.authModal.hidden) closeAuth();
});

currentUser = restoreSession();
syncAccountUI();
renderLoans();
elements.searchInput.value = "ficção";
searchBooks("fiction");
