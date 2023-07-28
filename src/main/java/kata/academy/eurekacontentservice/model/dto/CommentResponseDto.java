package kata.academy.eurekacontentservice.model.dto;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        Long userId,
        String text,
        Long postId,
        LocalDateTime createData) {
}
