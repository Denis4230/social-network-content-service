package kata.academy.eurekacontentservice.model.dto;

import javax.validation.constraints.NotBlank;

public record CommentUpdateRequestDto(
        @NotBlank String text) {
}
