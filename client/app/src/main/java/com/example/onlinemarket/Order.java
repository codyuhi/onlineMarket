// This file isn't used, but can be built upon and used in future iterations of the app
// define the package
package com.example.onlinemarket;

// import the product class
import com.example.onlinemarket.Product;

// create class definition
public class Order {

// every Order has an id, a product, a number of the product, and a userId of the buyer
    public String _id;
    public Product product;
    public Integer quantity;
    public String userId;

// constructor
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
