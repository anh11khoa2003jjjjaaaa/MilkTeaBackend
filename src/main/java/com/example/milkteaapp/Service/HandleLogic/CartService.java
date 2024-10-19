package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.CartModel;
import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Repository.CartDetailRepository;
import com.example.milkteaapp.Repository.CartRepository;
import com.example.milkteaapp.Repository.ProductRepository;
import com.example.milkteaapp.Service.Interface.ICartService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, CartDetailRepository cartDetailRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
    }

    // Get all carts
    @Override
    public List<CartModel> getCartAll() {
        try {
            return cartRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching all carts from the database", e);
        }
    }

    // Add a product to the cart
//    @Override
//    @Transactional
//    public CartDetailModel AddCart(CartModel cartModel, CartDetailModel cartdetail) {
//        try {
//            // Check if the product exists
//            Optional<ProductModel> product = productRepository.findById(cartdetail.getProductID());
//            if (!product.isPresent()) {
//                throw new EntityNotFoundException("Product not found with ID: " + cartdetail.getProductID());
//            }
//
//            // Find cart by UserID, create a new one if not present
//            CartModel cart = cartRepository.findByUserID(cartModel.getUserID()).orElseGet(() -> {
//                CartModel newCart = new CartModel();
//                newCart.setUserID(cartModel.getUserID());
//                newCart.setCreatedDate(new Date()); // Ensure the created date is set
//                return cartRepository.save(newCart);
//            });
//
//            // Create cart detail
//            CartDetailModel cartDetail = new CartDetailModel();
//            cartDetail.setCartID(cart.getCartID());
//            cartDetail.setProductID(cartdetail.getProductID());
//            cartDetail.setQuantity(cartdetail.getQuantity());
//            cartDetail.setSize(cartdetail.getSize());
//            cartDetail.setPrice(product.get().getPrice());
//            cartDetail.setImage(product.get().getImageURL());
//
//            // Save the cart detail and return the saved object
//            return cartDetailRepository.save(cartDetail);
//        } catch (EntityNotFoundException e) {
//            throw new EntityNotFoundException("Product not found or other entity error: " + e.getMessage());
//        } catch (DataAccessException e) {
//            throw new RuntimeException("Error adding product to cart", e);
//        } catch (Exception e) {
//            throw new RuntimeException("Unexpected error occurred while adding product to cart", e);
//        }
//    }
    @Override
    @Transactional
    public CartDetailModel AddCart(CartModel cartModel, CartDetailModel cartDetailModel) {
        try {
            // Kiểm tra sản phẩm có tồn tại hay không
            Optional<ProductModel> product = productRepository.findById(cartDetailModel.getProductID());
            if (!product.isPresent()) {
                throw new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + cartDetailModel.getProductID());
            }

            // Tìm tất cả các giỏ hàng của người dùng
            List<CartModel> userCarts = cartRepository.findByUserID(cartModel.getUserID());

            CartModel cart;
            if (userCarts.isEmpty()) {
                // Nếu không có giỏ hàng nào, tạo một giỏ hàng mới
                cart = new CartModel();
                cart.setUserID(cartModel.getUserID());
                cart.setCreatedDate(new Date()); // Đảm bảo ngày tạo được thiết lập
                cart = cartRepository.save(cart);
            } else {
                // Nếu đã có giỏ hàng, chọn giỏ hàng hiện tại (giả sử là giỏ hàng đầu tiên)
                cart = userCarts.get(0); // Bạn có thể thay đổi logic để chọn đúng giỏ hàng
            }

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng hay chưa
            Optional<CartDetailModel> existingCartDetail = cartDetailRepository.findByCartIDAndProductID(cart.getCartID(), cartDetailModel.getProductID());

            CartDetailModel cartDetail;
            if (existingCartDetail.isPresent()) {
                // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng
                cartDetail = existingCartDetail.get();
                cartDetail.setQuantity(cartDetail.getQuantity() + cartDetailModel.getQuantity());
            } else {
                // Nếu sản phẩm chưa có, thêm sản phẩm mới vào giỏ hàng
                cartDetail = new CartDetailModel();
                cartDetail.setCartID(cart.getCartID());
                cartDetail.setProductID(cartDetailModel.getProductID());
                cartDetail.setQuantity(cartDetailModel.getQuantity());
                cartDetail.setSize(cartDetailModel.getSize());
                cartDetail.setPrice(product.get().getPrice());
                cartDetail.setImage(product.get().getImageURL());
            }

            // Lưu chi tiết giỏ hàng và trả về đối tượng đã được lưu
            return cartDetailRepository.save(cartDetail);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Không tìm thấy sản phẩm hoặc có lỗi khác: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("Lỗi khi thêm sản phẩm vào giỏ hàng", e);
        } catch (Exception e) {
            throw new RuntimeException("Xảy ra lỗi không mong muốn khi thêm sản phẩm vào giỏ hàng", e);
        }
    }


    // Update cart
    @Override
    public CartModel UpdateCart(long cartID, CartModel cartModel) {
        try {
            return cartRepository.findById(cartID).map(existingCart -> {
                existingCart.setUserID(cartModel.getUserID());
                existingCart.setCreatedDate(cartModel.getCreatedDate());
                return cartRepository.save(existingCart);
            }).orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + cartID));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cart with ID " + cartID + " not found: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating cart", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while updating the cart", e);
        }
    }

    // Delete cart by ID or delete all if ID <= 0
    @Override
    public boolean deleteCart(long cartID) {
        try {
            if (cartID <= 0) {
                cartRepository.deleteAll();
                return true;
            }
            return cartRepository.findById(cartID).map(cart -> {
                cartRepository.deleteById(cartID);
                return true;
            }).orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + cartID));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cart with ID " + cartID + " not found: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting cart", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while deleting the cart", e);
        }
    }

    // Search carts by creation date
    @Override
    public List<CartModel> SearchCart(Date createdDate) {
        try {
            return cartRepository.findByCreatedDate(createdDate);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error searching carts by date", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while searching carts", e);
        }
    }

    // Update cart detail
    @Override
    public CartDetailModel updateCartDetail(long cartDetailID, CartDetailModel cartDetailModel) {
        try {
            return cartDetailRepository.findById(cartDetailID).map(existingDetail -> {
                existingDetail.setQuantity(cartDetailModel.getQuantity());
                existingDetail.setSize(cartDetailModel.getSize());
                existingDetail.setPrice(cartDetailModel.getPrice());
                return cartDetailRepository.save(existingDetail);
            }).orElseThrow(() -> new EntityNotFoundException("Cart detail not found with ID: " + cartDetailID));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cart detail with ID " + cartDetailID + " not found: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating cart detail", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while updating the cart detail", e);
        }
    }

    // Delete cart detail by ID
    @Override
    public boolean deleteCartDetail(long cartDetailID) {
        try {
            if (cartDetailRepository.existsById(cartDetailID)) {
                cartDetailRepository.deleteById(cartDetailID);
                return true;
            } else {
                throw new EntityNotFoundException("Cart detail not found with ID: " + cartDetailID);
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cart detail with ID " + cartDetailID + " not found: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting cart detail", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while deleting the cart detail", e);
        }
    }

    // Get cart details by cart ID
    @Override
    public List<CartDetailModel> getCartDetailsByCartID(String userID) {
        try {
            return cartDetailRepository.findByCart_UserID(userID);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching cart details by cart ID", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while fetching cart details", e);
        }
    }
    @Override
    public CartDetailModel updateCartItem(Long cartDetailID, String size,int quantity) {
        // Kiểm tra sản phẩm có tồn tại không
        Optional<CartDetailModel> existingCartItemOpt = cartDetailRepository.findById(cartDetailID);
        if (!existingCartItemOpt.isPresent()) {
            throw new EntityNotFoundException("Cart item not found");
        }

        CartDetailModel existingCartItem = existingCartItemOpt.get();

        // Kiểm tra xem sản phẩm có tồn tại trong giỏ hàng hay không và số lượng có hợp lệ không
        if (existingCartItem.getQuantity() < 1) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        // Cập nhật các trường
        existingCartItem.setQuantity(quantity);
        existingCartItem.setSize(size);

        // Lưu lại thay đổi vào database
        return cartDetailRepository.save(existingCartItem);
    }

@Override
    // Hàm xóa sản phẩm khỏi giỏ hàng dựa trên cartDetailID
    public void removeCartItem(Long cartDetailID)  {
        Optional<CartDetailModel> cartDetailOptional = cartDetailRepository.findById(cartDetailID);

    if (cartDetailOptional.isPresent()) {
        cartDetailRepository.delete(cartDetailOptional.get());
    } else {
        return;
    }
    }



//    public List<CartModel> getCartByUserID(String userID) {
//        return cartRepository.findByUserID(userID);
//    }


}
