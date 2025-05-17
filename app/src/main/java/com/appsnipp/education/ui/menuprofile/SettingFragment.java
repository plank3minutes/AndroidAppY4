package com.appsnipp.education.ui.menuprofile;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.navigation.fragment.NavHostFragment;

import com.appsnipp.education.R;
import com.appsnipp.education.ui.base.BaseFragment;
import com.appsnipp.education.ui.utils.helpers.DarkModePrefManager;
import com.appsnipp.education.ui.utils.helpers.FontSizePrefManager;

public class SettingFragment extends BaseFragment {

    private ImageView backSettingImageView;
    private CardView themeCardView;
    private CardView fontCardView;
    private CardView introCardView;
    private DarkModePrefManager darkModePrefManager;
    private FontSizePrefManager fontSizePrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize managers
        darkModePrefManager = new DarkModePrefManager(requireContext());
        fontSizePrefManager = new FontSizePrefManager(requireContext());

        // Views
        backSettingImageView = view.findViewById(R.id.back_setting_img_view_id);
        themeCardView = view.findViewById(R.id.card_theme_setting);
        fontCardView = view.findViewById(R.id.card_font_setting);
        introCardView = view.findViewById(R.id.card_intro_setting);

        // Back button
        backSettingImageView.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );

        // Theme selection
        themeCardView.setOnClickListener(v -> showThemeDialog());

        // Font size selection
        fontCardView.setOnClickListener(v -> showFontSizeDialog());

        // Introduction dialog
        introCardView.setOnClickListener(v -> showIntroductionDialog());

        return view;
    }

    private void showThemeDialog() {
        final String[] themes = {"Light", "Dark"};
        int currentTheme = darkModePrefManager.isNightMode() ? 1 : 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Theme")
                .setSingleChoiceItems(themes, currentTheme, (dialog, which) -> {
                    boolean isDarkMode = which == 1;
                    darkModePrefManager.setDarkMode(isDarkMode);
                    AppCompatDelegate.setDefaultNightMode(
                        isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                    );
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showFontSizeDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_font_size, null);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupFontSize);
        
        // Set the current font size
        int currentFontSize = fontSizePrefManager.getFontSize();
        switch (currentFontSize) {
            case FontSizePrefManager.FONT_SMALL:
                radioGroup.check(R.id.radioSmall);
                break;
            case FontSizePrefManager.FONT_MEDIUM:
                radioGroup.check(R.id.radioMedium);
                break;
            case FontSizePrefManager.FONT_BIG:
                radioGroup.check(R.id.radioBig);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedFontSize;
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    
                    if (checkedRadioButtonId == R.id.radioSmall) {
                        selectedFontSize = FontSizePrefManager.FONT_SMALL;
                    } else if (checkedRadioButtonId == R.id.radioBig) {
                        selectedFontSize = FontSizePrefManager.FONT_BIG;
                    } else {
                        selectedFontSize = FontSizePrefManager.FONT_MEDIUM;
                    }
                    
                    fontSizePrefManager.setFontSize(selectedFontSize);
                    // Apply font size changes to current fragment
                    if (getView() != null) {
                        applyFontSizeToView(getView());
                    }
                    // Recreate activity to apply changes everywhere
                    requireActivity().recreate();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showIntroductionDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_introduction, null);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
