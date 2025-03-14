package fitnessapp.workout.homeworkout.stretching.exceptions


import fitnessapp.workout.homeworkout.stretching.utils.Debug

class PermissionException : BaseException() {

    override fun printStackTrace() {
        super.printStackTrace()
        Debug.e("Permission","Permission denied" )
    }
}