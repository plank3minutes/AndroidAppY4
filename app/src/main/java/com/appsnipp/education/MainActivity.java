package com.appsnipp.education;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.appsnipp.education.databinding.ActivityMainBinding;
import com.appsnipp.education.ui.base.BaseActivity;
import com.appsnipp.education.ui.utils.AppLogger;
import com.appsnipp.education.ui.utils.helpers.BottomNavigationBehavior;
import com.appsnipp.education.ui.utils.helpers.DarkModePrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DarkModePrefManager darkModePrefManager;
    ActivityMainBinding binding;
    NavHostFragment navHostFragment;
    NavController navController;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        initializeManagers();
        setAppTheme();
        setSupportActionBar(binding.appBarMain.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar, 
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        setupNavigation();
        
        // Apply font size to all views
        applyFontSizeToAllViews();
    }

    private void initializeManagers() {
        darkModePrefManager = new DarkModePrefManager(this);
    }

    private void setAppTheme() {
        boolean isDarkMode = darkModePrefManager.isNightMode();
        AppCompatDelegate.setDefaultNightMode(
            isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reapply font sizes when returning to activity
        applyFontSizeToAllViews();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void setupNavigation() {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) binding.appBarMain.bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Định nghĩa các màn hình cấp cao nhất để hiển thị bottom navigation
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeCoursesFragment,
                    R.id.coursesStaggedFragment,
                    R.id.profileFragment)
                    .setOpenableLayout(binding.drawerLayout)
                    .build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            // Ẩn bottom navigation khi vào màn hình chi tiết
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.courseDetailFragment
                        || destination.getId() == R.id.lessonDetailFragment
                        || destination.getId() == R.id.quizFragment) {
                    binding.appBarMain.bottomNavigationView.setVisibility(View.GONE);
                } else {
                    binding.appBarMain.bottomNavigationView.setVisibility(View.VISIBLE);
                }

//                // Hiển thị toolbar khi vào màn hình chi tiết
//                if (destination.getId() == R.id.courseDetailFragment
//                        || destination.getId() == R.id.lessonDetailFragment
//                        || destination.getId() == R.id.quizFragment) {
//                    binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
//                } else {
//                    binding.appBarMain.toolbar.setVisibility(View.GONE);
//                }

//              Luôn ẩn toolbar
                binding.appBarMain.toolbar.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = navHostFragment.getNavController();

        // Nếu Drawer đang mở thì đóng lại
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        // Điều hướng lùi nếu có thể
        if (NavigationUI.navigateUp(navController, appBarConfiguration)) {
            return true;
        }

        // Mặc định
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_dark_mode) {
            //code for setting dark mode
            //true for dark mode, false for day mode, currently toggling on each click

//            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            startActivity(new Intent(MainActivity.this, MainActivity.class));
//            finish();
//            overridePendingTransition(0, 0);

            toggleDarkMode();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Phương thức để điều hướng đến màn hình chi tiết khóa học
    public void navigateToCourseDetail(String courseId) {
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        navController.navigate(R.id.action_homeCoursesFragment_to_courseDetailFragment, args);
    }

    // Phương thức để điều hướng đến màn hình chi tiết bài học
    public void navigateToLessonDetail(String lessonId, String courseId) {
        Bundle args = new Bundle();
        args.putString("lessonId", lessonId);
        args.putString("courseId", courseId);
        navController.navigate(R.id.action_courseDetailFragment_to_lessonDetailFragment, args);
    }

    // Phương thức để điều hướng đến màn hình quiz
    public void navigateToQuiz(String courseId) {
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        navController.navigate(R.id.action_lessonDetailFragment_to_quizFragment, args);
    }

    // Método para cambiar el estado del modo oscuro
    private void toggleDarkMode() {
        AppLogger.d("toggleDarkMode init");
        boolean isDarkModeEnabled = darkModePrefManager.isNightMode();
        darkModePrefManager.setDarkMode(!isDarkModeEnabled);
        
        // Apply theme change without recreating activity
        int nightMode = !isDarkModeEnabled ? 
            AppCompatDelegate.MODE_NIGHT_YES : 
            AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}