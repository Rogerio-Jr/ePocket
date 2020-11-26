package com.example.epocket2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.epocket2.Model.Movimentacao;
import com.example.epocket2.Model.Usuario;
import com.example.epocket2.R;
import com.example.epocket2.config.ConfiguracaoFirebase;
import com.example.epocket2.helper.Base64Custom;
import com.example.epocket2.helper.DateUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EntradaActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double entradaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoDescricao = findViewById(R.id.editDescricao);
        campoCategoria = findViewById(R.id.editCategoria);



        campoData.setText(DateUtil.dataAtual());
        recuperarEnradaTotal();
    }

    public void salvarEntrada(View view){

        if(validarCamposReceita()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado =  Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria( campoCategoria.getText().toString() );
            movimentacao.setDescricao( campoDescricao.getText().toString() );
            movimentacao.setData( data );
            movimentacao.setTipo( "e" );


            Double entradaAtualizada = entradaTotal + valorRecuperado;;

            atualizarEntrada(entradaAtualizada);

            movimentacao.salvar(data);

            finish();
        }

    }

    public Boolean validarCamposReceita(){

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if(!textoValor.isEmpty()){
            if(!textoData.isEmpty()){
                if(!textoCategoria.isEmpty()){
                    if(!textoDescricao.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(EntradaActivity.this,
                                "Descrição não preenchido",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(EntradaActivity.this,
                            "Categoria não preenchido",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(EntradaActivity.this,
                        "Data não preenchido",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(EntradaActivity.this,
                    "Valor não preenchido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarEnradaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuaRefe = firebaseRef.child("usuarios").child(idUsuario);

        usuaRefe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                entradaTotal = usuario.getEntradaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void atualizarEntrada(Double entrada){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        DatabaseReference usuaRefe = firebaseRef.child("usuarios").child(idUsuario);

        usuaRefe.child("entradaTotal").setValue(entrada);
    }
}
