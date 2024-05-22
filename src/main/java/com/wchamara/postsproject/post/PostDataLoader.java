package com.wchamara.postsproject.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PostDataLoader implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(PostDataLoader.class);
    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    public PostDataLoader(ObjectMapper objectMapper, PostRepository postRepository) {
        this.objectMapper = objectMapper;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {

        String POST_JSON = "/data/posts.json";
        log.info("Loading posts into data base from " + POST_JSON);

        try(InputStream inputStream = TypeReference.class.getResourceAsStream(POST_JSON)) {
            Posts response = objectMapper.readValue(inputStream, Posts.class);
            postRepository.saveAll(response.posts());
        }catch (Exception e){
          throw new RuntimeException("Failed to load posts from " + POST_JSON, e);
        }

        }
    }
}
