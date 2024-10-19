package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.CategorieModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategorieService {

    List<CategorieModel> getCategorieAll();
    CategorieModel AddCategorie(CategorieModel categorie);
    CategorieModel UpdateCategorie(String categorieID,CategorieModel categorie);
    boolean deleteCategorie(String categorieID);
    List<CategorieModel> SearchCategorie(String categorieID);
}
