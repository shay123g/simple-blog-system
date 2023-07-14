package service;

import model.BlogPost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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
    @InjectMocks
    BlogPostService underTest;
    @Test
    void createBlogPost() {
        BlogPost postToSave = new BlogPost(1,"title","content","author");
        HttpStatus expected = HttpStatus.CREATED;
        HttpStatus actual = underTest.createBlogPost(postToSave);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.value(),actual.value())
        );
    }

    @Test
    void getBlogPostById() {
    createBlogPost();

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