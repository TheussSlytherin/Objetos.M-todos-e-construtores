import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    // Método responsável por conectar ao banco de dados SQLite
    public static Connection conectar() {
        Connection conexao = null;
        try {
            // Garante o carregamento manual da classe do driver
            Class.forName("org.sqlite.JDBC");

            // Caminho para o arquivo do banco de dados (será criado na raiz do projeto)
            String url = "jdbc:sqlite:biblioteca.db";
            conexao = DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC do SQLite não foi encontrado no Classpath: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }

        return conexao;
    }

    // Método responsável por criar as tabelas iniciais
    public static void criarTabelas() {
        Connection conn = conectar();

        // Evita NullPointerException se a conexão tiver falhado
        if (conn == null) {
            System.out.println("Falha na conexão. A criação de tabelas foi cancelada.");
            return;
        }

        // SQL para criação das tabelas 'usuarios' e 'livros'
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "senha TEXT NOT NULL, "
                + "email TEXT"
                + ");";

        String sqlLivros = "CREATE TABLE IF NOT EXISTS livros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "autor TEXT NOT NULL, "
                + "isbn TEXT"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            // Executa os comandos para criar as tabelas
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlLivros);
            System.out.println("Tabelas verificadas/criadas com sucesso.");

        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        } finally {
            // Garante o fechamento da conexão após criar as tabelas
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}