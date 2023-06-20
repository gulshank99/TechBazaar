package com.gsa.tech.bazaar.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "jpa_user")
public class User {

      @Id
      //@GeneratedValue(strategy = GenerationType.IDENTITY)
      private String userId;
      @Column(name = "user_name")
      private String name;
      @Column(name = "user_email",unique = true)
      private String email;
      @Column(name = "user_password",length = 10)
      private String password;
      private String gender;
      @Column( length = 1000)
      private String about;
      @Column(name = "user_image_name" )
      private String imageName;

      @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
      private List<Order> orders = new ArrayList<>();

}
