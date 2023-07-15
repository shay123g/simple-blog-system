package controller;

import errorhandling.ValidationException;
import model.BlogComment;
import model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BlogCommentService;
import service.BlogPostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class BlogCommentController {

    private BlogCommentService blogCommentService;
    private BlogPostService blogPostService;

    @Autowired
    public BlogCommentController(BlogCommentService blogCommentService, BlogPostService blogPostService) {
        this.blogCommentService = blogCommentService;
        this.blogPostService = blogPostService;
    }

    /**
     * Get all comments associated with specific post
     * @param postId - the ID of the post to search and retrieve its comments
     * @return List of comments associated with the post along with OK status or NOT_FOUND status in case the post not found
     */
    @GetMapping("/{post-id}")
    public ResponseEntity<List<BlogComment>> getBlogCommentByPostId(@PathVariable long postId){
        BlogPost existingBlogPost = blogPostService.getBlogPostById(postId);
        if (existingBlogPost != null) {
            return new ResponseEntity<>(blogCommentService.getCommentsByPostId(postId),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Create new comment without associate it to post
     * @param newBlogComment - data for comment
     * @return the created object
     * @throws ValidationException - in case input validation fails
     */
    @PostMapping("/create")
    public ResponseEntity<BlogComment> createNewBlogComment(@RequestBody BlogComment newBlogComment) throws Exception {
        return blogCommentService.createBlogComment(newBlogComment);
    }

    /**
     * Update existing comment by its ID
     * @param commentId the ID of the comment
     * @param newBlogCommentData - new data to update
     * @return the updated comment
     * @throws ValidationException - in case input validation fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlogComment> updateExistingBlogComment(@PathVariable long commentId, @RequestBody BlogComment newBlogCommentData) throws Exception {
        return new ResponseEntity<>(blogCommentService.updateBlogComment(commentId, newBlogCommentData));
    }

    /**
     * Delete comment by its ID
     * @param commentId the ID of the comment
     * @return the deleted object
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BlogComment> deleteExistingBlogComment(@PathVariable long commentId){
        return new ResponseEntity<>(blogCommentService.deleteBlogComment(commentId));
    }
}
