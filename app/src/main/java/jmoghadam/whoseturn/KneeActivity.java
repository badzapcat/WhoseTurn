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
import java.util.Random;

/**
 * The main activity of the app. In this activity you can view whose turn it is to pay, have one
 * person make a payment, and check the total debt owed.
 */
public class KneeActivity extends AppCompatActivity implements ChangeKneeSlapFragment.OnFragmentInteractionListener{

    private static final String AMOUNT_KNEE_SLAPS = "amountKneeSlaps";
    private static final String SINGULAR_KNEE_SLAP_MESSAGE = "%s owes %s 1 knee slap!";
    private static final String PLURAL_KNEE_SLAP_MESSAGE = "%s owes %s %s knee slaps!";

    private static final String ADD_MESSAGE = "Good job %s!";
    private static final String[] USE_MESSAGES = {"Such style!", "Such grace!", "Such pizzazz!",
            "Such gusto!"};
    private static final String PARTNER_1 = "Tomomi";
    private static final String PARTNER_2 = "Joey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knee);
        displayKneeSlaps();
    }

    /**
     * Callback for when add knee slap button is pressed.
     * @param view The button that was pressed.
     */
    public void addKneeSlap(View view) {
        changeKneeSlaps(1);
        Toast.makeText(this, String.format(ADD_MESSAGE, PARTNER_1), Toast.LENGTH_SHORT).show();
        displayKneeSlaps();
    }


    /**
     * Callback for when use knee slap button is pressed.
     * @param view The button that was pressed.
     */
    public void useKneeSlap(View view) {
        changeKneeSlaps(-1);
        String useMessage = USE_MESSAGES[new Random().nextInt(USE_MESSAGES.length)];
        Toast.makeText(this, useMessage, Toast.LENGTH_SHORT).show();
        displayKneeSlaps();
    }

    /**
     * Display on the screen how many knee slaps are owed.
     */
    private void displayKneeSlaps() {
        TextView kneeSlaps = (TextView) findViewById(R.id.knee_slap_state);
        int owedSlaps = owedSlaps();
        if (owedSlaps == 1){
            kneeSlaps.setText(String.format(SINGULAR_KNEE_SLAP_MESSAGE, PARTNER_2, PARTNER_1));
        } else {
            kneeSlaps.setText(String.format(PLURAL_KNEE_SLAP_MESSAGE, PARTNER_2, PARTNER_1,
                    owedSlaps));
        }
    }

    /**
     * Commit a change in knee slaps.
     * @param amount The amount to change by. Positive for owing more knee slaps, negative for owing
     *               fewer.
     */
    private void changeKneeSlaps(int amount) {
        SharedPreferences checkActivityPreferences = getPreferences(Context.MODE_PRIVATE);
        int amountKneeSlaps = checkActivityPreferences.getInt(AMOUNT_KNEE_SLAPS, 0);
        SharedPreferences.Editor editor = checkActivityPreferences.edit();
        editor.putInt(AMOUNT_KNEE_SLAPS, amountKneeSlaps + amount);
        editor.commit();
    }

    /**
     * @return The number of knee slaps partner 2 owes partner 1.
     */
    private int owedSlaps() {
        SharedPreferences checkActivityPreferences = getPreferences(Context.MODE_PRIVATE);
        return checkActivityPreferences.getInt(AMOUNT_KNEE_SLAPS, 0);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Not implemented
    }
}