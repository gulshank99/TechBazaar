package com.gsa.tech.bazaar.services.impl;

import com.gsa.tech.bazaar.dtos.CategoryDto;
import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.dtos.ProductDto;
import com.gsa.tech.bazaar.entities.Category;
import com.gsa.tech.bazaar.entities.Product;
import com.gsa.tech.bazaar.exceptions.ResourceNotFoundEception;
import com.gsa.tech.bazaar.helper.Helper;
import com.gsa.tech.bazaar.repositories.CategoryRepository;
import com.gsa.tech.bazaar.repositories.ProductRepository;
import com.gsa.tech.bazaar.services.ProductService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper mapper;
    @Value("${product.profile.image.path}")
    private String imagePath;

    @Autowired
    private CategoryRepository categoryRepository;





    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        //Product Id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);

        //Added Date
        product.setAddedDate(new Date());

        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {

        //Fetch product bt id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundEception("Product not Found with this Id !!"));
        product.setTitle(productDto.getTitle());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(product.isStock());
        product.setProductImageName(productDto.getProductImageName());

        //Save entity
       Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundEception("Product not Found with this Id !!"));

        // delete user Profile image  let image/user/abc.png
        String fullPath = imagePath + product.getProductImageName();

        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
            //  logger.info("User image not Found in folder");
            ex.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        productRepository.delete(product);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundEception("Product not Found with this Id !!"));

        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        //fetch the category from Database
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundEception("Category not found with id  !!"));
        Product product = mapper.map(productDto, Product.class);

        //Product I'd
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);

        //Added Date
        product.setAddedDate(new Date());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);


    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {

        //Product Fetch
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundEception("Product not Found with this Id !!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundEception("Category not found with id  !!"));

        product.setCategory(category);

        //Save entity
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);

    }

    @Override
    public  PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundEception("Category not found with id  !!"));

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);

        Page<Product> page = productRepository.findByCategory(category,pageable);

        return Helper.getPageableResponse(page,ProductDto.class);



    }
}
