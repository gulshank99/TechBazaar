package com.gsa.tech.bazaar.services;

import com.gsa.tech.bazaar.dtos.CreateOrderRequest;
import com.gsa.tech.bazaar.dtos.OrderDto;
import com.gsa.tech.bazaar.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //Create Order
    OrderDto createOrder(CreateOrderRequest createOrderRequest);

    //Remove Order
    void removeOrder(String orderId);

    //Get Order of User
   List<OrderDto> getOrdersOfUser(String userId);

    //Get order
    PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);


}
