package com.gsa.tech.bazaar.entities;

import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    private String cartId;
    private Date createdAt;
    @OneToOne
    private User user;
    //mapping cart-items


    @OneToMany(mappedBy = "cart",cascade =CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
}
