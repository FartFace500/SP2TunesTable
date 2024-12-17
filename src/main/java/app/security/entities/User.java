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
import java.util.ArrayList;
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
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})
    private Stat stats = new Stat();
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})
    List<Badge> badges = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})
    List<Comment> comments = new ArrayList<>();

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

    @PrePersist
    public void SetRelations(){
        this.stats.setUser(this);
        this.badges.forEach(badge -> badge.getUsers().add(this));
        this.comments.forEach(comment -> comment.setUser(this));
    }

    public void addBadge(Badge badge) {
        if (this.badges.stream().noneMatch(b -> b.getId().equals(badge.getId()))){
        badge.getUsers().add(this);
        this.badges.add(badge);
        }
    }

    public void clearBadges(){
        badges.forEach(badge -> badge.getUsers().remove(this));
        this.badges.clear();
    }

    public void copyInfoFromDto(UserDTO dto) {
        List<Comment> commentList = dto.getComments();
        commentList.forEach(comment -> comment.setUser(this));
        this.comments = commentList;
        Stat stat = dto.getStats();
        stat.setUser(this);
        this.stats = stat;
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

