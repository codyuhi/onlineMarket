Web Service Description: Purpose:
For Project 2, I created an Online Marketplace Web Service. The purpose of this RESTful API is to create an environment in which buyers and sellers of products can interface to list products for sale, browse wares, and place/fulfill orders. A seller can log onto this application and list a new product for sale in the online marketplace. A buyer can then log on and see all products that are listed for sale at once, investigate the orders that are most interesting to them, and place an order for the item to be shipped to them. The seller, upon receiving the order notification, will be able to ship out the product to the customer and receive payment through a third-party app.
This is a valuable web service because it facilitates production and consumption of any good or potentially even service. This is also a project idea which works for project 2’s requirements because does the following things:
1. Uses NodeJS to implement the web service development platform
 
Cody Uhi – Project 2
 Demonstration of NodeJS Server Starting
2. Hosts a RESTful API interface with a URL routing and JSON data schema
 3. // Define the routes through which requests can be received and processed
4. const productRoutes = require('./api/routes/products');
5. const orderRoutes = require('./api/routes/orders');
6. const userRoutes = require('./api/routes/user');
7. const checkRoute = require('./api/routes/checkConnection');
8.
9. // 10.
11.// Anytime requests aimed at /products are made, handle it with the produc ts script
 12.app.use('/products', productRoutes); 13.// Same thing with orderRoutes 14.app.use('/orders', orderRoutes); 15.// Same thing with userRoutes 16.app.use('/user', userRoutes);
17.// This checkConnection GET request handler will verify that a connection is possible with the server before doing anything on the app
18.// This allows for user feedback and avoids crashes 19.app.use('/checkConnection', checkRoute);

Cody Uhi – Project 2
Code Snippet Showing URL Routing
3. Runs with little restrictions and can be deployed from a PC, Raspberry Pi (Rpi), or from the cloud. The service’s CRUD acceptance includes handling the HTTP methods of GET, POST, DELETE, and PUT
 4. // Handle CORS errors
5. app.use((req, res, next) => {
 6. 7. 8. 9. 10. 11. 12. 13. 14. 15. 16. 17. 18.
19. 20. 21. 22. 23.});
// Set the Access control allow origin property in the header // If you want to restrict access to be only from one source, // Just use res.header('A . . .', 'THE-URL-OF-THE-SOURCE'); res.header('Access-Control-Allow-Origin', '*');
// Define what headers can be allowed with the request
res.header(
'Access-Control-Allow-Headers',
'Origin, X-Requested-With, Content-Type, Accept, Authorization'
);
// Check if the incoming request's method is options if(req.method === 'OPTIONS'){
// Add an additional header telling the browser what it can send
res.header('Access-Control-Allow- Methods','PUT, POST,PATCH,DELETE,GET');
return res.status(200).json({}); }
// Allow other routes to take over if none of the above is triggered
next();
Code Snippet showing acceptance of PUT, POST, DELETE, and GET methods
4. Has functionality through an Android app acting as the client sending HTTP requests to the service. This also works when connecting a phone to a Rpi that is connected to the internet

Cody Uhi – Project 2
 Picture Showing Android App Client and Rpi Server For Full-Stack Functionality
5. Responds clearly and robustly to API calls from a web browser or Postman, giving a proper JSON response

Cody Uhi – Project 2
 Functionality:
Postman API calls and responses
In addition to the basic functionality that meets minimum requirements (Web Service implements RESTful practices, JSON data, 2 HTTP methods, works on mobile device hosted from Rpi, demonstrable from Postman), the Web Service has the following functionality:
• Authentication (Capability and Access Control hybrid)

Cody Uhi – Project 2
 Postman Demonstration of token generation

Cody Uhi – Project 2
 Code Used to Generate Session Token (Valid 1 hr.)
 
Cody Uhi – Project 2
Login form on Android App
• Access to a MongoDB Atlas hosted on AWS
 MongoDB Atlas Showing the test Cluster Contents
• Password Hashing and Salting for data at rest

Cody Uhi – Project 2
 MongoDB Cluster showing hashed/salted passwords in DB at rest
• More than 2 http methods (GET, POST, DELETE, and PUT all functional). See Purpose section part 3 for a code snippet demonstrating all the http methods possible.
• Allow for images to be uploaded to the server for a new product.
• • • • •
// Allows you to configure the way that files are stored when uploaded
const storage = multer.diskStorage({ destination: function(req, file, cb){
cb(null, './uploads/'); },
  • filename: function(req,file,cb) {
• cb(null, new Date().toISOString().replace(/:/g, "-
") + file.originalname); •}
• }); •
• // Custom filter for multer
• const fileFilter = (req, file, cb) => {
• // only accept files that are jpegs, pngs, or jpgs

Cody Uhi – Project 2
 •
if(file.mimetype === 'image/jpeg' || file.mimetype === 'image/png' || file.mimetype === 'image/jpg'){
•
•
•
•
• •}
• // if you don't set null throws an error
• // like this:
• // cb(new Error('message'),false); • };
// accept a file
    cb(null, true);
}else{
// reject a file
cb(null, false);
 • • • • • • • • • •
// Execute/initialize multer
// /uploads/ is the place where multer will try to store the files const upload = multer({
    storage: storage,
    limits: {
fileSize: 1024 * 1024 * 5 },
    fileFilter: fileFilter
});
Code Snippet showing how images are uploaded to the database
Technical Details of the Web Service:
A line-by-line technical analysis and justification is provided in the comments of the code for the application. This includes both the code for the web service/API and the code for the client/mobile application. See the project folders included in this ZIP file for the in-depth technical analysis.
Examples of JSON data:
 {
"count": 5,
"products": [ {
"name": "Harry Potter 8", "price": 12.99, "productImage": "",

Cody Uhi – Project 2
 "_id": "5dea0aad6bdc396e201815fc", "request": {
"type": "GET",
"url": "http://localhost:3000/products/5dea0aad6bdc396e201815fc" },
"userId": "5dea0a936bdc396e201815fb",
"email": "codyuhi2@live.com" },
{
"name": "wowpe",
"price": 666, "productImage": "",
 "_id": "5df07d7b89e84375a1760e3c", "request": {
"type": "GET",
"url": "http://localhost:3000/products/5df07d7b89e84375a1760e3c" },
"userId": "5df07d5189e84375a1760e3b",
"email": "k@k.k" },
{
"name": "sjso",
"price": 86,
"productImage": "",
"_id": "5df07d9a89e84375a1760e3d", "request": {
"type": "GET",
"url": "http://localhost:3000/products/5df07d9a89e84375a1760e3d" },
"userId": "5df07d5189e84375a1760e3b",
"email": "k@k.k" },
{
"name": "Harry Potter 8",
"price": 12.99,
"productImage": "uploads\\2019-12-12T23-39-36.122Zshayandcody.jpg",
 "_id": "5df2cfb8e02d3b1e48e93b6a", "request": {
"type": "GET",
"url": "http://localhost:3000/products/5df2cfb8e02d3b1e48e93b6a" },
"userId": "5dea09a736c9ab65bc5d59aa",
"email": "codyuhi@live.com" },
{

Cody Uhi – Project 2
 "name": "give me an A",
"price": 0,
"productImage": "",
"_id": "5df2dbc1d343b050a4c8ec54", "request": {
"type": "GET",
"url": "http://localhost:3000/products/5df2dbc1d343b050a4c8ec54" },
"userId": "5df07cc789e84375a1760e3a",
"email": "l@l.l" }
]
 }
Response for GET request for all products
Response for any failed authentication request
 {
"message": "Authentication Failed"
}
 {
"message": "Product Deleted", "request": {
"type": "POST",
"url": "http://localhost:3000/products/", "body": {
"name": "String",
"price": "Number" }
} }
Response for DELETE request to delete product
 {
"message": "Created product successfully", "createdProduct": {
"name": "Harry Potter 8",
"price": 12.99,
"_id": "5df2dd72d343b050a4c8ec55",

Cody Uhi – Project 2
 "request": { "type": "GET",
"url": "http://localhost:3000/products/5df2dd72d343b050a4c8ec55" },
"userId": "5dea09a736c9ab65bc5d59aa",
"email": "codyuhi@live.com" }
}
Response for POST Request to create new Product
PUT request JSON data
 [{
"propName": "name",
 "value": "test" }]
 {
"message": "Authorization Successful",
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6eyJyb2xlIjoidXNlci
IsIl9pZCI6IjVkZWEwOWE3MzZjOWFiNjViYzVkNTlhYSIsImVtYWlsIjoiY29keXVoaUBsaXZlLmNvbSI sInBhc3N3b3JkIjoiJDJiJDEwJE5acE5GL3BpL2k5UzlXU2NaVUdySS5JRVF4YTQzMGxOV055VXhHeUlB WWIySi5qUUtFT1RLIiwiX192IjowfSwidXNlcklkIjoiNWRlYTA5YTczNmM5YWI2NWJjNWQ1OWFhIiwic m9sZSI6InVzZXIiLCJpYXQiOjE1NzYxOTM5MjAsImV4cCI6MTU3NjE5NzUyMH0.ieMtJVmxiVZRIoWUKQ _TVVrJ-8vCUEqz0dGI8PZBHhk"
}
JSON response for POST request to login
How the App Uses JSON Data:
The application client sends data in JSON format to the server, which parses and handles the JSON data and returns JSON data which is parsed and handled by the client. This data is used to pass given user strings which are present through the use of EditText views on the Android application. Below are examples of these all these views in the app, where the user input is given, placed into a JSON object as an attribute, then sent to the server as part of the request.

Cody Uhi – Project 2
 Login Screen
For the login page, this JSON data contains the username and password. These attributes are POSTed to the API which hashes the password and compares it to the password hash that is stored in the MongoDB. If it matches, an authentication token is returned with a session time of 1 hour and the user is allowed access to limited resources on the server which can be GETted, DELETEd, or PUTted.
 
Cody Uhi – Project 2
Account Creation Screen
The account creation screen is much like the login screen except that there are multiple POST requests executed with JSON data being passed. JSON data is passed in a POST request to create a new user from the given email address and password. There are several things in place to verify that a valid email address and password are given, which are consistent with good security practice. For example, email addresses must be between 4 and 64 characters in order to be accepted and the POST request initiated. Likewise, passwords must be between 8 and 128 characters. Furthermore, once the length is valid, the API only accepts email addresses that are in the correct email address format. After the initial POST request is performed, a response is received from the server at the client. If the server returned a successful status for creating the account, the same functionality is performed to log the user into the newly created account through another POST request.
Main Screen
The main screen for the application is the dashboard which shows all products that are on the marketplace. This list of products is retrieved by a simple GET request whose path denotes
 
Cody Uhi – Project 2
that all products should be obtained from the database. This GET request has no body, since the request is passed in the query string. The API receives this request and returns a JSON array of JSON objects which are products with all their attributes. The client receives this array and iterates through the array to grab JSON data for every product object. When the product objects have been retrieved, the client creates individual cells in a RecyclerView for every product in the array. These cells display the product name and the price. Clicking on any one of these cells will start a new intent to take the user into a different view that GETs information for a specific product. On this page, there is also a + button that, when clicked, starts a new intent for a page where new products can be POSTed to the database.
View Product Screen
The page used to view individual products initiates another GET request, passing the request information in the query string, to the API for a certain product’s id. This product id was passed in the intent from the page where all products are visible. The server returns a JSON object with attributes of product name, price, product id, and the user’s email who created the
 
Cody Uhi – Project 2
product in the marketplace. This JSON object is parsed and its data used to populate TextViews which display the product’s data. On this activity, there are two buttons: an edit product button and a delete product button. Pressing the edit product button creates a new intent that will allow the user to PUT changes to this product in the database. Pressing the delete button performs an API call to DELETE this product from the database. The DELETE request does not have a body either but passes its data in the query string. However, this request contains an authorization header which holds a bearer of the authentication token that the user received when he/she logged in. When the DELETE request arrives at the server, the server checks whether the user who is trying to delete the object was the creator of the object. If they were the creator of the object, the DELETE request is performed, and a successful response status is returned. If the current user is not the creator of the product, an error code is returned denoting that the current user does not have permissions to delete this product. There also exists a back button that will allow the user to return to the screen to view all products concurrently.
 
Cody Uhi – Project 2
Edit Product Screen
Pressing the edit button opens an entirely new intent with EditText views asking for the desired product name and price. The user enters the desired information and press a button which initiates the edit functionality. Pressing the button grabs the string data that was given by the user and places that data in a JSON object to be PUTted to the API. A PUT request is performed and passes the desired product name to the server. Part of this PUT request is an authorization header, which (like the delete button in the previous page) verifies that the user who is trying to change the product in the database is the same user who created the product. This ensures that only the product creator can change or delete their products. If this authentication was successful, the API returns a successful status. If the client receives a successful response, it executes another PUT request to the API with the same parameters as the previous PUT request but passes the product’s price property to be updated. If the client received an insufficient permissions error from the first PUT request, the user is given feedback that they don’t have permission and returned to a previous screen.
Product Creation Screen
 
Cody Uhi – Project 2
Pressing the plus button from the main screen allows the user to create a new product. Much like the edit screen, the create product screen has EditText views which take user input and when the submit button is pushed, sends a POST request to the server to add the product. The user must be logged in to perform this function, since every product must have a user associated with it as its creator.
Services the Code Uses: JS Express:
Express is a light-weight web app framework that is used to organize a web application into a Model View Controller architecture on the server side of things. In a RESTful API, the View part of the MVC model is not followed since the API is stateless, but in my application I used models for defining data structures that would be used by the application and I used controllers to define services that the API would be able to offer when given specific parameters and variables. The MVC architecture can be seen in my file structure in the picture below:

Cody Uhi – Project 2
 Picture of file system demonstrating Express-enabled MVC
Express allowed for effective routing of requests based on the provided parameters. Here is a code snippet demonstrating how express was used to route requests for products:

Cody Uhi – Project 2
 // This is a method that registers different routes based on the // provided resource (GET or POST)
const router = express.Router();
// Import the product mongoose model schema so we can store // The product in the mongodb database
const Product = require('../models/product');
// This is a method that will handle GET requests
// This is just a slash because they will only get to this script // if the user already is at some http:// ... /products/ route router.get('/', ProductsController.get_all_products);
 // upload is an image file. single will parse only one file. productImage is the field that will hold the file
router.post('/', checkAuth, upload.single('productImage'), ProductsController.pro ducts_create_product);
// This responds with a message and an id if an id was passed in the query string
router.get('/:productId', ProductsController.products_get_product);
// Verify that the correct user is logged in before the database entry is PUTted
router.put('/:productId', checkAuth, checkCorrectProduct, ProductsController.prod ucts_put_product);
// Verify that the correct user is logged in before the database entry is DELETEd
router.delete('/:productId', checkAuth, checkCorrectProduct, ProductsController.p roducts_delete_product);
// Export the module for use in other files
module.exports = router;
Code Snippet showing express usage for product request routing
See code that was included with this file for line-by-line walkthrough of how Express was implemented in the project.
MongoDB Atlas:
The project used a MongoDB cluster to provide database functionality. The database was used to keep track of all products that the application had access to and which users existed.

Cody Uhi – Project 2
This was important because it allowed for authentication and functionality of the app. Without the database, none of the data would have been able to be stored.
To use MongoDB, I created an account with them and spun up a cluster using their free tier of products. To use MongoDB in the project, I establish a connection with MongoDB as one of the first things that the server does when it starts up. This connection is established by using mongoose, an npm module which has built-in methods that allow for easy access/manipulation of the MongoDB resources that are mine. I use the code below to access the MongoDB cluster:
 // Import mongoose for using the MongoDB API
const mongoose = require('mongoose');
// The environment variable is not included in the git files. You have to manual ly create that file yourself and configure it to hold the mongo password
// This is for security. I don't want to give my mongo password to anyone via git hub
try{
mongoose.connect('mongodb+srv://Uhi:' + process.env.MONGO_ATLAS_PW_ENCODED + '@re stful-api-pavba.mongodb.net/test?retryWrites=true&w=majority',
{
} );
}catch(err){
// Provide user feedback for if the mongo connection failed
// The below line is for a deprecated version of mongoose. // The lines below it replace its functionality
// useMongoClient: true,
useNewUrlParser: true,
useUnifiedTopology: true, useFindAndModify: false, useCreateIndex: true
 console.log(err); }
Code Snippet showing connection to MongoDB
MongoDB provided a URL that I use to connect to my cluster. As part of this, I pass my MongoDB user password as part of the url for authentication. In here, I created an

Cody Uhi – Project 2
environmental variable and added it to .gitignore to make sure that I didn’t accidentally push my private password to the internet.
After having the connection established, I was able to use mongoose to define a schema and model that would be used to access database resources in a formatted way through JSON and other basic data types. See below for an example of how mongoose was used to create the user schema and open a connection to the MongoDB resources in the user model file:
 // import mongoose for database functionality
// We will use the mongoose.Schema method to create a schema that can be stored i n mongodb
const mongoose = require('mongoose');
// create the new schema
const userSchema = mongoose.Schema({
_id: mongoose.Schema.Types.ObjectId,
// unique: true makes it necessary for the email to be unique in the database // It also makes searching the array more efficient?
// Create email attribute which has to be a string and match a certain regula
r expression
    email: {
        type: String,
required: true,
unique: true,
match: /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-
]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01- \x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0- 9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0- 9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0- 9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e- \x7f])+)\])/
},
 // every user needs a password
password: { type: String, required: true },
role: { type: String, default: "user" } });
// Export the module for use in other files
module.exports = mongoose.model('User', userSchema);
Code Snippet showing mongoose usage to define schema

Cody Uhi – Project 2
 Picture of MongoDB Atlas functionality from Compass
JSON Web Tokens:
JSON Web Tokens (JWTs) were used here to create tokens that could be used for authentication. These tokens were hashed and the key to the hash was stored as an environmental variable. These tokens were vital to the maintenance of sessions, as they were able to take user identification data and hash it, and define a valid amount of time. This is a two- way hash, given the correct key was used to reverse the hash, which allowed for the current user’s data to also be used in the code. Below is how JWT was used to create a session when logging in:
 // Create a token
// jwt.sign()
const token = jwt.sign({ email: users[0],
userId: users[0]._id,
role: users[0].role },
// This sets the JWT_KEY to expire in 1 hour
// After that time, they will have to log back in
process.env.JWT_KEY,

Cody Uhi – Project 2
 {
expiresIn: "1h" });
// result is true if it succeeds, false if it fails
return res.status(200).json({
message: "Authorization Successful", token: token
});
Bcrypt:
Code Snippet showing how to create a JWT
Bcrypt was the hashing method that was used to keep user data safe at rest. Users would provide a password in plaintext, which would be passed to the server to hash the password for storage. Any subsequent authentication attempts would compare the hashed password with a string which was provided by the user and hashed for the comparison. Here is how Bcrypt was used during the account creation process:
 // Hash the given password
// first argument is the password to hash, the second argument
n the error
// is the number of salting rounds
bcrypt.hash(req.body.password, 10, (err, hash) => { if (err) {
return res.status(500).json({
// If there was an error creating the password, retur
            error: err
        });
} else {
// Else create a user and return the hashed password
 // Create a new user
const user = new User({
_id: new mongoose.Types.ObjectId(), email: req.body.email,
// Don't store password in plaintext password: hash
});
// Save the user in the database user.save()
.then(result => {

Cody Uhi – Project 2
 // log the result and return 201 status
console.log(result); res.status(201).json({
message: "User created" });
})
.catch(err => {
// log the error and return 500 status
console.log(err); res.status(500).json({
message: "Unable to create user.", error: err
 } });
}); });
Multer:
Code snippet showing bcrypt usage to hash password
Although it didn’t make it into the final client product, the API has the functionality to accept image files to be uploaded with the product creation request through Multer. Multer allows for an image to be converted from binary data into image format (jpg, png, etc). This is necessary because the image itself cannot be included in the post request, as it needs to be sent via a datastream. Below is the implementation of Multer:
 // Allows you to configure the way that files are stored when uploaded
const storage = multer.diskStorage({ destination: function(req, file, cb){
cb(null, './uploads/'); },
 filename: function(req,file,cb) {
cb(null, new Date().toISOString().replace(/:/g, "-
") + file.originalname); }
});
// Custom filter for multer
const fileFilter = (req, file, cb) => {
// only accept files that are jpegs, pngs, or jpgs

Cody Uhi – Project 2
 if(file.mimetype === 'image/jpeg' || file.mimetype === 'image/png' || file.mi metype === 'image/jpg'){
        // accept a file
        cb(null, true);
    }else{
        // reject a file
        cb(null, false);
    }
// if you don't set null throws an error // like this:
// cb(new Error('message'),false);
};
 // Execute/initialize multer
// /uploads/ is the place where multer will try to store the files const upload = multer({
    storage: storage,
    limits: {
        fileSize: 1024 * 1024 * 5
    },
fileFilter: fileFilter });
Code Snippet showing how Multer converts data stream into image file
Morgan:
Morgan is a logging tool that made it easier to log things as they happened. This was easily implemented with a few lines of code. Specifically, it was used here:
Morgan usage
Conclusion:
This project was valuable to me because I gained experience working with NodeJS and Java again, but more importantly because this is a full-stack product that I created from scratch. Much like the growth I experienced in IT210, I feel like this was a validating undertaking for me because I made something great. I learned a lot about networking by troubleshooting getting the
 // Log the activity
app.use(morgan('dev'));

Cody Uhi – Project 2
network connection to work between my Android device and the Raspberry Pi. I worked with SSL certificates, learned about the Node development environment, and did a ton of Googling. I have always wanted to learn more about MongoDB, and I had the chance to do that here. If I were to iterate on this project, I would definitely revamp the UI and increase the functionality of the API and the mobile application by hosting images and allowing them to be uploaded, implementing the capability to create orders or add things to a cart, implement oAuth or payment systems, and more. This was a great culmination of everything I’ve learned in the IT major so far and I was happy to make something great.
DISCLAIMER: The code will not work without connection to the MongoDB, and the MongoDB connection will not work unless authentication is complete. This means that the environmental variable must be set for the MongoDB password, and the environmental variable must also be set for the JWT web token for session validation. The NodeJS folder included with this write up also did not include the packages, so the packages must be installed before the server will run (“npm i"). This means that the code may not run immediately upon opening, but the code works given the correct configuration, which I demonstrated in the pass off.
References:
• https://stackoverflow.com/questions/12616153/what-is-express-js
• https://linoxide.com/linux-how-to/install-install-nodejs-linux/
• https://stackoverflow.com/questions/34323659/cant-connect-to-mongodb-when-service-
is-running-raspbian
• https://www.widriksson.com/install-mongodb-raspberrypi/
      
Cody Uhi – Project 2
• https://www.tecmint.com/find-linux-server-public-ip-address/
• https://www.w3schools.com/js/js_errors.asp
• https://stackoverflow.com/questions/15809611/bcrypt-invalid-elf-header-when-running-
node-app
• https://stackoverflow.com/questions/17914744/invalid-elf-header-with-node-bcrypt-on-
awsbox?noredirect=1
• https://stackoverflow.com/questions/37774994/nodejs-rest-api-delete-function
• https://stackoverflow.com/questions/52256720/how-to-perfom-put-and-delete-operation-
in-restful-api-using-express-nodejs-and-m
• https://stackoverflow.com/questions/51869312/delete-request-on-node-js-rest-api-works-
fine-from-postman-not-from-app
• https://jwt.io/
• https://www.npmjs.com/package/jwt-decode
• https://stackoverflow.com/questions/41228499/mongodb-node-js-deleteone-via-id-
doesnt-work-on-objectid
• https://stackoverflow.com/questions/28343745/how-do-i-print-a-sign-using-string-
formatting
• https://www.daniweb.com/programming/software-
development/threads/463071/percentages-and-dollar-signs-in-output
• https://www.reddit.com/r/learnpython/comments/45c9kw/adding_or_in_python_without_
space_in_between_the/
• https://fsymbols.com/computer/copyright/
                      
Cody Uhi – Project 2
• https://stackoverflow.com/questions/11127578/java-lang-classcastexception-libcore-net- http-httpurlconnectionimpl-cannot-be-c
• https://stackoverflow.com/questions/50840101/curl-35-error1408f10bssl-routinesssl3- get-recordwrong-version-number
• https://stackoverflow.com/questions/5998694/how-to-create-an-https-server-in-node-js
• https://medium.com/@son.rommer/fix-cleartext-traffic-error-in-android-9-pie-
2f4e9e2235e6
• https://stackoverflow.com/questions/56266801/java-net-socketexception-socket-failed-
eperm-operation-not-permitted
• https://developer.android.com/training/articles/security-ssl
• https://flaviocopes.com/express-https-self-signed-certificate/
• https://stackoverflow.com/questions/35795589/javax-net-ssl-sslhandshakeexception-
handshake-failed-on-android-5-0-0-when-sslv
• https://stackoverflow.com/questions/28441791/sslhandshakeexception-handshake-failed
• https://stackoverflow.com/questions/39133437/sslhandshakeexception-handshake-failed-
on-android-n-7-0
• https://coderwall.com/p/wv6fpq/add-self-signed-ssl-certificate-to-android-for-browsing
• https://stackoverflow.com/questions/2012497/accepting-https-connections-with-self-
signed-certificates
• https://aboutssl.org/how-to-create-and-import-self-signed-certificate-to-android-device/
• https://medium.com/@johnmarktagle/using-self-signed-ssl-in-android-nougat-
6d59593a3b6f
• https://medium.com/@noumaan/ssl-app-dev-a2923d5113c6
                       
Cody Uhi – Project 2
• https://stackoverflow.com/questions/6825226/trust-anchor-not-found-for-android-ssl- connection
• https://developer.android.com/training/articles/security-ssl.html#CommonProblems
• https://developer.android.com/reference/javax/net/ssl/TrustManager.html
• https://stackoverflow.com/questions/25122287/java-security-cert-
certpathvalidatorexception-trust-anchor-for-certification-pa
• https://stackoverflow.com/questions/52294588/whats-the-correct-way-of-implementing-
https-connection-with-self-signed-certifi
• https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-
permitted
• https://developer.android.com/training/articles/security-ssl.html#SelfSigned
• https://www.youtube.com/playlist?list=PL55RiY5tL51q4D-B63KBnygU6opNPFk_q
• https://exceptionshub.com/http-content-type-header-and-json.html
• https://stackoverflow.com/questions/13593069/androidhide-keyboard-after-button-click
• https://www.geeksforgeeks.org/compare-two-strings-in-java/
• https://www.javatpoint.com/string-comparison-in-java
• https://docs.atlas.mongodb.com/reference/api/whitelist-delete-one/
• https://docs.mongodb.com/manual/reference/method/db.collection.deleteOne/
• https://docs.mongodb.com/stitch/mongodb/delete-documents-from-mongodb/
• https://stackoverflow.com/questions/11939362/android-get-and-post-request
• https://stackoverflow.com/questions/15267385/get-json-response-for-post
• https://stackoverflow.com/questions/38046429/get-json-response-while-performing-post-
request-with-httpurlconnection
                       
Cody Uhi – Project 2
• https://stackoverflow.com/questions/20252727/is-not-an-enclosing-class-java
• https://stackoverflow.com/questions/3625837/android-what-is-wrong-with-
openfileoutput
• https://stackoverflow.com/questions/44587187/android-how-to-write-a-file-to-internal-
storage
• https://abhiandroid.com/database/internal-storage
• https://stackoverflow.com/questions/43455481/show-android-internal-storage-file-in-
android-studio
• https://stackoverflow.com/questions/41027340/curl-connection-refused
• https://www.codebrainer.com/blog/how-to-display-data-with-recyclerview
• https://www.leveluplunch.com/java/examples/convert-json-array-to-arraylist-gson/
• https://coderanch.com/t/697439/java/convert-JSON-Array-arraylist
• https://stackoverflow.com/questions/26890190/images-in-recyclerview
• https://stackoverflow.com/questions/15722787/android-view-inflateexception-binary-
xml-file-line-7-error-inflating-class-fr
• https://stackoverflow.com/questions/37781579/android-view-inflateexception-binary-
xml-file-line-7-error-inflating-class-an
• https://stackoverflow.com/questions/32244851/androidjava-lang-outofmemoryerror-
failed-to-allocate-a-23970828-byte-allocatio
• https://stackoverflow.com/questions/26722001/android-when-do-we-use-getintent
• https://stackoverflow.com/questions/2559527/non-static-variable-cannot-be-referenced-
from-a-static-context
• https://developer.android.com/guide/topics/ui/floating-action-button
                       
Cody Uhi – Project 2
• https://stackoverflow.com/questions/43156077/how-to-check-a-string-is-float-or-int
• https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-
post-request-using-java
• http://zetcode.com/java/getpostrequest/
• https://stackoverflow.com/questions/47883665/how-to-post-data-in-key-value-pair
• https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-
method-easily
• https://stackoverflow.com/questions/5195837/format-float-to-n-decimal-places
• https://stackoverflow.com/questions/38158953/how-to-create-button-in-action-bar-in-
android
• https://stackoverflow.com/questions/1051004/how-to-send-put-delete-http-request-in-
httpurlconnection
            
