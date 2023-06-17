package com.gsa.tech.bazaar.services.impl;

import com.gsa.tech.bazaar.dtos.CategoryDto;
import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.entities.Category;
import com.gsa.tech.bazaar.entities.User;
import com.gsa.tech.bazaar.exceptions.ResourceNotFoundEception;
import com.gsa.tech.bazaar.helper.Helper;
import com.gsa.tech.bazaar.repositories.CategoryRepository;
import com.gsa.tech.bazaar.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${category.profile.image.path}")
    private String imagePath;



    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        // Creating categoryId:Randomly

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);


        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);

        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        // Get Category of Given Id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundEception("Category not Found with  given Id !!"));

        // Category detail Update
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        // Now Save Details
        Category updatedCategory = categoryRepository.save(category);

        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {

        // Get Category of Given I'd
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundEception("Category not Found with  given Id !!"));

         // delete user Profile image  let image/user/abc.png
        String fullPath = imagePath + category.getCoverImage();

        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
          //  logger.info("User image not Found in folder");
            ex.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        // Delete
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundEception("Category not Found with  given Id !!"));
        return  mapper.map(category,CategoryDto.class);

    }
}
