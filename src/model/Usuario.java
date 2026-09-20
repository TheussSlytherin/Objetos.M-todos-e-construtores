package model;

public class Usuario {
    private String nome;
    private String cpf;
    private String email;
    private int idade;
    private String senha;
    private int qtdLivrosEmprestados;

    public Usuario(String nome, String cpf, String email, int idade, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.idade = idade;
        this.senha = senha;
        this.qtdLivrosEmprestados = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public int getQtdLivrosEmprestados() {
        return qtdLivrosEmprestados;
    }

    public void adicionarEmprestimo() {
        this.qtdLivrosEmprestados++;
    }

    public void removerEmprestimo() {
        if (this.qtdLivrosEmprestados > 0) {
            this.qtdLivrosEmprestados--;
        }
    }

    public void exibirDados() {
        System.out.println("\n--- DADOS DO USUÁRIO ---");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("E-mail: " + email);
        System.out.println("Idade: " + idade);
        System.out.println("Livros emprestados atualmente: " + qtdLivrosEmprestados);
    }
}