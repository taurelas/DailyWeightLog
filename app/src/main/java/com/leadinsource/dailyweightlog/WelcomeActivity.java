package com.leadinsource.dailyweightlog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leadinsource.dailyweightlog.app.DWLApplication;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @Inject SharedPreferences defaultSharedPreferences;

    @BindView(R.id.view_pager)  public ViewPager viewPager;

    @BindView(R.id.layoutDots)  public LinearLayout dotsLayout;

    @BindView(R.id.btn_next)    public Button btnNext;

    @BindString(R.string.enter_valid_height) String errorText;



    private TextView[] dots;
    private int[] layouts;

    private FirstRunManager firstRunManager;
    private boolean heightRequired = true;
    float height;
    String heightString = "";
    final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DWLApplication.app().appComponent().inject(this);
        // Checking for first time launch - before calling setContentView()
        firstRunManager = new FirstRunManager(defaultSharedPreferences);
        if (!firstRunManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        } else {
            saveDefaultSettings();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.slide_screen1,
                R.layout.slide_screen2,
                R.layout.slide_screen3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    @OnClick(R.id.btn_next)
    public void onClick() {

        // binding to check if editText is available
        @Nullable EditText editText = findViewById(R.id.etHeight);

        // checking for last page
        // if last page home screen will be launched
        int current = getItem(+1);
        if (current < layouts.length) {
            // move to next screen
            viewPager.setCurrentItem(current);
        } else {
            if (editText!=null) {
                if ((editText.getText().toString().length() == 0) && (heightRequired)) {
                    editText.requestFocus();
                    editText.setError(errorText);
                } else {
                    launchHomeScreen();
                }
            }
        }
    }

    private void saveDefaultSettings() {
        boolean usesBMI = getResources().getBoolean(R.bool.pref_uses_bmi_default);
        saveSetting(getString(R.string.pref_uses_bmi_key),usesBMI);
        boolean usesFatPc = getResources().getBoolean(R.bool.pref_uses_fat_pc_default);
        saveSetting(getString(R.string.pref_uses_fat_pc_key), usesFatPc);
    }

    private void saveSetting(String key, boolean value) {
        defaultSharedPreferences
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    private void saveSetting(String key, String value) {
        defaultSharedPreferences
                .edit()
                .putString(key, value)
                .apply();

    }

    @BindArray(R.array.array_dot_active) int[] colorsActive;
    @BindArray(R.array.array_dot_inactive) int[] colorsInactive;

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        //int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        //int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        firstRunManager.setFirstTimeLaunched();
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                //btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }

            // adding listener to the checkbox to find out if Height is required

            CheckBox cbBMI = findViewById(R.id.cbBMI);

            if (cbBMI != null) {
                cbBMI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        heightRequired = isChecked;
                        saveSetting(getString(R.string.pref_uses_bmi_key), heightRequired);
                        FrameLayout frame = findViewById(R.id.frame);
                        frame.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                    }
                });
            }

            CheckBox cbBodyFat = findViewById(R.id.cbBodyFat);
            if (cbBodyFat != null) {
                cbBodyFat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        saveSetting(getString(R.string.pref_uses_fat_pc_key), isChecked);
                    }
                });
            }

            final EditText etHeight = findViewById(R.id.etHeight);
            if (etHeight != null) {
                etHeight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    // to use PreferenceScreen we try to parse the float but save to
                    // preferences as String
                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            heightString = s.toString();
                            height = Float.parseFloat(s.toString());
                            saveSetting(getString(R.string.pref_height_key), heightString);
                        } catch (NumberFormatException nfe) {
                            etHeight.setError(errorText);
                        }

                    }
                });
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = null;
            if (layoutInflater != null) {
                view = layoutInflater.inflate(layouts[position], container, false);
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}