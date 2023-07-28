package kata.academy.eurekacontentservice.repository;

import feign.Param;
import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByIdAndUserId(Long postId, Long userId);

    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM posts p JOIN posts_tags t ON p.id = t.post_id WHERE t.tag IN(:tags)")
    Page<Post> findByTags(@Param("tags")List<String> tags, Pageable pageable);

    @Query("select p from Post p where p.id in(:id)")
    Page<Post> findAllTopPostLike(@Param("id") List<Long> id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM posts p JOIN posts_tags t ON p.id = t.post_id WHERE t.tag IN(:tags) AND p.user_id = :userId")
    Page<Post> findPostsByUserIdAndTags(@Param("userId")Long userId,
                                        @Param("tags")List<String> tags, Pageable pageable);

    Page<Post> findPostsByUserId(Long userId, Pageable pageable);

    Optional<Post> findByIdAndUserId(Long postId, Long userId);
}
