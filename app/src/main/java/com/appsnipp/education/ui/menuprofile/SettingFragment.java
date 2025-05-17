package com.appsnipp.education.ui.menuprofile;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appsnipp.education.R;

public class SettingFragment extends Fragment {

    private ImageView backSettingImageView;
    private CardView themeCardView;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "settings_pref";
    private static final String KEY_THEME = "theme_mode";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, 0);

        // Apply saved theme
        applySavedTheme();

        // Views
        backSettingImageView = view.findViewById(R.id.back_setting_img_view_id);
        themeCardView = view.findViewById(R.id.card_theme_setting);

        // Back button
        backSettingImageView.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );

        // Theme selection
        themeCardView.setOnClickListener(v -> showThemeDialog());

        return view;
    }

    private void showThemeDialog() {
        final String[] themes = {"Light", "Dark"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Theme")
                .setItems(themes, (dialog, which) -> {
                    if (which == 0) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        saveThemeMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else if (which == 1) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveThemeMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveThemeMode(int mode) {
        sharedPreferences.edit().putInt(KEY_THEME, mode).apply();
    }

    private void applySavedTheme() {
        int savedMode = sharedPreferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(savedMode);
    }
}
