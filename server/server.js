// This file initiates the server functionality for the API
// This provides some functionality for spinning up a server
// import http module for http connections
const http = require('http');
// import https module for https connections
const https = require('https');
// import the fs module for accessing the file system for the ssl connection
const fs = require('fs');

// Specify the port number that's used over which the http traffic will run
const port = process.env.PORT || 3000;
// import the app module
const app = require('./app');

// create the server with the listener through http
const server = http.createServer(app);

// create the server with the listener through https
https.createServer({
    // import the keys and certs for the SSL connection
    key: fs.readFileSync('./certs/server.key'),
    cert: fs.readFileSync('./certs/server.cert')
    // start the https capability on port 3001
}, app).listen(3001, () => {
    // provide feedback verifying that https works
    console.log('Listening for https on port 3001 . . . ')
});

// start the server and pass the port number
server.listen(port);
// provide feedback verifying that http works
console.log('Listening for http on port 3000 . . . ');