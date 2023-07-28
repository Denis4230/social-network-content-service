package kata.academy.eurekacontentservice.service.impl;

import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.entity.Comment;
import kata.academy.eurekacontentservice.repository.CommentRepository;
import kata.academy.eurekacontentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final LikeServiceFeignClient likeServiceFeignClient;

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
        likeServiceFeignClient.deleteByCommentId(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findByIdAndPostIdAndUserId(Long commentId, Long postId, Long userId) {
        return commentRepository.findByIdAndPostIdAndUserId(commentId, postId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndPostIdAndUserId(Long commentId, Long postId, Long userId) {
        return commentRepository.existsByIdAndPostIdAndUserId(commentId, postId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long commentId) {
        return commentRepository.existsById(commentId);
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        List<Long> commentIds = commentRepository.findAllIdsByPostId(postId);
        commentRepository.deleteAllByIdInBatch(commentIds);
        likeServiceFeignClient.deleteAllByCommentIds(commentIds);
    }
}
