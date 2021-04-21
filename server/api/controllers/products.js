// import the product model
const Product = require('../models/product');
// import mongoose so we can create random ids
const mongoose = require('mongoose');
// import jwt decode so we can decode json web tokens
const jwtDecode = require('jwt-decode');

// handle GET requests for /products
exports.get_all_products = (req, res, next) => {
    // Below is dummy code
    // res.status(200).json({
    //     message: 'Handling GET requests to /products'
    // });

    // Not passing an element to find() will find all elements
    Product.find()
        // select defines which fields from the response json that you want to fetch with the GET Request
        .select('name price _id productImage userId email')
        .exec()
        .then(docs => {
            // provide user feedback showing what has been produced from the GET request
            console.log(docs);
            // create the response object
            const response = {
                count: docs.length,
                products: docs.map(doc => {
                    return {
                        name: doc.name,
                        price: doc.price,
                        productImage: doc.productImage,
                        _id: doc._id,
                        request: {
                            type: 'GET',
                            url: 'http://localhost:3000/products/' + doc._id
                        },
                        userId: doc.userId,
                        email: doc.email
                    };
                })
            };

            // The below if/else statement could be used to handle if the array of 
            // elements was empty.  Currently it returns an empty array.
            // You could set it up to return a different http status and json data
            // By uncommenting the below if/else statement
            // if (docs.length >= 0){
            res.status(200).json(response);
            // } else {
            //     res.status(404).json({
            //         message: 'No entries found'
            //     });
            // }
        })
        // handle errors with internal server error response code
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
};

// handle POST requests to create a new product
exports.products_create_product = (req, res, next) => {
    // The filePath variable will hold the path to the image file, if an image file was uploaded
    let filePath = "";
    // if there's a file, log the file that was uploaded and define filePath
    if (req.file){
        console.log(req.file);
        filePath = req.file.path;
    } else {
        // If there was no file, let the user know that no image was uploaded
        console.log("No image uploaded");
    }
    // This const product is replaced by the one below it
    // The one below it supports mongodb storage
    // const product = {
    //     name: req.body.name,
    //     price: req.body.price
    // };
    // grab the login token from the given header
    const token = req.headers.authorization.split(" ")[1];
    // decode the token
    const decoded = jwtDecode(token);

    // Create a new product object and give it the values that were passed in the POST request body
    const product = new Product({
        _id: new mongoose.Types.ObjectId(),
        name: req.body.name,
        price: req.body.price,
        productImage: filePath,
        userId: decoded.userId,
        email: decoded.email.email
    });
    // save is a method that is provided by mongoose
    // to store this object in the mongodb
    product
        .save()
        .then(result => {
            // log the result for user feedback
            console.log(result);
            res.status(201).json({
                // Dummy message
                // message: 'Handling POST requests to /products',
                message: "Created product successfully",
                // createdProduct: result
                createdProduct: {
                    name: result.name,
                    price: result.price,
                    _id: result._id,
                    request: {
                        type: 'GET',
                        url: 'http://localhost:3000/products/' + result._id
                    },
                    userId: decoded.userId,
                    email: decoded.email.email
                }
            });
        })
        .catch(err => {
            // log the user provided feedback and throw an internal server error
            console.log(err);
            console.log("The name is " + req.body.name);
            console.log("The price is " + req.body.price);
            res.status(500).json({
                error: err
            });
        });
};

// Handle GET requests for a single product
exports.products_get_product = (req, res, next) => {
    // Extract the product ID
    const id = req.params.productId;

    // This dummy code is no longer necessary after adding the MongoDB integration
    // // If the id is special, return a message
    // if(id === 'special'){
    //     res.status(200).json({
    //         message: 'You discovered the special ID',
    //         id: id
    //     });
    // } else {
    //     // else return a standard message with the id
    //     res.status(200).json({
    //         message: 'You passed an ID',
    //         id: id
    //     })
    // }

    // This is asynchronous
    Product.findById(id)
    // grab the attributes that will be worked with from the product in the mongodb
        .select('name price _id productImage userId email')
        .exec()
        .then(doc => {
            console.log("From database", doc);
            if (doc) {
                // if there is a returned selection, return a successful status
                res.status(200).json(doc);
            } else {
                // if there is no returned selection, return a resource not found status
                res.status(404).json({
                    message: "No valid entry found for provided ID"
                });
            }
        })
        .catch(err => {
            // if there is an error, return an internal server error status
            console.log(err);
            res.status(500).json({ error: err });
        });
};

// Handle PUT requests to edit existing product data
exports.products_put_product = (req, res, next) => {
    // Below is dummy code
    // res.status(200).json({
    //     message: 'Updated product!'
    // });

    // grab the product ID from the passed query string
    const id = req.params.productId;
    // create a operator set for changes to be made
    const updateOps = {};
    // Grab the attributes/values of the object
    // This way of implementing it makes it so you have to pass an array
    // and not just a JSON object
    for (const ops of req.body) {
        updateOps[ops.propName] = ops.value;
        console.log(ops.propName, ops.value);
    }
    // update the product with the matching id to hold the changed attributes' values
    Product.update({ _id: id }, {
        // updateOps below these attributes does the same as the below two lines
        // When used in conjunction with the above for loop
        // name: req.body.newName,
        // price: req.body.newPrice
        $set: updateOps
    })
        .exec()
        .then(result => {
            // if this request was successful, log the result and return a successful status
            console.log(result);
            res.status(200).json({
                message: 'Product updated',
                request: {
                    type: 'GET',
                    url: 'http://localhost:3000/products/' + id
                }
            });
        })
        .catch(err => {
            // errors produce an internal error response
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
};

// Handle DELETE requests
exports.products_delete_product = (req, res, next) => {
    // Below is some dummy code
    // res.status(200).json({
    //     message: 'Deleted product!'
    // });

    // NEED TO IMPLEMENT CHECK TO SEE IF THE ENTITY EXISTS,
    // and if so, it needs to also delete the file upload, if there is one

    // grab the product id from the query string
    const id = req.params.productId;
    // this is to handle weird cases where an item is requested to be deleted but does not exist
    var productExists = true;
    // this will be used to delete the file for the product too if the product has a file associated with it
    var filePath = "";

    // access the product whose id matches the one passed in the query string
    Product.find({_id: id})
        .select('_id name')
        .exec()
        .then(queryResult => {
            // let the user know what happened as a result of the request
            console.log("queryResult is",queryResult);
            // if the queryResult produced nothing, let the user know that nothing happened
            if (queryResult.length < 1) {
                console.log("Could not find the product to delete");
            } else {
                // else let the user know that it found the product and is going to delete it
                console.log("Found product to delete");
                // grab the filePath from the product that was found in the mongodb
                filePath = queryResult[0].filePath;
                // provide user feedback
                console.log("filePath is " + filePath);
                // Define the product id from the request parameters
                // Then remove any products in the database whose id matches the given id
                Product.deleteOne({ _id: id})
                    .exec()
                    .then(result => {
                        res.status(200).json({
                            message: "Product Deleted",
                            request: {
                                type: 'POST',
                                url: 'http://localhost:3000/products/',
                                body: {
                                    name: 'String',
                                    price: 'Number'
                                }
                            }
                        });
                    })
                    .catch(err => {
                        // log any errors and return an internal server error response
                        console.log(err);
                        res.status(500).json({
                            error: err
                        });
                    });
            }
        })
        .catch(err => {
            // report error found
            console.log("Error occurred while checking to see if the product exists");
            // Return resource not found
            res.status(500).json({
                error: err
            });
        });
};