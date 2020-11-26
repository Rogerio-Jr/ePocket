package com.example.epocket2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.epocket2.R;
import com.example.epocket2.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());

    }

    @Override
    protected void onStart() {
        verificarUsuarioLogado();
        super.onStart();

    }

    public void btEntrar(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
            }

    public void btCadastrar(View view){
        Intent intent = new Intent(this, TelaCadastroActivity.class);
        startActivity(intent);
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(MainActivity.this, Splashctivity2.class);
        startActivity(intent);

    }


}
