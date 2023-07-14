package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import service.BlogPostService;

@Entity
@Data
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogComment)) return false;

        return id != null && id.equals(((BlogComment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
