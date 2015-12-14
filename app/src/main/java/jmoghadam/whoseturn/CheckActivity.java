package jmoghadam.whoseturn;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CheckActivity extends AppCompatActivity implements AddMoneyFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void addMoney(View view) {
        String enteredAmount = "";
        EditText editText = (EditText) findViewById(R.id.add_money_input);
        enteredAmount = editText.getText().toString();

        try {
            double amount = Double.parseDouble(enteredAmount);
            DecimalFormat moneyFormat = new DecimalFormat("#.00");
            String stringAmount = moneyFormat.format(amount);
            if (view.getId() == R.id.button_add_1) {
                Toast.makeText(this, "Tomomi paid $" + stringAmount + "!",
                        Toast.LENGTH_LONG).show();
            } else if (view.getId() == R.id.button_add_2) {
                Toast.makeText(this, "Joey paid $" + stringAmount + "!",
                        Toast.LENGTH_LONG).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a number amount.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
