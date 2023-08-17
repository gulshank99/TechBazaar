package com.gsa.tech.bazaar.controller;

import com.gsa.tech.bazaar.dtos.*;
import com.gsa.tech.bazaar.services.CategoryService;
import com.gsa.tech.bazaar.services.FileService;
import com.gsa.tech.bazaar.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private ProductService productService;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    // Create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        // Call service to save object
        CategoryDto categoryDto1=categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
        // Call service to save object
        CategoryDto updatedCategory=categoryService.update(categoryDto,categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    //Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){

        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category Deleted Successfully !!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //GetAll
    @GetMapping
    public ResponseEntity<PageableResponse> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
            ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //Get Single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId){

        CategoryDto categoryDto=categoryService.get(categoryId);
        return ResponseEntity.ok(categoryDto);
    }


    // Upload user Image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("categoryImage") MultipartFile image , @PathVariable("categoryId") String categoryId ) throws IOException {
        String coverImage = fileService.uploadFile(image, imageUploadPath);

        CategoryDto category = categoryService.get(categoryId);
        category.setCoverImage(coverImage);
        categoryService.update(category,categoryId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(coverImage).success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    // Serve user Image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.get(categoryId);
        logger.info("User image Name : {}",category.getCoverImage());

        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

    //create product with category
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,@RequestBody ProductDto dto){
        ProductDto productWithCategory = productService.createWithCategory(dto, categoryId);

        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    // Update Category of Product
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryProduct(@PathVariable String categoryId,@PathVariable String productId){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //Return products of Given category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
