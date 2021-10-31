package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaff.jaffwhats.Activity.CadastroActivity;
import com.jaff.jaffwhats.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.UsuarioFirebase;
import model.Usuario;

public class ContatosAdapter extends RecyclerView.Adapter <ContatosAdapter.MyViewHolder>{

    private final ArrayList<Usuario> listaContatos;
    private final Context context;
    public ContatosAdapter(ArrayList<Usuario> listaContatos, Context context) {
        this.listaContatos = listaContatos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_contatos, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Usuario usuario = listaContatos.get(i);
        myViewHolder.nome.setText(usuario.getNome());
        myViewHolder.email.setText(usuario.getEmail());


        if(usuario.getFoto() != null) {
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(myViewHolder.foto);
        }else
            myViewHolder.foto.setImageResource(R.drawable.padrao);
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public static class MyViewHolder  extends RecyclerView.ViewHolder{
        CircleImageView foto;
        TextView nome, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            email = itemView.findViewById(R.id.textEmailContatos);
        }
    }
}
