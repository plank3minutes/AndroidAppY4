package com.appsnipp.education;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.appsnipp.education.databinding.ActivityMainBinding;
import com.appsnipp.education.ui.utils.helpers.BottomNavigationBehavior;
import com.appsnipp.education.ui.utils.helpers.DarkModePrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        setAppTheme();
        setSupportActionBar(binding.appBarMain.toolbar);

        setupNavigation();
    }

    private void setAppTheme() {
        darkModePrefManager = new DarkModePrefManager(this);
        boolean isDarkModeEnabled = darkModePrefManager.isNightMode();
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

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
        BottomNavigationView bottomNavigationView = binding.appBarMain.bottomNavigationView;
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Định nghĩa các màn hình cấp cao nhất để hiển thị bottom navigation
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeCoursesFragment,
                    R.id.coursesStaggedFragment,
                    R.id.matchesCoursesFragment)
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
            });
        }
    }

    /*
    private void toggleDarkMode() {
        AppLogger.d("toggleDarkMode init");
        boolean isDarkModeEnabled = darkModePrefManager.isNightMode();
        darkModePrefManager.setDarkMode(!isDarkModeEnabled);

        // startActivity(new Intent(MainActivity.this, MainActivity.class));
        // finish();
        overridePendingTransition(0, 0);
    }
     */
}