package com.gsa.tech.bazaar.services;

import com.gsa.tech.bazaar.dtos.CategoryDto;
import com.gsa.tech.bazaar.dtos.PageableResponse;

public interface CategoryService {
    //Create
    CategoryDto create(CategoryDto categoryDto);

    //Update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //Delete
    void delete(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get single category detail
    CategoryDto get(String categoryId);
    //Search
}
