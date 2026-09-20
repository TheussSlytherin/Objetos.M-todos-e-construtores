package model;

import java.util.ArrayList;
import service.LivroAPIService;

public class Biblioteca {
    private ArrayList<Livro> acervo;
    private ArrayList<Usuario> usuarios;

    public Biblioteca() {
        this.acervo = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public void adicionarLivro(Livro livro) {
        acervo.add(livro);
        System.out.println("Livro '" + livro.getTitulo() + "' cadastrado no acervo.");
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario autenticar(String nome, String senha) {
        for (Usuario u : usuarios) {
            if (u.getNome().equalsIgnoreCase(nome) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    public void listarLivros() {
        System.out.println("\n=== ACERVO DA BIBLIOTECA ===");
        if (acervo.isEmpty()) {
            System.out.println("Nenhum livro no acervo local.");
            return;
        }

        for (int i = 0; i < acervo.size(); i++) {
            Livro l = acervo.get(i);
            String status = l.isDisponivel() ? "Disponível" : "Emprestado";
            System.out.println((i + 1) + ". " + l.getTitulo() + " - " + l.getAutor() + " [" + status + "]");
        }
    }

    public Livro buscarLivroPorTitulo(String titulo) {
        for (Livro l : acervo) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }

        // Tenta buscar na API Pública caso não encontre localmente
        System.out.println("\nLivro não encontrado localmente. Consultando a Open Library API...");
        String respostaAPI = LivroAPIService.buscarNaAPI(titulo);

        if (respostaAPI != null && !respostaAPI.contains("\"numFound\":0")) {
            System.out.println("Livro localizado na API pública! Registrando no acervo local...");
            Livro novoLivro = new Livro(titulo, "Autor (Open Library)");
            adicionarLivro(novoLivro);
            return novoLivro;
        }

        System.out.println("Livro não localizado na API pública.");
        return null;
    }
}