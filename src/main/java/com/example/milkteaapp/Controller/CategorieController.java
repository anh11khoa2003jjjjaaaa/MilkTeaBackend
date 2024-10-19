package com.example.milkteaapp.Controller;

import com.example.milkteaapp.DTO.CategoryDTO;
import com.example.milkteaapp.Model.CategorieModel;
import com.example.milkteaapp.Service.Interface.ICategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/categorie")
@CrossOrigin(origins = "http://localhost:4200")//3000 dành cho react
public class CategorieController {
    @Autowired
    private final ICategorieService categorieService;

    @Autowired
    public CategorieController(ICategorieService categorieService) {
        this.categorieService = categorieService;
    }


    //Hàm GetALl có kiểu dữ liệu trả về là Object.
    @GetMapping("/getAllCategories")
    public ResponseEntity<Object> getAllCategories() {
        try {
            // Lấy danh sách từ CategoryModel
            List<CategorieModel> categories = categorieService.getCategorieAll();
            return ResponseEntity.ok(categories); // Đơn giản hóa việc tạo phản hồi
        } catch (Exception e) {
            // Trả về mã lỗi và thông tin lỗi cho phía client
            Map<String, String> errorDetails = Map.of("error", "Lỗi khi lấy danh sách các danh mục", "message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
        }
    }

    //Hàm getAll có kiểu trả về đối tượng CategorieModel
// @GetMapping("/getAllCategorie")
// public ResponseEntity<List<CategoryDTO>> getAllCustomers() {
//     try {
//         //Lấy danh sách từ CategoryModel
//         List<CategorieModel> categories = categorieService.getCategorieAll();
//         // Chuyển đổi từ CategoryModel sang CustomerDTO
//         List<CategoryDTO> categoriesDTOs = categories.stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//
//         return new ResponseEntity<>(categoriesDTOs, HttpStatus.OK);
//     } catch (Exception e) {
//         // Xử lý lỗi và trả về mã lỗi HTTP phù hợp
//
//         return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi khi lấy danh sách các danh mục", "message", e.getMessage()));
//     }
// }

//    private CategoryDTO convertToDTO(CategorieModel categoryModel) {
//        CategoryDTO categoryDto = new CategoryDTO();
//        categoryDto.setCategoryID(categoryModel.getCategoryID());
//        categoryDto.setCategoryName(categoryModel.getCategoryName());
//        categoryDto.setCategoryDescription(categoryModel.getDescription());
//        // Thêm các thuộc tính khác nếu cần
//        return categoryDto;
//    }

    @GetMapping("search/{categoryName}")
    public ResponseEntity<Object> searchCategory(@PathVariable String categoryName) {

        if(categoryName==null){
            List<CategorieModel>list=categorieService.getCategorieAll();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }else{
        List<CategorieModel> categorieModel = categorieService.SearchCategorie(categoryName);
//        List<CategorieModel> categorieModel1 = categorieService.SearchCategorie(categoryName==null?"":categoryName);
        if (categorieModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }else{
            return ResponseEntity.status(HttpStatus.OK).body(categorieModel);
        }

    }
    }

    //Hàm thêm Category
    @PostMapping("/addCategories")
    public ResponseEntity<Object> addCategory(@RequestBody CategorieModel categorieModel) {
        try {

            CategorieModel categorieModelData = categorieService.AddCategorie(categorieModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(categorieModelData);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Loại sản phẩm đã tồn tại", "message", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi khi thêm loại sản phẩm", "message", e.getMessage()));
        }

    }

    //Hàm cập nhật
    @PutMapping("/updateCategories/{categoryID}")
    public ResponseEntity<Object> updateCategory(@PathVariable String categoryID, @RequestBody CategorieModel categorieModel) {
        try {
            CategorieModel categorieModelData=categorieService.UpdateCategorie(categoryID, categorieModel);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success",categorieModelData,"message","Cập nhật loại sản phẩm thành công!"));
        }catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",categoryID+" không tồn tại!","message", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Đã xảy ra lỗi khi cập nhật loại sản phẩm","message",e.getMessage()));
        }

    }
    @DeleteMapping("/delete/{categorieID}")
    public ResponseEntity<Object> deleteCategorie(@PathVariable String categorieID) {
        boolean isDeleted = categorieService.deleteCategorie(categorieID);

        if (isDeleted) {
            // Trả về trạng thái thành công nếu danh mục được xóa thành công
            return ResponseEntity.ok("Category deleted successfully");

        } else {
            // Trả về trạng thái lỗi nếu không tìm thấy danh mục
            return ResponseEntity.status(404).body("Category not found or could not be deleted");
        }
    }

}
