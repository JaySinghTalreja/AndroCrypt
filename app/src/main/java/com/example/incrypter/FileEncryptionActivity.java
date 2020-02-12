package com.example.incrypter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptionActivity extends AppCompatActivity {

    private TextView varSelectFile;
    private Button varSelectFileButton;
    private EditText varPassword;
    private Button varEncryptButton;
    private Button varDecryptButton;
    private String varFilePath = "";
    //INTENT
    Intent getFileIntent;

    String[] appPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int REQUEST_PERMISSION_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_encryption);


        varSelectFile = (TextView) findViewById(R.id.selectfile);
        varSelectFileButton = (Button)findViewById(R.id.selectbutton);
        varPassword = (EditText) findViewById(R.id.Userpassword);
        varEncryptButton = (Button) findViewById(R.id.doEncrypt);
        varDecryptButton = (Button) findViewById(R.id.doDecrypt);

        varSelectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getFileIntent.setType("*/*");
                startActivityForResult(getFileIntent, 10);

            }
        });

        varEncryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arePermissionsEnabled()){
                    performEncode(varFilePath, varPassword.getText().toString());
                }
            }
        });

        varDecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arePermissionsEnabled()) {
                    performDecode(varFilePath, varPassword.getText().toString());
                }
            }
        });
    }

    public boolean arePermissionsEnabled() {
        List<String> listPermissionRequired = new ArrayList<>();
        for(String permission : appPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                listPermissionRequired.add(permission);
            }
        }

        if(!listPermissionRequired.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionRequired.toArray(new String[listPermissionRequired.size()]),
                    REQUEST_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    private void performEncode(String varPath, String varPass) {
        try{
            if(varPath.isEmpty() || varPass.isEmpty()) {
                InvalidDataEncryptNotifcation("Data Validation Error");
            }
            else {
                File dFile = new File(varPath);
                FileInputStream fis = new FileInputStream(dFile);
                File kFile = new File(varPath.concat(".incrypt"));
                FileOutputStream fos = new FileOutputStream(kFile);

                byte[] key = ("JAYSINGHTALREJA" + varPass).getBytes("UTF-8");

                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
                key = Arrays.copyOf(key,16);

                SecretKeySpec sks = new SecretKeySpec(key, "AES");

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                int b;
                byte[] d = new byte[8];

                while((b = fis.read(d)) != -1) {
                    cos.write(d, 0, b);
                }
                cos.flush();
                cos.close();
                fis.close();
                InvalidDataEncryptNotifcation("Encrypted");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void performDecode(String varPath, String varPass) {
        try{
            if(varPath.isEmpty() || varPass.isEmpty()) {
                InvalidDataEncryptNotifcation("Data Validation Error");
            }
            else{
                FileInputStream fis = new FileInputStream(varPath);
                FileOutputStream fos = new FileOutputStream(varPath.replace(".incrypt",""));
                byte[] key = ("JAYSINGHTALREJA" + varPass).getBytes("UTF-8");
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
                key = Arrays.copyOf(key,16);
                SecretKeySpec sks = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, sks);
                CipherInputStream cis = new CipherInputStream(fis, cipher);
                int b;
                byte[] d = new byte[8];
                while((b = cis.read(d)) != -1) {
                    fos.write(d, 0, b);
                }
                fos.flush();
                fos.close();
                cis.close();
                InvalidDataEncryptNotifcation("Decrypted");
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void InvalidDataEncryptNotifcation(String clipboardnotification) {
        Context context = getApplicationContext();
        CharSequence text = clipboardnotification;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 10:
                if(requestCode == 10) {

                    /*Uri varData = data.getData();
                    File varFile = new File(varData.getPath());
                    final String[] split = varFile.getPath().split(":");//split the path.
                    varFilePath = split[1];//assign it to a string(your choice).
                    */
                    //varFilePath = varFile.getAbsolutePath();
                    //varSelectFile.setText(varFilePath);
                    /*String dir= Environment.getExternalStorageDirectory().getAbsolutePath();
                    varFilePath = dir.substring(dir.indexOf(':') + 1);
                    varSelectFile.setText(varFilePath);
                    varFilePath = data.getData().getPath();
                    String filename=varFilePath.substring(varFilePath.lastIndexOf("/")+1);
                    varSelectFile.setText(varFilePath);
                    */
                    File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
                    String varDir = dir.toString() + '/';
                    varFilePath = data.getData().getPath();
                    String filename=varFilePath.substring(varFilePath.indexOf(':') + 1);
                    varFilePath = varDir.concat(filename);
                    varSelectFile.setText(varFilePath);
                }
        }
    }
}
