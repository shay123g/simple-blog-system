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

import java.util.List;

@RestController
@RequestMapping("vi/comments")
public class BlogCommentController {

    private BlogCommentService blogCommentService;
    private BlogPostService blogPostService;

    @Autowired
    public BlogCommentController(BlogCommentService blogCommentService, BlogPostService blogPostService) {
        this.blogCommentService = blogCommentService;
        this.blogPostService = blogPostService;
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<List<BlogComment>> getBlogCommentByPostId(@PathVariable long postId){
        BlogPost existingBlogPost = blogPostService.getBlogPostById(postId);
        if (existingBlogPost != null) {
            return new ResponseEntity<>(blogCommentService.getCommentsByPostId(postId),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<BlogComment> createNewBlogComment(@RequestBody BlogComment newBlogComment) throws ValidationException {
        return new ResponseEntity<>(blogCommentService.createBlogComment(newBlogComment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogComment> updateExistingBlogComment(@PathVariable long commentId, @RequestBody BlogComment newBlogCommentData) throws ValidationException {
        return new ResponseEntity<>(blogCommentService.updateBlogComment(commentId, newBlogCommentData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BlogComment> deleteExistingBlogComment(@PathVariable long commentId){
        return new ResponseEntity<>(blogCommentService.deleteBlogComment(commentId));
    }
}
