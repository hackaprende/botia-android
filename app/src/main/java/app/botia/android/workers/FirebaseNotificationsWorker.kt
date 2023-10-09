package app.botia.android.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class FirebaseNotificationsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    companion object {
        private val TAG = FirebaseNotificationsWorker::class.java.simpleName
    }

    override fun doWork(): Result {
        Log.d(TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return Result.success()
    }
}