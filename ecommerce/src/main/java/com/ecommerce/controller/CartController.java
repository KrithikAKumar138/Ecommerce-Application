package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private  CartService cartService;


    @GetMapping
    public String viewCart(Model model, Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        Cart cart = cartService.getCartByUserEmail(email);

        model.addAttribute("cart", cart);
        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotalPrice());

        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        cartService.addToCart(email, productId);

        redirectAttributes.addFlashAttribute("success", "Item added to cart");

        return "redirect:/cart";
    }




    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Long itemId,
                                 @RequestParam int quantity,
                                 RedirectAttributes redirectAttributes) {

        cartService.updateQuantity(itemId, quantity);

        redirectAttributes.addFlashAttribute("success", "Cart updated");

        return "redirect:/cart";
    }

    @GetMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId,
                             RedirectAttributes redirectAttributes) {

        cartService.removeItem(itemId);

        redirectAttributes.addFlashAttribute("success", "Item removed from cart");

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        cartService.clearCartByEmail(email);

        redirectAttributes.addFlashAttribute("success", "Cart cleared successfully");

        return "redirect:/cart";
    }
}
