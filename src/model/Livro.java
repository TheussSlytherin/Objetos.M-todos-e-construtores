package model;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int ano;
    private boolean disponivel;

    public Livro(String titulo, String autor) {
        this(0, titulo, autor, 0);
    }

    public Livro(int id, String titulo, String autor, int ano) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.disponivel = true;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean emprestar(String nomeUsuario) {
        if (disponivel) {
            disponivel = false;
            System.out.println("O livro '" + titulo + "' foi emprestado com sucesso para: " + nomeUsuario);
            return true;
        } else {
            System.out.println("O livro '" + titulo + "' não está disponível no momento.");
            return false;
        }
    }

    public boolean devolver(String nomeUsuario) {
        if (!disponivel) {
            disponivel = true;
            System.out.println("O livro '" + titulo + "' foi devolvido por: " + nomeUsuario);
            return true;
        } else {
            System.out.println("O livro '" + titulo + "' já consta como disponível.");
            return false;
        }
    }
}