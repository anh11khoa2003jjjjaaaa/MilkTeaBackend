package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Service.HandleLogic.CartDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/cartdetail")
public class CartDetailController {

    private final CartDetailService cartDetailService;

    public CartDetailController(CartDetailService cartDetailService) {
        this.cartDetailService = cartDetailService;
    }

    // Lấy tất cả chi tiết giỏ hàng
    @GetMapping("/getAllCartDetails")
    public ResponseEntity<Object> getAllCartDetails() {
        try {
            List<CartDetailModel> cartDetails = cartDetailService.getCartAll();
            return ResponseEntity.status(HttpStatus.OK).body(cartDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    // Thêm chi tiết vào giỏ hàng
    @PostMapping("/addCartDetail")
    public ResponseEntity<Object> addCartDetail(@RequestBody CartDetailModel cartDetailModel) {
        try {
            CartDetailModel addedCartDetail = cartDetailService.AddCartDetail(cartDetailModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", "Chi tiết giỏ hàng đã được thêm", "cartDetail", addedCartDetail));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi khi thêm chi tiết giỏ hàng", "message", e.getMessage()));
        }
    }

    // Cập nhật chi tiết giỏ hàng
    @PutMapping("/updateCartDetail/{cartDetailID}")
    public ResponseEntity<Object> updateCartDetail(@PathVariable long cartDetailID, @RequestBody ProductModel productModel, @RequestBody CartDetailModel cartDetailModel) {
        try {

            CartDetailModel updatedCartDetail = cartDetailService.UpdateCartDetail(cartDetailID, cartDetailModel,productModel);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Cập nhật chi tiết giỏ hàng thành công", "cartDetail", updatedCartDetail));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi khi cập nhật chi tiết giỏ hàng", "message", e.getMessage()));
        }
    }

    // Xóa chi tiết giỏ hàng
    @DeleteMapping("/deleteCartDetail/{cartDetailID}")
    public ResponseEntity<Object> deleteCartDetail(@PathVariable long cartDetailID) {
        try {
            boolean isDeleted = cartDetailService.DeleteCartDetail(cartDetailID);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Xóa chi tiết giỏ hàng thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy chi tiết giỏ hàng để xóa"));
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi khi xóa chi tiết giỏ hàng", "message", e.getMessage()));
        }
    }

    // Tìm kiếm sản phẩm trong giỏ hàng
    @GetMapping("/searchCartDetail")
    public ResponseEntity<Object> searchCartDetail(@RequestParam Long cartID, @RequestParam String productName) {
        try {
            CartDetailModel foundCartDetail = cartDetailService.SearchCartDetail(cartID, productName);
            return ResponseEntity.status(HttpStatus.OK).body(foundCartDetail);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi khi tìm kiếm chi tiết giỏ hàng", "message", e.getMessage()));
        }
    }
}
