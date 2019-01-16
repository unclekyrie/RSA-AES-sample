package com.ddtechi.rsaexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ddtechi.rsaexample.utils.AES256Cipher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userdataEdit;
    private EditText encryptedEdit;
    private EditText decryptedEdit;
    private Button encryptButton;
    private Button decryptButton;
    byte[] encodeData = null;

    byte[] encryptedAESKey = null;
    byte[] encryptedAESIv = null;
    byte[] decryptedAESKey = null;
    byte[] decryptedAESIv = null;

    String userStr;
    String testSrt;

    JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);

        userdataEdit = (EditText)findViewById(R.id.userdataEdit);
        encryptedEdit = (EditText)findViewById(R.id.encryptedEdit);
        decryptedEdit = (EditText)findViewById(R.id.decryptedEdit);
        encryptButton = (Button)findViewById(R.id.encryptButton);
        decryptButton = (Button)findViewById(R.id.decryptButton);
        encryptButton.setOnClickListener(this);
        decryptButton.setOnClickListener(this);
        testSrt = "test";
    }

    @Override
    public void onClick(View view) {

        Intent intent = getIntent();
        String publicKey = intent.getStringExtra("pubkey");
        String privateKey = intent.getStringExtra("prikey");
        if (view == encryptButton){
            int joke = 0;
            userStr = userdataEdit.getText().toString();

            if(userStr.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter message",Toast.LENGTH_SHORT).show();
                return;
            }

            //start AES encrypt.
            byte[] aesCryptKey = AES256Cipher.getRandomAesCryptKey();
            byte[] aesCryptIv = AES256Cipher.getRandomAesCryptIv();
//            userdataEdit.append("AESKey: " + new String(aesCryptKey) + "\n");
//            userdataEdit.append("AESKey: " + new String(aesCryptIv) + "\n");
            try {
                userStr = AES256Cipher.encrypt(aesCryptKey, aesCryptIv, userStr);//평문 데이터 AES로 암호화.
                encryptedEdit.append("EncryptedData: " + userStr + "\n");
                Log.i("srt", userStr);

                testSrt = AES256Cipher.encrypt(aesCryptKey, aesCryptIv, testSrt);
                jsonObject.put("data", testSrt);
                Log.i("암호화 제이슨 데이터", jsonObject.getString("data"));
                String dts = AES256Cipher.decrypt(aesCryptKey, aesCryptIv,jsonObject.getString("data"));
                Log.i("복호화 제이슨 데이터", dts);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //AES키 암호화
            try {
                encryptedAESKey = RSA.encryptByPublicKey(aesCryptKey, publicKey);
//                encryptedEdit.append("EncryptedAESKey: " + new String(encryptedAESKey) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //AES Iv 암호화
            try {
                encryptedAESIv = RSA.encryptByPublicKey(aesCryptIv, publicKey);
//                encryptedEdit.append("EncryptedAESIv: " + new String(encryptedAESIv) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //end AES encrypt.



//            byte[] userData = userStr.getBytes();
//            try {
//                encodeData = RSA.encryptByPublicKey(userData, publicKey);
//                String encodeStr = new BigInteger(1, encodeData).toString(16);
//                encryptedEdit.setText(encodeStr);
//            }catch (Exception e){
//                e.printStackTrace();
//            }

        }else if (view == decryptButton){

//            if(encodeData == null){
//                Toast.makeText(getApplicationContext(),"Please use public key to encrypt",Toast.LENGTH_SHORT).show();
//                return;
//            }

            //AES키 복호화
            try {
                decryptedAESKey = RSA.decryptByPrivateKey(encryptedAESKey, privateKey);
//                Log.i("DecryptedAESKey", new String(decryptedAESKey));
//                decryptedEdit.append("DecryptedAESKey: " + new String(decryptedAESKey) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //AES Iv 복호화
            try {
                decryptedAESIv = RSA.decryptByPrivateKey(encryptedAESIv, privateKey);
//                Log.i("DecryptedAESIv", new String(decryptedAESIv));
//                decryptedEdit.append("DecryptedAESIv: " + new String(decryptedAESIv) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //복호화가 완료된 AES를 이용하여 데이터 복호화
            try {
                String resultStr = AES256Cipher.decrypt(decryptedAESKey, decryptedAESIv, userStr);
//                decryptedEdit.setText(resultStr);
                String resultTestStr = AES256Cipher.decrypt(decryptedAESKey, decryptedAESIv, jsonObject.getString("data"));
                decryptedEdit.setText(resultStr + "\n" + resultTestStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            try {
//                byte[] decodeData = RSA.decryptByPrivateKey(encodeData,privateKey);
//                String decodeStr = new String(decodeData);
//                decryptedEdit.setText(decodeStr);
//            }catch (Exception e){
//                e.printStackTrace();
//            }

        }
    }
}
