package com.example.incrypter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Setting Back Button in Custom Action Bar On File Action Activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutactionbar);
        setSupportActionBar(toolbar);
        //Set Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //No Title On Action Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //Explicit Back Button Task on About Section
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
