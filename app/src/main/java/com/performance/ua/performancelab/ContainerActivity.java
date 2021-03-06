package com.performance.ua.performancelab;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by sergey on 4/22/16.
 */
public class ContainerActivity extends AppCompatActivity {
    public static void start(Context context) {
        context.startActivity(new Intent(context, ContainerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);
        findViewById(R.id.container_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dumpPopularRandomNumbersByRank();
//                dumpPopularRandomNumbersByRankMap();
            }
        });



        WebView webView = (WebView) findViewById(R.id.anim_view);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("file:///android_asset/androidify.gif");
    }

    /**
     * Using the pre-formed array of random numbers ordered by popularity, prints out an ordered
     * list of the random number + rank in the form "(RandomNumber): #(Rank)".
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void dumpPopularRandomNumbersByRank() {
        Trace.beginSection("Data Structures");
        // First we need a sorted list of the numbers to iterate through.
        Integer[] sortedNumbers = coolestRandomNumbers.clone();
        Arrays.sort(sortedNumbers);

        // Great!  Now because we have no rank lookup in the population-sorted array,
        // take the random number in sorted order, and find its index in the array
        // that's sorted by popularity.  The index is the rank, so report that.  Easy and efficient!
        // Except that it's... you know... It's not.
        for (int i = 0; i < sortedNumbers.length; i++) {
            Integer currentNumber = sortedNumbers[i];
            for (int j = 0; j < coolestRandomNumbers.length; j++) {
                if (currentNumber.compareTo(coolestRandomNumbers[j]) == 0) {
                    Log.i("Popularity Dump", currentNumber + ": #" + j);
                }
            }
        }
        Trace.endSection();
    }

    /**
     * Using the pre-formed array of random numbers ordered by popularity, prints out an ordered
     * list of the random number + rank in the form "(RandomNumber): #(Rank)". By sorting the
     * keyset, we can easily sort the numbers and retrieve their rank without needing to maintain
     * two redundant data structures.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void dumpPopularRandomNumbersByRankMap() {
        Trace.beginSection("Data structures");
        // Make a copy so that we don't accidentally shatter our data structure.
        Map<Integer, Integer> rankedNumbers = new HashMap<>();
        rankedNumbers.putAll(coolestRandomNumbersMap);
        // Then, we need a sorted version of the numbers to iterate through.
        Integer[] sortedNumbers = {};
        sortedNumbers = rankedNumbers.keySet().toArray(sortedNumbers);
        Arrays.sort(sortedNumbers);

        Integer number;
        for (int i = 0; i < sortedNumbers.length; i++) {
            number = sortedNumbers[i];
            Log.i("Popularity Dump", number + ": #" + rankedNumbers.get(number));
        }
        Trace.endSection();
    }

    public static Integer[] coolestRandomNumbers = new Integer[3000];
    public static HashMap<Integer, Integer> coolestRandomNumbersMap = new HashMap<>();
    static int temp;

    static {
        Random randomGenerator = new Random();
        for (int i = 0; i < 3000; i++) {
            temp = randomGenerator.nextInt();
            coolestRandomNumbers[i] = temp;
            coolestRandomNumbersMap.put(temp, i);
        }
    }
}
