package unal.edu.co.reto10;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView lvContacts;
    TextView editSearch;
    TextView greaterThan;
    TextView lessThan;
    Spinner country;
    Button btGreater;
    Button btLess;
    Button btCountry;
    ArrayList<Contact> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvContacts = (ListView) findViewById(R.id.lvContacts);

        greaterThan = (TextView) findViewById(R.id.etMayor);
        lessThan = (TextView) findViewById(R.id.etMenor);
        country = (Spinner) findViewById(R.id.spCountry);

        btGreater = (Button) findViewById(R.id.btMayor);
        btLess = (Button) findViewById(R.id.btMenor);
        btCountry = (Button) findViewById(R.id.btCountry);

        btGreater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getData(null, greaterThan.getText().toString(), null);
                final ContactAdapter adapter = new ContactAdapter(getBaseContext(), list);
                lvContacts.setAdapter(adapter);

            }
        });

        btLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getData(null, null, lessThan.getText().toString());
                final ContactAdapter adapter = new ContactAdapter(getBaseContext(), list);
                lvContacts.setAdapter(adapter);

            }
        });

        btCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getData(getCountry(), null, null);
                final ContactAdapter adapter = new ContactAdapter(getBaseContext(), list);
                lvContacts.setAdapter(adapter);

            }
        });

        country.setOnItemSelectedListener(new SelectOnItemSelectedListener());

        list = getData(null, null, null);
        final ContactAdapter adapter = new ContactAdapter(this, list);
        lvContacts.setAdapter(adapter);

    }

    public ArrayList<Contact> getData(String country, String ageGreater, String ageLess){
        String sql = "https://www.datos.gov.co/resource/vzbu-9j7p.json";

        if(country != null) {
            sql += (sql.indexOf('?') >= 0) ? '&' : '?';
            sql += "pa_s=" + country;
        }
        if (ageGreater != null) {
            sql += (sql.indexOf('?') >= 0) ? '&' : '?';
            sql += "$where=edad_a_os>" + ageGreater.trim();
        }
        if (ageLess != null) {
            sql += (sql.indexOf('?') >= 0) ? '&' : '?';
            sql += "$where=edad_a_os<" + ageLess.trim();
        }

        ArrayList<Contact> temp = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);
            String mensaje = "";
            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                temp.add(new Contact(jsonObject.optString("edad_a_os"), jsonObject.optString("pa_s")));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String getCountry() {
        return String.valueOf(country.getSelectedItem()).trim();
    }

 }
