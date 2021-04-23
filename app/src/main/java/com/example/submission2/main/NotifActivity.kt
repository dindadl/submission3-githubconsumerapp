package com.example.submission2.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.submission2.R
import com.example.submission2.alarm.AlarmReceiver

class NotifActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {



        private lateinit var alarmReceiver: AlarmReceiver
        private lateinit var isNotif: SwitchPreferenceCompat
        private lateinit var mContext: Context

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mContext = context
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)


            alarmReceiver = AlarmReceiver()
            init()
            setSummaries()

        }

        private fun init() {
            isNotif = findPreference<SwitchPreferenceCompat>("sync") as SwitchPreferenceCompat
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences
            isNotif.isChecked = sh.getBoolean("sync", false)
            Log.d("isChecked ", isNotif.isChecked.toString())
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String) {
            if (key == "sync") {
                isNotif.isChecked = sp.getBoolean("sync", false)
                if (isNotif.isChecked){
                    alarmReceiver.setRepeatingAlarm(mContext, getString(R.string.msg_notif))
                    Log.d("setAlarm ", isNotif.isChecked.toString())
                } else {
                    alarmReceiver.cancelAlarm(mContext)
                    Log.d("cancelAlarm ", isNotif.isChecked.toString())
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}