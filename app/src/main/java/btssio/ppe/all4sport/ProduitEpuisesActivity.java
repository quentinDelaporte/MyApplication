package btssio.ppe.all4sport;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static btssio.ppe.all4sport.R.layout.activity_produits_epuises;

public class ProduitEpuisesActivity extends AppCompatActivity {

    private String msgRetour;
    private String entrepot;
    private ArrayAdapter<String> adapter;
    private ListView pdtEpuiseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_produits_epuises);
        pdtEpuiseListView = (ListView) findViewById(R.id.pdtEpuiseListView);
        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            entrepot = extras.getString("location");
            if(entrepot != null){
                try {
                    String str[] = getListeProduit().split(";");
                    List<String> al = new ArrayList<String>();
                    al = Arrays.asList(str);
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (this, android.R.layout.simple_list_item_1, al);
                    pdtEpuiseListView.setAdapter(arrayAdapter);


                    for(String s: al){
                        System.out.println(s);
                    }
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

    public String getListeProduit() throws IOException {
        entrepot = entrepot.replaceAll(" ", "-");
        URL url = new URL("https://quentindelaporte.fr/PPE4_ALL4SPORT/Controller/produitHorStock.php?ville="+entrepot);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        URLConnection request = url.openConnection();
        request.connect();
        String output=rd.readLine();
        return jsonToString(output);
    }

    public String jsonToString(String js){
        String output=js;
        output = output.replaceAll("\\[", " ");
        output = output.replaceAll("]", " ");
        output = output.replaceAll("\",\"", "\"};{\"");
        output = output.replaceAll(",", ";");
        String str[] = output.split(";");
        List<String> al = new ArrayList<String>();
        al = Arrays.asList(str);

        String s = "";

        JsonParser jp = new JsonParser();
        for(String n: al){
            JsonElement root = jp.parse(n);
            JsonObject rootobj = root.getAsJsonObject();
            JsonElement json = rootobj.get("PR_Libelle");
            try{
                s=s+";"+json.getAsString();
            } catch (NullPointerException e){

            }
        }
        return s;
    }


}