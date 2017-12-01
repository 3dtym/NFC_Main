package com.example.tim.nfcshop;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Product> data;
    private Listener listener;
    private int selectedPosition = 0;

    public CustomAdapter(List<Product> data, Listener listener) {
        this.data = data;
        this.listener = listener;
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
        holder.foodPrice.setText( data.get(position).cena + "€");
        switch(data.get(position).picture) {
            case 1:
                holder.foodImage.setImageResource(R.drawable.water);
            case 2:
                holder.foodImage.setImageResource(R.drawable.snack);
            default:
                holder.foodImage.setImageResource(R.drawable.food);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface Listener{
        void onSelected(Product product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foodName;
        private TextView foodPrice;
        private ImageView foodImage;

        public ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(selectSender);
        }

        private View.OnClickListener selectSender = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = getLayoutPosition();
                Product itemData = data.get(selectedPosition);
                listener.onSelected(itemData);
            }
        };



        /*
        private View.OnClickListener deleteSender = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
        */
    }
}
