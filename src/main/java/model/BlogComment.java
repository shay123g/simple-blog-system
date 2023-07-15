/**
 * This class represent Comment object in DB.
 * One comment can have only one post hence the @OneToOne relationship.
 * Using LAZY fetch for better performance
 */
package model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    public BlogPost post;

    @Autowired
    public BlogComment(long id, String title, String content, String author, BlogPost post) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.post = post;
    }

    /**
     * Two comments consider equivalent if and only if title, content and author fields are equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogComment that = (BlogComment) o;

        if (!title.equals(that.title)) return false;
        if (!content.equals(that.content)) return false;
        return author.equals(that.author);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }
}
