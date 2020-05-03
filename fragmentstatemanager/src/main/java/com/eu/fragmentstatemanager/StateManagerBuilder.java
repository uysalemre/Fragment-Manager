package com.eu.fragmentstatemanager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/*
 * Created By Emre UYSAL
 * 03.05.2020
 */

public class StateManagerBuilder {

    private Map<Integer, Stack<Fragment>> navigationStacks;
    private FragmentManager supportFragmentManager;
    private ViewGroup fragmentContainer;

    /*
     * Constructor takes parameters to create Stacks and Map
     */
    public StateManagerBuilder(@NonNull int... ids) {
        navigationStacks = new HashMap<>();

        for(int id : ids) {
            navigationStacks.put(id, new Stack<Fragment>());
        }
    }

    /*
     * Gets the supportFragmentManager as parameter to use in StateManager class
     */
    public StateManagerBuilder withSupportFragmentManager(@NonNull FragmentManager manager) {
        supportFragmentManager = manager;
        return this;
    }

    /*
     * Gets the viewGroup as parameter to use in StateManager class
     */
    public StateManagerBuilder withViewGroup(@NonNull ViewGroup viewGroup) {
        fragmentContainer = viewGroup;
        return this;
    }

    /* Getter Methods */
    public Map<Integer, Stack<Fragment>> getNavigationStacks() {
        return navigationStacks;
    }

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    public ViewGroup getFragmentContainer() {
        return fragmentContainer;
    }
}
