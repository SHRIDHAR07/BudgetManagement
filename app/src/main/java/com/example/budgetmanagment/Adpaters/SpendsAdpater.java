package com.example.budgetmanagment.Adpaters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetmanagment.Activity.CreateSpends;
import com.example.budgetmanagment.Model.Spends;
import com.example.budgetmanagment.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendsAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public static List<Spends> spendsList;

    public SpendsAdpater(Context context, List<Spends> spendsList) {
        mContext = context;

        this.spendsList = spendsList;


        if (this.spendsList == null) {
            this.spendsList = new ArrayList<>();

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
        return new SpendsAdpater.ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {


        final Spends itemList = spendsList.get(position);
        final ItemsViewHolder itemHolder = (ItemsViewHolder) holder;

        itemHolder.label.setText(itemList.getLabel());
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
        String price = itemList.getPrice().replace("INR", "");

        itemHolder.price.setText("₹ " + decimalFormat.format(Double.parseDouble(price)));
        Date date = new Date(Long.parseLong(itemList.getDate())); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy "); // the format of your date
        itemHolder.date.setText("" + sdf.format(date));

        if (itemList.getLabel().equalsIgnoreCase("Entertainment")) {
            itemHolder.imageView.setImageResource(R.drawable.entertaiment);
        } else if (itemList.getLabel().equalsIgnoreCase("Atm")) {
            itemHolder.imageView.setImageResource(R.drawable.atm);
        }  else if (itemList.getLabel().equalsIgnoreCase("fuel")) {
            itemHolder.imageView.setImageResource(R.drawable.fuel);
        }else {
            itemHolder.imageView.setImageResource(R.drawable.money);
        }

        itemHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("date", itemList.getDate());
                bundle.putString("label", itemList.getLabel());
                bundle.putString("id", String.valueOf(itemList.getId()));
                bundle.putString("amount", String.valueOf(itemList.getPrice()));
                Intent intent = new Intent(mContext, CreateSpends.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return spendsList.size();
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView price;
        TextView date;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            price = itemView.findViewById(R.id.price_card);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.image);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
        }
    }

}
