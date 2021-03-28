package btssio.ppe.all4sport;

import android.Manifest;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class AcceuilActivity extends AppCompatActivity {

    private Button listePdtEpuiseBtn;
    private Button qrCodeReaderButton;
    private LocationManager locationManager;
    private LocationListener Listener;
    private double lat, lon;
    private String location;
    private TextView texteAcceuil;
    private String idProduit;
    private String name;
    private String msgRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        qrCodeReaderButton =  findViewById(R.id.readQrCode);
        texteAcceuil = (TextView) findViewById(R.id.texteAcceuil);
        listePdtEpuiseBtn = (Button) findViewById(R.id.listePdtEpuiseBtn);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            msgRetour=extras.getString("msgRetour");
            texteAcceuil.setText(msgRetour);
        }

        qrCodeReaderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Création de l'interface de scan.
                IntentIntegrator intentIntegrator = new IntentIntegrator(AcceuilActivity.this);
                intentIntegrator.setPrompt("Encadrez un QR Code avec le viseur pour le scanner");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }
        });

        listePdtEpuiseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchProduitEpuiseActivity();
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getLocation(this);


    }

    //Fonction appelé si un resultat est trouvé lors du scan.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode,resultCode,data
        );

        if(intentResult.getContents() !=null){
            idProduit = intentResult.getContents();

            //Verif que ref pdt existe avec info prdt


            launchActivity();
        } else {
            //Erreur si retour en arriere ou si qr code non trouvé.
            Toast.makeText(getApplicationContext(),
                    "Aucun QR Code scanné.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                verifPerms();
                break;
            default:
                break;
        }
    }

    public void verifPerms(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, Listener);
    }

    private void getLocation(AcceuilActivity contexte) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                lat = loc.getLatitude();
                lon = loc.getLongitude();
                Geocoder gcd = new Geocoder(contexte, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(lat, lon, 1);
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getLocality());
                        location = addresses.get(0).getLocality();
                    } else {
                        // do your stuff
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        verifPerms();
    }

    private void launchActivity() {
        Intent intent = new Intent(this, FormAddActivity.class);
        intent.putExtra("location",location);
        intent.putExtra("idProduit",idProduit);
        intent.putExtra("name", name);

        startActivity(intent);
    }

    private void launchProduitEpuiseActivity() {
        Intent intent = new Intent(this, ProduitEpuisesActivity.class);
        intent.putExtra("location",location);
        intent.putExtra("name", name);

        startActivity(intent);
    }






}