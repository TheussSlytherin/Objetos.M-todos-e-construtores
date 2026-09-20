/*

# 1. Compila as alterações feitas no código para execução das atualizações
javac -cp "lib/*" -d src src/*.java

# 2. Executa a aplicação.
java --enable-native-access=ALL-UNNAMED -cp "src:lib/*" Main

 */

public class Main {
    public static void main(String[] args) {
        // Garante que o arquivo do banco e as tabelas sejam criados ao iniciar
        Conexao.criarTabelas();

        // Inicia a aplicação
        Login login = new Login();
        login.escolha();
    }
}