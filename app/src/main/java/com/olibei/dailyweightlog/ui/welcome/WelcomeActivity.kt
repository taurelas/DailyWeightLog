package com.olibei.dailyweightlog.ui.welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.olibei.dailyweightlog.R
import com.olibei.dailyweightlog.app.DWLApplication
import com.olibei.dailyweightlog.databinding.ActivityWelcomeBinding
import com.olibei.dailyweightlog.ui.main.MainActivity
import javax.inject.Inject

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    //injecting shared preferences singleton
    var defaultSharedPreferences: SharedPreferences? = null
        @Inject set

    private lateinit var dots: Array<TextView?>
    private lateinit var layouts: IntArray

    private lateinit var firstRunManager: FirstRunManager
    private var heightRequired = true
    internal var height: Float = 0.toFloat()
    internal var heightString = ""

    //  viewpager change listener
    private val viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.text = getString(R.string.start)
                binding.btnNext.visibility = View.GONE
                binding.btnComplete.visibility = View.VISIBLE
                //btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                binding.btnNext.visibility = View.VISIBLE
                binding.btnComplete.visibility = View.GONE
                //btnSkip.setVisibility(View.VISIBLE);
            }

            // adding listener to the checkbox to find out if Height is required

            val cbBMI = findViewById<CheckBox>(R.id.cbBMI)

            cbBMI?.setOnCheckedChangeListener { _, isChecked ->
                heightRequired = isChecked
                saveSetting(getString(R.string.pref_uses_bmi_key), heightRequired)
                val frame = findViewById<FrameLayout>(R.id.frame)
                frame.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
            }

            val cbBodyFat = findViewById<CheckBox>(R.id.cbBodyFat)
            cbBodyFat?.setOnCheckedChangeListener { _ , isChecked -> saveSetting(getString(R.string.pref_uses_fat_pc_key), isChecked) }

            val etHeight = findViewById<EditText>(R.id.etHeight)
            etHeight?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                // to use PreferenceScreen we try to parse the float but save to
                // preferences as String
                override fun afterTextChanged(s: Editable) {
                    try {
                        heightString = s.toString()
                        height = java.lang.Float.parseFloat(s.toString())
                        saveSetting(getString(R.string.pref_height_key), heightString)
                    } catch (nfe: NumberFormatException) {
                        etHeight.error = resources.getString(R.string.enter_valid_height)
                    }
                }
            })
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DWLApplication.app().appComponent().inject(this)
        // Checking for first time launch - before calling setContentView()

        firstRunManager = FirstRunManager(defaultSharedPreferences!!)
        if (!firstRunManager.isFirstTimeLaunch) {
            launchHomeScreen()
            finish()
        } else {
            saveDefaultSettings()
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(R.layout.slide_screen1, R.layout.slide_screen2, R.layout.slide_screen3)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()


        binding.viewPager.adapter = MyViewPagerAdapter()
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        binding.btnNext.setOnClickListener {
            val current = getItem(+1)
            binding.viewPager.currentItem = current
        }

        binding.btnComplete.setOnClickListener {
            // binding to check if editText is available
            val editText = findViewById<EditText>(R.id.etHeight)

            if (editText.text.toString().isEmpty() && heightRequired) {
                editText.requestFocus()
                editText.error = resources.getString(R.string.enter_valid_height)
            } else {
                launchHomeScreen()
            }
        }

    }

    fun onClick() {

        // binding to check if editText is available
        val editText = findViewById<EditText>(R.id.etHeight)

        // checking for last page
        // if last page home screen will be launched
        val current = getItem(+1)
        if (current < layouts.size) {
            // move to next screen
            binding.viewPager.currentItem = current
        } else {
            if (editText != null) {
                if (editText.text.toString().isEmpty() && heightRequired) {
                    editText.requestFocus()
                    editText.error = resources.getString(R.string.enter_valid_height)
                } else {
                    launchHomeScreen()
                }
            }
        }
    }

    private fun saveDefaultSettings() {
        val usesBMI = resources.getBoolean(R.bool.pref_uses_bmi_default)
        saveSetting(getString(R.string.pref_uses_bmi_key), usesBMI)
        val usesFatPc = resources.getBoolean(R.bool.pref_uses_fat_pc_default)
        saveSetting(getString(R.string.pref_uses_fat_pc_key), usesFatPc)
    }

    private fun saveSetting(key: String, value: Boolean) {
        defaultSharedPreferences!!
                .edit()
                .putBoolean(key, value)
                .apply()
    }

    private fun saveSetting(key: String, value: String) {
        defaultSharedPreferences!!
                .edit()
                .putString(key, value)
                .apply()

    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)

        //int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        //int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(resources.getIntArray(R.array.array_dot_inactive)[currentPage])
            binding.layoutDots.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]?.setTextColor(resources.getIntArray(R.array.array_dot_active)[currentPage])
    }

    private fun getItem(i: Int): Int {
        return binding.viewPager.currentItem + i
    }

    private fun launchHomeScreen() {
        Log.d("XCHK", "launchHomeScreen")
        firstRunManager.setFirstTimeLaunched()
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter internal constructor() : PagerAdapter() {
        private val layoutInflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val view = layoutInflater.inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}