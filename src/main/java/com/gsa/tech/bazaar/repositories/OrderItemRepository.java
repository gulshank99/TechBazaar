package com.gsa.tech.bazaar.repositories;

import com.gsa.tech.bazaar.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

}
