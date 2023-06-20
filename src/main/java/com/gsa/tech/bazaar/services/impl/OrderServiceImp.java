package com.gsa.tech.bazaar.services.impl;

import com.gsa.tech.bazaar.controller.CategoryController;
import com.gsa.tech.bazaar.dtos.CreateOrderRequest;
import com.gsa.tech.bazaar.dtos.OrderDto;
import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.entities.*;
import com.gsa.tech.bazaar.exceptions.BadApiRequestException;
import com.gsa.tech.bazaar.exceptions.ResourceNotFoundEception;
import com.gsa.tech.bazaar.helper.Helper;
import com.gsa.tech.bazaar.repositories.CartRepository;
import com.gsa.tech.bazaar.repositories.OrderRepository;
import com.gsa.tech.bazaar.repositories.UserRepository;
import com.gsa.tech.bazaar.services.OrderService;
import lombok.Lombok;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {
    
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        //Fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User Not found with the given userId "));
        //Fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundEception("Cart Not found on the server "));

        List<CartItem> cartItems = cart.getItems();
        if(cartItems.size() <= 0){
            throw new BadApiRequestException("Invalid number of items in cart !!");
        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();
//        --------------------------------------------------------------------------
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
//            CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            logger.info("cartItems: {}", cartItems);
            logger.info("cartItem: {}", cartItem);
            logger.info("orderItem: {}", orderItem);
            logger.info("orderAmount: {}", orderAmount);

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;



        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDto.class);
//        --------------------------------------------------------------------------

//        //OrderItem,amount
//        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
//        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
//
//           // Convert  CartItem->OrderItem
//            OrderItem orderItem = OrderItem
//                    .builder()
//                    .quantity(cartItem.getQuantity())
//                    .product(cartItem.getProduct())
//                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
//                    .order(order)
//                    .build();
//
//
//            return orderItem;
//
//        }).collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//        order.setOrderAmount(orderAmount.get());
//
//        // Cart clear
//        cart.getItems().clear();
//        cartRepository.save(cart);
//        Order saveorder = orderRepository.save(order);
//        return mapper.map(saveorder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundEception("Order Not found with the given orderId "));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User Not found with the given userId "));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page,OrderDto.class);
    }


}
