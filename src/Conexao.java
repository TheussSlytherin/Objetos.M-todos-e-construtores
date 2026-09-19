import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    // Caminho do arquivo do banco SQLite (será criado na raiz da pasta do projeto)
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    /**
     * Estabelece e retorna uma conexão ativa com o banco de dados.
     */
    public static Connection conectar() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco SQLite: " + e.getMessage());
        }
        return conexao;
    }

    /**
     * Cria as tabelas necessárias no banco de dados, caso ainda não existam.
     */
    public static void criarTabelas() {
        // Tabela para armazenar os usuários cadastrados
        String sqlUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL,
                email TEXT
            );
        """;

        // Tabela para armazenar os livros
        String sqlLivros = """
            CREATE TABLE IF NOT EXISTS livros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                disponivel INTEGER DEFAULT 1
            );
        """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuarios);
            stmt.execute(sqlLivros);
            System.out.println("Banco de dados SQLite pronto para uso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }
}