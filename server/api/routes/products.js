// import express
const express = require('express');
// multer is used to parse the data for an image file
const multer = require('multer');
// Import the middleware to check to see if the user is logged in
const checkAuth = require('../middleware/check-auth');
// Import the products controller
const ProductsController = require('../controllers/products');
const checkCorrectProduct = require('../middleware/check-correct-product');

// Allows you to configure the way that files are stored when uploaded
const storage = multer.diskStorage({
    destination: function(req, file, cb){
        cb(null, './uploads/');
    },
    filename: function(req,file,cb) {
        cb(null, new Date().toISOString().replace(/:/g, "-") + file.originalname);
    }
});

// Custom filter for multer
const fileFilter = (req, file, cb) => {
    // only accept files that are jpegs, pngs, or jpgs
    if(file.mimetype === 'image/jpeg' || file.mimetype === 'image/png' || file.mimetype === 'image/jpg'){
        // accept a file
        cb(null, true);
    }else{
        // reject a file
        cb(null, false);
    }
    // if you don't set null throws an error
    // like this:
    // cb(new Error('message'),false);
};

// Execute/initialize multer
// /uploads/ is the place where multer will try to store the files
const upload = multer({
    storage: storage,
    limits: {
        fileSize: 1024 * 1024 * 5
    },
    fileFilter: fileFilter
});

// This is a method that registers different routes based on the 
// provided resource (GET or POST)
const router = express.Router();

// Import the product mongoose model schema so we can store
// The product in the mongodb database
const Product = require('../models/product');

// This is a method that will handle GET requests
// This is just a slash because they will only get to this script
// if the user already is at some http:// ... /products/ route
router.get('/', ProductsController.get_all_products);

// upload is an image file. single will parse only one file. productImage is the field that will hold the file
router.post('/', checkAuth, upload.single('productImage'), ProductsController.products_create_product);

// This responds with a message and an id if an id was passed in the query string
router.get('/:productId', ProductsController.products_get_product);

// Verify that the correct user is logged in before the database entry is PUTted
router.put('/:productId', checkAuth, checkCorrectProduct, ProductsController.products_put_product);

// Verify that the correct user is logged in before the database entry is DELETEd
router.delete('/:productId', checkAuth, checkCorrectProduct, ProductsController.products_delete_product);

// Export the module for use in other files
module.exports = router;