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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AcceuilActivity extends AppCompatActivity {

    private Button qrCodeReaderButton;
    private LocationManager locationManager;
    private LocationListener Listener;
    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        qrCodeReaderButton =  findViewById(R.id.readQrCode);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AcceuilActivity.this
            );
            builder.setTitle("Resultats");
            //Affichage du texte du QR code
            builder.setMessage(intentResult.getContents());

            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
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

    private void getLocation(AcceuilActivity contexte) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("\n" + location.getLatitude() + " " + location.getLongitude());
                lat = location.getLatitude();
                lon = location.getLongitude();
                Geocoder gcd = new Geocoder(contexte, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(lat, lon, 1);
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getLocality());
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
        configureButton();
    }


}