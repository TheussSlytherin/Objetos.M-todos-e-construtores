import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. Inicializa o recurso de entrada de dados
        Scanner scanner = new Scanner(System.in);

        // 2. Instancia a classe responsável por iniciar o fluxo da aplicação
        Login sistemaLogin = new Login();

        System.out.println("=== INICIANDO O SISTEMA ===");

        // 3. Delega o controle para a classe especialista
        sistemaLogin.escolha();

        // 4. Finaliza a execução e encerra recursos
        System.out.println("\n=== OBRIGADO POR USAR O SISTEMA ===");
        scanner.close();
    }
}