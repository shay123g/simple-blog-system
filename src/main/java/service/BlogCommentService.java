package service;

import errorhandling.ValidationException;
import model.BlogComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Validate user input then create new comment
     * @param newComment - date for comment
     * @return CREATED status if object created
     * @throws ValidationException - in case input validation fails
     */
    public ResponseEntity<BlogComment> createBlogComment(BlogComment newComment) throws Exception {
        validator.validate(newComment);
        BlogComment saveComment = blogCommentRepository.save(newComment);
        return new ResponseEntity<>(saveComment,HttpStatus.CREATED);
    }

    /**
     * Get list of all comments associate with specific post
     * @param postId the post ID
     * @return List of comments
     */
    public List<BlogComment> getCommentsByPostId(long postId) {
        if (blogCommentRepository.existsById(postId)) {
            return blogCommentRepository.findAllById(Collections.singleton(postId));
        }
        return null;
    }

    /**
     * Check if comment exist and delete it
     * @param commentId comment ID to search
     * @return OK status if comment deleted or NOT_FOUND if comment not exists.
     */
    public HttpStatus deleteBlogComment(long commentId) {
        if (blogCommentRepository.existsById(commentId)) {
            blogCommentRepository.deleteById(commentId);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    /**
     * Validate the input from the user then update existing comment
     * or create new one in case there is no comment to update
     * @param commentIdToUpdate - search the comment in DB according to its ID
     * @param newDataBlogComment - new data for the comment
     * @return  OK if comment update or CREATED if new comment created
     * @throws ValidationException - in case input validation fails
     */
    public HttpStatus updateBlogComment(long commentIdToUpdate, BlogComment newDataBlogComment) throws Exception {
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

