package fitnessapp.workout.homeworkout.stretching.interfaces

interface PermissionListener {

    fun onPermissionClick()

    fun onPermissionAllow(isAllow: Boolean)
}