package com.example.authme.ui.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.authme.data.FirebaseStorageRepository;
import com.example.authme.databinding.ItemProductoBinding;
import com.example.authme.model.Producto;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterProducto extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final View emptyView;
    private ArrayList<Producto> listProducto;
    private AdapterProductoClickItem listener;

    public AdapterProducto(View emptyView, AdapterProductoClickItem listener) {
        listProducto = new ArrayList<>();
        this.listener=listener;
        this.emptyView = emptyView;

    }

    public interface AdapterProductoClickItem {
        void showProducto(Producto producto);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemProductoBinding itemBinding = ItemProductoBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderProducto(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderProducto) holder).onBind(listProducto.get(position));


    }

    @Override
    public int getItemCount() {
        if (listProducto.size() > 0) {
            emptyView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

        return listProducto.size();
    }
    public void setList(ArrayList<Producto> list) {
        this.listProducto = list;
        notifyDataSetChanged();
    }

    /*
    VIEW HOLDER
     */

    class ViewHolderProducto extends RecyclerView.ViewHolder {
        ItemProductoBinding binding;

        ViewHolderProducto(ItemProductoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Producto producto){
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     listener.showProducto(producto);
                 }
             });
            binding.tvItemNombre.setText( producto.getName());
            binding.tvItemPrecio.setText(producto.getPrecio().toString().concat("€"));
            Glide.with(binding.getRoot().getContext())
                    .load(producto.getImagen())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivItemImage);
            binding.executePendingBindings();

        }
    }
}
