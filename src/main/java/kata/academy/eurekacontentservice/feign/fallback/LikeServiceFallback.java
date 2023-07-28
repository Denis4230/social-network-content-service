package kata.academy.eurekacontentservice.feign.fallback;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.ResponseEntity;

import java.util.List;

record LikeServiceFallback(Throwable cause) implements LikeServiceFeignClient {

    @Override
    public ResponseEntity<Void> deleteByPostId(Long postId) {
        throw new NoFallbackAvailableException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public ResponseEntity<Void> deleteByCommentId(Long commentId) {
        throw new NoFallbackAvailableException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public ResponseEntity<Void> deleteAllByCommentIds(List<Long> commentIds) {
        throw new NoFallbackAvailableException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

    @Override
    public List<Long> findAllTopPostLike(Long count) {
        throw new NoFallbackAvailableException("Сервис временно недоступен. Причина -> %s"
                .formatted(cause.getMessage()), cause);
    }

}
