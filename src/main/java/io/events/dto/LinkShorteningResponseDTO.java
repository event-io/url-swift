package io.events.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkShorteningResponseDTO {

    @JsonProperty("shortened_link")
    private String shortnedLink;

    @JsonProperty("original_link")
    private String originalLink;

    @JsonProperty("created_at")
    private Instant createdAt;

}
