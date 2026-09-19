import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Método anterior de cadastro...
    public boolean cadastrarUsuario(String nome, String senha, String email) {
        String sql = "INSERT INTO usuarios(nome, senha, email) VALUES(?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, senha);
            pstmt.setString(3, email);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("\n[ERRO] Este nome de usuário já está cadastrado!");
            } else {
                System.out.println("\n[ERRO] Falha ao salvar no banco de dados: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Consulta o SQLite para verificar se existe um usuário com o nome e senha informados.
     */
    public boolean validarLogin(String nome, String senha) {
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, senha);

            ResultSet rs = pstmt.executeQuery();

            // Se encontrar pelo menos um registro correspondente, o login é válido
            return rs.next();

        } catch (SQLException e) {
            System.out.println("\n[ERRO] Erro ao consultar o banco de dados: " + e.getMessage());
            return false;
        }
    }
}