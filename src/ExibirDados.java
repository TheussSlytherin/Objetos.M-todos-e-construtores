public class ExibirDados {

    // Recebe a instância onde os dados foram armazenados
    public void exibir(ColetandoDados c) {
        System.out.println("\n--- EXIBINDO DADOS ---");
        System.out.println("Nome: " + c.nome);
        System.out.println("Idade: " + c.idade);
        System.out.println("CPF: " + c.cpf);
        System.out.println("Email: " + c.email);
    }
}