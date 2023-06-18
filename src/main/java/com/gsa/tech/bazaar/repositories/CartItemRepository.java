package com.gsa.tech.bazaar.repositories;

import com.gsa.tech.bazaar.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
