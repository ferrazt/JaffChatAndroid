package com.jaff.jaffwhats.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.DatabaseFirebase;
import helper.Midia;
import helper.Permissao;
import helper.UsuarioFirebase;
import model.Usuario;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private ImageButton imageButtoncamera, imageButtongaleria;
    private CircleImageView circleImageViewFotoPerfil;
    private EditText editText;
    private ImageView imageViewAtualizarNome;
    private StorageReference storageReference;
    //private DatabaseReference database = firebase.getDatabase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //Permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        imageButtoncamera = findViewById(R.id.imageButtonCamera);
        imageButtongaleria = findViewById(R.id.imageButtonGaleria);
        editText = findViewById(R.id.editTextNome);
        circleImageViewFotoPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        imageViewAtualizarNome = findViewById(R.id.imageViewAtualizarNome);
        //database = firebase.getDatabase();
        storageReference  = firebase.getStorage();


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recuperar dados do usuario
        Uri url = UsuarioFirebase.getUrlPhotoUser();

        if(url != null){
            Glide.with(ConfiguracoesActivity.this).load(url).into(circleImageViewFotoPerfil);

        }else
            circleImageViewFotoPerfil.setImageResource(R.drawable.padrao);

        editText.setText(UsuarioFirebase.getDisplayNameUser());
        imageButtoncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(i, Midia.SELECAO_CAMERA);
                }


            }
        });
        imageButtongaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(i,Midia.SELECAO_GALERIA);
                }
            }
        });
        imageViewAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean retorno = UsuarioFirebase.setDisplayNameUser(editText.getText().toString());

                if(retorno) {

                    DatabaseFirebase.updateUser(editText.getText().toString(),"nome");
                    Toast.makeText(ConfiguracoesActivity.this, "Nome de usuário atualizado", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ConfiguracoesActivity.this, "Erro ao atualizar nome, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
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
                        imagem  = (Bitmap) data.getExtras().get("data");
                        break;
                    case Midia.SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null){
                    circleImageViewFotoPerfil.setImageBitmap(imagem);
                }
                //Recuperar dados da imagem a o fierebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                byte[] dadosImagem = baos.toByteArray();

                //Salvandoi imagem
                String uid = UsuarioFirebase.getUid();

                StorageReference imageRef = storageReference
                        .child("imagens")
                        .child("perfil")
                        .child(uid + ".jpeg");



                UploadTask uploadTask = imageRef.putBytes(dadosImagem);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracoesActivity.this);
                        builder.setTitle("Falha ao atualizar a imagem");
                        builder.setMessage("Falha ao atualizar a imagem, se o problema persistir, entre em contato com o Administrador do sistema");
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

                        UsuarioFirebase.setPhotoUser(taskSnapshot.getDownloadUrl());

                        DatabaseFirebase.updateUser(taskSnapshot.getDownloadUrl().toString(),"foto");
                        Toast.makeText(ConfiguracoesActivity.this, "Sucesso ao atualizar imagem", Toast.LENGTH_SHORT).show();
                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            for (int permissao : grantResults){
                if(permissao == PackageManager.PERMISSION_DENIED){
                    alertaValidacaoPermissao();
                }
            }
        }
    }
    public void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar os recursos desta tela, é necessário aceitar as permissões");
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
}