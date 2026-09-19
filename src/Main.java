public class Main {
    public static void main(String[] args) {
        // Garante que o arquivo do banco e as tabelas sejam criados ao iniciar
        Conexao.criarTabelas();

        // Inicia a aplicação
        Login login = new Login();
        login.escolha();
    }
}