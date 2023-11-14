package io.events.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(http|https)://([a-zA-Z0-9-.]*)(:[0-9]{1,5})?(/.*)?$", message = "Invalid URL")
    private String originalLink;

    @JsonProperty("shortened_link")
    @Length(min = 7, max = 7)
    @Nullable
    private String shortnedLink;
}
