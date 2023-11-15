package io.events.resources;

import java.net.URI;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import io.events.dto.LinkShorteningCreationDTO;
import io.events.dto.LinkShorteningResponseDTO;
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
@Schema(name = "Url Swift", description = "Url Swift OpenAPI")
public class UrlSwiftResource {

    @Inject
    private UrlSwiftService urlSwiftService;
    
    @GET
    @Path("/r/{shortenedLink}")
    @Counted(name = "counted-redirects", absolute = true, description = "Number of redirects")
    @Timed(
        name = "timed-redirects",
        description = "A measure of how long it takes to redirect to original URL",
        unit = MetricUnits.MILLISECONDS
    )
    @Gauge(
        name = "gauge-redirects",
        description = "Number of redirects",
        unit = MetricUnits.MINUTES
    )
    @ConcurrentGauge(
        name = "concurrent-gauge-redirects",
        description = "Number of concurrent redirects",
        unit = MetricUnits.MINUTES
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "302", description = "Redirect to original URL",
            content = @Content(mediaType = "text/plain", 
            schema = @Schema(implementation = String.class, example = "http://your-domain.com")))
    })
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
    @Counted(name = "counted-infos", absolute = true, description = "Number of infos")
    @Timed(
        name = "timed-infos",
        description = "A measure of how long it takes to redirect to original URL",
        unit = MetricUnits.MILLISECONDS
    )
    @Gauge(
        name = "gauge-infos",
        description = "Number of infos",
        unit = MetricUnits.MINUTES
    )
    @ConcurrentGauge(
        name = "concurrent-gauge-infos",
        description = "Number of concurrent infos",
        unit = MetricUnits.MINUTES
    )
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Get link information",
            content = @Content(mediaType = "application/json", 
            schema = @Schema(implementation = LinkShorteningResponseDTO.class, example = "{\"originalLink\": \"http://your-domain.com\", \"shortenedLink\": \"abcdefg\", \"createdAt\": \"2021-08-01T00:00:00.000Z\"}")))
    })
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
    @Counted(name = "counted-creations", absolute = true, description = "Number of creations")
    @Timed(
        name = "timed-creations",
        description = "A measure of how long it takes to redirect to original URL",
        unit = MetricUnits.MILLISECONDS
    )
    @Gauge(
        name = "gauge-creations",
        description = "Number of creations",
        unit = MetricUnits.MINUTES
    )
    @ConcurrentGauge(
        name = "concurrent-gauge-creations",
        description = "Number of concurrent creations",
        unit = MetricUnits.MINUTES
    )
    @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkShorteningCreationDTO.class, example = "{\"originalLink\": \"http://your-domain.com\"}")))
    public Uni<Response> create(@Valid LinkShorteningCreationDTO linkShorteningCreationDTO) {
        return Uni.createFrom().item(linkShorteningCreationDTO)
            .chain(dto -> Uni.createFrom().completionStage(this.urlSwiftService.create(dto).subscribeAsCompletionStage()))
            .map(response -> Response.ok(response).build())
            .onFailure().recoverWithItem(failure ->  Response.serverError().entity(failure.getMessage()).build());
    }

}
