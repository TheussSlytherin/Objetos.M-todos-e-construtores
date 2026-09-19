public class Usuario {

    // Atributos de instância (todos sem static)
    private String nome;
    private String cpf;
    private String email;
    private int idade;
    private String senha;
    private int qtdLivrosEmprestados;

    // Construtor completo
    public Usuario(String nome, String cpf, String email, int idade, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.idade = idade;
        this.senha = senha;
        this.qtdLivrosEmprestados = 0; // Todo usuário começa com 0 empréstimos
    }

    // Getters para acessar as informações quando necessário
    public String getNome() {
        return nome;
    }

    public int getQtdLivrosEmprestados() {
        return qtdLivrosEmprestados;
    }

    // Métodos de negócio
    public void exibirDados() {
        System.out.println("--- DADOS DO USUÁRIO ---");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("E-mail: " + email);
        System.out.println("Idade: " + idade);
        System.out.println("Livros emprestados no momento: " + qtdLivrosEmprestados);
    }

    public void adicionarLivro() {
        qtdLivrosEmprestados++;
    }

    public void removerLivro() {
        if (qtdLivrosEmprestados > 0) {
            qtdLivrosEmprestados--;
        }
    }
}