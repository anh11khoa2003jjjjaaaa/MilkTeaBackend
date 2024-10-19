package com.example.milkteaapp;

import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.CartModel;
import lombok.Data;

@Data
public class CartRequest {
    private CartModel cartModel;
    private CartDetailModel cartDetailModel;
}
