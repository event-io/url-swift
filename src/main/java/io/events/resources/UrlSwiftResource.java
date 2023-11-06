package io.events.resources;

import org.jboss.resteasy.reactive.common.NotImplementedYet;

import io.events.dto.LinkShorteningCreationDTO;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/url-swift")
public class UrlSwiftResource {
    
    @GET
    @Path("/r/{shortenedLink}")
    public Uni<Response> redirect(String shortenedLink) {
        throw new NotImplementedYet();
    }

    @GET
    @Path("/i/{shortenedLink}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> info(String shortenedLink) {
        throw new NotImplementedYet();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> create(@Valid LinkShorteningCreationDTO linkShorteningCreationDTO) {
        throw new NotImplementedYet();
    }

}
