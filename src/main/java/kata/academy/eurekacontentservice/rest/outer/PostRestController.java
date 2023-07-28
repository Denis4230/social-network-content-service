package kata.academy.eurekacontentservice.rest.outer;

import kata.academy.eurekacontentservice.api.Response;
import kata.academy.eurekacontentservice.feign.LikeServiceFeignClient;
import kata.academy.eurekacontentservice.model.converter.PostMapper;
import kata.academy.eurekacontentservice.model.dto.PostPersistRequestDto;
import kata.academy.eurekacontentservice.model.dto.PostUpdateRequestDto;
import kata.academy.eurekacontentservice.model.entity.Post;
import kata.academy.eurekacontentservice.service.PostService;
import kata.academy.eurekacontentservice.util.ApiValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;
    private final LikeServiceFeignClient likeServiceFeignClient;


    @PostMapping
    public Response<Post> addPost(@RequestBody @Valid PostPersistRequestDto dto,
                                  @RequestParam @Positive Long userId) {
        Post post = PostMapper.toEntity(dto);
        post.setUserId(userId);
        return Response.ok(postService.addPost(post));
    }

    @PutMapping("/{postId}")
    public Response<Post> updatePost(@RequestBody @Valid PostUpdateRequestDto dto,
                                     @PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId) {
        Optional<Post> postOptional = postService.findByIdAndUserId(postId, userId);
        ApiValidationUtil.requireTrue(postOptional.isPresent(), String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        return Response.ok(postService.updatePost(PostMapper.toEntity(dto, postOptional.get())));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable @Positive Long postId,
                                     @RequestParam @Positive Long userId) {
        ApiValidationUtil.requireTrue(postService.existsByIdAndUserId(postId, userId), String.format("Пост с postId %d и userId %d нет в базе данных", postId, userId));
        postService.deleteById(postId);
        return Response.ok();
    }

    @GetMapping
    Response<Page<Post>> getPostPage(@RequestBody(required = false) List<String> tags,
                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                     @RequestParam(value = "page", required = false, defaultValue = "0") Integer page){
        return Response.ok(postService.findByTagsOrAllPosts(tags, PageRequest.of(page, size)));
    }

    @GetMapping("/top")
    Response<Page<Post>> getPostPageByTop(@RequestParam(defaultValue = "100") @Positive Long count,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page){
        return Response.ok(postService.findAllTopPostLike(count, PageRequest.of(page, size)));
    }

    @GetMapping("/owner")
    Response<Page<Post>> getPostPageByOwner(@RequestBody(required = false) List<String> tags,
                                            @RequestParam @Positive Long userId,
                                            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page){
        return Response.ok(postService.findPostsByUserIdOrUserIdEndTags(userId,tags, PageRequest.of(page, size)));
    }
}
