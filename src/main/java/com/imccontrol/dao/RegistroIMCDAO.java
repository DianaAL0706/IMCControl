
package com.imccontrol.dao;

import com.imccontrol.config.ConexionBD;
import com.imccontrol.model.RegistroIMC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
}
