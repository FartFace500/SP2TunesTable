package app.dtos;

import app.entities.Badge;
import app.entities.Comment;
import app.entities.Stat;
import app.security.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String password;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Set<String> roles = new HashSet();
    List<Comment> comments = new ArrayList<>();
    List<Badge> badges = new ArrayList<>();
    Stat stats;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRolesAsStrings();
        this.comments = user.getComments();
        this.badges = user.getBadges();
        this.stats = user.getStats();
    }

    public static UserDTO getTrimmedDTO(User user) {
        UserDTO userDTO = new UserDTO(user);
        userDTO.setPassword(null);
        userDTO.setRoles(null);
        userDTO.getStats().setUser(null);
        userDTO.getStats().setId(null);
        userDTO.getBadges().forEach(badge -> {badge.setUser(null); badge.setId(null);});
        userDTO.getComments().forEach(comment -> {comment.setUser(null); comment.setId(null);});
        return userDTO;
    }

    public static UserDTO getLoginDTO(User user) {
        UserDTO userDTO = new UserDTO(user);
        userDTO.getStats().setUser(null);
        userDTO.getBadges().forEach(badge -> badge.setUser(null));
        userDTO.getComments().forEach(comment -> comment.setUser(null));
        return userDTO;
    }

    public void addRoles(Set<String> roles) {
        this.roles = roles;
    }

    static public List<UserDTO> fromUserDTOList(List<User> users) {
        return users.stream().collect(Collectors.toSet()).stream().map(UserDTO::getTrimmedDTO).collect(Collectors.toList());
    }
}
