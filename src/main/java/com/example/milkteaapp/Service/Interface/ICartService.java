package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.CartModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public interface ICartService {
    //Hàm lấy tất cả danh sách giỏ hàng
    List<CartModel> getCartAll();
    //Hàm thêm sản phẩm vào giỏ hàng
    CartDetailModel AddCart(CartModel cartmodel,CartDetailModel cartDetailModel);
    //Hàm cập nhật sản phẩm trong giỏ hàng
    CartModel UpdateCart(long cartID,CartModel cartModel);
    //Hàm xóa sản phẩm được chọn trong giỏ hàng

    //Hàm tìm kiếm sản phẩm trong giỏ hàng
    List<CartModel> SearchCart(Date createDate);
    CartDetailModel updateCartDetail(long cartDetailID, CartDetailModel cartDetailModel);
    // Xóa sản phẩm trong giỏ hàng
    boolean deleteCartDetail(long cartDetailID);

    // Xóa toàn bộ giỏ hàng
    boolean deleteCart(long cartID);
    List<CartDetailModel> getCartDetailsByCartID(String userID);

    CartDetailModel updateCartItem(Long cartDetailID, String size,int quantity);

    void removeCartItem(Long cartDetailID);
//    List<CartModel> getCartByUserID(String userID);
}
