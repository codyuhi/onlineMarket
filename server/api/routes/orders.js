// import express
const express = require('express');
// import the checkAuth function
const checkAuth = require('../middleware/check-auth');
// import the orders controller
const OrdersController = require('../controllers/orders');
const checkCorrectOrder = require('../middleware/check-correct-order');
// This is a method that registers different routes based on the 
// provided resource (GET or POST)
const router = express.Router();

// A GET request here returns all the recorded orders
router.get('/', checkAuth, OrdersController.orders_get_all);

// A POST request allows for an order to be created
router.post('/', checkAuth, OrdersController.orders_create_order);

// A GET request for a certain order ID will return that order's information
// The correct user must be logged in for this to be accepted
router.get('/:orderId', checkAuth, checkCorrectOrder, OrdersController.orders_get_order);

// A DELETE request here will delete the order
// The correct user must be logged in for this to be accepted
router.delete('/:orderId', checkAuth, checkCorrectOrder, OrdersController.orders_delete_order);

// Export this module for use in other files
module.exports = router;