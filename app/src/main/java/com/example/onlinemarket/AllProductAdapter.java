package com.example.onlinemarket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.FullListViewHolder>{

    // create a arraylist for all the fulllist pokes
    ArrayList<Product> prods;
    public AllProductAdapter(ArrayList<Product> prods){
        this.prods = prods;
    }

    @Override
    public FullListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
// initialize the context
        Context context = parent.getContext();
// create the itemview
        try{
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.product_list_item, parent, false);
            return new FullListViewHolder(itemView);
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.product_list_item, parent, false);
        return new FullListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FullListViewHolder holder, int position) {
// bind the viewholder for the fulllistpoke
        Product fullListPoke = prods.get(position);
        holder.bind(fullListPoke);
    }

    @Override
    public int getItemCount() {
        try{
            return prods.size();
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        return prods.size();
    }

    public class FullListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView fullListName;
        TextView fullListId;

        public FullListViewHolder(View itemView) {
            super(itemView);
// the fulllist name and id are found and set for the item
            fullListName = (TextView) itemView.findViewById(R.id.fullListName);
            fullListId = (TextView) itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }
        public void bind (Product fullListPoke){
            try{
                fullListName.setText(Character.toUpperCase(fullListPoke.name.charAt(0))
                        + fullListPoke.name.substring(1));
                fullListId.setText("Price: $" + Float.toString(fullListPoke.price));
            } catch(Exception e){
                Log.d("Error: ", e.getMessage());
            }

        }

        @Override
        public void onClick(View view) {
// when clicked, start a new intent for the pokemon name who occupied the cell that was clicked
            int position = getAdapterPosition();
            Product flpoke = prods.get(position);
            Intent intent = new Intent(view.getContext(), ViewSingleProduct.class);
            intent.putExtra("com.example.onlinemarket.MESSAGE", flpoke._id);
            intent.putExtra("auth", ViewAllProducts.auth);
            view.getContext().startActivity(intent);
        }
    }
}
