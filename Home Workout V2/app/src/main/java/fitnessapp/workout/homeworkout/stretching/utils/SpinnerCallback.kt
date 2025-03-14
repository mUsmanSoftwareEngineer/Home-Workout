package fitnessapp.workout.homeworkout.stretching.utils


import fitnessapp.workout.homeworkout.stretching.objects.Spinner
import java.util.*


interface SpinnerCallback {
    abstract fun onDone(list: ArrayList<Spinner>)
}