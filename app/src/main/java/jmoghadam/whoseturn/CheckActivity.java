package jmoghadam.whoseturn;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CheckActivity extends AppCompatActivity implements AddMoneyFragment.OnFragmentInteractionListener {

    private static final String INPUT_ERROR_MESSAGE = "Please enter a number amount.";
    private static final String AMOUNT_1_OWES_2 = "amount1Owes2";
    private static final String WHOSE_TURN_MESSAGE = "It's %s's turn to pay!";
    private static final String CONFIRM_MESSAGE = "%s paid $%s";
    private static final String TOTAL_AMOUNT_MESSAGE = "%s owes %s $%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        displayWhoseTurn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_total_amount) {
            toastCurrentAmount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toastCurrentAmount() {
        float amount = getPreferences(Context.MODE_PRIVATE).getFloat(AMOUNT_1_OWES_2, 0);
        if (amount >= 0) {
            Toast.makeText(this, String.format(TOTAL_AMOUNT_MESSAGE, getPartnerName(1),
                            getPartnerName(2), Float.toString(amount)),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, String.format(TOTAL_AMOUNT_MESSAGE, getPartnerName(2),
                            getPartnerName(1), Float.toString(Math.abs(amount))),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void addMoney(View view) {
        String enteredAmount = "";
        EditText editText = (EditText) findViewById(R.id.add_money_input);
        enteredAmount = editText.getText().toString();

        try {
            float amount = Float.parseFloat(enteredAmount);
            DecimalFormat moneyFormat = new DecimalFormat("#.00");
            String stringAmount = moneyFormat.format(amount);
            editText.getText().clear();
            if (view.getId() == R.id.button_add_1) {
                handlePayment(1, stringAmount);
            } else if (view.getId() == R.id.button_add_2) {
                handlePayment(2, stringAmount);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, INPUT_ERROR_MESSAGE,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handlePayment(int partnerNumber, String moneyAmount) {
        float amount = Float.parseFloat(moneyAmount);
        savePayment(partnerNumber, amount);
        Toast.makeText(this, String.format(CONFIRM_MESSAGE, getPartnerName(partnerNumber),
                        moneyAmount), Toast.LENGTH_LONG).show();
        displayWhoseTurn();
    }

    private void displayWhoseTurn() {
        TextView whoseTurnText = (TextView) findViewById(R.id.whose_turn_state);
        whoseTurnText.setText(String.format(WHOSE_TURN_MESSAGE,
                whoseTurnToPay()));
    }

    private void savePayment(int partnerNumber, float amount) {
        SharedPreferences checkActivityPreferences = getPreferences(Context.MODE_PRIVATE);
        float amount1Owes2 = checkActivityPreferences.getFloat(AMOUNT_1_OWES_2, 0);
        if (partnerNumber == 1) {
            amount1Owes2 -= amount;
        } else if (partnerNumber == 2) {
            amount1Owes2 += amount;
        }
        SharedPreferences.Editor editor = checkActivityPreferences.edit();
        editor.putFloat(AMOUNT_1_OWES_2, amount1Owes2);
        editor.commit();
    }

    private String getPartnerName(int partnerNumber) {
        if (partnerNumber == 1) {
            return "Tomomi";
        } else if (partnerNumber == 2) {
            return "Joey";
        } else {
            return "Nico (?!)";
        }
    }

    /**
     *
     * @return The name of the person who should pay.
     */
    private String whoseTurnToPay() {
        SharedPreferences checkActivityPreferences = getPreferences(Context.MODE_PRIVATE);
        float amount1Owes2 = checkActivityPreferences.getFloat(AMOUNT_1_OWES_2, 0);
        if (amount1Owes2 > 0) {
            return getPartnerName(1);
        } else {
            return getPartnerName(2);
        }
    }
}