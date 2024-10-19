package com.example.milkteaapp.Controller;

import com.example.milkteaapp.CartRequest;
import com.example.milkteaapp.DTO.CartWithDetailsDTO;
import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.CartModel;
import com.example.milkteaapp.Service.HandleLogic.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/public/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get all carts
    @GetMapping("/getallCart")
    public ResponseEntity<Object> getAllCart() {
        try {
            List<CartModel> carts = cartService.getCartAll();
            if (carts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No carts found"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(carts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error while fetching all carts", "message", e.getMessage()));
        }
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Object> addProductToCart(@RequestBody CartRequest cartRequest) {
        CartModel cartModel = cartRequest.getCartModel();
        CartDetailModel cartDetailModel = cartRequest.getCartDetailModel();
        try {
            if (cartModel.getCartID() == null || cartDetailModel.getProductID() == null || cartDetailModel.getQuantity() <= 0 || cartDetailModel.getSize() == null || cartDetailModel.getImage() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid input parameters"));
            }
            CartDetailModel cartDetail = cartService.AddCart(cartModel, cartDetailModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Product added to cart successfully", "data", cartDetail));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found", "message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid arguments provided", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error while adding product to cart", "message", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CartDetailModel> updateCartItem(@RequestParam Long cartDetailID, @RequestParam String size, @RequestParam int quantity) {
        try {
            // Gọi service để cập nhật giỏ hàng
            CartDetailModel updatedItem = cartService.updateCartItem(cartDetailID, size,quantity);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    // Update a cart
    @PutMapping("/updateCart/{cartID}")
    public ResponseEntity<Object> updateCart(@PathVariable long cartID, @RequestBody CartModel cartModel) {
        try {
            if (cartModel == null || cartID <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid cart ID or cart data"));
            }
            CartModel updatedCart = cartService.UpdateCart(cartID, cartModel);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Cart updated successfully", "data", updatedCart));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found", "message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid cart update data", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error while updating cart", "message", e.getMessage()));
        }
    }

    // Delete a cart
    @DeleteMapping("/deleteCart/{cartID}")
    public ResponseEntity<Object> deleteCart(@PathVariable long cartID) {
        try {
            if (cartID <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid cart ID"));
            }
            boolean isDeleted = cartService.deleteCart(cartID);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Cart deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error while deleting cart", "message", e.getMessage()));
        }
    }

    // Search carts by creation date
    @GetMapping("/searchCart")
    public ResponseEntity<Object> searchCart(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDate) {
        try {
            if (createdDate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Creation date is required"));
            }
            List<CartModel> carts = cartService.SearchCart(createdDate);
            if (carts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No carts found for the given creation date"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(carts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error while searching carts by date", "message", e.getMessage()));
        }
    }

    // Get cart details by cart ID
    @GetMapping("/cartDetails/{userID}")
    public ResponseEntity<Object> getCartDetails(@PathVariable String userID) {
        try {
            if (userID.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid cart ID"));
            }
            List<CartDetailModel> cartDetails = cartService.getCartDetailsByCartID(userID);
            if (cartDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No details found for this cart"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(cartDetails);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart details not found", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error while fetching cart details", "message", e.getMessage()));
        }
    }
    @GetMapping("/getCartByUserID/{userID}")
    public List<CartDetailModel> getCartByUserID(@PathVariable(required = false) String userID) {
        // Kiểm tra nếu có `cartID`, thì thực hiện lấy giỏ hàng dựa trên `cartID`


        // Nếu có `userID`, thì thực hiện lấy giỏ hàng dựa trên `userID`
        if (userID != null && !userID.isEmpty()) {
            return cartService.getCartDetailsByCartID(userID);
        }

        // Trả về giỏ hàng rỗng nếu không có tham số
        return new ArrayList<>();
    }

    @DeleteMapping("/remove/{cartDetailID}")
    public ResponseEntity<Map<String, String>> removeCartItem(@PathVariable Long cartDetailID) {
        try {
            cartService.removeCartItem(cartDetailID);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sản phẩm đã được xóa khỏi giỏ hàng.");
            return ResponseEntity.ok(response); // Trả về JSON
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi: Không thể xóa sản phẩm khỏi giỏ hàng.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



}
