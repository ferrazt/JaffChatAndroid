package helper;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jaff.jaffwhats.config.firebase;

import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class DatabaseFirebase {

    private static DatabaseReference databaseReference = firebase.getDatabase();

    public static void setDatabaseUser(String uid, Usuario usuario){
        DatabaseReference data = databaseReference.child("users").child(uid);
        data.setValue(usuario);

    }
    public static void updateUser(String url, String child){
        try{
            DatabaseReference data = databaseReference.child("users").child(UsuarioFirebase.getUid()).child(child);
            data.setValue(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
