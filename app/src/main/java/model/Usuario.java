package model;

import android.net.Uri;

import java.io.Serializable;
/**
 * Created by Jhoel Fiorese
 **/
public class Usuario implements Serializable {
    private String uid;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public Usuario() {
    }

    public String getuId() {
        return uid;
    }

    public void setuId(String uId) {
        this.uid = uId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

