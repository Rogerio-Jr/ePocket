package com.example.epocket2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.epocket2.Model.Usuario;
import com.example.epocket2.R;
import com.example.epocket2.config.ConfiguracaoFirebase;
import com.example.epocket2.helper.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class TelaCadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        getSupportActionBar().setTitle("Cadastro");

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();

                //validar se os campos foram preenchidos

                if( !textoNome.isEmpty() ){
                    if ( !textEmail.isEmpty()){
                        if ( !textSenha.isEmpty() ){

                            usuario = new Usuario();
                            usuario.setNome( textoNome );
                            usuario.setEmail( textEmail );
                            usuario.setSenha( textSenha );
                            cadastrarUsuario();

                        }else{
                            Toast.makeText(TelaCadastroActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(TelaCadastroActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(TelaCadastroActivity.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
            usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(TelaCadastroActivity.this, "Carregando...", Toast.LENGTH_SHORT).show();
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario( idUsuario);
                    usuario.salvar();
                    Intent intent = new Intent(TelaCadastroActivity.this, Splashctivity2.class);
                    startActivity(intent);
                    finish();

                }else {
                        String excessao ="";
                    try {
                        throw  task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                            excessao = "Digite uma senha mais forte, plis!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                            excessao = "Por favor, digite um email valido!";
                    }catch (FirebaseAuthUserCollisionException e){
                            excessao = "Essa conta já foi cadastrada!";
                    }catch (Exception e){
                        excessao = "Erro ao cadastrar o usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(TelaCadastroActivity.this, excessao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
