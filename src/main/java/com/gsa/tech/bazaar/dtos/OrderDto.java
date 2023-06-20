package com.gsa.tech.bazaar.dtos;

import com.gsa.tech.bazaar.entities.OrderItem;
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
public class OrderDto {
    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private int orderAmount;

    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;

    //User
   // private UserDto user;

    //Order items
     private List<OrderItemDto> orderItems = new ArrayList<>();
}
