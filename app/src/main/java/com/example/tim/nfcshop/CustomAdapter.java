package com.example.tim.nfcshop;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Product> data;
    private Listener listener;
    private int selectedPosition = 0;
    private boolean asc = false;
    private static final String TAG = "TAG";

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

    public interface Listener{
        void onSelected(Product product);
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
            buyImage.setOnClickListener(selectSender);
            foodPrice.setOnClickListener(sortByPrice);
            foodImage.setOnClickListener(sortByType);

        }

        private View.OnClickListener selectSender = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = getLayoutPosition();
                Product itemData = data.get(selectedPosition);
                listener.onSelected(itemData);

            }
        };

        private View.OnClickListener sortByPrice = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asc = !asc;
                Collections.sort(data, new Comparator<Product>(){
                    public int compare(Product obj1, Product obj2) {
                        if (asc) {
                            return Double.compare(obj1.getCena(), obj2.getCena());
                        }
                        return Double.compare(obj2.getCena(), obj1.getCena());
                    }
                });
                notifyDataSetChanged();
            }
        };

        private View.OnClickListener sortByName = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asc = !asc;
                Collections.sort(data, new Comparator<Product>(){
                    public int compare(Product obj1, Product obj2) {
                        if (asc) {
                            return obj1.getNazov().compareToIgnoreCase(obj2.getNazov());
                        }
                        return obj2.getNazov().compareToIgnoreCase(obj1.getNazov());
                    }
                });
                notifyDataSetChanged();
            }
        };

        private View.OnClickListener sortByType = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asc = !asc;
                Collections.sort(data, new Comparator<Product>(){
                    public int compare(Product obj1, Product obj2) {
                        if (asc) {
                            return Integer.compare(obj1.getPicture(), obj2.getPicture());
                        }
                        return Integer.compare(obj2.getPicture(), obj1.getPicture());
                    }
                });
                notifyDataSetChanged();
            }
        };


    }
}
