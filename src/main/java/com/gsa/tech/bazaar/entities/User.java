package com.gsa.tech.bazaar.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "jpa_user")
public class User implements UserDetails {

      @Id
      //@GeneratedValue(strategy = GenerationType.IDENTITY)
      private String userId;
      @Column(name = "user_name")
      private String name;
      @Column(name = "user_email",unique = true)
      private String email;
      @Column(name = "user_password",length = 500)
      private String password;
      private String gender;
      @Column( length = 1000)
      private String about;
      @Column(name = "user_image_name" )
      private String imageName;

      @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
      private List<Order> orders = new ArrayList<>();



      // Spring Security Part

      //Must have to implement
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
      }

      @Override
      public String getUsername() {
            return this.email;
      }
      @Override
      public String getPassword() {
            return this.password;
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
}
