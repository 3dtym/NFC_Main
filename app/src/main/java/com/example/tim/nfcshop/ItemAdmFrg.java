package com.example.tim.nfcshop;

import android.content.DialogInterface;
import android.graphics.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemAdmFrg extends Fragment {

    private User user;
    private List<Product> products;
    private Double price;
    private TextView creditView;
    ItemAdmAdapter adapter;
    private int edit_position;
    private Paint p = new Paint();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_frag, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        final DBHelper db = new DBHelper(getContext());

        products = db.getAllProducts();

        FloatingActionButton fab = view.findViewById(R.id.fb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog(db,0,0);
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdmAdapter(products);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initSwipe();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position);
                } else {
                    removeView();
                    edit_position = position;

//                  Treba dorobit dialog na Set Item.

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void removeView() {
        View view = getView();
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void initDialog(final DBHelper db, final int id, final int mod) {

        LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.item_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Item edit");

        builder.setView(inflator);
        final EditText input = inflator.findViewById(R.id.name);
        final EditText input2 = inflator.findViewById(R.id.price);
        final Spinner spinner = inflator.findViewById(R.id.spinner);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mod) {
                    case 0: {
                        if (input.length() != 0 && input2.length() != 0) {
                            String name = input.getText().toString();
                            double credit = Double.valueOf(input2.getText().toString());
                            int picture = Integer.valueOf(spinner.getSelectedItem().toString());
                            Product pr = new Product(name, credit, picture);
                            db.createProductDB(pr);
                            adapter.addItem(pr);
                        }else
                            Toast.makeText(getContext(), "Obe polia musia byt vyplnene", Toast.LENGTH_SHORT).show();
                    }
                    case 1:{
                        if (input2.length() != 0) {
                            double credit = Double.valueOf(input2.getText().toString());
//                            db.updateCreditUser(id, credit);
                        } else if (input.length() != 0) {
                            String name = input.getText().toString();
//                            db.updateNameUser(id, name);
                        } else if (input.length() != 0 && input2.length() != 0) {
                            String name = input.getText().toString();
                            double credit = Double.valueOf(input2.getText().toString());
//                            db.updateUser(id, name, credit);
                        } else
                            Toast.makeText(getContext(), "Obe polia musia byt vyplnene", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }
}
