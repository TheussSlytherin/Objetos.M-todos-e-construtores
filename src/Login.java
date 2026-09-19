import java.util.Scanner;

public class Login {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Pede as credenciais do usuário e repete até que sejam válidas no banco de dados.
     */
    private void fazerLogin(Scanner scanner) {
        String nomeUsuario;
        String senha;

        do {
            System.out.print("Digite o seu nome de usuário: ");
            nomeUsuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            senha = scanner.nextLine();

            // Consulta o SQLite através do UsuarioDAO
            if (usuarioDAO.validarLogin(nomeUsuario, senha)) {
                System.out.println("\nLogin bem-sucedido! Bem-vindo, " + nomeUsuario + "!\n");
                break; // Encerra o loop do login
            } else {
                System.out.println("\n[ERRO] Nome de usuário ou senha incorretos. Tente novamente.\n");
            }
        } while (true);
    }

    /**
     * Exibe o menu principal para o usuário.
     */
    public void escolha() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== SISTEMA DE ACESSO ===");
            System.out.println("1. Fazer login");
            System.out.println("2. Criar nova conta");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha tratada pelo nextInt()

            if (opcao == 1) {
                fazerLogin(scanner);
                break; // Encerra o menu após autenticação bem-sucedida
            } else if (opcao == 2) {
                CriarConta criarConta = new CriarConta();
                criarConta.criarConta(scanner);
                System.out.println();
            } else if (opcao == 3) {
                System.out.println("Saindo do sistema...");
                break;
            } else {
                System.out.println("\nOpção inválida. Tente novamente.\n");
            }
        }
    }
}