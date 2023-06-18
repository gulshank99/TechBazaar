package com.gsa.tech.bazaar.services.impl;

import com.gsa.tech.bazaar.dtos.AddItemToCartRequest;
import com.gsa.tech.bazaar.dtos.CartDto;
import com.gsa.tech.bazaar.entities.Cart;
import com.gsa.tech.bazaar.entities.CartItem;
import com.gsa.tech.bazaar.entities.Product;
import com.gsa.tech.bazaar.entities.User;
import com.gsa.tech.bazaar.exceptions.BadApiRequestException;
import com.gsa.tech.bazaar.exceptions.ResourceNotFoundEception;
import com.gsa.tech.bazaar.repositories.CartItemRepository;
import com.gsa.tech.bazaar.repositories.CartRepository;
import com.gsa.tech.bazaar.repositories.ProductRepository;
import com.gsa.tech.bazaar.repositories.UserRepository;
import com.gsa.tech.bazaar.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;



    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

       String productId =request.getProductId();
        int quantity = request.getQuantity();

       if(quantity<=0){
           throw new BadApiRequestException("Requested quantity is not valid !!");
       }

        //get the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundEception("Product not Found in dataBase"));
        //Fetch user from Db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User not Found in dataBase"));

        Cart cart = null;
        try{
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //Perform cart operations
        //If cart items already present : then update
        AtomicBoolean updated= new AtomicBoolean(false);
        List<CartItem> items = cart.getItems();
        items  = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                // items already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);
            }

            return item;
        }).collect(Collectors.toList());

       // cart.setItems(updatedItems);

        //create items
         if(!updated.get()){
             //create items
             CartItem cartItem = CartItem.builder()
                     .quantity(quantity)
                     .totalPrice(quantity*product.getDiscountedPrice())
                     .cart(cart)
                     .product(product)
                     .build();
             cart.getItems().add(cartItem);
         }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        //use for validation if you want  : Skip

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundEception("Cart Item not found"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        //Fetch user from Db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User not Found in dataBase"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundEception("Cart Item not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        //Fetch user from Db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundEception("User not Found in dataBase"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundEception("Cart Item not found"));

        return mapper.map(cart,CartDto.class);
    }
}
