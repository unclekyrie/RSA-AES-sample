package com.ddtechi.rsaexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignatureActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userdataEdit;
    private EditText signedEdit;
    private EditText provedEdit;
    private Button signButton;
    private Button proveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        userdataEdit = (EditText)findViewById(R.id.userdataEdit);
        signedEdit = (EditText)findViewById(R.id.signedEdit);
        provedEdit = (EditText)findViewById(R.id.provedEdit);
        signButton = (Button)findViewById(R.id.signButton);
        proveButton = (Button)findViewById(R.id.proveButton);
        signButton.setOnClickListener(this);
        proveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        String publicKey = intent.getStringExtra("pubkey");
        String privateKey = intent.getStringExtra("prikey");
        if (view == signButton){

            String userStr = userdataEdit.getText().toString();
            if (userStr.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter message",Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] usrData = userStr.getBytes();
            try {
                String
                        sign = RSA.sign(usrData, privateKey);
                signedEdit.setText(sign);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if (view == proveButton){


            String sign = signedEdit.getText().toString();
            if (sign.equals("")){
                Toast.makeText(getApplicationContext(),"Please sign your message",Toast.LENGTH_SHORT).show();
                return;
            }
            String usrStr = userdataEdit.getText().toString();
            if(usrStr.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter message",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                boolean status = RSA.verify(usrStr.getBytes(),publicKey,sign);
                if (status){
                    provedEdit.setText("Verify Sign Success");
                }else {
                    provedEdit.setText("Verify Sign Fail! Message Modified or Not Correct");
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
