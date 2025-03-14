package fitnessapp.workout.homeworkout.stretching.exceptions.networks

import fitnessapp.workout.homeworkout.stretching.utils.Debug


class HttpRequestException : NetworkException() {
    override var errMessage: String
        get() = "Server not responding"
        set(value) {}

    override var title: String
        get() = "Server Error"
        set(value) {}

    fun printStackTrace() {
        super.printStackTrace()
        Debug.e("Server Error","Server not responding")

    }

    fun getLocalizedMessage(): String {
        return super.getLocalizedMessage()
    }
}