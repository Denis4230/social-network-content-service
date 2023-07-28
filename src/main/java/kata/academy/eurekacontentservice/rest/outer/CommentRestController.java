package kata.academy.eurekacontentservice.rest.outer;

import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.model.converter.CommentMapper;
import kata.academy.eurekacontentservice.model.dto.CommentPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.CommentResponseDto;
import kata.academy.eurekacontentservice.model.dto.CommentUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class CommentRestController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/{postId}/comments")
    public Response<CommentResponseDto> addComment(@RequestBody @Valid CommentPersistRequestDto dto,
                                                   @PathVariable @Positive Long postId,
                                                   @RequestParam @Positive Long userId) {
        Optional<Post> optionalPost = postService.findById(postId);
        ApiValidationUtil.requireTrue(optionalPost.isPresent(), String.format("Пост с postId %d нет в базе данных", postId));
        Comment comment = CommentMapper.toEntity(dto);
        comment.setPost(optionalPost.get());
        comment.setUserId(userId);
        return Response.ok(CommentMapper.toDto(commentService.addComment(comment)));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public Response<CommentResponseDto> updateComment(@RequestBody @Valid CommentUpdateRequestDto dto,
                                                      @PathVariable @Positive Long postId,
                                                      @PathVariable @Positive Long commentId,
                                                      @RequestParam @Positive Long userId) {
        Optional<Comment> commentOptional = commentService.findByIdAndPostIdAndUserId(userId, postId, commentId);
        ApiValidationUtil.requireTrue(commentOptional.isPresent(), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        return Response.ok(CommentMapper.toDto(commentService.updateComment(CommentMapper.toEntity(dto, commentOptional.get()))));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<Void> deleteComment(@PathVariable @Positive Long postId,
                                        @PathVariable @Positive Long commentId,
                                        @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(commentService.existsByIdAndPostIdAndUserId(commentId, postId, userId), String.format("Комментарий с commentId %d, postId %d и userId %d нет в базе данных", commentId, postId, userId));
        commentService.deleteById(commentId);
        return Response.ok();
    }
}
