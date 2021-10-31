package com.jaff.jaffwhats.Activity;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import java.util.ArrayList;
import java.util.List;

import adapter.MensagensAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.DatabaseFirebase;
import helper.UsuarioFirebase;
import model.Mensagem;
import model.Usuario;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView circleImageViewFoto;
    private TextView textViewNome;
    private Usuario usuarioDestinatario;
    private EditText editMensagem;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    //Indentificador usuario remetente e destinatário
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

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
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);


        //Recuperar os dados do usuário destinatário
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioDestinatario = (Usuario)bundle.getSerializable("chatContato");

           textViewNome.setText(usuarioDestinatario.getNome());
           String foto = usuarioDestinatario.getFoto();
           if(foto != null){
               Uri url = Uri.parse(usuarioDestinatario.getFoto());
               Glide.with(ChatActivity.this)
                       .load(url).
                       into(circleImageViewFoto);
           }else
               circleImageViewFoto.setImageResource(R.drawable.padrao);
        }
        //Recuperar os dados do usuário remetente
        idUsuarioRemetente = UsuarioFirebase.getUid();
        //Recuperar dados do usuário rementente
        idUsuarioDestinatario = usuarioDestinatario.getuId();

        //Configurações Adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());

        //Configuração Recycler View
        RecyclerView.LayoutManager recyclerMenager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(recyclerMenager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        //Configurando a recuperação de mensagens
        database = firebase.getDatabase();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);
    }
    public void enviarMensagem(View view){
        String mensagemText = editMensagem.getText().toString();
        if(!mensagemText.isEmpty()){

            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(mensagemText);

            //Salver mensagem para o rementente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
            //Salver mensagem para o destinatário
            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

        }else
            Toast.makeText(this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
    }
    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg){
        DatabaseReference database = firebase.getDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);
            editMensagem.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagem();
    }

    @Override
    protected void onStop() {
        super.onStop();
         mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagem(){
        childEventListenerMensagens = mensagensRef.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                        mensagens.add(mensagem);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}