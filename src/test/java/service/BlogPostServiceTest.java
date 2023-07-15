package service;

import model.BlogPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import repository.BlogPostRepository;
import validator.Validator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlogPostServiceTest {

    @Mock
    BlogPostRepository blogPostRepository;
    @Mock
    Validator validator;

    BlogPostService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BlogPostService(blogPostRepository, validator);
    }
    @Test
    void createBlogPost() throws Exception {
        BlogPost postToSave = new BlogPost();
        postToSave.setId(1L);
        postToSave.setTitle("title");
        postToSave.setContent("content");
        postToSave.setAuthor("author");

        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(postToSave);
        ResponseEntity<BlogPost> savedPost = underTest.createBlogPost(postToSave);

        assertAll(
                () -> assertNotNull(savedPost),
                () -> assertEquals((postToSave),savedPost.getBody()),
                () ->assertEquals(HttpStatus.CREATED,savedPost.getStatusCode())

        );
    }

    @Test
    void found_getBlogPostById() throws Exception {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("title");
        blogPost.setContent("content");
        blogPost.setAuthor("author");
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(blogPost);
        when(blogPostRepository.findById(any(Long.class))).thenReturn(Optional.of(blogPost));
        ResponseEntity<BlogPost>p = underTest.createBlogPost(blogPost);
        BlogPost foundPost= underTest.getBlogPostById(blogPost.getId());

        assertAll(
                () -> assertNotNull(foundPost),
                () -> assertEquals((blogPost),foundPost));
    }

    @Test
    void getBlogPostByIdShouldReturnNull() {
    }

    @Test
    void getAllBlogPosts() {
    }

    @Test
    void deleteBlogPostById() {
    }

    @Test
    void updateBlogPost() {
    }
}