package btssio.ppe.all4sport;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FormAddActivity extends AppCompatActivity {
    private TextView entrepotTextView;
    private TextView produitIdTextView;
    private Button modifierButton;
    private EditText editTextNumber;
    private String produitId;
    private String entrepot;
    private String name;
    private String msgRetour;
    private String quantite;

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
        modifierButton = (Button) findViewById(R.id.Modifier);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            produitId = extras.getString("idProduit");
            entrepot = extras.getString("location");
            entrepotTextView.setText(entrepot);
            produitIdTextView.setText(produitId);
            if(entrepot==null) {
                msgRetour = "Nous ne sommes pas parvenu à vous localiser, veuillez réessayer.";
                launchActivity();
            }
        } else {
            msgRetour="Erreur, veuillez réessayer";
            launchActivity();
        }


        modifierButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    System.out.println(entrepot);
                    update();
                    msgRetour="Le produit as été ajouté avec succés.";
                } catch (IOException e) {
                    e.printStackTrace();
                    msgRetour="Erreur lors de l'insertion du produit veuillez réessayer.";

                }
                launchActivity();
            }
        });
    }

    private void update() throws IOException {
        quantite = editTextNumber.getText().toString();

        System.out.println(quantite);
        System.out.println(entrepot);
        System.out.println(produitId);
        entrepot = entrepot.replace(' ', '-');
        System.out.println(entrepot);

        URL url = new URL("https://quentindelaporte.fr/PPE4_ALL4SPORT/Controller/modifierQuantite.php?quantite="+quantite+"&ville="+entrepot+"&id="+produitId);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        URLConnection request = url.openConnection();
        request.connect();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, AcceuilActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("msgRetour", msgRetour);

        startActivity(intent);
    }

}