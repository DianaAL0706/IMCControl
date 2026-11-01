
package com.imccontrol.model;

import java.time.LocalDateTime;

public class RegistroIMC {

    private int idRegistro;
    private int idUsuario;
    private double peso;
    private double estatura;
    private double imc;
    private LocalDateTime fechaRegistro;

    public RegistroIMC(int idRegistro, int idUsuario,
                       double peso, double estatura) {

        if (!Double.isFinite(peso) || peso <= 0) {
            throw new IllegalArgumentException(
                "El peso debe ser mayor a cero."
            );
        }

        if (!Double.isFinite(estatura) ||
            estatura < 1.0 || estatura > 2.5) {
            throw new IllegalArgumentException(
                "La estatura debe estar entre 1 y 2.5 metros."
            );
        }

        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.peso = peso;
        this.estatura = estatura;
        this.imc = peso / (estatura * estatura);
        this.fechaRegistro = LocalDateTime.now();
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public double getPeso() {
        return peso;
    }

    public double getEstatura() {
        return estatura;
    }

    public double getImc() {
        return imc;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public RegistroIMC(int idRegistro, int idUsuario,
                   double peso, double estatura,
                   LocalDateTime fechaRegistro) {

    this(idRegistro, idUsuario, peso, estatura);
    this.fechaRegistro = fechaRegistro;
    }
}
