
package com.imccontrol.pruebas;

import com.imccontrol.config.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexionBD {

    public static void main(String[] args) {

        System.out.println("=== PRUEBA DE CONEXION IMC CONTROL ===");

        try (Connection conexion = ConexionBD.obtenerConexion()) {

            if (conexion != null && !conexion.isClosed()) {
                System.out.println(
                    "Conexion exitosa con IMCControlDB");
            }

        } catch (SQLException e) {
            System.out.println(
                "Error de conexion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
