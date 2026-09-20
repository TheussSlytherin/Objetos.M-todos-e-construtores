package main;

import model.Biblioteca;
import model.Livro;
import view.Login;

public class Main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();

        // Carga inicial de livros no acervo
        biblioteca.adicionarLivro(new Livro("O Senhor dos Anéis", "J.R.R. Tolkien"));
        biblioteca.adicionarLivro(new Livro("Dom Casmurro", "Machado de Assis"));

        Login loginControlador = new Login(biblioteca);
        loginControlador.menuPrincipal();
    }
}