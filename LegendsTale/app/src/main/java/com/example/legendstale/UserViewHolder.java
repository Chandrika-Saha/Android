package com.example.legendstale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserViewHolder extends RecyclerView.Adapter<UserViewHolder.ImageViewHolder> {

    private Context mcontext;
    private List<Update> mUp;
    private OnItemClickListener mListener;


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public ImageView img;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name1);
            img = itemView.findViewById(R.id.img1);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
    }
    public UserViewHolder(Context c,List<Update> up){
        mcontext = c;
        mUp = up;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.legends,viewGroup,false);
        return new ImageViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {


        Update curr = mUp.get(i);
        imageViewHolder.name.setText(curr.getName());
        Picasso.with(mcontext).load(curr.getImgUrl()).into(imageViewHolder.img);


    }

    @Override
    public int getItemCount() {
        return mUp.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }



}