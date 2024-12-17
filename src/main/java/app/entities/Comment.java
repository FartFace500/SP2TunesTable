package app.entities;

import app.security.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer id;
    private String message;
    @ManyToOne
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ToString.Exclude
    private User user;

    public Comment(String message) {
        this.message = message;
    }
}
