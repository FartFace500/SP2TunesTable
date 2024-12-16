package app.entities;

import app.security.entities.User;
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
    private Integer id;
    private String message;
    @ManyToOne
    private User user;

    public Comment(String message) {
        this.message = message;
    }
}
