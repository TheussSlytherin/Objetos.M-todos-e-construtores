package dao;

import conexao.Conexao;
import model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public LivroDAO() {
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS livros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "autor TEXT, " +
                "ano INTEGER)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de livros: " + e.getMessage());
        }
    }

    /**
     * Busca livros no SQLite cujo título contenha o termo pesquisado (case-insensitive).
     */
    public List<Livro> buscarPorTitulo(String termo) {
        List<Livro> resultados = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE LOWER(titulo) LIKE LOWER(?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // O % permite buscar por palavras parciais (ex: "Java" encontra "Effective Java")
            pstmt.setString(1, "%" + termo + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int ano = rs.getInt("ano");

                resultados.add(new Livro(id, titulo, autor, ano));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro no banco: " + e.getMessage());
        }

        return resultados;
    }

    /**
     * Método utilitário para cadastrar um livro no banco.
     */
    public boolean cadastrarLivro(String titulo, String autor, int ano) {
        String sql = "INSERT INTO livros(titulo, autor, ano) VALUES(?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setInt(3, ano);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar livro: " + e.getMessage());
            return false;
        }
    }
}