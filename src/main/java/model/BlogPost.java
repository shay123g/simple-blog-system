/**
 * This class represent Post object in DB.
 * One post can have more then one comment hence the @OneToMany relationship.
 * Using LAZY fetch for better performance
 */
package model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table
@Data
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String author;


    @OneToMany(
            mappedBy = "BlogPost",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    public List<BlogComment> comments;

    public BlogPost(){};

    @Autowired
    public BlogPost(long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    /**
     * Two posts consider equivalent if and only if title, content and author fields are equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogPost blogPost = (BlogPost) o;

        if (!title.equals(blogPost.title)) return false;
        if (!content.equals(blogPost.content)) return false;
        if (!author.equals(blogPost.author)) return false;
        return comments.equals(blogPost.comments);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + comments.hashCode();
        return result;
    }
}
