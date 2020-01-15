package com.example.sih.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.Models.PostModel;
import com.example.sih.R;

import java.util.ArrayList;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.PostViewHolder>  {
    private ArrayList<PostModel> mList;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public PostViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);

        }
    }

    public PostAdapter(ArrayList<PostModel> exampleList) {
        mList = exampleList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,
                parent, false);
        PostViewHolder p = new PostViewHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        PostModel currentItem = mList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filterList(ArrayList<PostModel> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }
}
