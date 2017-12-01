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
    private List<String> data;//todo: change list from string to foodItem
    private Listener listener;
    private int selectedPosition = 0;

    public CustomAdapter(List<String> data, Listener listener) {
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
        holder.foodName.setText(data.get(position));
        holder.foodPrice.setText( Integer.toString(position) + "€");
        holder.buyLabel.setText("Buy!");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface Listener{
        void onSelected(String string);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView foodName;
        private TextView foodPrice;
        private TextView buyLabel;
        private ImageView foodImage;

        public ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            buyLabel = itemView.findViewById(R.id.buyLabel);
            foodImage = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(selectSender);
        }

        private View.OnClickListener selectSender = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = getLayoutPosition();
                String itemData = data.get(selectedPosition);
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
