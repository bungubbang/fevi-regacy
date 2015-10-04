package com.app.fevir.movie.list.presenter;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.app.fevir.movie.list.MovieListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MovieListPresenterTest {

    @Rule
    public ActivityTestRule<AppCompatActivity> activityTestRule = new ActivityTestRule<>(AppCompatActivity.class);

    @Before
    public void setUp() throws Exception {
        AppCompatActivity activity = activityTestRule.getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().add(MovieListFragment.newInstance(0), null).commit();

    }

    @Test
    public void testHahaha() throws Exception {
    }

}