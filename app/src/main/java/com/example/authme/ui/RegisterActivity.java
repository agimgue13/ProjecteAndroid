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
import com.example.authme.databinding.ActivityRegisterBinding;
import com.example.authme.utils.UtilTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity    {

    private static FirebaseAuth mAuth;
    private ActivityRegisterBinding registerBinding;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initBinding();
        initFirebaseAuth();
    }
    private void initBinding(){
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
    }
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void clickRegister(View view) {
        final String email = registerBinding.etRegisterEmail.getText().toString();
        String password = registerBinding.etRegisterPassword.getText().toString();
        String passwordRepeat = registerBinding.etRegisterPasswordRepeat.getText().toString();


        if(email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()){
            UtilTools.showToast("Debes de rellenar todos los campos",getApplicationContext());
        }else {
            if(!password.equals(passwordRepeat)){
                UtilTools.showToast("Las contraseñas deben de coincidir",getApplicationContext());
            }else{
                registerBinding.progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UtilTools.showToast("Registrado con éxito",getApplicationContext());
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                    registerBinding.progressBar.setVisibility(View.GONE);
                                } else {
                                    String error = "Aviso: ";
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {

                                        error = error.concat(" La contraseña debe de contener 8 caracteres");
                                    } catch(FirebaseAuthInvalidCredentialsException e) {

                                        error = error.concat(" Usuario o contraseña invalidos");
                                    } catch(FirebaseAuthUserCollisionException e) {
                                        error = error.concat(" El usuario con el email "+email+" ya existe");
                                    } catch(Exception e) {
                                        Log.e("ERROR REGISTER", e.getMessage());
                                    }
                                    UtilTools.showToast(error,getApplicationContext());
                                    registerBinding.progressBar.setVisibility(View.GONE);
                                }


                            }
                        });
            }
        }
    }


}
