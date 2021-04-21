//This file is used to enable the recycler view to have the look of the
//layout cells defined in product_list_item.xml layout
//much of this code was taken from Project 1, and so it has a lot of variable names that are based on pokemon
//Define the package
package com.example.onlinemarket;

//import the context module so the context provided can be referred to
import android.content.Context;
//import the intent module to be able to access parent intent and navigate to other pages
import android.content.Intent;
//import the log module to be able to log errors
import android.util.Log;
//impor the layoutinflater module to be able to inflate the list item layouts in the recyclerview
import android.view.LayoutInflater;
//import the view module to be able to access view methods
import android.view.View;
//import the viewgroup module to be able to work with multiple views at once
import android.view.ViewGroup;
//import the textview module to be able to work with textviews
import android.widget.TextView;

import androidx.annotation.NonNull;
//import the recyclerview module to be able to work with recyclerviews
import androidx.recyclerview.widget.RecyclerView;

//import the arraylist module to be able to work with arraylists of product data
import java.util.ArrayList;

//initiate the class definition as an extension of the recyclerview adapter class
public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.FullListViewHolder>{

    // create a arraylist for all the fulllist products
    ArrayList<Product> prods;
//    constructor for class instantiation
    public AllProductAdapter(ArrayList<Product> prods){
        this.prods = prods;
    }

//    override the oncreateviewholder method to be able to inflate the cell layouts
    @Override
    public FullListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
// initialize the context
        Context context = parent.getContext();
// create the itemview
//        try/catch to be able to handle issues with inflating the layout and resources
        try{
//            inflate the layout
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.product_list_item, parent, false);
//            return the newly inflated version of the item layout
            return new FullListViewHolder(itemView);
        } catch (Exception e) {
//            log the error message if this operation fails
            Log.d("Error: ", e.getMessage());
        }
//        Define this again outside the try/catch block, but it should essentially never get here
//        This is because of the return necessity
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.product_list_item, parent, false);
        return new FullListViewHolder(itemView);
    }

//    Method to bind the viewholder
    @Override
    public void onBindViewHolder(@NonNull FullListViewHolder holder, int position) {
// bind the viewholder for the product
        Product fullListPoke = prods.get(position);
        holder.bind(fullListPoke);
    }

//    Method to find the number of items in the arraylist
    @Override
    public int getItemCount() {
        try{
//            return the number of items in the arraylist
            return prods.size();
        } catch (Exception e) {
//            log any errors
            Log.d("Error: ", e.getMessage());
        }
//        essentially, this should never be reached.
        return prods.size();
    }

//    this class populates the recyclerview cells with the correct data
    public class FullListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        Each cell will show the product name and product price
        TextView fullListName;
        TextView fullListId;

        public FullListViewHolder(View itemView) {
            super(itemView);
// the fulllist name and price are found and set for the item
            fullListName = (TextView) itemView.findViewById(R.id.fullListName);
            fullListId = (TextView) itemView.findViewById(R.id.price);
//            allow something to happen if a cell is clicked
            itemView.setOnClickListener(this);
        }

//        this method is used to bind the product data with the textviews that are onscreen
        public void bind (Product fullListPoke){
            try{
//                set the name and price
                fullListName.setText(Character.toUpperCase(fullListPoke.name.charAt(0))
                        + fullListPoke.name.substring(1));
                fullListId.setText("Price: $" + Float.toString(fullListPoke.price));
            } catch(Exception e){
//                log any errors
                Log.d("Error: ", e.getMessage());
            }

        }

//        This method is called if a recyclerview cell is clicked
        @Override
        public void onClick(View view) {
// when clicked, start a new intent for the pokemon name who occupied the cell that was clicked
            int position = getAdapterPosition();
            Product flpoke = prods.get(position);
//            create intent to navigate to the product's single product view if clicked
            Intent intent = new Intent(view.getContext(), ViewSingleProduct.class);
//            pass the product id to the viewsingleproduct page
            intent.putExtra("com.example.onlinemarket.MESSAGE", flpoke._id);
//            pass the auth information so changes can be made to the existing product
            intent.putExtra("auth", ViewAllProducts.auth);
//            navigate to other page
            view.getContext().startActivity(intent);
        }
    }
}
