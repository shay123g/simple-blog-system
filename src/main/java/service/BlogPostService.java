package service;

import errorhandling.ValidationException;
import lombok.RequiredArgsConstructor;
import model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Validate the input from the user then create new post
     * @param newPost - new post data
     * @return CREATED status when post create
     * @throws ValidationException - in case input validation fails
     */
    public ResponseEntity<BlogPost> createBlogPost(BlogPost newPost) throws Exception {
        validator.validate(newPost);
        BlogPost created = blogPostRepository.save(newPost);
        return new ResponseEntity<>(created,HttpStatus.CREATED);
    }

    /**
     * First, find if post exist in DB, then retrieve it.
     * @param postId the post ID to search.
     * @return the post.
     */
    public BlogPost getBlogPostById(long postId) {
        if (blogPostRepository.existsById(postId)) {
            return blogPostRepository.findById(postId).get();
        }
        return null;
    }

    /**
     * Get all the post from the DB
     * @return List of posts or empty list in case no post found
     */
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    /**
     * Delete post by id.
     * First, check if the post exists
     * @param postId
     * @return OK status if the operation completed successfully or NOT_FOUND status in case the post not exist in DB
     */
    public HttpStatus deleteBlogPostById(long postId) {
        if (blogPostRepository.existsById(postId)) {
            blogPostRepository.deleteById(postId);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    /**
     * Validate the input from the user then update existing post
     * or create new one in case there is no post to update
     * @param postIdToUpdate - search the post in DB according to its ID
     * @param newDataBlogPost - new data for the post
     * @return  OK if post update or CREATED if new post created
     * @throws ValidationException - in case input validation fails
     */
    public HttpStatus updateBlogPost(long postIdToUpdate, BlogPost newDataBlogPost) throws Exception {
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

