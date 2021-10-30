package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jaff.jaffwhats.Activity.ChatActivity;
import com.jaff.jaffwhats.R;
import com.jaff.jaffwhats.config.firebase;

import java.util.ArrayList;

import adapter.ContatosAdapter;
import helper.RecyclerItemClickListener;
import helper.UsuarioFirebase;
import model.Usuario;

/**
 * create an instance of this fragment.
 */
public class Contatosragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private ValueEventListener valueEventListenerContatos;
    private DatabaseReference databaseReference;

    public Contatosragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatosragment, container, false);

        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);

        //COnfigurar adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());
        //Configurar Recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        databaseReference = firebase.getDatabase();
        recyclerViewListaContatos.setAdapter(adapter);

        recyclerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(), recyclerViewListaContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuarioSelecionado = listaContatos.get(position);
                                Intent i = new Intent(getActivity(), ChatActivity.class);
                                i.putExtra("chatContato", usuarioSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recoverUsersList();
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerContatos);
    }

    public void recoverUsersList(){
        valueEventListenerContatos = databaseReference
                .child("users")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    if(!UsuarioFirebase.getEmailUser().equals(usuario.getEmail())){
                        listaContatos.add(usuario);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}