package dog.snow.androidrecruittest.scheduler

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun mainThread(): Scheduler
}