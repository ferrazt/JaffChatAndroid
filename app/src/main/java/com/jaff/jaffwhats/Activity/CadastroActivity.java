package com.jaff.jaffwhats.Activity;

import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import helper.AuthFirebase;
import helper.DatabaseFirebase;
import helper.UsuarioFirebase;
import model.Usuario;


public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        auth = firebase.getAuth();

    }

    public void validarCadastroUsuario(View view){
        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if( !nome.isEmpty())
            if( !email.isEmpty())
                if(!senha.isEmpty()){
                    Usuario model = new Usuario();
                    model.setNome(nome);
                    model.setEmail(email);
                    model.setSenha(senha);
                    AuthFirebase.registerUserAuth(model, this);
                }else
                    Toast.makeText(CadastroActivity.this, "Preencha sua senha", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(CadastroActivity.this, "Preencha seu Email!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(CadastroActivity.this, "Preencha seu nome!", Toast.LENGTH_SHORT).show();

    }
}