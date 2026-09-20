# Entrelinhas

Aplicação de biblioteca com catálogo pesquisado na Open Library. Na interface web é possível criar conta, entrar, pegar livros emprestados e registrar devoluções. Contas e empréstimos são armazenados somente no navegador; as senhas ficam derivadas com PBKDF2, mas isso não substitui autenticação em servidor e não é apropriado para uso em produção.

## Executar a interface

Na raiz do projeto, execute:

```bash
python3 -m http.server 8000 --directory src
```

Abra `http://localhost:8000` no navegador. A busca de livros, as capas e a criação de conta precisam de conexão com a internet para funcionarem corretamente. As contas e os empréstimos ficam salvos apenas no navegador e no dispositivo em que foram criados; use uma senha exclusiva para esta demonstração.

## Executar a aplicação Java

Compile e inicie o aplicativo de terminal usando o driver SQLite incluído:

```bash
mkdir -p out
javac -cp 'lib/*' -d out $(find src -name '*.java')
java -cp 'out:lib/*' main.Main
```
