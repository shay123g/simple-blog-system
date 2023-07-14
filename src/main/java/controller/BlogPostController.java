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
import java.util.Optional;

@RestController
@RequestMapping("/v1/posts")
public class BlogPostController {

    private BlogPostService blogPostService;
    private BlogCommentService blogCommentService;


    @Autowired
    public BlogPostController(BlogPostService blogPostService, BlogCommentService blogCommentService) {
        this.blogPostService = blogPostService;
        this.blogCommentService = blogCommentService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<BlogPost>> getAllBlogPosts(){
        return new ResponseEntity<>(blogPostService.getAllBlogPosts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable long postId){
        BlogPost existingBlogPost = blogPostService.getBlogPostById(postId);
        if (existingBlogPost != null) {
            return new ResponseEntity<>(existingBlogPost, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<BlogPost> createNewBlogPost(@RequestBody BlogPost newBlogPost) throws ValidationException {
        return new ResponseEntity<>(blogPostService.createBlogPost(newBlogPost));
    }

    @PostMapping("/{post-id}/comment")
    public ResponseEntity<BlogPost> addNewCommentToPost(@PathVariable("post-id") long id, @RequestBody BlogComment newComment) throws ValidationException {
        BlogPost existingPost = blogPostService.getBlogPostById(id);
        if (existingPost != null){
            newComment.setPost(existingPost);
            blogCommentService.createBlogComment(newComment);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updateExistingBlogPost(@PathVariable long postId, @RequestBody BlogPost newBlogPostData) throws ValidationException {
        return new ResponseEntity<>(blogPostService.updateBlogPost(postId, newBlogPostData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BlogPost> deleteExistingBlogPost(@PathVariable long postId){
        return new ResponseEntity<>(blogPostService.deleteBlogPostById(postId));
    }
}
