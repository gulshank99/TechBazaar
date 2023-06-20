package com.gsa.tech.bazaar.dtos;

import com.gsa.tech.bazaar.entities.Order;
import com.gsa.tech.bazaar.entities.Product;
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
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private int totalPrice;


    private ProductDto product;
    //private OrderDto order;
}
