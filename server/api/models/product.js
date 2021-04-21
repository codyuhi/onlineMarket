// import mongoose for mongodb support
// This is used to create a mongodb schema
const mongoose = require('mongoose');

// Create a product Schema that is the kind of data that mongo works with
// The schema is the layout of the objects that you want to create
const productSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    // Define the attribute data type and whether it's required
    name: { type: String, required: true },
    price: { type: Number, required: true },
    productImage: { type: String },
    userId: { type: String, required: true },
    email: {type: String, required: true }
});

// Product is the way that you refer to this model
// productSchema defines the schema layout that we created above
// to be the schema for any mongoose model referred to by the Product reference
module.exports = mongoose.model('Product', productSchema);