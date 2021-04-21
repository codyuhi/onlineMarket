// The user routes file defines the possible routes for API calls to affect User functionality
// import express
const express = require('express');
// Create an express router to route requests
const router = express.Router();
// import the user controller to handle user requests
const UserController = require('../controllers/user');
// import the check-auth middleware to verify the correct user is logged in before they perform a delete request
const checkAuth = require('../middleware/check-auth');
// import the check-correct-user middleware to verify a user is logged in before they perform a delete request
const checkCorrectUser = require('../middleware/check-correct-user');

// Two routes: sign up and sign in
// The logged in/out status is handled by the client

// This route gets all users. In production, this should be disabled. Here, it is enabled for my own functionality
router.get('/', UserController.get_all_users);

// This route creates a new user with a POST request
router.post('/signup', UserController.user_create_user);

// This route logs in a user with a POST request
// It will also establish a session for the logged in user
router.post('/login', UserController.user_login);

// Method to delete existing users
// The user must be logged in as the correct user in order to delete themselves
router.delete('/:userId', checkAuth, checkCorrectUser, UserController.user_delete_user);

// export this module for use in other files
module.exports = router;