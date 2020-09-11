package dog.snow.androidrecruittest

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber.d

class SplashActivity : AppCompatActivity(R.layout.splash_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text: TextView = findViewById(R.id.test_tv)
        val button: Button = findViewById(R.id.buttonrefetch)
        val useCase = (application as MyApplication).appGraph.getItemsUseCase
        useCase.getListItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            d("size ${it.size}  :: ${it[0].title}  ")
                            text.text = "size ${it.size}  :: ${it[0].title} :: ${it[90].albumTitle} "
                        },
                        onError = { d(it) }
                )

        button.setOnClickListener {
            d("button clicked")
            useCase.refetch()
        }
    }

    private fun showError(errorMessage: String?) {
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.cant_download_dialog_title)
                .setMessage(getString(R.string.cant_download_dialog_message, errorMessage))
                .setPositiveButton(R.string.cant_download_dialog_btn_positive) { _, _ -> /*tryAgain()*/ }
                .setNegativeButton(R.string.cant_download_dialog_btn_negative) { _, _ -> finish() }
                .create()
                .apply { setCanceledOnTouchOutside(false) }
                .show()
    }
}