package com.example.flixster.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.databinding.FragmentMoviesBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MoviesFragment extends Fragment {

    public static final String TAG = "MoviesFragment";

    List<Movie> movies;
    MovieAdapter movieAdapter;

    // creates a new fragment with it's list of movies
    public static MoviesFragment newInstance(int index) {
        MoviesFragment moviesFragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putInt("listIndex", index);
        moviesFragment.setArguments(args);
        return moviesFragment;
    }

    // for data initialization - load in list of movies
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movies = new ArrayList<>();

        // Create the adapter
        movieAdapter = new MovieAdapter(getActivity(), movies);

        // get movie list url
        String API = getString(R.string.themoviedb_api_key);
        String url;
        int listIndex = getArguments().getInt("listIndex", 0);
        switch (listIndex) {
            case 0:
                url = String.format(getString(R.string.now_playing), API);
                break;
            case 1:
                url = String.format(getString(R.string.popular), API);
                break;
            case 2:
                url = String.format(getString(R.string.top_rated), API);
                break;
            case 3:
                url = String.format(getString(R.string.upcoming), API);
                break;
            default:
                url = String.format(getString(R.string.now_playing), API);
        }
        Log.d(TAG, url);

        // request movies list
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    // notify adapter that data changed so it renders
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure: " + throwable.getMessage());
            }
        });
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_movies, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here

        // Set the adapter and layout manager on the recycler view
        RecyclerView rvMovies = (RecyclerView) view.findViewById(R.id.rvMovies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
