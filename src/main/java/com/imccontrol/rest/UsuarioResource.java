
package com.imccontrol.rest;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.imccontrol.dao.UsuarioDAO;
import com.imccontrol.model.Usuario;
import com.imccontrol.seguridad.SeguridadContrasena;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import javax.ws.rs.GET;

@Path("/usuarios")
public class UsuarioResource {

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/registro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
   
public Response registrar(String datos) {
    try (JsonReader lector = Json.createReader(
            new StringReader(datos))) {

        JsonObject json = lector.readObject();

        String nombre = json.getString("nombre").trim();
        int edad = json.getInt("edad");
        String sexo = json.getString("sexo");
        double estatura = json.getJsonNumber("estatura")
                .doubleValue();
        String nombreUsuario = json.getString("usuario").trim();
        String contrasena = json.getString("contrasena");
    
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
            if (!nombreUsuario.isEmpty()
            && usuarioDAO.existeNombreUsuario(nombreUsuario)) {

        return Response.status(Response.Status.CONFLICT)
            .entity("{\"mensaje\":\"El nombre de usuario ya existe\"}")
            .build();
} 
        
if (nombre.isEmpty() || sexo.trim().isEmpty()
        || nombreUsuario.isEmpty() || contrasena.isEmpty()) {
    throw new IllegalArgumentException(
            "Todos los campos son obligatorios.");
}

if (edad < 15) {
    throw new IllegalArgumentException(
            "La edad minima permitida es de 15 años.");
}

if (!Double.isFinite(estatura)
        || estatura < 1.0 || estatura > 2.5) {
    throw new IllegalArgumentException(
            "La estatura debe estar entre 1 y 2.5 metros.");
}

String contrasenaHash =
        SeguridadContrasena.generarHash(contrasena);

Usuario usuario = new Usuario(
        0,
        nombre,
        edad,
        sexo,
        estatura,
        nombreUsuario,
        contrasenaHash
);  
    boolean registrado = usuarioDAO.registrarUsuario(usuario);

    if (registrado) {
        return Response.status(Response.Status.CREATED)
            .entity("{\"mensaje\":\"Usuario registrado correctamente\"}")
            .build();
}

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("{\"mensaje\":\"No se pudo registrar el usuario\"}")
        .build();

   } catch (IllegalArgumentException e) {
    return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"mensaje\":\"" + e.getMessage() + "\"}")
            .build();

    } catch (Exception e) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("{\"mensaje\":\"Error interno al registrar el usuario\"}")
            .build();
    }
}

@POST
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public Response iniciarSesion(String datos) {

    try (JsonReader lector = Json.createReader(
            new StringReader(datos))) {

        JsonObject json = lector.readObject();

        String nombreUsuario =
                json.getString("usuario").trim();

        String contrasena =
                json.getString("contrasena");

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Todos los campos son obligatorios\"}")
                    .build();
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario =
                usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        if (usuario == null ||
                !SeguridadContrasena.verificarContrasena(
                        contrasena, usuario.getContrasenaHash())) {

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"mensaje\":\"Usuario o contraseña incorrectos\"}")
                    .build();
        }

        HttpSession sesion = request.getSession(true);
            sesion.setAttribute("idUsuario", usuario.getIdUsuario());
            sesion.setAttribute("nombreUsuario", usuario.getNombreUsuario());

                return Response.ok(
             "{\"mensaje\":\"Credenciales correctas\"}"
            ).build();  
    
        } catch (Exception e) {
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error interno al iniciar sesión\"}")
                    .build();
        }
}

@GET
@Path("/sesion")
@Produces(MediaType.APPLICATION_JSON)
public Response verificarSesion() {

    HttpSession sesion = request.getSession(false);

    if (sesion == null ||
        sesion.getAttribute("idUsuario") == null) {

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"mensaje\":\"No hay una sesión activa\"}")
                .build();
    }

    int idUsuario = (Integer) request.getSession(false).getAttribute("idUsuario");

        return Response.ok(
         "{\"mensaje\":\"Sesión activa\",\"idUsuario\":" + idUsuario + "}"
    ).build();
}

@POST
@Path("/logout")
@Produces(MediaType.APPLICATION_JSON)
public Response cerrarSesion() {

    HttpSession sesion = request.getSession(false);

    if (sesion != null) {
        sesion.invalidate();
    }

    return Response.ok(
        "{\"mensaje\":\"Sesión cerrada correctamente\"}"
    ).build();
}

}
