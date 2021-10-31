package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaff.jaffwhats.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Conversa;
import model.Usuario;

public class ConversasAdapter extends RecyclerView.Adapter <ConversasAdapter.MyViewHolder>  {
    private List<Conversa> conversas;
    private Context context;
    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_contatos, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Conversa conversa = conversas.get(i);
        myViewHolder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        Usuario usuario= conversa.getUsuarioExibicao();
        myViewHolder.nome.setText((usuario.getNome()));
        if(usuario.getFoto() != null){
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(myViewHolder.foto);
        }else
            myViewHolder.foto.setImageResource(R.drawable.padrao);
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView foto;
        TextView nome, ultimaMensagem;

        public MyViewHolder(View view){
            super(view);

            foto = view.findViewById(R.id.imageViewFotoContato);
            nome = view.findViewById(R.id.textNomeContato);
            ultimaMensagem = view.findViewById(R.id.textContatos);
        }
    }
}
