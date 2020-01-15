package com.example.sih;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.Models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> productList;

    public ProductAdapter(Activity context, List<ProductModel> products) {
        this.mContext = context;
        this.productList = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.name.setText(product.getProname());
        holder.price.setText(product.getProprice());
        holder.date.setText(product.getProddate());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, date, nameLabel, priceLabel, dateLabel;
        public ImageView image;

        public  MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.productName);
            price = view.findViewById(R.id.productPrice);
            date = view.findViewById(R.id.productDate);
            nameLabel = view.findViewById(R.id.productName_label);
            priceLabel = view.findViewById(R.id.productPrice_label);
            dateLabel = view.findViewById(R.id.productDate_label);
            image = view.findViewById(R.id.productImage);
        }
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View productListView = convertView;
//        if (productListView == null) {
//            productListView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.product_card, parent, false);
//        }
//
//        final ProductModel currentProduct = getItem(position);
//
//        TextView nameTextView = productListView.findViewById(R.id.productName);
//        nameTextView.setText(currentProduct.getProname());
//
//        TextView priceTextView = productListView.findViewById(R.id.productPrice);
//        priceTextView.setText(currentProduct.getProprice());
//
//        TextView dateTextView = productListView.findViewById(R.id.productDate);
//        dateTextView.setText(currentProduct.getProddate());
//
//        return productListView;
//    }
}
