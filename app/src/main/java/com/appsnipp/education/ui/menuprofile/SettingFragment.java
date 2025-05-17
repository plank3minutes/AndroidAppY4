package com.appsnipp.education.ui.menuprofile;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.utils.helpers.DarkModePrefManager;

public class SettingFragment extends Fragment {

    private ImageView backSettingImageView;
    private CardView themeCardView;
    private DarkModePrefManager darkModePrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize DarkModePrefManager
        darkModePrefManager = new DarkModePrefManager(requireContext());

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
        int currentTheme = darkModePrefManager.isNightMode() ? 1 : 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Theme")
                .setSingleChoiceItems(themes, currentTheme, (dialog, which) -> {
                    boolean isDarkMode = which == 1;
                    if (isDarkMode != darkModePrefManager.isNightMode()) {
                        darkModePrefManager.setDarkMode(isDarkMode);
                        // Delay theme change slightly for smoother transition
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (isAdded()) {
                                darkModePrefManager.applyTheme();
                            }
                        }, 100);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
