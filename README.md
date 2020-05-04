# Fragment Manager

## Information
   - This is an Android Library that works with fragments and bottom navigation view.
   - This library easily manages nested fragments, bottom navigation based fragments.
   - Uses an Instagram like way to show, hide and remove fragments.

## Download Library
   - Add it in your root build.gradle at the end of repositories:
   
          allprojects {
            repositories {
              ...
              maven { url 'https://jitpack.io' }
            }
          }

   - Add the dependency

          dependencies {
            implementation 'com.github.uysalemre:Fragment-Manager:0.1.0'
          }


## How to Use ?

### In Your Activity Which Includes BottomNavigationView
   - In OnCreate method
   - Define your ViewGroup which is your FrameLayout
   - Define your BottomNavigationView
   - Call StateManager.buildInstance as shown below
   - Pass parameters which is ids for your bottom navigation items in builder
   - Pass ViewGroup as parameter in builder
   - Pass getSupportFragmentManager() as parameter in builder
   - You must be careful that call StateManager.buildInstance after findViewById's and before   bottomNavigation.setOnNavigationItemSelectedListener

          public class MainActivity extends AppCompatActivity {
              private ViewGroup fragments;
              private BottomNavigationView bottomNavigation;
              ...

              @Override
              protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  setContentView(R.layout.activity_main);

                  fragments = findViewById(R.id.frame_layout);
                  bottomNavigation = findViewById(R.id.bottom_navigation_menu);
                  ...
                  StateManager.buildInstance(new StateManagerBuilder(   R.id.navigation_accounts,
                                                                        R.id.navigation_explore,
                                                                        R.id.navigation_likes
                  )
                                                     .withViewGroup(fragments)
                                                     .withSupportFragmentManager(getSupportFragmentManager()));

                  bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
                      switch(menuItem.getItemId()) {
                          case R.id.navigation_accounts:
                              StateManager.getInstance().showOnNavigationClick(R.id.navigation_accounts, new FragmentAccounts());
                              return true;
                          case R.id.navigation_explore:
                              StateManager.getInstance().showOnNavigationClick(R.id.navigation_explore, new FragmentExplore());
                              return true;
                          case R.id.navigation_likes:
                              StateManager.getInstance().showOnNavigationClick(R.id.navigation_likes, new FragmentLikes());
                              return true;
                      }
                      return false;
                  });

                  bottomNavigation.setSelectedItemId(R.id.navigation_accounts);

                  bottomNavigation.setOnNavigationItemReselectedListener(item -> { });

              }

### Assume that you want to open FragmentExplore2 from FragmentExplore
   - You can easily use showFragment method
   - StateManager will manage it in background
   - You must know that you need to give the id of your stream that you want to add, in this case R.id.navigation_explore
   - For example when you click navigation_explore in bottomNavigation you will see FragmentExplore2 like in Instagram
   - Parameter 1 : id of your stream
   - Parameter 2 : fragment you want to show

          FragmentExplore2 exploreFragment2 = new FragmentExplore2();
          StateManager.getInstance().showFragment(R.id.navigation_explore, exploreFragment2);

### Assume that you want to go back from FragmentExplore2 to FragmentExplore
   - For example you have a back button like below
   - You can call fragmentOnBackPressed function like below
   - You will see FragmentExplore after use back_button when you click navigation_explore in bottomNavigation
   - Parameter 1 : id of your stream

          back_button.setOnClickListener(v -> {
              StateManager.getInstance().fragmentOnBackPressed(R.id.navigation_explore);
          });

### Assume that you opened 3 Nested Fragments in FragmentExplore and you want to finish the stream
   - You can call removeAllFragmentStream like below to return to FragmentExplore
   - Parameter 1 : id of your stream
   - Parameter 2 : fragment you want to show

         StateManager.getInstance().removeAllFragmentStream(R.id.navigation_explore, new FragmentExplore());

### Assume that you want to remove fragment manager control over fragments
   - Warning this function will set instance to null
   - For example you must use this when your user wants to logout from application
   - If you are not using system.exit(0) in your activity to exit from application or your fragment container activity destroyed from somewhere in code you have to use this function **onDestroy** state because otherwise you will get Exception from onSavedInstanceState
   

         StateManager.getInstance().removeAll();

### Example Application Will Be Published Soon...
