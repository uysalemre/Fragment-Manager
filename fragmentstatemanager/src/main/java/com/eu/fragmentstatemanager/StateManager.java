package com.eu.fragmentstatemanager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/*
 * Created By Emre UYSAL
 * 03.05.2020
 */
public class StateManager {

    private static StateManager instance;
    private Map<Integer, Stack<Fragment>> bottomNavigations;
    private FragmentManager supportFragmentManager;
    private ViewGroup fragmentContainer;

    /*
     * This function gets StateManagerBuilder object as a parameter
     * Builds the instance to use with getInstance
     */

    public static StateManager buildInstance(@NonNull StateManagerBuilder builder) {
        if(instance == null)
            instance = new StateManager(builder);
        return instance;
    }

    public static StateManager getInstance() {
        return instance;
    }

    /*
     * Constructor
     */

    private StateManager(StateManagerBuilder builder) {
        bottomNavigations = builder.getNavigationStacks();
        supportFragmentManager = builder.getSupportFragmentManager();
        fragmentContainer = builder.getFragmentContainer();
    }

    /*
     * OnBackPressed from a fragment user calls this function
     * Fragment will be removed
     * Last Fragment in stack will be showed
     */

    public void fragmentOnBackPressed(int id) {
        bottomNavigations.get(id).pop();
        manageFragments(bottomNavigations.get(id).peek(), id, true);
    }

    /*
     * In your activity which contains your bottomNavigationView
     * User calls this function to show initial fragments
     */

    public void showOnNavigationClick(int id, Fragment fragment) {
        if(bottomNavigations.get(id).isEmpty()) {
            bottomNavigations.get(id).push(fragment);
            manageFragments(fragment, id, false);
        }
        else {
            manageFragments(bottomNavigations.get(id).peek(), id, false);
        }
    }

    /*
     * User calls this function to show any fragment
     */

    public void showFragment(int id, Fragment fragment) {
        manageFragments(fragment, id, false);
    }

    /*
     * If user wants complete a stream of pages such as select option-> select option -> payment -> show initial fragment
     * Then user needs to use this function
     */

    public void removeAllFragmentStream(int id, Fragment nextToShow) {

        while(!bottomNavigations.get(id).isEmpty()) {
            Fragment fg = bottomNavigations.get(id).pop();
            Fragment removable = supportFragmentManager.findFragmentByTag(fg.getClass().getSimpleName());

            if(removable != null) {
                supportFragmentManager.beginTransaction().remove(removable).commit();
            }
        }

        FragmentTransaction transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment current = getVisibleFragment();

        if(current != null)
            transaction.remove(current);

        if(nextToShow != null) {
            bottomNavigations.get(id).push(nextToShow);
            transaction.add(fragmentContainer.getId(), nextToShow, nextToShow.getClass().getSimpleName()).commit();
        }
    }

    /*
     * If user wants to remove everything according to this manager than use this function
     * For example when you want to logout from app then use this function to clear everything
     */

    public void removeAll() {
        for(int key : bottomNavigations.keySet()) {
            bottomNavigations.get(key).clear();
        }
        instance = null;
    }

    /*
     * This function manages transactions to show, hide and remove the fragments
     */

    private void manageFragments(Fragment fragment, int id, boolean isComeByBackButton) {
        Fragment current = getVisibleFragment();
        Fragment foundFragment = supportFragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());

        FragmentTransaction transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if(foundFragment == null) {
            if(current != null)
                transaction.hide(current);

            bottomNavigations.get(id).push(fragment);
            transaction.add(fragmentContainer.getId(), fragment, fragment.getClass().getSimpleName()).commit();
        }
        else {
            if(current != null) {
                if(isComeByBackButton)
                    transaction.remove(current);
                else
                    transaction.hide(current);
            }

            transaction.show(foundFragment).commit();
        }
    }

    /*
     * This function gives the current fragment on screen
     */
    private Fragment getVisibleFragment() {
        List<Fragment> fragments = supportFragmentManager.getFragments();
        if(!fragments.isEmpty()) {
            for(Fragment fragment : fragments) {
                if(fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

}
