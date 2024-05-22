package com.wchamara.postsproject.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    private static final DockerImageName postgresSQLImage = DockerImageName.parse("postgres:16.3");
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(postgresSQLImage);

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void setUp() {
        List<Post> posts = List.of(
                new Post(1, 1, "title1", "body1", null),
                new Post(2, 1, "title2", "body2", null),
                new Post(3, 1, "title3", "body3", null)
        );
        postRepository.saveAll(posts);
    }

    @Test
    void checkDbConnection() {
        assertThat(container.isRunning()).isTrue();
        assertThat(container.getJdbcUrl()).isNotNull();
        assertThat(container.getDatabaseName()).isNotNull();
        assertThat(container.getUsername()).isNotNull();

    }

    @Test
    @DisplayName("Should return all posts")
    void shouldReturnAllPosts() {
        List<Post> getAllPosts = postRepository.findAll();
        assertThat(getAllPosts.size()).isEqualTo(3);
    }

    @DisplayName("Should return post by title")
    @Test
    void shouldReturnPostByTitle() {
        Post post = postRepository.findByTitle("title1");
        assertThat(post).isNotNull();
        assertThat(post.title()).isEqualTo("title1");
    }

}