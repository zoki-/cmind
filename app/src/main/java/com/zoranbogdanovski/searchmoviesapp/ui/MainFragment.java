package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zoranbogdanovski.searchmoviesapp.R;
import com.zoranbogdanovski.searchmoviesapp.util.DialogUtils;
import com.zoranbogdanovski.searchmoviesapp.util.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * A fragment containing a web view and an image view.
 */
public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.toString();
    private static final String LISTVIEW_FRAGMENT_TAG = "listview_fragment_tag";

    private Fragment listViewFragment = new ListViewFragment();

    private static String[] imageUrls = {
        "https://image.tmdb.org/t/p/w130/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg",
        "https://image.tmdb.org/t/p/w130/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg",
        "https://image.tmdb.org/t/p/w130/qKkFk9HELmABpcPoc1HHZGIxQ5a.jpg",
        "https://image.tmdb.org/t/p/w130/mUjWof8LHDgCZC9mFp0UYKBf1Dm.jpg",
        "https://image.tmdb.org/t/p/w130/5TQ6YDmymBpnF005OyoB7ohZps9.jpg",
        "https://image.tmdb.org/t/p/w130/6t3KOEUtrIPmmtu1czzt6p2XxJy.jpg",
        "https://image.tmdb.org/t/p/w130/1Ilv6ryHUv6rt9zIsbSEJUmmbEi.jpg",
        "https://image.tmdb.org/t/p/w130/eA2D86Y6VPWuUzZyatiLBwpTilQ.jpg",
        "https://image.tmdb.org/t/p/w130/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
        "https://image.tmdb.org/t/p/w130/dlIPGXPxXQTp9kFrRzn0RsfUelx.jpg",
        "https://image.tmdb.org/t/p/w130/aWVYDsVfZG61Q9wv13md1TkcHp5.jpg"};

    private WebView webView;
    private ImageView imageView;

    private Random random = new Random();

    public MainFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!NetworkUtils.isOnline()) {
            DialogUtils.showMessageDialog(getActivity(),
                getString(R.string.no_network_message),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        webView = (WebView) fragmentView.findViewById(R.id.web_view);
        webView.loadUrl("https://www.google.com/");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    URL urlObject = new URL(url);
                    String host = urlObject.getHost();
                    if (host.equals("www.yahoo.com")) {
                        goToListViewFragment();
                    } else {
                        view.loadUrl(url);
                    }
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Malformed url clicked by user.");
                    view.loadUrl(url);
                }
                return true;
            }
        });

        imageView = (ImageView) fragmentView.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomImage(imageView);
            }
        });
        return fragmentView;
    }

    private void goToListViewFragment() {
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
            .replace(android.R.id.content, listViewFragment, LISTVIEW_FRAGMENT_TAG)
            .addToBackStack("listview")
            .commit();
    }

    private void loadRandomImage(ImageView imageView) {
        Glide.with(getActivity())
            .load(getRandomUrl())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView);
    }

    private String getRandomUrl() {
        int randomIndex = random.nextInt(imageUrls.length);
        return imageUrls[randomIndex];
    }
}
