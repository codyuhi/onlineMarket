// this file is used to define product objects
// define the package
package com.example.onlinemarket;

// begin class definition
public class Product {

// every product has a product id, name, price, productImage (possible), userId for the seller, and email for the seller
    public String _id;
    public String name;
    public Float price;
    public String productImage;
    public String userId;
    public String email;

// class constructor
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
