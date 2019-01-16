package com.ddtechi.rsaexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText publickeyEdit;
    private EditText privatekeyEdit;
    private Button initkeyButton;
    private Button btnEncrypt;
    private Button btnSig;
    private static String publicKey = "";
    private static String privateKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        publickeyEdit = (EditText)findViewById(R.id.publickeyEdit);
        privatekeyEdit = (EditText)findViewById(R.id.privatekeyEdit);
        initkeyButton = (Button)findViewById(R.id.initkeyButton);
        btnEncrypt = (Button)findViewById(R.id.btnEncrypt);
        btnSig = (Button)findViewById(R.id.btnSig);

        btnEncrypt.setOnClickListener(this);
        btnSig.setOnClickListener(this);
        initkeyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == initkeyButton){
            try {
                Map<String, Object> keyMap = RSA.initKey();
                publicKey = RSA.getPublicKey(keyMap);
                privateKey = RSA.getPrivateKey(keyMap);
                publickeyEdit.setText(publicKey);
                privatekeyEdit.setText(privateKey);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(view == btnEncrypt){
            if(publicKey==null||privateKey==null){
                Toast.makeText(getApplicationContext(),"Please generate public and private key",Toast.LENGTH_LONG);
            }
            else{
                Intent intent = new Intent(this,EncryptionActivity.class);
                intent.putExtra("pubkey",publicKey);
                intent.putExtra("prikey",privateKey);
                startActivity(intent);
            }


        }else if(view == btnSig){
            if(publicKey==null||privateKey==null){
                Toast.makeText(getApplicationContext(),"Please generate public and private key",Toast.LENGTH_LONG);
            }
            else {
                Intent intent = new Intent(this, SignatureActivity.class);
                intent.putExtra("pubkey", publicKey);
                intent.putExtra("prikey", privateKey);
                startActivity(intent);
            }
        }

    }



}
