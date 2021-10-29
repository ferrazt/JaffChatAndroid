package com.jaff.jaffwhats.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import helper.AuthFirebase;
import model.Usuario;


import model.Usuario;

public class LogingActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);

        auth = firebase.getAuth();
        campoEmail = findViewById(R.id.loginEmail);
        campoSenha = findViewById(R.id.loginSenha);
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if(usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }

//Validator valida se os campos foram preenchidos pelo usuário e envia para o LoginUsuario
    public void validatorLogin(View v){

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if( !email.isEmpty())
            if(!senha.isEmpty()){
                Usuario model = new Usuario();
                model.setEmail(email);
                model.setSenha(senha);
                AuthFirebase.LoginUsuario(model, this);
                abrirTelaPrincipal();
                finish();
            }else
                Toast.makeText(LogingActivity.this, "Preencha sua senha", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(LogingActivity.this, "Preencha seu Email!", Toast.LENGTH_SHORT).show();
    }


    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LogingActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LogingActivity.this, MainActivity.class);
        startActivity(intent);
    }

}