package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.repository.PostRepository;
import kata.academy.eurekacontentservice.service.CommentService;
import kata.academy.eurekacontentservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Override
    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(Long postId) {
        commentService.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
        likeServiceFeignClient.deleteByPostId(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndUserId(Long postId, Long userId) {
        return postRepository.existsByIdAndUserId(postId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long postId) {
        return postRepository.existsById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    public Page<Post> findByTagsOrAllPosts(List<String> tags, Pageable pageable) {
        if (tags != null) {
            return postRepository.findByTags(tags, pageable);
        } else {
            return postRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Post> findAllTopPostLike(Long count, Pageable pageable) {
        List<Long> idPost = likeServiceFeignClient.findAllTopPostLike(count);
        return postRepository.findAllTopPostLike(idPost, pageable);
    }

    @Override
    public Page<Post> findPostsByUserIdOrUserIdEndTags(Long userId, List<String> tags, Pageable pageable) {
        if (tags != null){
            return postRepository.findPostsByUserIdAndTags(userId, tags, pageable);
        } else {
            return postRepository.findPostsByUserId(userId, pageable);
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Post> findByIdAndUserId(Long postId, Long userId) {
        return postRepository.findByIdAndUserId(postId, userId);
    }
}
