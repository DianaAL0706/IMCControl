package com.imccontrol.model;

public class Usuario {

    private int idUsuario;
    private String nombreCompleto;
    private int edad;
    private String sexo;
    private double estatura;
    private String nombreUsuario;
    private String contrasenaHash;
    
public Usuario(int idUsuario, String nombreCompleto,
               int edad, String sexo, double estatura,
               String nombreUsuario, String contrasenaHash) {

    this.idUsuario = idUsuario;
    this.nombreCompleto = nombreCompleto;
    setEdad(edad);
    this.sexo = sexo;
    setEstatura(estatura);
    this.nombreUsuario = nombreUsuario;
    this.contrasenaHash = contrasenaHash;
}

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
    if (edad < 15) {
        throw new IllegalArgumentException(
            "La edad debe ser de al menos 15 años."
        );
    }
    this.edad = edad;
}

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
    if (!Double.isFinite(estatura) ||
        estatura < 1.0 || estatura > 2.5) {
        throw new IllegalArgumentException(
            "La estatura debe estar entre 1 y 2.5 metros."
        );
    }
    this.estatura = estatura;
}   

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getSexo() {
        return sexo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }
}