package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Repository.CartDetailRepository;
import com.example.milkteaapp.Service.Interface.ICartDetailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartDetailService implements ICartDetailService {

    private final CartDetailRepository cartDetailRepository;

    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }

    @Override
    public List<CartDetailModel> getCartAll() {
        return cartDetailRepository.findAll();
    }

    @Override
    public CartDetailModel AddCartDetail(CartDetailModel cartDetailModel) {
//        Optional<CartDetailModel> existingCartDetail = cartDetailRepository.findById(cartDetailModel.getCartDetailID());
//        if (existingCartDetail.isPresent()) {
//            throw new EntityNotFoundException("Chi tiết giỏ hàng đã tồn tại!");
//        } else {
            return cartDetailRepository.save(cartDetailModel);
//        }
    }

    @Override
    public CartDetailModel UpdateCartDetail(long cartDetailID, CartDetailModel cartDetailModel, ProductModel productModel ) {
        Optional<CartDetailModel> existingCartDetail = cartDetailRepository.findById(cartDetailID);
        if (existingCartDetail.isPresent()) {
            CartDetailModel cartDetail = existingCartDetail.get();
            updateCartDetailInfo(cartDetailModel, cartDetail,productModel);
            return cartDetailRepository.save(cartDetail);
        } else {
            throw new EntityNotFoundException("Không tìm thấy chi tiết giỏ hàng với ID " + cartDetailID);
        }
    }

    private void updateCartDetailInfo(CartDetailModel source, CartDetailModel target, ProductModel productModel) {
        target.setProductID(source.getProductID());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setSize(source.getSize());
        target.setCartID(source.getCartID());
        target.setImage(productModel.getImageURL());
    }

    @Override
    public boolean DeleteCartDetail(long cartDetailID) {
        Optional<CartDetailModel> cartDetail = cartDetailRepository.findById(cartDetailID);
        if (cartDetail.isPresent()) {
            cartDetailRepository.deleteById(cartDetailID);
            return true;
        } else {
            throw new EntityNotFoundException("Không tìm thấy chi tiết giỏ hàng để xóa với ID " + cartDetailID);
        }
    }

    @Override
    public CartDetailModel SearchCartDetail(Long cartID, String productName) {
        Optional<CartDetailModel> cartDetail = cartDetailRepository.findByCartIDAndProduct_ProductNameContainingIgnoreCase(cartID,productName);
        if (cartDetail.isPresent()) {
            return cartDetail.get();
        } else {
            throw new EntityNotFoundException("Không tìm thấy sản phẩm với tên " + productName);
        }
    }
}
