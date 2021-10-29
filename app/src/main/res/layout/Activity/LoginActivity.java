package com.jaff.jaffwhats.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = firebase.getAuth();
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if(usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }
    public void LoginUsuario(Usuario model){
        auth.signInWithEmailAndPassword(
                model.getEmail(),model.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                    finish();
                }else{
                    String ex = null;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        Toast.makeText(LoginActivity.this, "USuário não está cadastrado", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(LoginActivity.this, "Email e senha não correspondem", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        ex = "Erro ao fazer login: " + e;
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, ex, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void validarLogin(){

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

            if( !email.isEmpty())
                if(!senha.isEmpty()){
                    Usuario model = new Usuario();
                    model.setEmail(email);
                    model.setSenha(senha);
                    LoginUsuario(model);
                }else
                    Toast.makeText(LoginActivity.this, "Preencha sua senha", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LoginActivity.this, "Preencha seu Email!", Toast.LENGTH_SHORT).show();
    }
    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}