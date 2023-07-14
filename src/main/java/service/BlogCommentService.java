package service;

import errorhandling.ValidationException;
import model.BlogComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import repository.BlogCommentRepository;
import repository.BlogPostRepository;
import validator.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BlogCommentService {
    private final BlogCommentRepository blogCommentRepository;
    private final BlogPostRepository blogPostRepository;

    private final Validator validator;

    @Autowired
    public BlogCommentService(BlogCommentRepository blogCommentRepository, BlogPostRepository blogPostRepository, Validator validator) {
        this.blogCommentRepository = blogCommentRepository;
        this.blogPostRepository = blogPostRepository;
        this.validator = validator;
    }

    public HttpStatus createBlogComment(BlogComment newComment) throws ValidationException {
        validator.validate(newComment);
        blogCommentRepository.save(newComment);
        return HttpStatus.CREATED;
    }

    public List<BlogComment> getCommentsByPostId(long postId) {
        if (blogCommentRepository.existsById(postId)) {
            return blogCommentRepository.findAllById(Collections.singleton(postId));
        }
        return null;
    }

    public HttpStatus deleteBlogComment(long commentId) {
        if (blogCommentRepository.existsById(commentId)) {
            blogCommentRepository.deleteById(commentId);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    public HttpStatus updateBlogComment(long commentIdToUpdate, BlogComment newDataBlogComment) throws ValidationException {
        validator.validate(newDataBlogComment);
        Optional<BlogComment> blogCommentToFind = blogCommentRepository.findById(commentIdToUpdate);
        if (blogCommentToFind.isPresent()){
            BlogComment blogCommentToUpdate = blogCommentToFind.get();
            blogCommentToUpdate.setAuthor(newDataBlogComment.getAuthor());
            blogCommentToUpdate.setContent(newDataBlogComment.getContent());
            blogCommentToUpdate.setTitle(newDataBlogComment.getTitle());
            return HttpStatus.OK;
        }
        blogCommentRepository.save(newDataBlogComment);
        return HttpStatus.CREATED;
    }

}

