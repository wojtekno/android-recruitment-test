package dog.snow.androidrecruittest.log

import timber.log.Timber


class MDebugTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "${tag}_MyTag", message, t)
    }
}