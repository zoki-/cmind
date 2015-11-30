package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zoranbogdanovski.searchmoviesapp.R;

import java.util.Random;

/**
 * A fragment containing a list to show to the user.
 */
public class ListViewFragment extends Fragment {

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

    public ListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_listview, container, false);

        webView = (WebView) fragmentView.findViewById(R.id.web_view);
        webView.loadUrl("https://www.google.com/");
        // TODO add web view client to handle redirecting to list fragment

        imageView = (ImageView) fragmentView.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomImage(imageView);
            }
        });

        return fragmentView;
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
