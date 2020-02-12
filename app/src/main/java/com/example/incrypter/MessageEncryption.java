package com.example.incrypter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MessageEncryption extends AppCompatActivity {

    private EditText inputMessage, inputPassword;
    private Button EncryptButton, DecryptButton;
    private TextView encryptedMessageView;
    private String OutputString;
    private String AES = "AES";
    public BottomNavigationView bottomNavigationView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MenuItem homeItem = bottomNavigationView.getMenu().getItem(0);
        bottomNavigationView.setSelectedItemId(homeItem.getItemId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_encryption);

        inputMessage = (EditText) findViewById(R.id.EnterMessage);
        inputPassword = (EditText) findViewById(R.id.passphrase);
        encryptedMessageView = (TextView) findViewById(R.id.encryptedmessageview);
        EncryptButton = (Button) findViewById(R.id.encryptbutton);
        DecryptButton = (Button) findViewById(R.id.decryptbutton);

        EncryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    OutputString = Encrypt(inputMessage.getText().toString(), inputPassword.getText().toString());
                    encryptedMessageView.setText(OutputString);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied to Clipboard", OutputString);
                    clipboard.setPrimaryClip(clip);
                    ToastClipboardNotifcation("Copied to Clipboard");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        DecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    OutputString = Decrypt(OutputString, inputPassword.getText().toString());
                    encryptedMessageView.setText(OutputString);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.message);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.file:
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        //overridePendingTransition(0, 0);
                        Intent a = new Intent(getApplicationContext(),MainActivity.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        return true;
                    case R.id.message:
                        return true;
                }
                return false;
            }
        });
    }

    private void ToastClipboardNotifcation(String clipboardnotification) {
        Context context = getApplicationContext();
        CharSequence text = clipboardnotification;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private String Decrypt(String outputString, String Password) throws Exception{
        SecretKeySpec key = generateKey(Password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString,Base64.DEFAULT);
        byte[] decVal = c.doFinal(decodedValue);
        String decryptedValue = new String(decVal);
        return decryptedValue;
    }
    private String Encrypt(String Message, String Password) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException,BadPaddingException, UnsupportedEncodingException {
        SecretKeySpec key = generateKey(Password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Message.getBytes());
        String EncryptedKey = Base64.encodeToString(encVal, Base64.DEFAULT);
        return EncryptedKey;
    }

    private SecretKeySpec generateKey(String Password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes= Password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;


    }
}
