// This controller performs all user-oriented functionality
// import the user schema
const User = require('../models/user');
// import the bcrypt module to hash and salt provided password
const bcrypt = require('bcrypt');
// import mongoose for mongodb support
const mongoose = require('mongoose');
// import the tool used to sign tokens
const jwt = require('jsonwebtoken');
// import the tool used to decode json web tokens
const jwtDecode = require('jwt-decode');

// This function is used to find all the users.
// THIS IS REALLY INSECURE. THIS WOULD BE REMOVED IN PRODUCTION ENVIRONMENT
// Seriously.
// Handle GET request for all users
exports.get_all_users = (req, res, next) => {
    // grab all the users found in the mongodb
    User.find()
        // .select('email _id userId')
        .exec()
        .then(result => {
            // provide user feedback
            console.log(result);
            // create a response object with an array of all the users and their attributes
            const response = {
                count: result.length,
                users: result.map(result => {
                    return {
                        email: result.email,
                        _id: result._id,
                        userId: result.userId
                    }
                })
            }
            // if no errors were found, return a successful status
            res.status(200).json(response);
        })
        .catch(err => {
            // log the error and return 500 status
            console.log(err);
            res.status(500).json({
                error: err
            });
        });
};

// Handle POST requests to create new users
exports.user_create_user = (req, res, next) => {
    // Check if the given email already exists in the database
    User.find({ email: req.body.email })
        .exec()
        .then(user => {
            if (user.length >= 1) {
                // If the user already exists in the database
                // status 409: duplicate
                return res.status(409).json({
                    message: "Email already in use. Unable to create new User."
                });
            } else {
                // Hash the given password
                // first argument is the password to hash, the second argument
                // is the number of salting rounds
                bcrypt.hash(req.body.password, 10, (err, hash) => {
                    if (err) {
                        return res.status(500).json({
                            // If there was an error creating the password, return the error
                            error: err
                        });
                    } else {
                        // Else create a user and return the hashed password
                        // Create a new user
                        const user = new User({
                            _id: new mongoose.Types.ObjectId(),
                            email: req.body.email,
                            // Don't store password in plaintext
                            password: hash
                        });
                        // Save the user in the database
                        user.save()
                            .then(result => {
                                // log the result and return 201 status
                                console.log(result);
                                res.status(201).json({
                                    message: "User created"
                                });
                            })
                            .catch(err => {
                                // log the error and return 500 status
                                console.log(err);
                                res.status(500).json({
                                    message: "Unable to create user.",
                                    error: err
                                });
                            });
                    }
                });
            }
        });
};

// Handle POST request to login user
exports.user_login = (req, res, next) => {
    // Find user with the given email in the database
    User.find({ email: req.body.email })
        .exec()
        .then(users => {
            // An array will be returned here.  If no user, empty array
            // if user exists, array with one user
            if (users.length < 1) {
                // Below is insecure code. Don't do this.
                // return res.status(404).json({
                //     message: "Email not found.  User doesn't exist."
                // });

                // Return a 401 status to show not authorized
                return res.status(401).json({
                    message: "Authorization failed"
                });
            }
            // make sure the password sent with the request matches the password in the database
            bcrypt.compare(req.body.password, users[0].password, (err, result) => {

                // If the password doesn't match, do this
                if (err) {
                    // Return a 401 status to show not authorized
                    return res.status(401).json({
                        message: "Authorization failed"
                    });
                }

                if (result) {
                    // Create a token
                    // jwt.sign()
                    const token = jwt.sign({
                        email: users[0],
                        userId: users[0]._id,
                        role: users[0].role
                    },
                    // This sets the JWT_KEY to expire in 1 hour
                    // After that time, they will have to log back in
                        process.env.JWT_KEY,
                        {
                            expiresIn: "1h"
                        });

                    // result is true if it succeeds, false if it fails
                    return res.status(200).json({
                        message: "Authorization Successful",
                        token: token
                    });
                }

                // Return a 401 status to show not authorized
                return res.status(401).json({
                    message: "Authorization failed"
                });
            });
        })
        .catch(err => {
            // log error and return 500 status
            console.log("Error: ", err);
            res.status(500).json({
                error: err
            });
        });
};

// Handle DELETE requests to delete user
exports.user_delete_user = (req, res, next) => {
    // Boolean to determine if the user is in the mongodb
    let userExists = true;

    // Determine whether user exists in database
    User.find({ _id: req.params.userId })
    // User.find()
        .exec()
        .then(queryResult => {
            // log whether the user exists in the database
            console.log(queryResult.length);
            if (queryResult.length < 1) {
                // provide user feedback and set boolean to false
                console.log("User does not exist");
                userExists = false;
            }
        }).catch(err => {
            // log error
            console.log("Error while searching for user before throwing error");
        });

        // delete the user
    User.deleteOne({ _id: req.params.userId })
        .exec()
        .then(result => {
            // if the user was found and deleted, return 200 status
            if (userExists) {
                res.status(200).json({
                    message: "User deleted"
                });
            } else {
                // if the user was not found, throw error (which would log the error and return 500 status)
                throw "";
            }
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                message: "Unable to delete user",
                error: err
            });
        });
};