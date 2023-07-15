package controller;

import errorhandling.ValidationException;
import lombok.Data;
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
@Data
@RequestMapping("/posts")
public class BlogPostController {

    private BlogPostService blogPostService;
    private BlogCommentService blogCommentService;


    @Autowired
    public BlogPostController(BlogPostService blogPostService, BlogCommentService blogCommentService) {
        this.blogPostService = blogPostService;
        this.blogCommentService = blogCommentService;
    }

    /**
     * Get all posts in the system
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<BlogPost>> getAllBlogPosts(){
        return new ResponseEntity<>(blogPostService.getAllBlogPosts(), HttpStatus.OK);
    }

    /**
     * Get post by its ID
     * @param postId - ID to search the post by it
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable long postId){
        BlogPost existingBlogPost = blogPostService.getBlogPostById(postId);
        if (existingBlogPost != null) {
            return new ResponseEntity<>(existingBlogPost, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Create new Post
     * @param newBlogPost - post to save
     * @return the post object saved in DB
     * @throws ValidationException - in case input validation fails
     */
    @PostMapping("/create")
    public ResponseEntity<BlogPost> createNewBlogPost(@RequestBody BlogPost newBlogPost) throws Exception {
        return blogPostService.createBlogPost(newBlogPost);
    }

    /**
     * Add new comment to specific post
     * @param id - the ID of the post to associate the comment to
     * @param newComment - new comment data
     * @return  the post whom comment associated to
     * @throws ValidationException - in case input validation fails
     */
    @PostMapping("/{post-id}/comment")
    public ResponseEntity<BlogPost> addNewCommentToPost(@PathVariable("post-id") long id, @RequestBody BlogComment newComment) throws Exception {
        BlogPost existingPost = blogPostService.getBlogPostById(id);
        if (existingPost != null){
            newComment.setPost(existingPost);
            ResponseEntity<BlogComment> blogComment = blogCommentService.createBlogComment(newComment);
            if (blogComment.getStatusCode() == HttpStatus.CREATED) {
                return new ResponseEntity<>(existingPost, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * Update existing post
     * @param postId - the post id to update
     * @param newBlogPostData - new data for the post
     * @return
     * @throws ValidationException - in case input validation fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updateExistingBlogPost(@PathVariable long postId, @RequestBody BlogPost newBlogPostData) throws Exception {
        return new ResponseEntity<>(blogPostService.updateBlogPost(postId, newBlogPostData));
    }

    /**
     * delete post by ID
     * @param postId - post ID
     * @return- the deleted item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BlogPost> deleteExistingBlogPost(@PathVariable long postId){
        return new ResponseEntity<>(blogPostService.deleteBlogPostById(postId));
    }
}
