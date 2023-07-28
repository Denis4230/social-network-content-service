package kata.academy.eurekacontentservice.feign;

import kata.academy.eurekacontentservice.feign.fallback.LikeServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Positive;
import java.util.List;

@FeignClient(value = "eureka-like-service", fallbackFactory = LikeServiceFallbackFactory.class)
public interface LikeServiceFeignClient {

    @DeleteMapping("/api/internal/v1/posts/{postId}/post-likes")
    ResponseEntity<Void> deleteByPostId(@PathVariable @Positive Long postId);

    @DeleteMapping("/api/internal/v1/comments/{commentId}/comment-likes")
    ResponseEntity<Void> deleteByCommentId(@PathVariable @Positive Long commentId);

    @DeleteMapping("/api/internal/v1/comments/comment-likes")
    ResponseEntity<Void> deleteAllByCommentIds(@RequestBody List<Long> commentIds);

    @GetMapping("/api/internal/v1/posts/{count}/top")
    List<Long> findAllTopPostLike(@PathVariable(name = "count") Long count);
}
