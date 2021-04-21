// This file is not used in the project.  It was created in case you want to make sure the correct order is being accessed (not useful)
const jwtDecode = require('jwt-decode');
const mongoose = require('mongoose');
const Order = require('../models/order');

module.exports = (req, res, next) => {

    const token = req.headers.authorization.split(" ")[1];
    const decoded = jwtDecode(token);

    var userId = "";

    // console.log("req.params.productId is", req.params.productId);

    Order.find({ _id: req.params.orderId })
        .exec()
        .then(result => {

            if (result.length < 1) {
                console.log("Unable to find orderId in the auth checker");
                return res.status(404).json({
                    message: "This order does not exist"
                })
            }
            // console.log("Result is", result);
            userId = result[0].userId;
            // console.log("The userId is now", userId);
            // console.log("decoded.userId is", decoded.userId);

            if (decoded.userId != userId) {

                if (decoded.role === "admin") {
                    next();
                }
                // console.log(decoded.userId, "was found to be different from", userId);
                return res.status(403).json({
                    message: "Insufficient Permissions to Perform this Action"
                });
            } else {
                next();
            }
        })
        .catch(err => {
            console.log("Error occurred while getting userId from the order");
            res.status(500).json({
                error: err
            });
        });
}
