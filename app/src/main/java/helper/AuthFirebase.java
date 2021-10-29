package helper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.jaff.jaffwhats.Activity.CadastroActivity;
import com.jaff.jaffwhats.Activity.LogingActivity;
import com.jaff.jaffwhats.config.firebase;

import model.Usuario;

public class AuthFirebase {

    public static void registerUserAuth(Usuario usuario, Activity activity){
        FirebaseAuth auth = firebase.getAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid = UsuarioFirebase.getUid();
                    DatabaseFirebase.setDatabaseUser(uid,usuario);
                    UsuarioFirebase.setDisplayNameUser(usuario.getNome());
                    Toast.makeText(activity, "Sucesso ao Cadastrar Usuário", Toast.LENGTH_SHORT).show();
                    activity.finish();

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
                    Toast.makeText(activity, ex, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    //Login usuario verifica se o email e senha existem no Firebase
    public static void LoginUsuario(Usuario model, Activity activity){
        FirebaseAuth auth = firebase.getAuth();
        auth.signInWithEmailAndPassword(
                model.getEmail(),model.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity, "Bem vindo! "+ UsuarioFirebase.getDisplayNameUser(), Toast.LENGTH_SHORT).show();
                }else{
                    String ex = null;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        Toast.makeText(activity, "USuário não está cadastrado", Toast.LENGTH_SHORT).show();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(activity, "Email e senha não correspondem", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        ex = "Erro ao fazer login: " + e;
                        e.printStackTrace();
                    }
                    Toast.makeText(activity, ex, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
