package cl.duoc.dsy2207.repository;

import cl.duoc.dsy2207.config.DatabaseConfig;
import cl.duoc.dsy2207.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    public List<Usuario> listar() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT ID_USUARIO, NOMBRE, EMAIL, ACTIVO, ID_ROL
                FROM USUARIOS
                ORDER BY ID_USUARIO
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        }

        return usuarios;
    }


    public Usuario buscarPorId(Long id) throws SQLException {

        String sql = """
                SELECT ID_USUARIO, NOMBRE, EMAIL, ACTIVO, ID_ROL
                FROM USUARIOS
                WHERE ID_USUARIO = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }


    public boolean existeRol(Long idRol) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM ROLES
                WHERE ID_ROL = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setLong(1, idRol);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }


    public Usuario crear(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO USUARIOS
                (NOMBRE, EMAIL, ACTIVO, ID_ROL)
                VALUES (?, ?, ?, ?)
                """;

        String[] generatedColumns = {"ID_USUARIO"};

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, generatedColumns)
        ) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getActivo());
            ps.setLong(4, usuario.getIdRol());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getLong(1));
                }
            }
        }

        return usuario;
    }


    public boolean actualizar(Long id, Usuario usuario)
            throws SQLException {

        String sql = """
                UPDATE USUARIOS
                SET NOMBRE = ?,
                    EMAIL = ?,
                    ACTIVO = ?,
                    ID_ROL = ?
                WHERE ID_USUARIO = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getActivo());
            ps.setLong(4, usuario.getIdRol());
            ps.setLong(5, id);

            return ps.executeUpdate() > 0;
        }
    }


    public boolean eliminar(Long id) throws SQLException {

        String sql = """
                DELETE FROM USUARIOS
                WHERE ID_USUARIO = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;
        }
    }


    private Usuario mapear(ResultSet rs) throws SQLException {

        return new Usuario(
                rs.getLong("ID_USUARIO"),
                rs.getString("NOMBRE"),
                rs.getString("EMAIL"),
                rs.getInt("ACTIVO"),
                rs.getLong("ID_ROL")
        );
    }
}