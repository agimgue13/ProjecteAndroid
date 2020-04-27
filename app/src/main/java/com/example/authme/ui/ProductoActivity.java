package com.example.authme.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.authme.R;
import com.example.authme.databinding.ActivityProductoBinding;
import com.example.authme.databinding.ActivityRegisterBinding;
import com.example.authme.model.ImageStorage;
import com.example.authme.model.Producto;
import com.example.authme.ui.adapter.AdapterImageStorage;
import com.example.authme.ui.viewmodel.ViewModelFactory;
import com.example.authme.ui.viewmodel.ViewModelProducto;
import com.example.authme.utils.UtilTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductoActivity extends AppCompatActivity {
    public static final String PRODUCTO_PARCEL = "producto";
    ActivityProductoBinding productoBinding;
    ViewModelProducto mViewModelProducto;
    private boolean isUpdate;
    private boolean isAdmin;
    private static FirebaseAuth mAuth;
    Producto p;
    ImageStorage imageStorage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFirebaseAuth();
        initGetExtras();
        initViewModel();
        initBinding();


    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null && mAuth.getUid().equals(UtilTools.UID_ADMIN)){
            isAdmin = true;
        }else{
            isAdmin = false;
        }

    }
    private void initGetExtras(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            isUpdate = true;
            p = bundle.getParcelable(PRODUCTO_PARCEL);
        }else{
            isUpdate = false;
            p = null;
        }
    }
    private void initBinding(){

        productoBinding = DataBindingUtil.setContentView(this, R.layout.activity_producto);

        if(isUpdate){
            productoBinding.btnEliminar.setVisibility(View.VISIBLE);
            productoBinding.btnProducto.setText("Modificar");
            productoBinding.etProductoDescription.setText(p.getDescripcion());
            productoBinding.etProductoName.setText(p.getName());
            productoBinding.etMarcaProducto.setText(p.getMarca());
            productoBinding.etProductoName.setText(p.getName());
            int index_selected = UtilTools.setSelectedItem(this,p.getTipo());
            productoBinding.spTipo.setSelection(index_selected);
            productoBinding.etProductoPrecio.setText(String.valueOf(p.getPrecio()));
            Glide.with(productoBinding.getRoot().getContext())
                    .load(p.getImagen())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(productoBinding.imageView);

            initViewAdmin();
        }

    }

    private void initViewModel(){
        ViewModelProvider.Factory factory = ViewModelFactory.createFor(new ViewModelProducto(getApplication(),""));
        mViewModelProducto = ViewModelProviders.of(this, factory).get(ViewModelProducto.class);
    }

    private void initViewAdmin(){

        if(isAdmin){
            productoBinding.btnEliminar.setVisibility(View.VISIBLE);
            productoBinding.btnProducto.setVisibility(View.VISIBLE);
            productoBinding.btnGallery.setVisibility(View.VISIBLE);
        }
    }
    public void clickProducto(View view){


            String productoName = productoBinding.etProductoName.getText().toString();
            String productoDescription = productoBinding.etProductoDescription.getText().toString();
            String productoPrecio = productoBinding.etProductoPrecio.getText().toString();
            String productoMarca = productoBinding.etMarcaProducto.getText().toString();
            String productoType = productoBinding.spTipo.getSelectedItem().toString();
            if (isUpdate && imageStorage == null){
                imageStorage = new ImageStorage(p.getImagen());
            }
            if(productoName.isEmpty() || productoDescription.isEmpty() || productoPrecio.isEmpty() || imageStorage==null || productoMarca.isEmpty()){
                UtilTools.showToast("Debes de rellenar todos los campos y seleccionar una imagen",getApplicationContext());
            }else{
                Float precio = Float.parseFloat(productoPrecio);
                String uid = UtilTools.generateUniqueID();
                String mensaje = " insertado ";
                if (isUpdate){
                    uid = p.getId();
                    mensaje = " modificado ";
                }
                mViewModelProducto.insertProducto(new Producto(uid,imageStorage.getUri(),productoName,productoDescription,productoType,productoMarca,precio));
                UtilTools.showToast("Producto"+mensaje+"con exito",getApplicationContext());

                finish();
            }


    }

    public void clickGallery(View view) {
        Intent i =new Intent(getApplicationContext(),GalleryActivity.class);
        startActivityForResult(i,UtilTools.REQUEST_GALLERY);
    }

    public void clickEliminarProducto(View view){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setTitle("Borrar Producto")
                .setMessage("Estás seguro de borrar el producto: "+p.getName())
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModelProducto.deleteProducto(p.getId());
                        UtilTools.showToast("Producto borrado",getApplicationContext());
                        finish();
                    }

                })
                .setNegativeButton("NO", null)
                .show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == UtilTools.REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                imageStorage = data.getExtras().getParcelable("ITEM");
                Glide.with(productoBinding.getRoot().getContext())
                        .load(imageStorage.getUri())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(productoBinding.imageView);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
