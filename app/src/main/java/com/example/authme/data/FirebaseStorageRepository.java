package com.example.authme.data;

import android.net.Uri;

import com.example.authme.model.ImageStorage;
import com.example.authme.ui.adapter.AdapterImageStorage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public abstract class FirebaseStorageRepository {


    public static void getUrlImages(final AdapterImageStorage adapterImageStorage){
        OnSuccessListener<ListResult> listResultOnSuccessListener = new OnSuccessListener<ListResult>() {
        @Override
        public void onSuccess(ListResult listResult) {
            callOther(listResult,adapterImageStorage);
            }
        };
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("producto");
        mStorageRef.listAll().addOnSuccessListener(listResultOnSuccessListener);
    }



    private static void callOther(final ListResult listResult, final AdapterImageStorage adapterImageStorage){

        final ArrayList<ImageStorage> listItem = new ArrayList<>();
        final int[] count = {0};
        for(StorageReference ref: listResult.getItems()){
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    listItem.add(new ImageStorage(uri.toString()));
                    count[0]++;
                    if(count[0]==listResult.getItems().size()){
                        adapterImageStorage.setList(listItem);
                    }

                }
            });
        }

    }

}
