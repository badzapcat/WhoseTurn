package jmoghadam.whoseturn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sync_toolbar);
        setSupportActionBar(toolbar);
    }

    public void syncLater(View view) {
        finish();
    }
}
