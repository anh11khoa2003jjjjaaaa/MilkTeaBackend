package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.CategorieModel;
import com.example.milkteaapp.Model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService {
     List<ProductModel>getAllProducts();
     List<ProductModel> searchProduct(String searchTerm);
     ProductModel addProduct(ProductModel product) throws IOException;
     ProductModel updateProduct(String productID,ProductModel product);
    Boolean deleteProduct(String productID);
   String getCategoryNameByProductID(String categoryID);
    Page<ProductModel> getProducts(int page, int size);
    ProductModel getProductID(String productID);
    List<ProductModel>getRelatedProductsByCategoryID(String categoryID);
    String getProductNameById(String productID);

}
