package fitnessapp.workout.homeworkout.stretching.utils

import android.content.Context
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.stretching.exceptions.networks.HttpRequestException
import fitnessapp.workout.homeworkout.stretching.exceptions.networks.NoInternetException
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener


object AlertDialogHelper {

    fun showNoInternetDialog(context: Context,callbackListener: CallbackListener) {
        val exception = NoInternetException()
        ErrorAlertDialog(context).setTitle(exception.title)
                .setMessage(exception.errMessage)
                .setNegativeButton(context.getString(R.string.cancel))
                .setPositiveButton(context.getString(R.string.retry))
                .setOnButtonClickListener(object : ErrorAlertDialog.DialogButtonClick{
                    override fun onPositiveClick() {
                        callbackListener.onRetry()
                    }

                    override fun onNegativeClick() {
                        callbackListener.onCancel()
                    }
                }).show()
    }

    fun showHttpExceptionDialog(context: Context) {
        val exception = HttpRequestException()
        ErrorAlertDialog(context).setTitle(exception.title)
                .setMessage(exception.errMessage)
                .setPositiveButton(context.getString(R.string.btn_ok))
                .setOnButtonClickListener(object : ErrorAlertDialog.DialogButtonClick{
                    override fun onPositiveClick() {
                    }

                    override fun onNegativeClick() {
                    }
                }).show()
    }
}