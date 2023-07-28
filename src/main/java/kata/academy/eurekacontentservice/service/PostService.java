package kata.academy.eurekacontentservice.service;

import kata.academy.eurekacontentservice.model.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post addPost(Post post);

    Post updatePost(Post post);

    void deleteById(Long postId);

    boolean existsByIdAndUserId(Long postId, Long userId);

    boolean existsById(Long postId);

    Optional<Post> findById(Long postId);

    Page<Post> findByTagsOrAllPosts(List<String> tags, Pageable pageable);

    Page<Post> findAllTopPostLike(Long count, Pageable pageable);

    Page<Post> findPostsByUserIdOrUserIdEndTags(Long userId, List<String> tags, Pageable pageable);

    Optional<Post> findByIdAndUserId(Long postId, Long userId);
}
