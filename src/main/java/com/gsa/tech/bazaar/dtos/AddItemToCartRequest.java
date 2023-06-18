package com.gsa.tech.bazaar.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddItemToCartRequest {
    private String productId;
    private int quantity;
}
