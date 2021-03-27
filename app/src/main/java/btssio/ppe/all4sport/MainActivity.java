package btssio.ppe.all4sport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText identifiantEditText;
    private TextView textErreur;
    private Button connecterButton;
    private LocationManager locationManager;
    private LocationListener Listener;
    private double lat, lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //Récuperation des boutons & des champs de saisie
        passwordEditText = (EditText) findViewById(R.id.passUser);
        identifiantEditText = (EditText) findViewById(R.id.idUser);
        connecterButton = (Button) findViewById(R.id.Connection);
        textErreur = (TextView) findViewById(R.id.Erreur);
        //Fonction du bouton se connecter.
        connecterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordEditText.getText().toString().equals("") && !identifiantEditText.getText().toString().equals("")){
                    connecter(identifiantEditText.getText().toString(),passwordEditText.getText().toString());
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("\n" +location.getLatitude()+" "+location.getLongitude());
                lat=location.getLatitude();
                lon=location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configureButton();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configureButton();
                break;
            default:
                break;
        }
    }

    public void configureButton(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, Listener);
    }

    private boolean connecter(String identifiant, String password) {
        try {
            /**
             * Pour utiliser en local:
             * URL url = new URL("http://IPLocal:PORT/PPE4_ALL4SPORT/controller/index.php?loginInfo={\"login\":\""+identifiant+"\",\"password\":\""+password+"\"}");
             */
            //Appel au web service avec un json contenant l'identifiant et le mot de passe.
            URL url = new URL("https://quentindelaporte.fr/PPE4_ALL4SPORT/Controller/index.php?loginInfo={\"login\":\""+identifiant+"\",\"password\":\""+password+"\"}");
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            URLConnection request = url.openConnection();
            request.connect();

            //Lecture du JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootobj = root.getAsJsonObject();
            String canConnect = rootobj.get("connexionValide").getAsString();

            //Si le webservice retourne 1 alors il peut se connecter
            if(Boolean.parseBoolean(canConnect)){
                launchActivity();
            } else {
                textErreur.setText("Ce couple identifiant / Mot de passe n'est pas valide.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void launchActivity() {
        //Changement d'activité -> Affichage du menu Main.
        Intent intent = new Intent(this, AcceuilActivity.class);
        startActivity(intent);
    }

}