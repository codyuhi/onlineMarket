// This file is the main interface with which routes are included and resources are defined
// import express
const express = require('express');
// spin up an express application
const app = express();
// morgan is a logging tool.  We will make express funnel all requests through
// morgan in order to log activity
const morgan = require('morgan');
// Used to parse incoming http requests
const bodyParser = require('body-parser');
// Import mongoose for using the MongoDB API
const mongoose = require('mongoose');

// The environment variable is not included in the git files.  You have to manually create that file yourself and configure it to hold the mongo password
// This is for security. I don't want to give my mongo password to anyone via github
try{
mongoose.connect('mongodb+srv://Uhi:' + process.env.MONGO_ATLAS_PW_ENCODED + '@restful-api-pavba.mongodb.net/test?retryWrites=true&w=majority',
    {  
        // The below line is for a deprecated version of mongoose.
        // The lines below it replace its functionality
        // useMongoClient: true,
        useNewUrlParser: true,
        useUnifiedTopology: true,
        useFindAndModify: false,
        useCreateIndex: true
    }
);
}catch(err){
    // Provide user feedback for if the mongo connection failed
    console.log(err);
}

// make the mongoose promise globally available
mongoose.Promise = global.Promise;


// Log the activity
app.use(morgan('dev'));
// Makes a folder publicly available for images to be uploaded to the API
app.use('/uploads', express.static('uploads'));
// parse the urlencoded http request
app.use(bodyParser.urlencoded({extended: false}));
// parse the app into json data
app.use(bodyParser.json());

// Handle CORS errors
app.use((req, res, next) => {
    // Set the Access control allow origin property in the header
    // If you want to restrict access to be only from one source,
    // Just use res.header('A . . .', 'THE-URL-OF-THE-SOURCE');
    res.header('Access-Control-Allow-Origin', '*');
    // Define what headers can be allowed with the request
    res.header(
        'Access-Control-Allow-Headers',
        'Origin, X-Requested-With, Content-Type, Accept, Authorization'
    );
    // Check if the incoming request's method is options
    if(req.method === 'OPTIONS'){
        // Add an additional header telling the browser what it can send
        res.header('Access-Control-Allow-Methods','PUT, POST,PATCH,DELETE,GET');
        return res.status(200).json({});
    }
    // Allow other routes to take over if none of the above is triggered
    next();
});

// Define the routes through which requests can be received and processed
const productRoutes = require('./api/routes/products');
const orderRoutes = require('./api/routes/orders');
const userRoutes = require('./api/routes/user');
const checkRoute = require('./api/routes/checkConnection');

// Set up middleware. THIS WOULD BE USED IF A SINGLE RESPONSE IS GIVEN FOR EVERY REQUEST
// app.use((req, res, next) => {
//     // send a json response if the http response is 200 (everything ok)
//     res.status(200).json({
//         message: 'It works!'
//     });
// });

// Anytime requests aimed at /products are made, handle it with the products script
app.use('/products', productRoutes);
// Same thing with orderRoutes
app.use('/orders', orderRoutes);
// Same thing with userRoutes
app.use('/user', userRoutes);
// This checkConnection GET request handler will verify that a connection is possible with the server before doing anything on the app
// This allows for user feedback and avoids crashes
app.use('/checkConnection', checkRoute);

// Handle errors by catching all the requests that make it past the above requests
app.use((req, res, next) => {
    const error = new Error('Route Not Found');
    // Create a status code for the page not found
    error.status = 404;
    // Forward the error request instead of the original request
    next(error);
});

// If the route not found request is missed, then handle errors with a internal server error response
app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: {
            message: error.message
        }
    });
});

// Export this module for use in other files
module.exports = app;