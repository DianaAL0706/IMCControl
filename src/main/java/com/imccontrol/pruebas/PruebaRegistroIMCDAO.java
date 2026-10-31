
package com.imccontrol.pruebas;

import com.imccontrol.dao.RegistroIMCDAO;
import com.imccontrol.model.RegistroIMC;
import java.sql.SQLException;
import java.util.List;

public class PruebaRegistroIMCDAO {

    public static void main(String[] args) {

        try {
            // Usuario de prueba registrado anteriormente
            int idUsuario = 1;

            // Crear un registro de IMC
            RegistroIMC registro = new RegistroIMC(
                    0,
                    idUsuario,
                    70.0,
                    1.70
            );

            RegistroIMCDAO dao = new RegistroIMCDAO();
             
List<RegistroIMC> historial =
        dao.consultarHistorial(idUsuario);

System.out.println("=== HISTORIAL DE IMC ===");

for (RegistroIMC registroHistorial : historial) {
    System.out.println(
            "Fecha: " + registroHistorial.getFechaRegistro()
            + " | Peso: " + registroHistorial.getPeso()
            + " kg | IMC: " + registroHistorial.getImc()
    );
}
//            if (dao.guardarRegistro(registro)) {
//                System.out.println(
//                        "Registro de IMC guardado correctamente");
//                System.out.println(
//                        "IMC calculado: " + registro.getImc());
//            } else {
//                System.out.println(
//                        "No se pudo guardar el registro");
//            }

        } catch (SQLException |
                 IllegalArgumentException e) {

            System.out.println(
                    "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
