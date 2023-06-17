package com.gsa.tech.bazaar.repositories;

import com.gsa.tech.bazaar.entities.Category;
import com.gsa.tech.bazaar.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {
    // Search
    Page<Product> findByTitleContaining(String subtitle,Pageable pageable);

    //List<Product> findByLive(boolean live);
    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);

    //Others methods
    //Custom Finder methods
    //Query methods
}
