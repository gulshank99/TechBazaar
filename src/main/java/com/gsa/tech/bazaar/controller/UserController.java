package com.gsa.tech.bazaar.controller;

import com.gsa.tech.bazaar.dtos.ApiResponseMessage;
import com.gsa.tech.bazaar.dtos.ImageResponse;
import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.dtos.UserDto;
import com.gsa.tech.bazaar.services.FileService;
import com.gsa.tech.bazaar.services.UserService;
import com.gsa.tech.bazaar.services.impl.FileServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    //Create  //// HERE @VALID is used for BEan Validator that we have used in UserDto
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //Update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable ("userId")String userId,@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
    //Delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)  {
       userService.DeleteUser(userId);
         ApiResponseMessage message= ApiResponseMessage.builder()
                                                       .message("User is Successfully Deleted")
                                                       .success(true)
                                                       .status(HttpStatus.OK)
                                                       .build();
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    //Getall
    @GetMapping
    public  ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return  new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }



   // get single
    @GetMapping("/{userId}")
   public  ResponseEntity<UserDto> getSingleUserById(@PathVariable String userId){
       return  new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
   }



    //grt by email
    @GetMapping("/email/{email}")
    public  ResponseEntity<UserDto> getSingleUserByEmail(@PathVariable String email){
        return  new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }



    // Search User
    @GetMapping("/search/{keywords}")
    public  ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
        return  new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }

      // Upload user Image
       @PostMapping("/image/{userId}")
       public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image , @PathVariable("userId") String userId ) throws IOException {
           String imageName = fileService.uploadFile(image, imageUploadPath);

           UserDto user =userService.getUserById(userId);
           user.setImageName(imageName);
           userService.updateUser(user,userId);

           ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();

           return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
       }

        // Serve user Image
    @GetMapping("/image/{userId}")
        public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
         UserDto user = userService.getUserById(userId);
         logger.info("User image Name : {}",user.getImageName());

        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

    // TODO:Implement PatchMapping

//    @PatchMapping("/{userId}")
//    public ResponseEntity<UserDto> partialUpdate(@PathVariable ("userId")String userId,@Valid @RequestBody UserDto userDto) {
//        UserDto user = userService.updateUser(userDto, userId);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//
//    }

}
