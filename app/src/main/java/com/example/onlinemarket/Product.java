package com.example.onlinemarket;

public class Product {

    public String _id;
    public String name;
    public Float price;
    public String productImage;
    public String userId;
    public String email;

    public Product(
            String _id,
            String name,
            Float price,
            String productImage,
            String userId,
            String email
    ){
        this._id = _id;
        this.name = name;
        this.price = price;
        this.productImage = productImage;
        this.userId = userId;
        this.email = email;
    }

}
