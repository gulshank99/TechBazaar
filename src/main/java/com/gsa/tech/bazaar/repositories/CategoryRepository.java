package com.gsa.tech.bazaar.repositories;

import com.gsa.tech.bazaar.entities.Category;
import com.gsa.tech.bazaar.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String> {




}
