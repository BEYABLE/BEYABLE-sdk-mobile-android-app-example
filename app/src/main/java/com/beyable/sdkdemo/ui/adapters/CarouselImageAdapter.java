package com.beyable.sdkdemo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.beyable.sdkdemo.R;
import com.beyable.sdkdemo.tools.Requester;

import java.util.ArrayList;

/**
 * Created by Gol D. Marko on 31/01/2024.
 * <p>
 **/

public class CarouselImageAdapter extends RecyclerView.Adapter<CarouselImageAdapter.ViewHolder> {

    Context context;
    ArrayList<String> arrayList;
    OnItemClickListener onItemClickListener;

    public CarouselImageAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Requester.getSharedInstance(holder.itemView.getContext()).setImageForNetworkImageView(
                holder.imageView,
                arrayList.get(position),
                R.drawable.img_placeholder,
                R.drawable.img_placeholder);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_image);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(ImageView imageView, String path);
    }
}