package com.example.authme.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.authme.R;
import com.example.authme.databinding.ActivityProductoInfoBinding;
import com.example.authme.model.Producto;
import com.example.authme.ui.viewmodel.ViewModelFactory;
import com.example.authme.ui.viewmodel.ViewModelProducto;

public class ProductoInfoActivity extends AppCompatActivity {
    public static final String PRODUCTO_PARCEL = "producto";
    ActivityProductoInfoBinding mBinding;
    ViewModelProducto mViewModelProducto;
    Producto p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initGetExtras();
        initViewModel();
        initBinding();

    }

    private void initGetExtras(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){

            p = bundle.getParcelable(PRODUCTO_PARCEL);
        }
    }
    private void initBinding(){

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_producto_info);



        mBinding.tvInfoDescripcion.setText(p.getDescripcion());
        mBinding.tvInfoName.setText(p.getName());
        mBinding.tvInfoMarca.setText(p.getMarca());
        mBinding.tvInfoType.setText(p.getTipo());
        mBinding.tvInfoPrecio.setText(String.valueOf(p.getPrecio()).concat("€"));
            Glide.with(mBinding.getRoot().getContext())
                    .load(p.getImagen())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(mBinding.imageView);



    }

    private void initViewModel(){
        ViewModelProvider.Factory factory = ViewModelFactory.createFor(new ViewModelProducto(getApplication(),""));
        mViewModelProducto = ViewModelProviders.of(this, factory).get(ViewModelProducto.class);
    }

    public void clickShop(View view) {
        Intent i =new Intent(getApplicationContext(), ShopActivity.class);
        startActivity(i);
        finish();
    }
}
