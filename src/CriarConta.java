import java.util.Scanner;

public class CriarConta {

    public void criarConta(Scanner scanner) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        System.out.println("\n--- Criando uma nova conta ---");

        System.out.print("Digite o nome de usuário: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        System.out.print("Digite o seu email: ");
        String email = scanner.nextLine();

        // Envia os dados coletados diretamente para o banco SQLite
        boolean sucesso = usuarioDAO.cadastrarUsuario(nome, senha, email);

        if (sucesso) {
            System.out.println("Conta criada com sucesso no banco de dados!");
        } else {
            System.out.println("Não foi possível concluir o cadastro. Tente novamente.");
        }
    }
}