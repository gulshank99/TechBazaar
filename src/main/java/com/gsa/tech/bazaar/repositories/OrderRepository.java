package com.gsa.tech.bazaar.repositories;

import com.gsa.tech.bazaar.entities.Order;
import com.gsa.tech.bazaar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findByUser(User user);
}
