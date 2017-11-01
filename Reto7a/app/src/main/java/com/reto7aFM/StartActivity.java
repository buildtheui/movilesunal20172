package com.reto7aFM;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class StartActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("sessions");

    Calendar c;

    boolean isCodeGenerated = false;
    int sessionCode = 0;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        pd = new ProgressDialog(StartActivity.this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Generando codigo");
        pd.show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                while (!isCodeGenerated) {

                    Random random = new Random();
                    int n;
                    n = random.nextInt(9000) + 1000;

                    if(dataSnapshot.child(String.valueOf(n)).exists()) {
                        if(dataSnapshot.child(String.valueOf(n)).child("p1").exists()) {
                            c = Calendar.getInstance();
                            if(dataSnapshot.child(String.valueOf(n)).child("start_time")
                                    .getValue(Long.class) - c.getTimeInMillis() >= 86400000) {
                                startSession(n);
                            }
                        } else {
                            startSession(n);
                        }
                    } else {
                        startSession(n);
                    }
                }

                if(isCodeGenerated) {
                    if(dataSnapshot.child(String.valueOf(sessionCode)).child("p2").exists()) {
                        Toast.makeText(getBaseContext(), "Comencemos a jugar!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), GameActivity.class)
                                .putExtra("session_code", String.valueOf(sessionCode))
                                .putExtra("my_player", "X"));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Cancelled", "Failed to read value.", error.toException());
            }
        });

    }

    private void startSession(int n) {
        String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        isCodeGenerated = true;
        sessionCode = n;

        myRef.child(String.valueOf(n)).child("p1").setValue(android_id);

        c = Calendar.getInstance();
        myRef.child(String.valueOf(n)).child("start_time").setValue(c.getTimeInMillis());

        TextView textView = (TextView) findViewById(R.id.code_text);
        textView.setText(String.valueOf(n));

        pd.dismiss();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
