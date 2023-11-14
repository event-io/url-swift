package io.events.resources;

import java.net.URI;

import org.jboss.resteasy.reactive.common.NotImplementedYet;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.exceptions.RedirectUrlMatchException;
import io.events.services.UrlSwiftService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
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

    @Inject
    private UrlSwiftService urlSwiftService;
    
    @GET
    @Path("/r/{shortenedLink}")
    public Uni<Response> redirect(String shortenedLink) {
        return Uni.createFrom().item(shortenedLink)
            .map(item -> {
                if(item.length() != 7 || !item.matches("[a-zA-Z0-9]*")) throw new IllegalArgumentException("Shortened link must be 7 characters and must respect regex [a-zA-Z0-9]*");
                return item;
            })
            .chain(sL -> Uni.createFrom().completionStage(this.urlSwiftService.getInfo("eq."+shortenedLink).subscribeAsCompletionStage()))
            .map(redirectURL -> Response.seeOther(URI.create(redirectURL.getOriginalLink())).build())
            .onFailure().recoverWithItem(failure -> {
                if(failure instanceof IllegalArgumentException) return Response.status(Response.Status.BAD_REQUEST).entity(failure.getMessage()).build();
                if(failure instanceof RedirectUrlMatchException) return Response.status(Response.Status.NOT_FOUND).entity(failure.getMessage()).build();
                return Response.serverError().entity(failure.getMessage()).build();
        });
    }

    @GET
    @Path("/i/{shortenedLink}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> info(String shortenedLink) {
        return Uni.createFrom().item(shortenedLink)
            .map(item -> {
                if(item.length() != 7 || !item.matches("[a-zA-Z0-9]*")) throw new IllegalArgumentException("Shortened link must be 7 characters and must respect regex [a-zA-Z0-9]*");
                return item;
            })
            .chain(sL -> Uni.createFrom().completionStage(this.urlSwiftService.getInfo("eq."+shortenedLink).subscribeAsCompletionStage()))
            .map(info -> Response.ok(info).build())
            .onFailure().recoverWithItem(failure -> {
                if(failure instanceof IllegalArgumentException) return Response.status(Response.Status.BAD_REQUEST).entity(failure.getMessage()).build();
                if(failure instanceof RedirectUrlMatchException) return Response.status(Response.Status.NOT_FOUND).entity(failure.getMessage()).build();
                return Response.serverError().entity(failure.getMessage()).build();
        });
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> create(@Valid LinkShorteningCreationDTO linkShorteningCreationDTO) {
        return Uni.createFrom().item(linkShorteningCreationDTO)
            .chain(dto -> Uni.createFrom().completionStage(this.urlSwiftService.create(dto).subscribeAsCompletionStage()))
            .map(response -> Response.ok(response).build())
            .onFailure().recoverWithItem(failure ->  Response.serverError().entity(failure.getMessage()).build());
    }

}
