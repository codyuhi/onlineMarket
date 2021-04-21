// import express
const express = require('express');
// import the check connection controller
const checkController = require('../controllers/checkConns');
// Create an express router to route the requests
const router = express.Router();

// A GET request here will return simple information verifying that the API is able to handle requests
router.get('/', checkController.validate);

// Export the module for use in other files
module.exports = router;