package com.gsa.tech.bazaar.entities;

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
@Entity
@Table(name = "orders")
public class Order{
    @Id
    private String orderId;
    // PENDING,DISPATCHED,DELIVERED,
    // ENUM
    private String orderStatus;
    //NOT-PAID,PAID
    //ENUM
    //boolean-false=>NOT-PAID || true=>PAID
    private String paymentStatus;
    private int orderAmount;
    @Column(length=1000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;

    //User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    //Order items
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


}
