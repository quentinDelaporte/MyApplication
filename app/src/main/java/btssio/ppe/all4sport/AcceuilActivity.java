package btssio.ppe.all4sport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AcceuilActivity extends AppCompatActivity {

    private Button qrCodeReaderButton;

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

}