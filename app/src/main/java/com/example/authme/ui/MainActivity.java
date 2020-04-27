package com.example.authme.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.authme.R;
import com.example.authme.utils.UtilTools;
import com.example.authme.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private static FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBinding();
        initFirebaseAuth();
        checkUserLogged();
    }


    private void initBinding(){
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //comentar
        mainBinding.etEmail.setText("admin@admin.com");
        mainBinding.etPassword.setText("admin123");
    }

    private void checkUserLogged(){
        if(mAuth.getCurrentUser()!=null){
            Intent i = new Intent(this, SecondActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void clickLogin(View view)   {
       String email = mainBinding.etEmail.getText().toString();
       String password = mainBinding.etPassword.getText().toString();

       if(email.isEmpty() || password.isEmpty()){
           UtilTools.showToast("Debes de rellenar todos los campos",getApplicationContext());
       }else{

           mainBinding.progressBar.setVisibility(View.VISIBLE);
           mAuth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               mainBinding.progressBar.setVisibility(View.GONE);
                               UtilTools.showToast("Hola "+mAuth.getCurrentUser().getEmail(),getApplicationContext());
                               Intent i=new Intent(getApplicationContext(), SecondActivity.class);
                               startActivity(i);
                               finish();

                           } else {

                               mainBinding.progressBar.setVisibility(View.GONE);
                               UtilTools.showToast("Revisa tus credenciales, no se ha podido conectar",getApplicationContext());
                           }


                       }


                   });

       }
    }

    public void clickRegister(View view) {
        Intent i=new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        finish();
    }
}
