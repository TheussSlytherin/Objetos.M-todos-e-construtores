public class Livro {

    // Atributos para a classe Livro
    String titulo;
    String autor;
    boolean disponivel;

    public Livro(String titulo, String autor) {
        this.autor = autor;
        this.titulo = titulo;
        this.disponivel = true; // Por padrão, o livro está disponível
    }

    public void emprestar(String nomeUsuario) {
        if (disponivel) {
            disponivel = false;
            System.out.println("O livro '" + titulo + "' foi emprestado com sucesso para o usuário: " + nomeUsuario);
        } else {
            System.out.println("O livro '" + titulo + "' não está disponível para empréstimo.");
        }
    }

    public void devolver(String nomeUsuario) {
        if (!disponivel) {
            disponivel = true;
            System.out.println("O titulo '" + titulo + "' foi devolvido com sucesso pelo usuário: " + nomeUsuario);
        } else {
            System.out.println("O livro '" + titulo + "' já está disponível na biblioteca.");
        }
    }
}
