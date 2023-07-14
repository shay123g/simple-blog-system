package service;

import errorhandling.ValidationException;
import lombok.RequiredArgsConstructor;
import model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import repository.BlogPostRepository;
import validator.Validator;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final Validator validator;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, Validator validator) {
        this.blogPostRepository = blogPostRepository;
        this.validator = validator;
    }

    public HttpStatus createBlogPost(BlogPost newPost) throws ValidationException {
        validator.validate(newPost);
        blogPostRepository.save(newPost);
        return HttpStatus.CREATED;
    }

    public BlogPost getBlogPostById(long postId) {
        if (blogPostRepository.existsById(postId)) {
            return blogPostRepository.findById(postId).get();
        }
        return null;
    }

    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    public HttpStatus deleteBlogPostById(long postId) {
        if (blogPostRepository.existsById(postId)) {
            blogPostRepository.deleteById(postId);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    public HttpStatus updateBlogPost(long postIdToUpdate, BlogPost newDataBlogPost) throws ValidationException {
        validator.validate(newDataBlogPost);
        Optional<BlogPost> blogPostToFind = blogPostRepository.findById(postIdToUpdate);
        if (blogPostToFind.isPresent()){
            BlogPost blogPostToUpdate = blogPostToFind.get();
            blogPostToUpdate.setAuthor(newDataBlogPost.getAuthor());
            blogPostToUpdate.setContent(newDataBlogPost.getContent());
            blogPostToUpdate.setTitle(newDataBlogPost.getTitle());
            return HttpStatus.OK;
        }
        blogPostRepository.save(newDataBlogPost);
        return HttpStatus.CREATED;
    }

}

