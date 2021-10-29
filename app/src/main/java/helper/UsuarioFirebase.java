package helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jaff.jaffwhats.config.firebase;

import java.util.List;

import model.Usuario;
/*
* Toda essa sessao recupera os dados do usuario dentro do Firebase
* Todos os membros incorporam da classe firebase dentro do package config
* Autor: Jhoel Fiorese
*/


public class UsuarioFirebase {

    public static String getUid(){
        FirebaseUser user = firebase.getAuth().getCurrentUser();
        if(user != null){
            String uid = user.getUid();
            return  uid;
        }
        return null;
    }
    public static boolean setDisplayNameUser(String name){
      FirebaseUser  user = firebase.getAuth().getCurrentUser();
        try{
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                //return;
                                Log.d("perfil", "Erro ao atualizar nome usuario");
                            }
                        }
                    });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static String getDisplayNameUser(){
        FirebaseUser  user = firebase.getAuth().getCurrentUser();
        if(user != null){
            String name = user.getDisplayName();
            return name;
        }
        return null;
    }
    public static boolean setPhotoUser(Uri url){
        FirebaseUser  user = firebase.getAuth().getCurrentUser();
        try {
            if(user != null){

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(url)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("perfil", "Erro ao atualizar foto usuario");
                                }
                            }
                        });
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static Uri getUrlPhotoUser(){
        FirebaseUser  user = firebase.getAuth().getCurrentUser();
        if(user != null){
            Uri url = user.getPhotoUrl();
            return url;
        }
        return null;
    }
    public static String getEmailUser(){
        FirebaseUser  user = firebase.getAuth().getCurrentUser();
        if(user !=null){
            return user.getEmail();
        }
        return null;
    }
    public static void signOut(){
        FirebaseAuth auth = firebase.getAuth();
        auth.signOut();
    }
}
