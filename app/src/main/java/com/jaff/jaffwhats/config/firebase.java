package com.jaff.jaffwhats.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class firebase {

    private static DatabaseReference database;
    private static FirebaseAuth auth;
    private static StorageReference storage;

    //retorna a instância do firebase realtime database
    public static DatabaseReference getDatabase(){
        if(database == null)
            database = FirebaseDatabase.getInstance().getReference();

        return database;
    }
    //retorna a instancia do firebase auth

    public static FirebaseAuth getAuth() {
        if(auth == null)
            auth = FirebaseAuth.getInstance();

        return auth;
    }
    public static StorageReference getStorage(){
        if(storage == null)
            storage = FirebaseStorage.getInstance().getReference();

        return storage;
    }

}
