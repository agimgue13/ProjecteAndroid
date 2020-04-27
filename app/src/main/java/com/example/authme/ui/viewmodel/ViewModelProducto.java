package com.example.authme.ui.viewmodel;

import android.app.Application;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.authme.data.FirebaseDatabaseRespository;
import com.example.authme.data.FirebaseStorageRepository;
import com.example.authme.model.ImageStorage;
import com.example.authme.model.Producto;
import com.example.authme.ui.adapter.AdapterImageStorage;
import com.example.authme.ui.adapter.AdapterProducto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ViewModelProducto extends AndroidViewModel {



    private FirebaseDatabaseRespository respository;
    LiveData<OnSuccessListener> successListenerLiveData;

    public ViewModelProducto(@NonNull Application application, String orderBy) {
        super(application);
        respository = new FirebaseDatabaseRespository(orderBy);

    }

    /* =====================
      |                    |
      |     DATA BASE      |
      |                    |
       =====================
     */
    public LiveData<DataSnapshot> getDataSnapShot(){
        return respository;
    }

    public void insertProducto(Producto p){
        respository.insert(p);
    }

    public void deleteProducto(String idProd){
        respository.delete(idProd);
    }
    /* =====================
      |                    |
      |     STORAGE        |
      |                    |
      ======================
    */

    public void getListUri(AdapterImageStorage adapterImageStorage){
        FirebaseStorageRepository.getUrlImages(adapterImageStorage);
    }





}
