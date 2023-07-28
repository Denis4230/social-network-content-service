package kata.academy.eurekacontentservice.model.converter;

import kata.academy.eurekacontentservice.model.dto.CommentPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.model.dto.CommentUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Comment;

import java.time.LocalDateTime;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toEntity(CommentPersistRequestDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.text());
        comment.setCreateData(LocalDateTime.now());
        return comment;
    }

    public static Comment toEntity(CommentUpdateRequestDto dto, Comment comment) {
        comment.setText(dto.text());
        return comment;
    }

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUserId(),
                comment.getText(),
                comment.getPost().getId(),
                comment.getCreateData()
        );
    }
}
