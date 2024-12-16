package app.security.entities;

import app.dtos.UserDTO;
import app.entities.Badge;
import app.entities.Comment;
import app.entities.Stat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
@Entity
@Table(name = "users")
@NamedQueries(@NamedQuery(name = "User.deleteAllRows", query = "DELETE from User"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable, ISecurityUser {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "username", length = 25)
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private Stat stats;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    List<Badge> badges;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    List<Comment> comments;

    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_name", referencedColumnName = "username")}, inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Role> roles = new HashSet<>();

    public Set<String> getRolesAsStrings() {
        if (roles.isEmpty()) {
            return null;
        }
        Set<String> rolesAsStrings = new HashSet<>();
        roles.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
    }

    public void copyInfoFromDto(UserDTO dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.comments = dto.getComments();
        this.badges = dto.getBadges();
        this.stats = dto.getStats();
    }

    public User(UserDTO dto){
        this.username = dto.getUsername();
        this.password = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        this.comments = dto.getComments();
        this.badges = dto.getBadges();
        this.stats = dto.getStats();
    }

    public User(String userName, String userPass) {
        this.username = userName;
        this.password = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public User(String userName, Set<Role> roleEntityList) {
        this.username = userName;
        this.roles = roleEntityList;
    }

    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(String userRole) {
        roles.stream()
                .filter(role -> role.getRoleName().equals(userRole))
                .findFirst()
                .ifPresent(role -> {
                    roles.remove(role);
                    role.getUsers().remove(this);
                });
    }
}

