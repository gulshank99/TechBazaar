package com.gsa.tech.bazaar.services.impl;

import com.gsa.tech.bazaar.controller.UserController;
import com.gsa.tech.bazaar.dtos.PageableResponse;
import com.gsa.tech.bazaar.dtos.UserDto;
import com.gsa.tech.bazaar.entities.Role;
import com.gsa.tech.bazaar.entities.User;
import com.gsa.tech.bazaar.exceptions.ResourceNotFoundEception;
import com.gsa.tech.bazaar.helper.Helper;
import com.gsa.tech.bazaar.repositories.RoleRepository;
import com.gsa.tech.bazaar.repositories.UserRepository;
import com.gsa.tech.bazaar.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Value("${normal.role.id}")
    private String normalRoleId;

    @Autowired
    private RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {

        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        //encoding password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // If we use userRepository.save(userDto) here it is asking for Entity i.e uder but we are
        // giving dto so we have to convert dto->entity and then entity->dto

        // User user = mapper.map(userDto,User.class);
        // User savedUser = userRepository.save(user);
        //  UserDto newDto =  mapper.map(savedUser,UserDto.class);

        User user = dtoToEntity(userDto);

        //Fetch role of normal user
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User Not found with the given userId "));

        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());

        if(!userDto.getPassword().equalsIgnoreCase(user.getPassword()) ) {
            user.setPassword(userDto.getPassword());
        }

        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());

        //save
        User updatedUser = userRepository.save(user);
        UserDto updatedDto =entityToDto(updatedUser);

        return updatedDto;
    }

    @Override
    public void DeleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User Not found with the given userId "));
        // delete user Profile image  let image/user/abc.png
        String fullPath = imagePath + user.getImageName();

        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);
        }catch(NoSuchFileException ex){
            logger.info("User image not Found in folder");
            ex.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        // Delete
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
       // Sort sort = Sort.by(sortBy);
        Sort sort =(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User Not found with the given userId "));

        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundEception("User Not found with the given Email "));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return dtoList;
    }


    private UserDto entityToDto(User savedUser) {

//       UserDto userDto= UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();

       return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        return mapper.map(userDto,User.class);
    }



}
