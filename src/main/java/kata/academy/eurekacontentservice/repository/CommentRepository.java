package kata.academy.eurekacontentservice.repository;

import kata.academy.eurekacontentservice.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndPostIdAndUserId(Long commentId, Long postId, Long userId);

    boolean existsByIdAndPostIdAndUserId(Long commentId, Long postId, Long userId);

    @Query("""
            SELECT c.id
            FROM Comment c
            WHERE c.post.id = :postId
                                """)
    List<Long> findAllIdsByPostId(Long postId);
}
