// This controller is just used to verify that a good connection is established from the client to this server
exports.validate = (req, res, next) => {
    try {
        // if it's a good connection, return 200 status
        return res.status(200).json({
            message: 'Good connection'
        });
    } catch {
        // log user feedback showing that the connection cannot be verified
        // retroactively, this probably would not be reached
        console.log("Unable to verify connection");
    }
}