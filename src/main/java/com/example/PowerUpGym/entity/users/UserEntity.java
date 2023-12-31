package com.example.PowerUpGym.entity.users;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mst_users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "image")
    private String image;

    @OneToOne
    @JoinColumn(name = "role_id")
    private UserRoleEntity role;

    @OneToOne(mappedBy = "user")
    private PlayersEntity player;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private TrainerEntity trainer;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AdminEntity admin;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEntity role = getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Builder
    public UserEntity(String fullName, String username, String email, String phoneNumber, String password, String image ,UserRoleEntity role, PlayersEntity player, TrainerEntity trainer) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.image = image;
        this.role = role;
        this.player = player;
        this.trainer = trainer;
    }
}