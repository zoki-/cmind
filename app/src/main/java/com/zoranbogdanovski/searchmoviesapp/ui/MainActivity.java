package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.zoranbogdanovski.searchmoviesapp.R;
import com.zoranbogdanovski.searchmoviesapp.core.App;
import com.zoranbogdanovski.searchmoviesapp.model.IResponseModel;
import com.zoranbogdanovski.searchmoviesapp.model.movie.Movie;
import com.zoranbogdanovski.searchmoviesapp.model.searchresult.MovieSearchResult;
import com.zoranbogdanovski.searchmoviesapp.model.searchresult.SearchResult;
import com.zoranbogdanovski.searchmoviesapp.services.IParsedResponseListener;
import com.zoranbogdanovski.searchmoviesapp.util.DialogUtils;
import com.zoranbogdanovski.searchmoviesapp.util.NetworkUtils;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_movie_search)
public class MainActivity extends RoboActivity {

    private static final String SEARCH_FRAGMENT_TAG = "search_fragment_tag";

    private Handler searchHandler = new Handler();
    private Fragment searchFragment = new SearchResultsFragment();
    private Runnable searchRunnable = null;
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
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(android.R.id.content, new SearchFragment(), null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        boolean onCreateOptionsMenu = super.onCreateOptionsMenu(menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(queryTextListener);
            searchItem.setOnActionExpandListener(actionExpandListener);

        }
        return onCreateOptionsMenu;
    }

    private MenuItem.OnActionExpandListener actionExpandListener = new MenuItem.OnActionExpandListener() {

        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            // expand
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            removeSearchFragment();
            return true;
        }
    };

    private void removeSearchFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(searchFragment).commit();
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

        public boolean onQueryTextChange(String newText) {
            if (searchRunnable != null) {
                searchHandler.removeCallbacks(searchRunnable);
            }

            startSearch(newText);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private void startSearch(String newText) {
        searchRunnable = new SearchTextRunnable(newText);
        searchHandler.postDelayed(searchRunnable, 300);
    }

    private class SearchTextRunnable implements Runnable {

        private final String searchText;

        public SearchTextRunnable(String searchText) {
            this.searchText = searchText;
        }

        @Override
        public void run() {
            startSearchTask(searchText);
        }
    }

    private void startSearchTask(String searchInput) {
        App app = App.getInstance();

        final ListView listView = (ListView) findViewById(R.id.fragment_list);
        final TextView noResultsView = (TextView) findViewById(R.id.no_results);
        final View progressView = findViewById(R.id.progress_view);
        if (TextUtils.isEmpty(searchInput)) {
            if (progressView != null) {
                progressView.setVisibility(View.GONE);
            }
            if (noResultsView != null) {
                noResultsView.setVisibility(View.VISIBLE);
            }
            if (listView != null) {
                listView.setVisibility(View.GONE);
            }
            return;
        }

        if (!NetworkUtils.isOnline()) {
            DialogUtils.showMessageDialog(this, getString(R.string.no_network_message),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        } else {
            progressView.setVisibility(View.VISIBLE);
            noResultsView.setVisibility(View.GONE);

            app.getServices().startGetMoviesSearchResult(searchInput, new IParsedResponseListener() {
                @Override
                public void onParsedResponseFinished(IResponseModel response) {
                    progressView.setVisibility(View.GONE);

                    SearchResult searchResultResponse = (SearchResult) response;
                    List<MovieSearchResult> results = searchResultResponse.getResults();

                    if (listView != null) {
                        if (results.size() > 0) {
                            noResultsView.setVisibility(View.GONE);
                            ArrayAdapter<MovieSearchResult> adapter = new SearchResultsListAdapter(
                                    MainActivity.this, results);

                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(onMovieItemClickListener);
                            listView.setVisibility(View.VISIBLE);
                        } else {
                            listView.setVisibility(View.GONE);
                            noResultsView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            addSearchFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSearchFragment() {
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .add(android.R.id.content, searchFragment, SEARCH_FRAGMENT_TAG)
                .commit();
    }
}
