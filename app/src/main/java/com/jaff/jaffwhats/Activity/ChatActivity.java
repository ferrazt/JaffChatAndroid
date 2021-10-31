package com.jaff.jaffwhats.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import adapter.MensagensAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import helper.Midia;
import helper.UsuarioFirebase;
import model.Conversa;
import model.Mensagem;
import model.Usuario;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView circleImageViewFoto;
    private TextView textViewNome;
    private Usuario usuarioDestinatario;
    private EditText editMensagem;
    private ImageView imageCamera;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;
    private StorageReference storageReference;

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
        imageCamera = findViewById(R.id.imageCamera);
        storageReference  = firebase.getStorage();

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

        //Evento de clique na camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, Midia.SELECAO_CAMERA);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch (requestCode){
                    case Midia.SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if(imagem !=null){
                    //Recuperar dados da imagem a o fierebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome imagem
                    String nomeImagem = UUID.randomUUID().toString();
                    //Configurar as referencias do firebase
                    StorageReference imageRef = storageReference
                            .child("imagens")
                            .child("fotosChat")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem + ".jpeg");
                    UploadTask uploadTask = imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer o upload da imagem");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                            builder.setTitle("Falha ao enviar imagem");
                            builder.setMessage("Houve um erro ao enviar a imagem para os nosso servidores. Contate o administrador do sistem!");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                            Mensagem mensagem = new Mensagem();
                            mensagem.setIdUsuario(idUsuarioRemetente);
                            mensagem.setMensagem("imagem.jpeg");
                            mensagem.setImagem(downloadUrl);
                            //Salvando mensagem para o remetente
                            salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario,mensagem);
                            //Salvando mensagem para o destinatário
                            salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente,mensagem);
                            Toast.makeText(ChatActivity.this, "Sucesso ao enviar imagem", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
            //Salvar conversa
            salvarConversa(mensagem);

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
    private void salvarConversa(Mensagem msg){
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idUsuarioRemetente);
        conversaRemetente.setIdDestinatario(idUsuarioDestinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());
        conversaRemetente.setUsuarioExibicao(usuarioDestinatario);
        conversaRemetente.salvar();
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