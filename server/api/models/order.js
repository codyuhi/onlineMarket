// import mongoose for mongodb support
// This is used to create a mongo schema
const mongoose = require('mongoose');

// Create the schema
const orderSchema = mongoose.Schema({
    // Define the schema attributes
    _id: mongoose.Schema.Types.ObjectId,
    product: { type: mongoose.Schema.Types.ObjectId, ref: 'Product', required: true },
    quantity: { type: Number, default: 1 },
    userId: { type: String, required: true }
});

// export the module for use in other files
module.exports = mongoose.model('Order', orderSchema);