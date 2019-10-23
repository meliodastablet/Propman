package com.example.senior1;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class
MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Property> data;

    public MyRecyclerViewAdapter(Context context, List<Property> data) {
        this.context = context;
        this.data =(ArrayList<Property>) data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //BIND DATA
        final Property property = data.get(position);
        holder.tvName.setText(property.getTitle() + "\nPrice: " + property.getPrice());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = property.getId()+property.getTitle()+"\n"+property.getPrice()+property.getOdasalon()+property.getM2()+property.getDescription() + property.getAddress() + property.getImage();
                ParcelableProperty parcelableProperty = new ParcelableProperty(property);
                Intent mIntent = new Intent(context.getApplicationContext(), ViewProperty.class);
                mIntent.putExtra("property", parcelableProperty);
                context.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageButton btnDetail;
        TextView tvName;


        MyViewHolder(View viewItem){
            super(viewItem);
            tvName = viewItem.findViewById(R.id.tvName);
            btnDetail = viewItem.findViewById(R.id.btnDetail);
        }
    }



}
