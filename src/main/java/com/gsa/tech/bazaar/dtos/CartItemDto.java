package com.gsa.tech.bazaar.dtos;

import com.gsa.tech.bazaar.entities.Cart;
import com.gsa.tech.bazaar.entities.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartItemDto {
    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private int totalPrice;

    //mapping Cart

    //private Cart cart;
}
