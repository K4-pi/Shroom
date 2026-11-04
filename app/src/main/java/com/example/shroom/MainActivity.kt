package com.example.shroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import com.example.shroom.databinding.ActivityMainBinding
import com.example.shroom.ui.glossary.GlossaryFragment
import com.example.shroom.ui.map.MapFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.nav_host, MapFragment(), TAG_MAP)
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> {
                    showFragment(TAG_MAP) { MapFragment() }
                    true
                }
                R.id.nav_glossary -> {
                    showFragment(TAG_GLOSSARY) { GlossaryFragment() }
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(tag: String, factory: () -> Fragment) {
        val existing = supportFragmentManager.findFragmentByTag(tag)
        if (existing != null && existing.isVisible) return

        supportFragmentManager.commitNow {
            supportFragmentManager.fragments
                .filter { it.tag == TAG_MAP || it.tag == TAG_GLOSSARY }
                .forEach { hide(it) }
            if (existing != null) {
                show(existing)
            } else {
                add(R.id.nav_host, factory(), tag)
            }
        }
    }

    companion object {
        private const val TAG_MAP = "map"
        private const val TAG_GLOSSARY = "glossary"
    }
}
