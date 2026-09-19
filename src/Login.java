import java.util.Scanner;

public class Login {

    // Método auxiliar para realizar e validar o login
    private void fazerLogin(ColetandoDados c, Scanner scanner) {
        String nomeUsuario;
        String senha;

        // Loop até que os dados informados sejam iguais aos cadastrados
        do {
            System.out.print("Digite o seu nome de usuário: ");
            nomeUsuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            senha = scanner.nextLine();

            if (nomeUsuario.equals(c.nome) && senha.equals(c.senha)) {
                System.out.println("\nLogin bem-sucedido! Bem-vindo, " + c.nome + "!\n");
                break;
            } else {
                System.out.println("\nNome de usuário ou senha incorretos. Tente novamente.\n");
            }
        } while (true);
    }

    // Método principal do menu
    public void escolha() {
        ColetandoDados c = new ColetandoDados();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Bem-vindo! Por favor, faça o login ou crie uma nova conta.");
            System.out.println("1. Fazer login");
            System.out.println("2. Criar nova conta");
            System.out.print("Escolha uma opção (1 ou 2): ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha do nextInt()

            if (opcao == 1) {
                // Verifica se há dados cadastrados antes de tentar o login
                if (c.nome == null || c.senha == null) {
                    System.out.println("\nNenhuma conta encontrada. Crie uma conta primeiro!\n");
                } else {
                    fazerLogin(c, scanner);
                    break; // Encerra o menu após o login com sucesso
                }
            } else if (opcao == 2) {
                CriarConta criarConta = new CriarConta();
                // Passa o objeto 'c' e 'scanner' para que a classe CriarConta preencha os dados no mesmo objeto
                CriarConta.criarConta(c, scanner);
                System.out.println("\nConta criada com sucesso! Agora você pode fazer login.\n");
            } else {
                System.out.println("\nOpção inválida. Tente novamente.\n");
            }
        }
    }
}