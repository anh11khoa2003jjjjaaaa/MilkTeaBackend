package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.CategorieModel;
import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Repository.ProductRepository;
import com.example.milkteaapp.Service.Interface.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductModel> searchProduct(String searchTerm) {

        if (searchTerm != null) {
            searchTerm = searchTerm.trim();  // Loại bỏ khoảng trắng thừa
        }
        List<ProductModel> productModel = productRepository.findByProductNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(searchTerm,searchTerm);
        if (productModel.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy sản phẩm với tên hoặc loại sản phẩm '" +  searchTerm  + "'");
        } else {
            return productModel;
        }
    }

    @Override
    public ProductModel addProduct(ProductModel product) {
        try {
            // Kiểm tra các thuộc tính bắt buộc của sản phẩm
            if (product.getProductID() == null || product.getProductID().isEmpty()) {
                throw new IllegalArgumentException("Mã sản phẩm không được bỏ trống.");
            }

            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được bỏ trống.");
            }


            if (product.getStock() < 0) {
                throw new IllegalArgumentException("Số lượng sản phẩm không thể âm.");
            }

            if (product.getCategoryID() == null || product.getCategoryID().isEmpty()) {
                throw new IllegalArgumentException("Mã danh mục không được bỏ trống.");
            }

            // Kiểm tra xem sản phẩm đã tồn tại trong cơ sở dữ liệu hay chưa
            Optional<ProductModel> existingProduct = productRepository.findById(product.getProductID());
            if (existingProduct.isPresent()) {
                throw new IllegalArgumentException("Sản phẩm với mã " + product.getProductID() + " đã tồn tại.");
            }

            // Lưu sản phẩm vào cơ sở dữ liệu
            productRepository.save(product);
            return product;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dữ liệu sản phẩm không hợp lệ: " + e.getMessage(), e);

        } catch (DataAccessException e) {
            throw new RuntimeException("Có lỗi xảy ra khi lưu sản phẩm vào cơ sở dữ liệu: " + e.getMessage(), e);

        } catch (Exception e) {
            throw new RuntimeException("Đã xảy ra lỗi không xác định khi thêm sản phẩm: " + e.getMessage(), e);
        }
    }



    @Override
    public ProductModel updateProduct(String productID, ProductModel product) {


        Optional<ProductModel> optionalproductModel = productRepository.findById(productID);
        if (optionalproductModel.isPresent()) {
            ProductModel productModel = optionalproductModel.get();
            update(productModel, product);
            return productRepository.save(productModel);
        } else {
            throw new EntityNotFoundException("Khách hàng có " + productID + " không tồn tại!");

        }
    }

    private void update(ProductModel productold, ProductModel productnew) {
        productold.setProductName(productnew.getProductName());
        productold.setCategoryID(productnew.getCategoryID());
        productold.setPrice(productnew.getPrice());
        productold.setStock(productnew.getStock());
        productold.setDescription(productnew.getDescription());
        productold.setImageURL(productnew.getImageURL());


    }

    @Override
    public Boolean deleteProduct(String productID) {

        Optional<ProductModel> optionalproductModel = productRepository.findByProductID(productID);
        if (optionalproductModel!=null){
            productRepository.deleteById(productID);
            return true;
        }
        return null;
    }
    @Override
    public String getCategoryNameByProductID(String productID) {
        // Tìm sản phẩm dựa trên productID
        Optional<ProductModel> productModelOptional = productRepository.findByProductID(productID);

        // Kiểm tra nếu tìm thấy sản phẩm
        if (productModelOptional.isPresent()) {
            // Lấy ra ProductModel
            ProductModel productModel = productModelOptional.get();

            // Lấy tên danh mục từ CategoryModel
            CategorieModel category = productModel.getCategory();
            if (category != null) {
                return category.getCategoryName();
            } else {
                return "Danh mục không tồn tại.";
            }
        } else {
            throw new EntityNotFoundException("Sản phẩm có ID " + productID + " không tồn tại!");
        }
    }

    @Override
    public Page<ProductModel> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);  // page - 1 vì Spring Boot bắt đầu từ trang 0
        return productRepository.findAll(pageable);
    }


    // Hàm này để lấy thông tin chi tiết sản phẩm dựa theo ProductID
    public ProductModel getProductID (String productID) {
        Optional<ProductModel> productOptional = productRepository.findByProductID(productID);
        return productOptional.orElse(null); // Trả về null nếu không tìm thấy sản phẩm
    }

    // Hàm lấy sản phẩm liên quan dựa trên CategoryID
    public List<ProductModel> getRelatedProductsByCategoryID(String categoryID) {
        return productRepository.findByCategoryID(categoryID);
    }


    private String saveImageToFolder(MultipartFile imageFile) throws IOException {
        String folderPath = "src/main/resources/static/images/";
        String imagePath = folderPath + imageFile.getOriginalFilename();

        File file = new File(imagePath);

        // Tạo thư mục nếu chưa tồn tại
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Lưu tệp ảnh vào thư mục
        imageFile.transferTo(file);

        // Trả về tên tệp để lưu vào cơ sở dữ liệu
        return imageFile.getOriginalFilename();
    }

    public String getProductNameById(String productID) {
        ProductModel product = productRepository.findByProductID(productID)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product != null) {
            return product.getProductName();  // Trả về tên sản phẩm
        } else {
            return "Product not found";  // Nếu không tìm thấy sản phẩm, trả về thông báo lỗi
        }// Trả về tên sản phẩm hoặc "Unknown Product" nếu không tìm thấy
    }

}
