
public class Main {
    public static void main(String[] args) {
        CriarConta w = new CriarConta();
        Login l = new Login();
        ColetandoDados c = new ColetandoDados();
        ExibirDados e = new ExibirDados();

        l.escolha();

        c.coletarDados();

        e.exibir(c);
    }
}