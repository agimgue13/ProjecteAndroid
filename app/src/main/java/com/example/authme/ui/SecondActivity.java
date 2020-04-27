package com.example.authme.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.authme.R;
import com.example.authme.databinding.ActivityRegisterBinding;
import com.example.authme.databinding.ActivitySecondBinding;
import com.example.authme.model.Producto;
import com.example.authme.ui.adapter.AdapterProducto;
import com.example.authme.ui.viewmodel.ViewModelFactory;
import com.example.authme.ui.viewmodel.ViewModelProducto;
import com.example.authme.utils.UtilTools;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class SecondActivity extends AppCompatActivity implements AdapterProducto.AdapterProductoClickItem {

    private ViewModelProducto  mViewModelProducto;
    private static FirebaseAuth mAuth;
    private ActivitySecondBinding mBinding;
    private AdapterProducto mAdapter;
    private String filtroTipo;
    private String filtroMarca;
    private boolean priceAsc;
    private boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initBinding();
        initFirebaseAuth();
        initViewModel("");
        checkUserLogged();

    }
    private void initBinding(){
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_second);
        filtroTipo = "";
        filtroMarca = "";
        priceAsc = true;
    }

    private void initViewModel(final String orderBy){
        ViewModelProvider.Factory factory = ViewModelFactory.createFor(new ViewModelProducto(getApplication(),orderBy));
        mViewModelProducto = ViewModelProviders.of(this, factory).get(ViewModelProducto.class);
        mViewModelProducto.getDataSnapShot().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    ArrayList<Producto> list = new ArrayList<>();
                    for(DataSnapshot readData: dataSnapshot.getChildren()){
                        Producto producto = readData.getValue(Producto.class);
                        if(!filtroTipo.isEmpty() && !filtroTipo.equals("Todos")){
                            if( filtroTipo.equals(producto.getTipo())){
                                list.add(producto);
                            }
                        }else if(!filtroMarca.isEmpty()) {
                            if(producto.getMarca().toLowerCase().contains(filtroMarca.toLowerCase())){
                                list.add(producto);
                            }
                        }
                        else{
                            list.add(producto);
                        }

                    }
                    if (!orderBy.isEmpty()){
                        Collections.sort(list);
                    }
                    fillRecycler(list);
                }
            }
        });

    }

    private void fillRecycler(ArrayList<Producto> list){
        if(list.size()!=0){
            mAdapter = new AdapterProducto(mBinding.emptyView, this);
            mBinding.rvProductos.setAdapter(mAdapter);
            LinearLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
            mBinding.rvProductos.setLayoutManager(mLayoutManager);
            mAdapter.setList(list);
        }else{
            mAdapter = new AdapterProducto(mBinding.emptyView, this);
            mBinding.rvProductos.setAdapter(mAdapter);
            LinearLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
            mBinding.rvProductos.setLayoutManager(mLayoutManager);
            mAdapter.setList(list);
            mBinding.emptyView.setText("No hay productos que mostrar");
        }

        if(!filtroMarca.isEmpty()){
            filtroMarca="";
        }

    }

    private void initFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getUid().equals(UtilTools.UID_ADMIN)){
            isAdmin=true;
            mBinding.btnAddProd.setVisibility(View.VISIBLE);
        }else{
            isAdmin=false;
            mBinding.btnAddProd.setVisibility(View.GONE);
        }
    }
    private void checkUserLogged(){
        if(mAuth.getCurrentUser()!=null){
            String id = mAuth.getCurrentUser().getUid();
        }else{
            UtilTools.showToast("Not logged",getApplicationContext());
        }
    }
    private void cerrarSession() {
        UtilTools.showToast("Adios "+mAuth.getCurrentUser().getEmail(),getApplicationContext());
        mAuth.signOut();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void clickInsertar(View view) {
        Intent i =new Intent(getApplicationContext(), ProductoActivity.class);
        startActivity(i);
    }
    @Override
    public void showProducto(Producto producto) {

        if (isAdmin){
            Intent i =new Intent(getApplicationContext(), ProductoActivity.class);
            i.putExtra(ProductoActivity.PRODUCTO_PARCEL, producto);
            startActivity(i);
        }else{
            Intent i =new Intent(getApplicationContext(), ProductoInfoActivity.class);
            i.putExtra(ProductoActivity.PRODUCTO_PARCEL, producto);
            startActivity(i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_menu_type:
                filtroListaProductos();
                return true;
            case R.id.search_menu_marca:
                filtroMarcaProductos();
                return true;
            case R.id.search_menu_order:
                initViewModel("precio");
                return true;
                case R.id.logout:
                    cerrarSession();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void filtroListaProductos(){
        new AlertDialog.Builder(this)
                .setTitle("Buscar por Tipo")
                .setItems(R.array.tipos_productos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        filtroTipo = UtilTools.getTipoFromArray(getApplicationContext(),which);
                        initViewModel("");
                    } })


                .show();
    }

    private void filtroMarcaProductos(){
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.item_dialog_filter, null);
        final EditText etSearch = (EditText)mView.findViewById(R.id.input_search_marca);


        new AlertDialog.Builder(this)
                .setTitle("Buscar por Marca")
                .setView(mView)
                .setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        filtroMarca = etSearch.getText().toString();
                        initViewModel("");
                    }
                }).setNeutralButton("Borrar Filtro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            filtroMarca = "";
                            initViewModel("");
                        }
                    })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
