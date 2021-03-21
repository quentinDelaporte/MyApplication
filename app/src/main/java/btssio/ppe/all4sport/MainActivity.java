package btssio.ppe.all4sport;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText identifiantEditText;

    private Button connecterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        passwordEditText = (EditText) findViewById(R.id.passUser);
        identifiantEditText = (EditText) findViewById(R.id.idUser);
        connecterButton = (Button) findViewById(R.id.Connection);


        connecterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordEditText.getText().toString().equals("") && !identifiantEditText.getText().toString().equals("")){

                    connecter(identifiantEditText.getText().toString(),passwordEditText.getText().toString());

                } else {

                }

            }
        });
    }

    private boolean connecter(String identifiant, String password) {
        try {

            //URL url = new URL("https://10.0.2.2:80/PPE4_ALL4SPORT/controller/index.php?loginInfo="+obj);
            //URL url = new URL("http://192.168.1.28:80/PPE4_ALL4SPORT/controller/index.php?loginInfo="+obj);
            URL url = new URL("https://quentindelaporte.fr/PPE4_ALL4SPORT/Controller/index.php?loginInfo={\"login\":\""+identifiant+"\",\"password\":\""+password+"\"}");
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            JsonObject rootobj = root.getAsJsonObject();
            String canConnect = rootobj.get("connexionValide").getAsString();
            System.out.println(canConnect);

            if(Boolean.parseBoolean(canConnect)){
                launchActivity();
            }



            /**String line;
            line = rd.readLine();
            if (line != null) {
                System.out.println(line);

                return true;

            }
            rd.close();**/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void launchActivity() {

        Intent intent = new Intent(this, AcceuilActivity.class);
        startActivity(intent);
    }

}