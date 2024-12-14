package app.dtos;

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
    List<String> comments = new ArrayList<>();
    List<String> badges = new ArrayList<>();
    List<String> stats = new ArrayList<>();

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.comments = Arrays.stream(user.getComments().split("!")).toList();
        this.badges = Arrays.stream(user.getBadges().split("!")).toList();
        this.stats = Arrays.stream(user.getStats().split("!")).toList();
    }

    public static UserDTO getTrimmedDto(User user) {
        UserDTO userDTO = new UserDTO(user);
        userDTO.setPassword(null);
        userDTO.setRoles(null);
        return userDTO;
    }

    public void addRoles(Set<String> roles) {
        this.roles = roles;
    }

    static public List<UserDTO> fromUserDTOList(List<User> users) {
        return users.stream().collect(Collectors.toSet()).stream().map(UserDTO::getTrimmedDto).collect(Collectors.toList());
    }
}
