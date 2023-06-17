package com.gsa.tech.bazaar.services;

import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //Create
    ProductDto create(ProductDto productDto);
    //Update
    ProductDto update(ProductDto productDto,String productId);
    //Delete
    void delete(String productId);
    //Get single
    ProductDto get(String productId);
    //Get all
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    //Get all : live
    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);
    //Search product
    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);



    //Create product with Category
    ProductDto createWithCategory(ProductDto productDto,String categoryId);

    //Update Category of Product
    ProductDto updateCategory(String productId,String categoryId);

    //Return products of Given category
    PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);


}
