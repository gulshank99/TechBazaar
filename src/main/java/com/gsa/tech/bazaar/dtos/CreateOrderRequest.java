package com.gsa.tech.bazaar.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateOrderRequest {

    @NotBlank(message="Cart id is required")
    private String cartId;
    @NotBlank(message="User id is required")
    private String userId;
    @NotBlank(message="required")
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message="required")
    private String billingAddress;
    @NotBlank(message="required")
    private String billingPhone;
    @NotBlank(message="required")
    private String billingName;

}
