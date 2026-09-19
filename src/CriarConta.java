import java.util.Scanner;

public class CriarConta {

    // Adicionado 'static' aqui
    public static void criarConta(ColetandoDados c, Scanner scanner) {
        System.out.println("\n--- Criando uma nova conta ---");

        System.out.print("Digite o nome de usuário: ");
        c.nome = scanner.nextLine();

        System.out.print("Digite a senha: ");
        c.senha = scanner.nextLine();

        System.out.print("Digite o seu email: ");
        c.email = scanner.nextLine();
    }
}