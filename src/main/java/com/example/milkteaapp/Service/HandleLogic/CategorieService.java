package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.CategorieModel;
import com.example.milkteaapp.Repository.CategorieRepository;
import com.example.milkteaapp.Service.Interface.ICategorieService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService implements ICategorieService {
    @Autowired
    private CategorieRepository categorieRepository;

    //Hàm Select Categorie
    @Override
    public List<CategorieModel> getCategorieAll() {
        return categorieRepository.findAll();
    }

    //Hàm Thêm
    @Override
    public CategorieModel AddCategorie(CategorieModel categorie) {
        // Tạo mã mới cho danh mục
        String newCategoryId = generateNewCategoryId();
        // Kiểm tra nếu mã mới đã tồn tại
        Optional<CategorieModel> existingCategory = categorieRepository.findById(newCategoryId);
        Optional<CategorieModel> existingCategoryName = categorieRepository.findByCategoryNameIgnoreCase(categorie.getCategoryName());
        if (existingCategory.isPresent() || existingCategoryName.isPresent()) {
            throw new IllegalStateException("Có thể tên loại sản phẩm bị trùng với tên : "+categorie.getCategoryName() + " hoặc có thể mã loại "+newCategoryId+" đã tồn tại ");
        }

        // Thiết lập mã cho danh mục
        categorie.setCategoryID(newCategoryId);
        return categorieRepository.save(categorie);
    }
    private String generateNewCategoryId() {
        // Lấy danh sách tất cả các danh mục
        List<CategorieModel> categories = categorieRepository.findAll();

        // Tìm mã lớn nhất hiện có
        String maxId = categories.stream()
                .map(CategorieModel::getCategoryID)
                .filter(id -> id.startsWith("CT"))
                .sorted()
                .reduce((first, second) -> second)
                .orElse("CT001"); // Nếu không có mã nào, bắt đầu từ CT000

        // Tăng mã lên một đơn vị
        int newIdNumber = Integer.parseInt(maxId.substring(2)) + 1; // Lấy phần số từ mã
        return String.format("CT%03d", newIdNumber); // Định dạng thành CT001, CT002,...
    }

    @Override
    public CategorieModel UpdateCategorie(String categorieID, CategorieModel categorie) {
        Optional<CategorieModel> optionalCategorieId = categorieRepository.findById(categorieID);
        if (optionalCategorieId.isPresent()) {
            CategorieModel categorieModel = optionalCategorieId.get();
            UpdateCategorieDetail(categorieModel, categorie);

            return categorieRepository.save(categorieModel);
        } else {
            throw new EntityNotFoundException("Categorie with ID " + categorieID + " not found.");

        }
    }

    //Hàm tạm để gán giá trị mới thành giá trị hiện tại
    private void UpdateCategorieDetail(CategorieModel exitingCategorie, CategorieModel newCategorie) {
        exitingCategorie.setCategoryName(newCategorie.getCategoryName());
        exitingCategorie.setDescription(newCategorie.getDescription());

    }

    @Override
    public boolean deleteCategorie(String categorieID) {
        CategorieModel categorieModel = categorieRepository.findByCategoryID(categorieID);
        if (categorieModel != null) {
            categorieRepository.deleteById(categorieID);
            return true;
        }
        return false;
    }

    //Hàm tìm kiếm
    @Override
    public List<CategorieModel> SearchCategorie(String categoryName) {
        //sử dụng Optional
        //kết quả trả về nó phụ thuộc vào giá trị của Optional
        //nếu như Optional =null thì nó sẽ trả về kết quả dự phòng là orElse(new CategorieModel())
        //Còn nếu như Optional tồn tại giá trị thì nó sẽ mặc định trả về giá tr của Optional đó
        if (categoryName == null) {

            List<CategorieModel> categorieList = categorieRepository.findAll();
            return categorieList;
        } else {
            List<CategorieModel> listCategoryWithName = categorieRepository.findByCategoryNameContainingIgnoreCase(categoryName);
            if (listCategoryWithName.isEmpty()) {
                throw new EntityNotFoundException("Không tìm thấy " + categoryName + "!");
            } else {
                return listCategoryWithName;
            }

        }
    }
}
