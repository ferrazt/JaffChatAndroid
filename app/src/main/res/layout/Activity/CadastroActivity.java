package com.jaff.jaffwhats.Activity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

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
    }
    public void cadastrarUsuario(Usuario usuario){
        auth = firebase.getAuth();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Sucesso ao Cadastrar Usuário", Toast.LENGTH_SHORT).show();
                     finish();
                }else{
                    String ex;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            ex = "Digite uma senha mais forte";
                        }catch(FirebaseAuthInvalidCredentialsException e){
                            ex = "Por favor, digite um email válido";
                        }catch (FirebaseAuthUserCollisionException e){
                            ex = "Essa conta já foi cadastrada";
                        }catch (Exception e){
                            ex ="Erro ao cadastrar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }
                    Toast.makeText(CadastroActivity.this, ex, Toast.LENGTH_SHORT).show();

                }
            }
        });
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
                    cadastrarUsuario(model);
                }else
                    Toast.makeText(CadastroActivity.this, "Preencha sua senha", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(CadastroActivity.this, "Preencha seu Email!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(CadastroActivity.this, "Preencha seu nome!", Toast.LENGTH_SHORT).show();

    }
}