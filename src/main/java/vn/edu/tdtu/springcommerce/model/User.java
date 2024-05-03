package vn.edu.tdtu.springcommerce.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    private String address;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(message = "{error.invalid_email}")
    private String email;

    private String phoneNumber;
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn (name = "ROLE_ID", referencedColumnName = "ID")}
    )
    private List<Role> roles;

//    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.ALL)
    private List<Order> orders;

    //custom lai constructor
    public User(){}

    public User(User user) {
        this.name = user.getName();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", address=" + address + ", email=" + email + "]";
    }
}//create table mapping trong db
