
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.LocalDateTime;

@Path("/imc")
public class IMCResource {

@Context
private HttpServletRequest request;

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

HttpSession sesion = request.getSession(false);

if (sesion == null ||
        sesion.getAttribute("idUsuario") == null) {

    return Response.status(Response.Status.UNAUTHORIZED)
            .entity("{\"error\":\"Debes iniciar sesión\"}")
            .build();
}

int idUsuarioSesion =
        (Integer) sesion.getAttribute("idUsuario");

if (idUsuarioSesion != idUsuario) {

    return Response.status(Response.Status.FORBIDDEN)
            .entity("{\"error\":\"No puedes consultar el historial de otro usuario\"}")
            .build();
}

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
@POST
@Path("/guardar")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response guardarIMC(String datos) {
    HttpSession sesion = request.getSession(false);

if (sesion == null ||
        sesion.getAttribute("idUsuario") == null) {

    return Response.status(Response.Status.UNAUTHORIZED)
            .entity("{\"error\":\"Debes iniciar sesión\"}")
            .build();
}

int idUsuario =
        (Integer) sesion.getAttribute("idUsuario");
        
JsonObject json;

try (JsonReader lector = Json.createReader(
        new StringReader(datos))) {

    json = lector.readObject();
}

double peso = json.getJsonNumber("peso").doubleValue();
double estatura = json.getJsonNumber("estatura").doubleValue();
   
if (!Double.isFinite(peso) || peso <= 0 ||
        !Double.isFinite(estatura) || estatura <= 0) {

    return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"error\":\"Peso y estatura deben ser mayores a cero\"}")
            .build();
}

double imc = peso / (estatura * estatura);
 
RegistroIMC registro = new RegistroIMC(
        0,
        idUsuario,
        peso,
        estatura,
        LocalDateTime.now()
);
RegistroIMCDAO dao = new RegistroIMCDAO();

boolean guardado;

try {
    guardado = dao.guardarRegistro(registro);
} catch (SQLException e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("{\"error\":\"No se pudo guardar el registro de IMC\"}")
            .build();
}
if (guardado) {
    return Response.ok()
            .entity("{\"mensaje\":\"Registro de IMC guardado correctamente\"}")
            .build();
}

return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("{\"error\":\"No se pudo guardar el registro de IMC\"}")
        .build();
}
}
