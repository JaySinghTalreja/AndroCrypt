package com.example.incrypter;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;

        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;

        import java.io.File;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton floatingActionButton;
    private BottomNavigationView bottomNavigationView;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileEncryptionActivity.class);
                startActivity(intent);
            }
        });

        //Custom App Bar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.file);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.file:
                        return true;
                    case R.id.message:
                        Intent a = new Intent(getApplicationContext(),MessageEncryption.class);
                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        //startActivity(new Intent(getApplicationContext(), MessageEncryption.class));
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }


    //For ActionBar's Overflow Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuactionbar, menu);
        //U can find item set icon and stuff...
        //MenuItem item= menu.findItem(R.id.action_search);
        return true;
    }
    //For Performing Actions on Overflow Menu | Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sign_out:
                finish();
                break;
            case R.id.about:
                Intent a = new Intent(getApplicationContext(),About.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
