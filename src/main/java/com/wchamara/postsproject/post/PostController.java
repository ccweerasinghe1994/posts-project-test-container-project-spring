package com.wchamara.postsproject.post;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final static Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("")
    public List<Post> getPosts() {
        log.info("Fetching all posts");
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Post> findById(@PathVariable int id) {
        log.info("Fetching post with id: {}", id);
        return Optional.ofNullable(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody @Valid Post post) {
        log.info("Creating post: {}", post);
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable int id, @RequestBody Post post) {
        Optional<Post> existingPost = postRepository.findById(id);

        if (existingPost.isPresent()) {
            log.info("Updating post with id: {}", id);
            Post updatedPost = new Post(
                    existingPost.get().id(),
                    existingPost.get().userId(),
                    post.title(),
                    post.body(),
                    existingPost.get().version()
            );
            return postRepository.save(updatedPost);
        } else {
            throw new PostNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable int id) {
        postRepository.deleteById(id);
    }
}
