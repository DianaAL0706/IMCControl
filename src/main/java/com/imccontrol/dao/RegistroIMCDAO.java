
package com.imccontrol.dao;

import com.imccontrol.config.ConexionBD;
import com.imccontrol.model.RegistroIMC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegistroIMCDAO {

    public boolean guardarRegistro(RegistroIMC registro)
            throws SQLException {

        String sql = "INSERT INTO REGISTROS_IMC "
                + "(ID_USUARIO, PESO, ESTATURA, IMC, "
                + "FECHA_REGISTRO) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, registro.getIdUsuario());
            ps.setDouble(2, registro.getPeso());
            ps.setDouble(3, registro.getEstatura());
            ps.setDouble(4, registro.getImc());
            ps.setTimestamp(5, Timestamp.valueOf(
                    registro.getFechaRegistro()));

            return ps.executeUpdate() > 0;
        }
    }
    
    public List<RegistroIMC> consultarHistorial(int idUsuario)
        throws SQLException {

    List<RegistroIMC> historial = new ArrayList<>();

    String sql = "SELECT ID_REGISTRO, ID_USUARIO, "
            + "PESO, ESTATURA, IMC, FECHA_REGISTRO "
            + "FROM REGISTROS_IMC "
            + "WHERE ID_USUARIO = ? "
            + "ORDER BY FECHA_REGISTRO DESC";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement ps =
                 conexion.prepareStatement(sql)) {

        ps.setInt(1, idUsuario);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                RegistroIMC registro = new RegistroIMC(
                        rs.getInt("ID_REGISTRO"),
                        rs.getInt("ID_USUARIO"),
                        rs.getDouble("PESO"),
                        rs.getDouble("ESTATURA"),
                        rs.getTimestamp("FECHA_REGISTRO")
                                .toLocalDateTime()
                );

                historial.add(registro);
            }
        }
    }
    return historial;
    }
}
