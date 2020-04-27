package com.example.authme.data;

import android.app.Activity;
import android.content.Context;
import android.database.Observable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.authme.databinding.ActivityRegisterBinding;
import com.example.authme.model.Producto;
import com.example.authme.utils.UtilTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebaseDatabaseRespository extends LiveData<DataSnapshot>   {
    private static final String LOG_TAG = "FirebaseDatabaseRespo";
    public final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();

    public FirebaseDatabaseRespository(String orderBy) {

        if (!orderBy.isEmpty()){
            this.query = FirebaseDatabase.getInstance().getReference().child(UtilTools.CHILD_NODE).orderByChild("precio");
        }else{
            this.query = FirebaseDatabase.getInstance().getReference().child(UtilTools.CHILD_NODE);
        }

    }

    public void insert(Producto p){
        FirebaseDatabase.getInstance().getReference().child(UtilTools.CHILD_NODE).child(p.getId()).setValue(p);
    }
    public void delete(String idProd){
        FirebaseDatabase.getInstance().getReference().child(UtilTools.CHILD_NODE).child(idProd).setValue(null);
    }
     @Override
    protected void onActive() {
        Log.d(LOG_TAG, "onActive");
        query.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(LOG_TAG, "onInactive");
        query.removeEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }

}
