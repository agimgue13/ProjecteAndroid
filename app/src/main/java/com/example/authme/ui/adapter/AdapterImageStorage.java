package com.example.authme.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.authme.databinding.ItemImageStorageBinding;
import com.example.authme.model.ImageStorage;

import java.util.ArrayList;

public class AdapterImageStorage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final View emptyView;
    private ArrayList<ImageStorage> listImages;
    private AdapterImageStorageClickItem listener;


    public AdapterImageStorage(View emptyView, AdapterImageStorageClickItem listener) {
        listImages = new ArrayList<>();
        this.listener=listener;
        this.emptyView = emptyView;

    }
    public interface AdapterImageStorageClickItem {
        void selectImage(ImageStorage imageStorage);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemImageStorageBinding itemBinding = ItemImageStorageBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderImageStorage(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderImageStorage) holder).onBind(listImages.get(position));
    }

    @Override
    public int getItemCount() {
        if (listImages.size() > 0) {
            emptyView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

        return listImages.size();
    }

    public void setList(ArrayList<ImageStorage> list) {
        this.listImages = list;
        notifyDataSetChanged();
    }

    class ViewHolderImageStorage extends RecyclerView.ViewHolder {
        ItemImageStorageBinding binding;

        ViewHolderImageStorage(ItemImageStorageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final ImageStorage img){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.selectImage(img);
                }
            });

            Glide.with(binding.getRoot().getContext())
                    .load(img.getUri())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivItemImagenStorage);

            binding.executePendingBindings();

        }
    }
}
