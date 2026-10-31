
package com.imccontrol.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import com.imccontrol.dao.RegistroIMCDAO;
import com.imccontrol.model.RegistroIMC;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


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

@GET
@Path("/historial/{idUsuario}")
@Produces(MediaType.APPLICATION_JSON)
public Response obtenerHistorial(
        @PathParam("idUsuario") int idUsuario) {

    if (idUsuario <= 0) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"ID de usuario no válido\"}")
                .build();
    }
    try {
        RegistroIMCDAO dao = new RegistroIMCDAO();

        List<RegistroIMC> historial =
                dao.consultarHistorial(idUsuario);

        StringBuilder json = new StringBuilder("[");
        java.time.format.DateTimeFormatter formato =
                java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (int i = 0; i < historial.size(); i++) {
            RegistroIMC registro = historial.get(i);

            if (i > 0) {
                json.append(",");
            }

            json.append(String.format(
                    java.util.Locale.US,
                    "{\"idRegistro\":%d,"
                    + "\"idUsuario\":%d,"
                    + "\"peso\":%.2f,"
                    + "\"estatura\":%.2f,"
                    + "\"imc\":%.2f,"
                    + "\"fechaRegistro\":\"%s\"}",
                    registro.getIdRegistro(),
                    registro.getIdUsuario(),
                    registro.getPeso(),
                    registro.getEstatura(),
                    registro.getImc(),
                    registro.getFechaRegistro().format(formato)
            ));
        }

        json.append("]");

        return Response.ok(json.toString(),
                MediaType.APPLICATION_JSON).build();

    } catch (SQLException e) {
        return Response.status(
                Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"No se pudo consultar el historial\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

}
