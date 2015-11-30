package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.zoranbogdanovski.searchmoviesapp.R;
import com.zoranbogdanovski.searchmoviesapp.core.App;
import com.zoranbogdanovski.searchmoviesapp.model.IResponseModel;
import com.zoranbogdanovski.searchmoviesapp.model.movie.Movie;
import com.zoranbogdanovski.searchmoviesapp.model.searchresult.MovieSearchResult;
import com.zoranbogdanovski.searchmoviesapp.services.IParsedResponseListener;
import com.zoranbogdanovski.searchmoviesapp.util.DialogUtils;
import com.zoranbogdanovski.searchmoviesapp.util.NetworkUtils;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    private static final String LISTVIEW_FRAGMENT_TAG = "listview_fragment_tag";

    private Fragment listViewFragment = new ListViewFragment();
    private AdapterView.OnItemClickListener onMovieItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MovieSearchResult model = (MovieSearchResult) parent.getAdapter().getItem(position);
            int movieId = model.getId();
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
                startMovieDetails(movieId);
            }
        }
    };

    private void startMovieDetails(int movieId) {
        App.getInstance().getServices().startGetMovieInfo(String.valueOf(movieId),
                new IParsedResponseListener() {
                    @Override
                    public void onParsedResponseFinished(IResponseModel response) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        Movie responseParsed = (Movie) response;
                        intent.putExtra(MovieDetailActivity.MODEL_EXTRA, responseParsed);
                        startActivity(intent);
                    }
                });
    }

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

    private void removeListViewFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(listViewFragment).commit();
    }

    private void addListViewFragment() {
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .add(android.R.id.content, listViewFragment, LISTVIEW_FRAGMENT_TAG)
                .commit();
    }
}
