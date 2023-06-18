package com.gsa.tech.bazaar.services;

import com.gsa.tech.bazaar.dtos.AddItemToCartRequest;
import com.gsa.tech.bazaar.dtos.CartDto;

public interface CartService {
    //: add items to cart
    //Case 1: cart for user is not available: we will create the cart and then add the items
    //Case 2: cart available add the items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // remove item from Cart:
    void removeItemFromCart(String userId,int cartItem);

    // remove item from Cart:
    void clearCart(String userId);

    //
    CartDto getCartByUser(String userId);


}
