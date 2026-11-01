
package com.imccontrol.dao;

import com.imccontrol.config.ConexionBD;
import com.imccontrol.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UsuarioDAO {

    public boolean registrarUsuario(Usuario usuario)
            throws SQLException {

        String sql = "INSERT INTO USUARIOS "
                + "(NOMBRE_COMPLETO, EDAD, SEXO, ESTATURA, "
                + "NOMBRE_USUARIO, CONTRASENA_HASH) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombreCompleto());
            ps.setInt(2, usuario.getEdad());
            ps.setString(3, usuario.getSexo());
            ps.setDouble(4, usuario.getEstatura());
            ps.setString(5, usuario.getNombreUsuario());
            ps.setString(6, usuario.getContrasenaHash());

            return ps.executeUpdate() > 0;
        }
    }
        public boolean existeNombreUsuario(String nombreUsuario)
            throws SQLException {

        String sql = "SELECT 1 FROM USUARIOS "
            + "WHERE NOMBRE_USUARIO = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

         ps.setString(1, nombreUsuario);

        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }
}
      
public Usuario buscarPorNombreUsuario(String nombreUsuario)
        throws SQLException {

    String sql = "SELECT * FROM USUARIOS "
            + "WHERE NOMBRE_USUARIO = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps =
                 conexion.prepareStatement(sql)) {

        ps.setString(1, nombreUsuario);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("ID_USUARIO"),
                        rs.getString("NOMBRE_COMPLETO"),
                        rs.getInt("EDAD"),
                        rs.getString("SEXO"),
                        rs.getDouble("ESTATURA"),
                        rs.getString("NOMBRE_USUARIO"),
                        rs.getString("CONTRASENA_HASH")
                );
            }
        }
    }

    return null;
}
}
