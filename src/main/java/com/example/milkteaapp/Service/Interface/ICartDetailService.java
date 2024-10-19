package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.CartModel;
import com.example.milkteaapp.Model.ProductModel;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ICartDetailService {
    //Hàm lấy tất cả danh sách giỏ hàng
    List<CartDetailModel> getCartAll();
    //Hàm thêm sản phẩm vào giỏ hàng
    CartDetailModel AddCartDetail(CartDetailModel cartdetailModel);
    //Hàm cập nhật sản phẩm trong giỏ hàng
    CartDetailModel UpdateCartDetail(long cartDetailID, CartDetailModel cartdetailModel, ProductModel productModel);
    //Hàm xóa sản phẩm được chọn trong giỏ hàng
    boolean DeleteCartDetail(long cartDetailID);
    //Hàm tìm kiếm sản phẩm trong giỏ hàng
    CartDetailModel SearchCartDetail(Long cartID, String productName);

    }

