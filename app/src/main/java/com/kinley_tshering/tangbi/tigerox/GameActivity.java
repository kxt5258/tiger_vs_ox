package com.kinley_tshering.tangbi.tigerox;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;


public class GameActivity extends AppCompatActivity {

    GameBoard board;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        board = (GameBoard) findViewById(R.id.gameboard);
        layout = (LinearLayout) findViewById(R.id.frame) ;

        layout.getLayoutParams().height = (int) Math.round(Resources.getSystem().getDisplayMetrics().heightPixels * 0.15);
        board.getLayoutParams().height = (int) Math.round(Resources.getSystem().getDisplayMetrics().heightPixels * 0.85);

    }

    /**
     * Method to listen to the button clicks
     * @param v view which triggered the event (button click)
     */
    public void buttonClick(View v) {
        if (v.getId() == R.id.restartBtn) {
            Activity activity = GameActivity.this;
            restartGame(activity);
        }
    }

    /**
     * Restart the game activity
     * @param _activity activity
     */
    private void restartGame(Activity _activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            _activity.recreate();
        }
        else {
            _activity.finish();
            _activity.startActivity(_activity.getIntent());
        }
        _activity.overridePendingTransition(0,0);
    }

}
