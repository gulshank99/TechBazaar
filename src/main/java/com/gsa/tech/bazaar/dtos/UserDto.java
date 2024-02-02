package com.gsa.tech.bazaar.dtos;

import com.gsa.tech.bazaar.entities.Role;
import com.gsa.tech.bazaar.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
        private String userId;
        @Size(min = 3,max = 20,message = "Invalid Name !!")
        private String name;

        @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !!")

        @NotBlank(message="Email is requires !!")
        private String email;

        @NotBlank(message="Password is requires !!")
        private String password;

        @Size(min = 4,max = 6,message = "Invalid Gender !!")
        private String gender;

        @NotBlank(message="Write something about Yourself !!")
        private String about;

      //  @ImageNameValid()
        private String imageName;

        private Set<RoleDto> roles = new HashSet<>();


        //@Pattern
        //@Custom Validator
}
