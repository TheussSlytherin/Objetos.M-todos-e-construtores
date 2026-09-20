package view;

import model.Biblioteca;
import model.Livro;
import model.Usuario;

import java.util.Scanner;

public class Login {
    private Biblioteca biblioteca;

    public Login(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
            System.out.println("1. Criar Conta");
            System.out.println("2. Fazer Login");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                criarConta(scanner);
            } else if (opcao == 2) {
                Usuario usuario = realizarLogin(scanner);
                if (usuario != null) {
                    menuUsuario(usuario, scanner);
                }
            } else if (opcao == 3) {
                System.out.println("Encerrando o sistema...");
                break;
            } else {
                System.out.println("Opção inválida!");
            }
        }
    }

    private void criarConta(Scanner scanner) {
        System.out.println("\n--- CRIAR CONTA ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario novoUsuario = new Usuario(nome, cpf, email, idade, senha);
        biblioteca.cadastrarUsuario(novoUsuario);
        System.out.println("Conta criada com sucesso!");
    }

    private Usuario realizarLogin(Scanner scanner) {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario u = biblioteca.autenticar(nome, senha);
        if (u != null) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + u.getNome());
            return u;
        } else {
            System.out.println("Usuário ou senha incorretos.");
            return null;
        }
    }

    private void menuUsuario(Usuario usuario, Scanner scanner) {
        while (true) {
            System.out.println("\n--- PAINEL DO LEITOR ---");
            System.out.println("1. Ver Meus Dados");
            System.out.println("2. Listar Acervo");
            System.out.println("3. Emprestar Livro");
            System.out.println("4. Devolver Livro");
            System.out.println("5. Logout");
            System.out.print("Escolha uma opção: ");

            int op = scanner.nextInt();
            scanner.nextLine();

            if (op == 1) {
                usuario.exibirDados();
            } else if (op == 2) {
                biblioteca.listarLivros();
            } else if (op == 3) {
                System.out.print("Digite o título do livro: ");
                String titulo = scanner.nextLine();
                Livro livro = biblioteca.buscarLivroPorTitulo(titulo);
                if (livro != null && livro.emprestar(usuario.getNome())) {
                    usuario.adicionarEmprestimo();
                }
            } else if (op == 4) {
                System.out.print("Digite o título do livro para devolução: ");
                String titulo = scanner.nextLine();
                Livro livro = biblioteca.buscarLivroPorTitulo(titulo);
                if (livro != null && livro.devolver(usuario.getNome())) {
                    usuario.removerEmprestimo();
                }
            } else if (op == 5) {
                break;
            }
        }
    }
}