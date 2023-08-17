package com.gsa.tech.bazaar.dtos;

import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoleDto {

    private String roleId;
    private String roleName;
}
