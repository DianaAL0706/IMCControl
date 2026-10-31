
package com.imccontrol.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

@Path("/imc")
public class IMCResource {

    @GET
    @Path("/saludo")
    @Produces(MediaType.TEXT_PLAIN)
    public String saludo() {
        return "Servicio REST de IMC Control funcionando correctamente";
    }
   
    @GET
    @Path("/calcular")
    @Produces(MediaType.APPLICATION_JSON)
    public String calcularIMC(
        @QueryParam("peso") double peso,
        @QueryParam("estatura") double estatura) {

    if (!Double.isFinite(peso) || peso <= 0
            || !Double.isFinite(estatura) || estatura <= 0) {
        return "{\"error\":\"Peso y estatura deben ser mayores a cero\"}";
    }

    double imc = peso / (estatura * estatura);

    return String.format(
            java.util.Locale.US,
            "{\"peso\":%.2f,\"estatura\":%.2f,\"imc\":%.2f}",
            peso, estatura, imc
    );
    }
    @GET
    @Path("/calcular/xml")
    @Produces(MediaType.APPLICATION_XML)
    public String calcularIMCXML(
        @QueryParam("peso") double peso,
        @QueryParam("estatura") double estatura) {

    if (!Double.isFinite(peso) || peso <= 0
            || !Double.isFinite(estatura) || estatura <= 0) {
        return "<error>Peso y estatura deben ser mayores a cero</error>";
    }

    double imc = peso / (estatura * estatura);

    return String.format(
            java.util.Locale.US,
            "<resultado>"
            + "<peso>%.2f</peso>"
            + "<estatura>%.2f</estatura>"
            + "<imc>%.2f</imc>"
            + "</resultado>",
            peso, estatura, imc
    );
}

}
