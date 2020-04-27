package com.example.authme.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import com.example.authme.R;
import com.example.authme.data.FirebaseStorageRepository;
import com.example.authme.databinding.ActivityGalleryBinding;
import com.example.authme.model.ImageStorage;
import com.example.authme.model.Producto;
import com.example.authme.ui.adapter.AdapterImageStorage;
import com.example.authme.ui.viewmodel.ViewModelFactory;
import com.example.authme.ui.viewmodel.ViewModelProducto;
import com.example.authme.utils.UtilTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements AdapterImageStorage.AdapterImageStorageClickItem{
    ActivityGalleryBinding mBinding;
    AdapterImageStorage mAdapter;
    ViewModelProducto viewModelProducto;
    ImageStorage selectedImageStorage;
    Boolean isSelected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_gallery);
        initViewModel();
        initBinding();
    }

    private void initViewModel(){
        ViewModelProvider.Factory factory = ViewModelFactory.createFor(new ViewModelProducto(getApplication(),""));
        viewModelProducto = ViewModelProviders.of(this, factory).get(ViewModelProducto.class);
    }


    private void initBinding(){

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        mBinding.btnSeleccionar.setVisibility(View.GONE);
        fillRecycler();

    }

    private void fillRecycler(){

        mAdapter = new AdapterImageStorage(mBinding.emptyView, this);
        mBinding.rvGallery.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mBinding.rvGallery.setLayoutManager(mLayoutManager);

       viewModelProducto.getListUri(mAdapter);
    }

    @Override
    public void selectImage(ImageStorage imageStorage) {
        isSelected  = true;
        selectedImageStorage= imageStorage;
        mBinding.btnSeleccionar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(isSelected){
            Intent i = new Intent();
            i.putExtra("ITEM",selectedImageStorage);
            setResult(RESULT_OK, i);
            finish();
        }else{
            super.onBackPressed();
        }

    }

    public void clickSeleccionar(View view) {

        if(!isSelected){
            UtilTools.showToast("Debes de seleccionar una imagen",getApplicationContext());
        }else{
            Intent i = new Intent();
            i.putExtra("ITEM",selectedImageStorage);
            setResult(RESULT_OK, i);
            finish();
        }

    }

}
