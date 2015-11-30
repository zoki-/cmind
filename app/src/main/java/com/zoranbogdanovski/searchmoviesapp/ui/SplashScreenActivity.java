package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zoranbogdanovski.searchmoviesapp.R;
import com.zoranbogdanovski.searchmoviesapp.core.App;
import com.zoranbogdanovski.searchmoviesapp.model.IResponseModel;
import com.zoranbogdanovski.searchmoviesapp.model.configuration.Configuration;
import com.zoranbogdanovski.searchmoviesapp.services.IParsedResponseListener;
import com.zoranbogdanovski.searchmoviesapp.util.DialogUtils;
import com.zoranbogdanovski.searchmoviesapp.util.NetworkUtils;

/**
 * Splashscreen activity.
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkUtils.isOnline()) {
            // start app after 3sec delay
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, MovieSearchActivity.class));
                }
            }, 3000);
        } else {
            DialogUtils.showMessageDialog(this, getString(R.string.no_network_message),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }
    }
}
