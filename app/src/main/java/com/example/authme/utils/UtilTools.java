package com.example.authme.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.authme.R;

import java.util.UUID;

public class UtilTools {


    public static final String CHILD_NODE ="producto";
    public static final int REQUEST_GALLERY = 1;
    public static final String UID_ADMIN = "qpFPihbXwGOpKhC4cfadltCWON92";

    public static void showToast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static String generateUniqueID(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }


    public static String getTipoFromArray(Context context, int index){
        Resources res = context.getResources();
        String[] tipos = res.getStringArray(R.array.tipos_productos);
        return tipos[index];
    }

    public static int setSelectedItem(Context context, String tipo){
        Resources res = context.getResources();
        String[] tipos = res.getStringArray(R.array.tipos_productos);

        for (int i=0; i<tipos.length; i++){
            if (tipos[i].equals(tipo)){
                return i;
            }
        }
        return -1;
    }
}
