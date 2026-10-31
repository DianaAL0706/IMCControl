package com.imccontrol.pruebas;

import com.imccontrol.model.RegistroIMC;

public class PruebaIMC {

    public static void main(String[] args) {

        RegistroIMC registro =
                new RegistroIMC(1, 1, 70.0, 1.70);

        System.out.println("=== PRUEBA DE IMC CONTROL ===");
        System.out.println("Peso: " + registro.getPeso() + " kg");
        System.out.println("Estatura: " + registro.getEstatura() + " m");
        System.out.printf("IMC calculado: %.2f%n",
                registro.getImc());
    }
}