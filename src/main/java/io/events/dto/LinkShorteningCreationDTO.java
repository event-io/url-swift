package io.events.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkShorteningCreationDTO {
    
    @JsonProperty("original_link")
    @NotBlank
    @Length(max = 512)
    private String originalLink;

    @JsonProperty("shortened_link")
    @Length(min = 7, max = 7)
    private String shortnedLink;
}
