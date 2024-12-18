package app.entities;

import app.security.entities.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer id;
    private String name;
    private String description;
    @ManyToMany
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    public Badge(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static List<Badge> getTrimmedBadges(List<Badge> badges) {
        badges.forEach(badge -> badge.getUsers().clear());
        return badges;
    }
}
