package btssio.ppe.all4sport;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ProduitEpuisesActivity extends AppCompatActivity {

    private String msgRetour;
    private String entrepot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits_epuises);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            entrepot = extras.getString("location");
            if(entrepot != null){
                try {
                    getListeProduit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            msgRetour="Erreur, veuillez réessayer";
            launchActivity();

        }
    }



    private void launchActivity() {
        Intent intent = new Intent(this, AcceuilActivity.class);
        intent.putExtra("msgRetour", msgRetour);

        startActivity(intent);
    }

    public void getListeProduit() throws IOException {
        entrepot = entrepot.replaceAll(" ", "-");
        URL url = new URL("https://quentindelaporte.fr/PPE4_ALL4SPORT/Controller/produitHorStock.php?ville="+entrepot);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        URLConnection request = url.openConnection();
        request.connect();
        String output;
        while ((output = rd.readLine()) != null) {
            System.out.println(output);
        }
    }

}