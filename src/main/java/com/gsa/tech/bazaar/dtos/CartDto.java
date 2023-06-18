package com.gsa.tech.bazaar.dtos;

import com.gsa.tech.bazaar.entities.CartItem;
import com.gsa.tech.bazaar.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartDto {

    private String cartId;
    private Date createdAt;

    private UserDto user;
    //mapping cart-items

       private List<CartItemDto> items = new ArrayList<>();
}
