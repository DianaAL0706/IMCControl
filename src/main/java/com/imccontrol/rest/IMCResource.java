
package com.imccontrol.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/imc")
public class IMCResource {

    @GET
    @Path("/saludo")
    @Produces(MediaType.TEXT_PLAIN)
    public String saludo() {
        return "Servicio REST de IMC Control funcionando correctamente";
    }
}
