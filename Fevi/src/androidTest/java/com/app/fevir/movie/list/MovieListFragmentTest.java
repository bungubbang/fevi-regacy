package com.app.fevir.movie.list;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import com.app.fevir.MainActivity;
import com.app.fevir.movie.list.presenter.MovieListPresenter;
import com.vikicast.app.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MovieListFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MovieListFragment movieListFragment;
    private MovieListPresenter movieListPresenter;

    @Before
    public void setUp() throws Exception {

        MainActivity activity = activityTestRule.getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        movieListFragment = (MovieListFragment) fragmentManager.findFragmentById(R.id.content_frame);

        movieListPresenter = movieListFragment.movieListPresenter;
    }

    @Test
    public void testFragment() throws Exception {


    }
}