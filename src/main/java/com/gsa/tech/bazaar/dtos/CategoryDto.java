package com.gsa.tech.bazaar.dtos;

import com.gsa.tech.bazaar.entities.Category;
import com.gsa.tech.bazaar.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CategoryDto {


    private String categoryId;
    @NotBlank
    @Size(min = 4,message = "title must be of minimum 4 character !!")
    private String title;
    @NotBlank(message = "Description Required !! ")
    private String description;
    @ImageNameValid()
    private String coverImage;


}
