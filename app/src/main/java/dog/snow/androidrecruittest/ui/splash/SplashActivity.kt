package dog.snow.androidrecruittest.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.MyApplication
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.ui.MainActivity
import kotlinx.android.synthetic.main.layout_progressbar.*
import kotlinx.android.synthetic.main.splash_activity.*
import timber.log.Timber.d

class SplashActivity : AppCompatActivity() {
    companion object {
        const val DATA_COMPLETION_FLAG = "data_completion"
    }

    private lateinit var vm: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        d("onCreate()")
        val vmFactory = (application as MyApplication).appGraph.splashVmFactory
        vm = ViewModelProvider(this, vmFactory).get(SplashViewModel::class.java)
        animate()


        vm.downloadingCompletion().observe(this, Observer { message ->
            progressbar.visibility = View.GONE
            startNewActivity(message)
        })

        vm.downloadingError()
                .observe(this, Observer { message ->
                    progressbar.visibility = View.GONE
                    showError(message)

                })
    }

    private fun startNewActivity(message: String = ""): Unit {
        val intent = Intent(this, MainActivity::class.java)
        if (message.isNotEmpty() && message != getString(R.string.downloading_successful)) {
            intent.putExtra(DATA_COMPLETION_FLAG, message)
        }
        startActivity(intent)
    }

    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.cant_download_dialog_title)
                .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
                .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> vm.retry() }
                .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
                .create()
                .apply { setCanceledOnTouchOutside(false) }
                .show()
    }

    private fun animate() {
        val slideLeft = loadAnimation(applicationContext, R.anim.slide_left_in)
        val slideRight = loadAnimation(applicationContext, R.anim.slide_right_in_long)
        iv_logo_sd_symbol.startAnimation(slideLeft)
        iv_logo_sd_text.startAnimation(slideRight)
    }
}