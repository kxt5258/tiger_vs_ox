package com.kinley_tshering.tangbi.tigerox;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);

        //add click listener
        buttonPlay.setOnClickListener(this);

        Button buttonHelp = (Button) findViewById(R.id.btnHelp);

        //add click listener
        buttonHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlay) {
            startActivity(new Intent(this, GameActivity.class));
        }
        else if (v.getId() == R.id.btnHelp) {
            startActivity(new Intent(this, HelpActivity.class));
        }
    }
}
