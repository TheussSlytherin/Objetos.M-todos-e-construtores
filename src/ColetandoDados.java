import java.util.Scanner;

public class ColetandoDados {
    public ColetandoDados ColetandoDados;
    // Atributos de instância (sem static)
    static String nome;
    String cpf;
    String email;
    int idade;
    String senha;

    // Método para ler dados
    public void coletarDados() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o seu nome: ");
        this.nome = scanner.nextLine();

        System.out.print("Digite o seu CPF: ");
        this.cpf = scanner.nextLine();

        System.out.print("Digite o seu email: ");
        this.email = scanner.nextLine();

        System.out.print("Digite a sua idade: ");
        this.idade = scanner.nextInt();

        System.out.print("Digite a sua senha: ");
        this.senha = scanner.next();
    }
}