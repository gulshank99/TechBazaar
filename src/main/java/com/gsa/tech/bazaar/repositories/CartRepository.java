package com.gsa.tech.bazaar.repositories;

import com.gsa.tech.bazaar.entities.Cart;
import com.gsa.tech.bazaar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
