package com.zoranbogdanovski.searchmoviesapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zoranbogdanovski.searchmoviesapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A fragment containing a list to show to the user.
 */
public class ListViewFragment extends Fragment {

    private static final int GENERATED_ITEMS_COUNT = 100;
    private static final String[] COUNT_WORDS = {"one", "two", "three", "four", "five", "six",
        "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
        "sixteen", "seventeen", "eighteen", "nineteen", "twenty"};

    private Random random = new Random();
    private ListView listView;

    public ListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View fragmentView = inflater.inflate(R.layout.fragment_listview, container, false);

      listView = (ListView) fragmentView.findViewById(R.id.fragment_list);
      List<String> listItems = generateListItems();
      NumbersListAdapter adapter = new NumbersListAdapter(getActivity(), listItems);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          if (position == 0) {
            // go back when first item of list is clicked
            getActivity().onBackPressed();
          }
        }
      });

      return fragmentView;
    }

  private List<String> generateListItems() {
    List<String> listItems = new ArrayList<>(GENERATED_ITEMS_COUNT);

    for (int i = 0; i < GENERATED_ITEMS_COUNT; i++) {
        long generatedNumber = random.nextLong();
        String generatedNumberString = String.valueOf(generatedNumber);
        String countedNumbersString = countNumbersInString(generatedNumberString);
        listItems.add(countedNumbersString);
    }

    return listItems;
  }

  private String countNumbersInString(String generatedNumber) {
    StringBuilder stringBuilder = new StringBuilder();
    char[] charactersArray = generatedNumber.toCharArray();
    char tempChar = charactersArray[0];
    int length = charactersArray.length;
    for (int i = 0; i < length; i++) {
      char c = charactersArray[i];
      // TODO implement
      if (tempChar == c) {
        if (i == length - 1) {
          // last character
          // TODO count the last char and print
        } else {
          // TODO if character is same as temp, count it, otherwise do the final count and print
        }
      }
    }

    return stringBuilder.toString();
  }
}
