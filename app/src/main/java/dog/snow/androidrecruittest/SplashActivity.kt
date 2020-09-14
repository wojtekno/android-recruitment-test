package dog.snow.androidrecruittest

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase
import dog.snow.androidrecruittest.usecases.NotifyDataFetchedUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber.d

class SplashActivity : AppCompatActivity(R.layout.splash_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d("onCreate()")
        val text: TextView = findViewById(R.id.test_tv)
        val button: Button = findViewById(R.id.buttonrefetch)

        val useCase = getListItemsUseCase(text)
        downloadedUseCase()

        button.setOnClickListener {
            d("button clicked")
            useCase.refetch()
        }
    }

    private fun getListItemsUseCase(text: TextView): GetListItemsUseCase {
        val useCase = (application as MyApplication).appGraph.getItemsUseCase
        useCase.getListItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            d("size ${it.size}  :: ${it[0].title}  ")
                            text.text = "size ${it.size}  :: ${it[0].title} :: ${it[90].albumTitle} "
                        },
                        onError = {
                            d("getListItems onError")
                            text.text = "Error ${it.message}"
                        }
                )
        return useCase
    }

    private fun downloadedUseCase(): NotifyDataFetchedUseCase {
        val downloadedUseCase = (application as MyApplication).appGraph.notifyDataFetchedUseCase
        downloadedUseCase.fetchingDataEnded()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = {
                            d("fetchingEnded on Error")
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                        },
                        onSuccess = {
                            Toast.makeText(applicationContext, "Dowloaded $it from 3", Toast.LENGTH_SHORT).show()
                            d("fetchingEnded onNext")
                        }
                )
        return downloadedUseCase
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