package dog.snow.androidrecruittest.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.ui.splash.SplashActivity

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        if (intent.hasExtra(SplashActivity.DATA_COMPLETION_FLAG)) {
            val message = intent.getStringExtra(SplashActivity.DATA_COMPLETION_FLAG)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}