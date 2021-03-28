package btssio.ppe.all4sport;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FormAddActivity extends AppCompatActivity {

    private TextView entrepotTextView;
    private TextView produitIdTextView;
    private String produitId;
    private String entrepot;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        entrepotTextView = (TextView) findViewById(R.id.Entrepot);
        produitIdTextView = (TextView) findViewById(R.id.ProduitId);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            produitId = extras.getString("idProduit");
            entrepot = extras.getString("location");
            entrepotTextView.setText(entrepot);
            produitIdTextView.setText(produitId);

        } else {

        }
    }
}