// This controller handles all the orders requests
// import the order schemas
const Order = require('../models/order');
// import the product schemas
const Product = require('../models/product');
// import the mongoose module
const mongoose = require('mongoose');
// import the jwt module
const jwtDecode = require('jwt-decode');

// Handle GET requests to return all orders
exports.orders_get_all = (req, res, next) => {
    // res.status(200).json({
    //     message: 'Orders were fetched'
    // });
    Order.find()
        .select('quantity product _id userId')
        .populate('product', 'name')
        .exec()
        .then(docs => {
            res.status(200).json({
                count: docs.length,
                orders: docs.map(doc => {
                    return {
                        _id: doc._id,
                        product: doc.product,
                        quantity: doc.quantity,
                        request: {
                            type: 'GET',
                            url: 'http://localhost:3000/orders/' + doc._id
                        },
                        userId: doc.userId
                    }
                })
            });
        })
        .catch(err => {
            // log the error and return 500 status
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
};

// Handle POST requests to create a new order
exports.orders_create_order = (req, res, next) => {
    // const order = {
    //     productId: req.body.productId,
    //     quantity: req.body.quantity
    // };

    // Check if you have a product for a given id
    Product.findById(req.body.productId)
        .then(product => {
            if (!product) {
                // if there is no product found from the given product id, return 404 status
                return res.status(404).json({
                    message: "Product not found"
                });
            }

            // Grab the user token from the authorization header
            const token = req.headers.authorization.split(" ")[1];
            // decode the user token
            const decoded = jwtDecode(token);

            // Create a new order
            const order = new Order({
                _id: mongoose.Types.ObjectId(),
                quantity: req.body.quantity,
                product: req.body.productId,
                userId: decoded.userId
            });
            // save the order
            return order.save()
        })
        .then(result => {
            // log the result
            console.log(result);
            // try returning a 201 status
            res.status(201).json({
                message: 'Order stored',
                createdOrder: {
                    _id: result._id,
                    product: result.product,
                    quantity: result.quantity,
                    userId: result.userId
                },
                request: {
                    type: 'GET',
                    url: 'http://localhost:3000/orders/' + result._id
                }
            });
        })
        .catch(err => {
            // log the error and return 500 status
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
    // .catch(err => {
    //     res.status(500).json({
    //         message: 'Product not found',
    //         error: err
    //     });
    // });

    // res.status(201).json({
    //     message: 'Order was created',
    //     order: order
    // })
};

// Handle GET request for a specific order
exports.orders_get_order = (req, res, next) => {
    // res.status(200).json({
    //     message: 'Order details',
    //     orderId: req.params.orderId
    // });
    // find the order whose ID matches the query string
    Order.findById(req.params.orderId)
        .populate('product')
        .exec()
        .then(order => {
            if (!order) {
                // if it can't find an order, return 404 status
                return res.status(404).json({
                    message: 'Order not found'
                });
            }
            // if it can find an order, return a 200 status with the requested order json data
            res.status(200).json({
                order: order,
                request: {
                    type: 'GET',
                    url: 'http://localhost:3000/orders'
                }
            })
        })
        .catch(err => {
            // log the error and return 500 status
            res.status(500).json({
                error: err
            });
        });
};

// Handle DELETE requests to delete orders
exports.orders_delete_order = (req, res, next) => {
    // res.status(200).json({
    //     message: 'Order deleted',
    //     orderId: req.params.orderId
    // });
    // remove the order with the id that matches the query string
    Order.remove({ _id: req.params.orderId })
        .exec()
        .then(result => {
            // if no errors are thrown, return 200 status
            res.status(200).json({
                message: 'Order deleted',
                request: {
                    type: "POST",
                    url: 'http://localhost:3000/orders',
                    body: {
                        productId: 'ID',
                        quantity: 'Number'
                    }
                }
            });
        })
        .catch(err => {
            // return 500 status if an error is thrown
            res.status(500).json({
                error: err
            });
        });
};