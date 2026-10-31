
package com.imccontrol.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:derby://localhost:1527/IMCControlDB";

    private static final String USUARIO = "app";
    private static final String PASSWORD = "app";

    public static Connection obtenerConexion()
            throws SQLException {

        return DriverManager.getConnection(
                URL, USUARIO, PASSWORD);
    }
}
