// This file allows for a middleware function to check whether a user is logged in

// import the jsonwebtoken module
const jwt = require('jsonwebtoken');

// This function is exported to the other routes so that it can check whether a user is logged in or not for certain functions
module.exports = (req, res, next) => {
    // Call next if successfully authenticated, call error if not
    // Verify that the token exists
    // jwt.verify throws an error if it fails, so we put it in a try/catch
    try {
        // try to get the token from the Authorization header
        const token = req.headers.authorization.split(" ")[1];
        // console.log(token);
        // Verify that the token matches a logged in user who has a valid session
        const decoded = jwt.verify(token, process.env.JWT_KEY);
        req.userData = decoded;
        next();
    } catch (error) {
        return res.status(401).json({
            message: 'Authentication Failed'
        });
    }
};