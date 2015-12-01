package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.FragmentManager;
import android.content.DialogInterface;

import com.zoranbogdanovski.searchmoviesapp.R;
import com.zoranbogdanovski.searchmoviesapp.util.DialogUtils;
import com.zoranbogdanovski.searchmoviesapp.util.NetworkUtils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isOnline()) {
            DialogUtils.showMessageDialog(MainActivity.this,
                    getString(R.string.no_network_message),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, new MainFragment(), null)
                    .commit();
        }
    }

}
