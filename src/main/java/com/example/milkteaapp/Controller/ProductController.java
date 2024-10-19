package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Repository.ProductRepository;
import com.example.milkteaapp.Service.HandleLogic.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/public/product")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"})
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<Object> getAllProduct() {
        try {
            List<ProductModel> listproductmodel = productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(listproductmodel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Xãy ra lỗi khi truy vấn dữ liệu!", "", e.getMessage()));
        }
    }

//    @GetMapping("/paginationProduct")
//    public Page<ProductModel> getProducts(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "6") int size) {
//        return productService.getProducts(page, size);
//    }
    @GetMapping("/search")
    public ResponseEntity<Object> searchProducts(@RequestParam(required = false) String searchTerm) {
        try {
            if(searchTerm == null) {
                List<ProductModel> productsall = productService.getAllProducts();
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", productsall));
            }else{
            // Gọi phương thức tìm kiếm từ tầng service
            List<ProductModel> products = productService.searchProduct(searchTerm);
                if (products == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy sản phẩm!", "message", products));

                }else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", products, "message", "Tìm kiếm thành công!"));
                }
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy sản phẩm!", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi khi tìm kiếm sản phẩm", "message", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProduct(
            @RequestParam(required = false) String productID,
            @RequestPart("product") String productJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        if (productID == null || productID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Thiếu ID sản phẩm", "message", "Vui lòng cung cấp ID sản phẩm để cập nhật."));
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductModel product = objectMapper.readValue(productJson, ProductModel.class);

            if (imageFile != null && !imageFile.isEmpty()) {
                String contentType = imageFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body(Map.of("error", "Tệp tải lên phải là định dạng hình ảnh."));
                }

                // Thêm thời gian hiện tại vào tên file ảnh
                String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-");
                String newFileName = timestamp + "_" + imageFile.getOriginalFilename();

                String imagePath = "D:\\Project\\Nam4_hk1\\MilkTeaApp\\src\\main\\resources\\static\\IMG\\" + newFileName;
                File file = new File(imagePath);

                if (!file.getParentFile().exists()) {
                    boolean dirsCreated = file.getParentFile().mkdirs();
                    if (!dirsCreated) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Không thể tạo thư mục để lưu trữ hình ảnh."));
                    }
                }

                if (file.exists()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("error", "Tệp ảnh đã tồn tại, vui lòng chọn tên tệp khác."));
                }

                imageFile.transferTo(file);
                product.setImageURL(newFileName);
            } else {
                Optional<ProductModel> existingProduct = productRepository.findById(productID);
                if (existingProduct.isPresent()) {
                    product.setImageURL(existingProduct.get().getImageURL());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Sản phẩm không tồn tại."));
                }
            }

            ProductModel updatedProduct = productService.updateProduct(productID, product);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Cập nhật sản phẩm thành công!", "message", updatedProduct));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi khi cập nhật sản phẩm!", "message", e.getMessage()));
        }
    }


    @DeleteMapping("/deleteproduct")
    public ResponseEntity<Object> deleteProduct(@RequestParam(required = false) String productID) {
        // Kiểm tra nếu productID null hoặc rỗng
        if (productID == null || productID.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Thiếu ID sản phẩm",
                    "message", "Vui lòng cung cấp ID sản phẩm để xóa."
            ));
        }

        // Xóa sản phẩm
        boolean handleDeleteProduct = productService.deleteProduct(productID);
        if (handleDeleteProduct) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", "Xóa thành công!",
                    "message", "Sản phẩm với ID " + productID + " đã được xóa."
            ));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Đã xảy ra lỗi khi xóa sản phẩm có ID " + productID,
                    "message", "Không thể xóa sản phẩm."
            ));
        }
    }

    @PostMapping("/addproduct")
    public ResponseEntity<Object> AddProduct(@RequestPart("product") String productJson,
                                             @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            // Kiểm tra nếu file ảnh không được truyền
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "File ảnh không được bỏ trống."));
            }

            // Kiểm tra xem file có phải là hình ảnh hay không (dựa trên loại MIME)
            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(Map.of("error", "Tệp tải lên phải là định dạng hình ảnh."));
            }

            // Lấy thời gian hiện tại và định dạng tên ảnh
            String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-");
            String newFileName = timestamp + "_" + imageFile.getOriginalFilename();

            // Đường dẫn để lưu ảnh vào thư mục static/IMG/
            String imagePath = "D:\\Project\\Nam4_hk1\\MilkTeaApp\\src\\main\\resources\\static\\IMG\\" + newFileName;
            File file = new File(imagePath);

            // Tạo thư mục nếu chưa tồn tại
            if (!file.getParentFile().exists()) {
                boolean dirsCreated = file.getParentFile().mkdirs();
                if (!dirsCreated) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Không thể tạo thư mục để lưu trữ hình ảnh."));
                }
            }

            // Kiểm tra xem file đã tồn tại hay chưa
            if (file.exists()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Tệp ảnh đã tồn tại, vui lòng chọn tên tệp khác."));
            }

            // Lưu file vào thư mục
            imageFile.transferTo(file);

            // Chuyển đổi từ JSON thành ProductModel
            ObjectMapper objectMapper = new ObjectMapper();
            ProductModel product = objectMapper.readValue(productJson, ProductModel.class);

            // Lưu tên file (hoặc đường dẫn) vào đối tượng sản phẩm
            product.setImageURL(newFileName);

            // Lưu sản phẩm vào cơ sở dữ liệu
            ProductModel savedProduct = productService.addProduct(product);

            // Trả về phản hồi thành công với chi tiết sản phẩm đã lưu
            return ResponseEntity.status(HttpStatus.OK).body(savedProduct);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Xảy ra lỗi khi lưu file hình ảnh.", "details", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Thông tin sản phẩm không hợp lệ.", "details", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định.", "details", e.getMessage()));
        }
    }

    @GetMapping("/getCategoryNameByProductID/{productID}")
    public ResponseEntity<Object> getCategoryNameByProductID(@PathVariable String productID) {
        try {
            // Gọi phương thức trong service để lấy tên danh mục
            String categoryName = productService.getCategoryNameByProductID(productID);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("categoryName", categoryName));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi", "message", e.getMessage()));
        }
    }

    @GetMapping("/product_detail")
    public ResponseEntity<ProductModel> getProductById(@RequestParam String productID) {
        ProductModel product = productService.getProductID(productID);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy sản phẩm
        }
    }
    @GetMapping("/related")
    public ResponseEntity<Object> getRelatedProducts(@RequestParam String productID) {
        try {
            // Lấy sản phẩm theo ID
            ProductModel product = productService.getProductID(productID);

            // Kiểm tra xem sản phẩm có danh mục hay không
            if (product != null && product.getCategory() != null) {
                // Lấy danh sách sản phẩm liên quan dựa trên categoryID
                List<ProductModel> relatedProducts = productService.getRelatedProductsByCategoryID(product.getCategory().getCategoryID());
                return ResponseEntity.ok(relatedProducts);
            } else {
                // Trả về thông báo lỗi nếu sản phẩm không có danh mục
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Sản phẩm không có danh mục liên quan."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi khi lấy sản phẩm liên quan."));
        }
    }

    @GetMapping("/DisplayListProductCategory_CategoryID/{categoryID}")
    public ResponseEntity<?> displayListProductCategoryByCategoryID(@PathVariable String categoryID) {
        List<ProductModel> result = productService.getRelatedProductsByCategoryID(categoryID);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy sản phẩm có mã danh mục."));
        } else {
            return ResponseEntity.ok(result);  // Trả về danh sách sản phẩm nếu tìm thấy
        }
    }

    @GetMapping("/{productID}/name")
    public ResponseEntity<Map<String, String>> getProductName(@PathVariable String productID) {
        try {
            String productName = productService.getProductNameById(productID);

            // Tạo một đối tượng Map để chứa giá trị trả về dưới dạng JSON
            Map<String, String> response = new HashMap<>();
            response.put("productName", productName);  // Đặt tên sản phẩm vào trong JSON

            return ResponseEntity.ok(response);  // Trả về giá trị JSON
        } catch (RuntimeException e) {
            // Trả về thông báo lỗi nếu không tìm thấy sản phẩm
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Product not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }


}


