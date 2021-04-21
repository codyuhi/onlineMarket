// This middleware file makes sure that the correct user is trying to make changes for the correct users' property
// import the jwt decode module to decode the hashed auth data
const jwtDecode = require('jwt-decode');

// export the middleware function
module.exports = (req, res, next) => {

    // grab the token from the authorization header in the request
    const token = req.headers.authorization.split(" ")[1];
    // decode the token so it's in plaintext
    const decoded = jwtDecode(token);

    // if the decoded user id is not the user id that was passed in the request,
    if (decoded.userId != req.params.userId) {

        // This would have been used to allow admin users to make any change they want
        if (decoded.role === "admin"){
            next();
        }
        
        // Return an error code indicating that you don't have permissions to perform the desired action
        return res.status(403).json({
            message: "Insufficient Permissions to Perform this Action"
        });
    } else {
        
        // if the decoded user is the same as the user who created the property, allow the request to fulfilled
        next();
    }
}
