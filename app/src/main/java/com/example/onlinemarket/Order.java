package com.example.onlinemarket;

import com.example.onlinemarket.Product;

public class Order {

    public String _id;
    public Product product;
    public Integer quantity;
    public String userId;

    public Order(
            String _id,
            Product product,
            Integer quantity,
            String userId
    ){
        this._id = _id;
        this.product = product;
        this.quantity = quantity;
        this.userId = userId;
    }
}
