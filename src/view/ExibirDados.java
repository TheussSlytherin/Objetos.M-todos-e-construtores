package view;

import model.ColetandoDados;

public class ExibirDados {
    public void exibir(ColetandoDados c) {
        System.out.println("Nome: " + c.getNome());
        System.out.println("Idade: " + c.getIdade());
        System.out.println("CPF: " + c.getCpf());
        System.out.println("Email: " + c.getEmail());
    }
}