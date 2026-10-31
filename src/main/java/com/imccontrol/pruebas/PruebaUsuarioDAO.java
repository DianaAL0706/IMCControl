
package com.imccontrol.pruebas;

import com.imccontrol.dao.UsuarioDAO;
import com.imccontrol.model.Usuario;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class PruebaUsuarioDAO {

    public static void main(String[] args) {

        try {
            // Contraseña ficticia para esta prueba
            String contrasenaPrueba = "Prueba123!";

            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(
                    contrasenaPrueba.getBytes(
                            StandardCharsets.UTF_8));

            StringBuilder hashTexto = new StringBuilder();

            for (byte b : hash) {
                hashTexto.append(
                        String.format("%02x", b & 0xff));
            }

            Usuario usuario = new Usuario(
                    0,
                    "Usuario de Prueba",
                    25,
                    "Femenino",
                    1.65,
                    "usuario_prueba",
                    hashTexto.toString()
            );

            UsuarioDAO dao = new UsuarioDAO();

            if (dao.registrarUsuario(usuario)) {
                System.out.println(
                        "Usuario registrado correctamente");
            } else {
                System.out.println(
                        "No se pudo registrar el usuario");
            }

        } catch (SQLException |
                 NoSuchAlgorithmException |
                 IllegalArgumentException e) {

            System.out.println(
                    "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}