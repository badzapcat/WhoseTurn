package jmoghadam.whoseturn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NfcAdapter;
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

/**
 * The main activity of the app. In this activity you can view whose turn it is to pay, have one
 * person make a payment, and check the total debt owed.
 */
public class CheckActivity extends AppCompatActivity implements AddMoneyFragment.OnFragmentInteractionListener {

    private static final String INPUT_ERROR_MESSAGE = "Please enter a number amount.";
    private static final String AMOUNT_1_OWES_2 = "amount1Owes2";
    private static final String DELTA_SINCE_SYNC = "deltaSinceSync";
    private static final String DELTA_MESSAGE = "Debt has changed by $%s since the last sync.";
    private static final String WHOSE_TURN_MESSAGE = "It's %s's turn to pay!";
    private static final String CONFIRM_MESSAGE = "%s paid $%s";
    private static final String TOTAL_AMOUNT_MESSAGE = "%s owes %s $%s";
    private static final String PARTNER_1 = "Tomomi";
    private static final String PARTNER_2 = "Joey";
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#.00");

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
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Toast.makeText(this, "received nfc!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initiate callbacks for pressing different action items.
     * @param item The item that was pressed.
     * @return true if item is processed here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_total_amount) {
            toastCurrentAmount();
            return true;
        } else if (id == R.id.action_sync) {
            Intent intent = new Intent(this, SyncActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delta_since_sync) {
            toastDelta();
            return true;
        } else if (id == R.id.action_knee_slaps) {
            Intent intent = new Intent(this, KneeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Pop a toast displaying the current debt.
     */
    private void toastCurrentAmount() {
        float amount = getPreferences(Context.MODE_PRIVATE).getFloat(AMOUNT_1_OWES_2, 0);
        if (amount >= 0) {
            Toast.makeText(this, String.format(TOTAL_AMOUNT_MESSAGE, getPartnerName(1),
                            getPartnerName(2), MONEY_FORMAT.format(amount)),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, String.format(TOTAL_AMOUNT_MESSAGE, getPartnerName(2),
                            getPartnerName(1), MONEY_FORMAT.format(Math.abs(amount))),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Pop a toast displaying change in the current debt since the last sync.
     */
    private void toastDelta() {
        float delta = getPreferences(Context.MODE_PRIVATE).getFloat(DELTA_SINCE_SYNC, 0);
            Toast.makeText(this, String.format(DELTA_MESSAGE, MONEY_FORMAT.format(Math.abs(delta))),
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Unused
    }

    /**
     * Callback for when a payment button is pressed. Validates the entered information, then
     * initiates logic for the payment.
     * @param view The button that was pressed.
     */
    public void addMoney(View view) {
        String enteredAmount = "";
        EditText editText = (EditText) findViewById(R.id.add_money_input);
        enteredAmount = editText.getText().toString();

        try {
            float amount = Float.parseFloat(enteredAmount);
            String stringAmount = MONEY_FORMAT.format(amount);
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

    /**
     * Handle all logic once a payment occurs. Update the current debt,
     * pop a toast indicating success, and update the message deciding whose turn is next.
     * @param partnerNumber The partner making a payment.
     * @param moneyAmount The amount by which the partner is paying.
     */
    private void handlePayment(int partnerNumber, String moneyAmount) {
        float amount = Float.parseFloat(moneyAmount);
        savePayment(partnerNumber, amount);
        Toast.makeText(this, String.format(CONFIRM_MESSAGE, getPartnerName(partnerNumber),
                        moneyAmount), Toast.LENGTH_LONG).show();
        displayWhoseTurn();
    }

    /**
     * Display on the screen whose should pay next given current debts.
     */
    private void displayWhoseTurn() {
        TextView whoseTurnText = (TextView) findViewById(R.id.whose_turn_state);
        whoseTurnText.setText(String.format(WHOSE_TURN_MESSAGE,
                whoseTurnToPay()));
    }

    /**
     * Commit a payment by a partner, which updates the current debt. This is NOT called during a
     * sync, so it also adds the amount to the delta since the last sync.
     * @param partnerNumber The number of the partner making the payment.
     * @param amount The amount by which the partner is paying.
     */
    private void savePayment(int partnerNumber, float amount) {
        SharedPreferences checkActivityPreferences = getPreferences(Context.MODE_PRIVATE);
        float amount1Owes2 = checkActivityPreferences.getFloat(AMOUNT_1_OWES_2, 0);
        float delta = checkActivityPreferences.getFloat(DELTA_SINCE_SYNC, 0);
        if (partnerNumber == 1) {
            amount *= -1;
        }
        amount1Owes2 += amount;
        delta += amount;
        SharedPreferences.Editor editor = checkActivityPreferences.edit();
        editor.putFloat(AMOUNT_1_OWES_2, amount1Owes2);
        editor.putFloat(DELTA_SINCE_SYNC, delta);
        editor.commit();
    }

    /**
     * @param partnerNumber A number either 1 or 2 specifying which partner.
     * @return The name of the partner corresponding to partnerNumber.
     */
    private String getPartnerName(int partnerNumber) {
        if (partnerNumber == 1) {
            return PARTNER_1;
        } else if (partnerNumber == 2) {
            return PARTNER_2;
        } else {
            throw new IllegalArgumentException("Partner " + partnerNumber + " does not exist.");
        }
    }

    /**
     * @return The name of the partner who should pay given current debts. In a tie, partner 2 pays.
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