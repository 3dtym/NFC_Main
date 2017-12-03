package com.example.tim.nfcshop;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ItemAdmAdapter extends RecyclerView.Adapter<ItemAdmAdapter.ViewHolder> {
    private List<Product> data;
    public ItemAdmAdapter(List<Product> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.foodName.setText(data.get(position).nazov);
        holder.foodPrice.setText(data.get(position).cena + "€");
        switch(data.get(position).picture) {
            case 1:
                holder.foodImage.setImageResource(R.drawable.water);
                break;
            case 2:
                holder.foodImage.setImageResource(R.drawable.snack);
                break;
            default:
                holder.foodImage.setImageResource(R.drawable.food);
                break;
        }
        holder.buyImage.setImageResource(R.drawable.shoppingcart);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Product item) {
        data.add(item);
        notifyItemInserted(data.size());
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foodName;
        private TextView foodPrice;
        private ImageView foodImage;
        private ImageView buyImage;

        public ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.imageView);
            buyImage = itemView.findViewById(R.id.buyIcon);
        }
    }
}
