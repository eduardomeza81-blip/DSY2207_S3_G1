package cl.duoc.dsy2207.repository;

import cl.duoc.dsy2207.config.DatabaseConfig;
import cl.duoc.dsy2207.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolRepository {

    public List<Rol> listar() throws SQLException {

        List<Rol> roles = new ArrayList<>();

        String sql = """
                SELECT ID_ROL, NOMBRE, DESCRIPCION
                FROM ROLES
                ORDER BY ID_ROL
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                Rol rol = new Rol(
                        rs.getLong("ID_ROL"),
                        rs.getString("NOMBRE"),
                        rs.getString("DESCRIPCION")
                );

                roles.add(rol);
            }
        }

        return roles;
    }


    public Rol buscarPorId(Long id) throws SQLException {

        String sql = """
                SELECT ID_ROL, NOMBRE, DESCRIPCION
                FROM ROLES
                WHERE ID_ROL = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Rol(
                            rs.getLong("ID_ROL"),
                            rs.getString("NOMBRE"),
                            rs.getString("DESCRIPCION")
                    );
                }
            }
        }

        return null;
    }


    public Rol crear(Rol rol) throws SQLException {

        String sql = """
                INSERT INTO ROLES (NOMBRE, DESCRIPCION)
                VALUES (?, ?)
                """;

        String[] generatedColumns = {"ID_ROL"};

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(
                                sql,
                                generatedColumns
                        )
        ) {

            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rol.setIdRol(rs.getLong(1));
                }
            }
        }

        return rol;
    }


    public boolean actualizar(Long id, Rol rol) throws SQLException {

        String sql = """
                UPDATE ROLES
                SET NOMBRE = ?,
                    DESCRIPCION = ?
                WHERE ID_ROL = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.setLong(3, id);

            int filas = ps.executeUpdate();

            return filas > 0;
        }
    }


    public boolean tieneUsuariosAsociados(Long id) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM USUARIOS
                WHERE ID_ROL = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }


    public boolean eliminar(Long id) throws SQLException {

        String sql = """
                DELETE FROM ROLES
                WHERE ID_ROL = ?
                """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            int filas = ps.executeUpdate();

            return filas > 0;
        }
    }
}