package com.jaff.jaffwhats.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaff.jaffwhats.R;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView circleImageViewFoto;
    private TextView textViewNome;
    private Usuario usuarioDestinatário;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbarChat);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        circleImageViewFoto = findViewById(R.id.circleImageViewFotoChat);
        textViewNome = findViewById(R.id.texteViewNomeChat);

        //Recuperar os dados do usuário destinatário
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
           usuarioDestinatário = (Usuario)bundle.getSerializable("chatContato");
           textViewNome.setText(usuarioDestinatário.getNome());
           String foto = usuarioDestinatário.getFoto();
           if(foto != null){
               Uri url = Uri.parse(usuarioDestinatário.getFoto());
               Glide.with(ChatActivity.this)
                       .load(url).
                       into(circleImageViewFoto);
           }else
               circleImageViewFoto.setImageResource(R.drawable.padrao);
        }

    }
}