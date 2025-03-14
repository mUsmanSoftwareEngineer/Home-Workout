package fitnessapp.workout.homeworkout.stretching.utils

import android.os.Environment
import java.io.File


internal object Constant {

    val GOAL_NAME: String?="GOAL_NAME"

    /*KG*/
    const val MIN_KG = 20
    const val MAX_KG = 997

    /*LB*/
    const val MIN_LB = 44.09
    const val MAX_LB = 2198.02

    /*FT*/
    const val MIN_FT = 0
    const val MAX_FT = 13

    /*IN*/
    const val MAX_IN = 7.9
    const val MIN_IN = 1.5

    /*CM*/
    const val MIN_CM = 20
    const val MAX_CM = 400


    val PREF_LANGUAGE= "pref_language"
    val PREF_LANGUAGE_NAME= "pref_language_name"



    val CHECK_LB_KG = "check_lb_kg"
    val FINISH_ACTIVITY = "finish_activity"
    val BANNER_TYPE = "BANNER_TYPE"
    val IS_PURCHASE = "is_purchase"
    val REC_BANNER_TYPE = "REC_BANNER_TYPE"
    val FOLDER_NAME = "Stretching Exercises"
    val CACHE_DIR = ".StretchingExercises/Cache"
    val TMP_DIR = (Environment
        .getExternalStorageDirectory().absolutePath
            + File.separator
            + FOLDER_NAME + "/tmp")
    val PATH = Environment.getExternalStorageDirectory()
        .absolutePath + File.separator + "" + FOLDER_NAME
    val FOLDER_RIDEINN_PATH = (Environment
        .getExternalStorageDirectory().absolutePath
            + File.separator
            + ".StretchingExercises")
    val USER_LATITUDE = "lat"
    const val APP_JSON = "application/json"
    val USER_LONGITUDE = "longi"
    val LOGIN_INFO = "login_info"
    val ARROW = "=>"
    val ERROR_CODE = -1
    val STATUS_ERROR_CODE = 5001
    val STATUS_SUCCESS_CODE = 5002
    val STATUS_SUCCESS_EXISTS_CODE = 5003
    val STATUS_SUCCESS_NOT_EXISTS_CODE = 5004
    val STATUS_SUCCESS_EMPTY_LIST_CODE = 5005
    val UTC = "UTC"

    val IS_SYNCING_START = "IS_SYNCING_START"
    val IS_SYNCING_STOP = "IS_SYNCING_STOP"

    val CONNECTIVITY_CHANGE = "CONNECTIVITY_CHANGE"

    val HOME_PLAN_TYPE_WORK_OUT = "workout"
    val HOME_PLAN_TYPE_TRAINING = "training"
    val HOME_PLAN_TYPE_PLAN = "plan"

    val PREF_IS_INSTRUCTION_SOUND_ON ="pref_is_instruction_sound_on"
    val PREF_IS_COACH_SOUND_ON ="pref_is_coach_sound_on"
    val PREF_IS_SOUND_MUTE ="pref_is_sound_mute"
    val PREF_IS_MUSIC_MUTE ="pref_is_music_mute"
    val PREF_IS_MUSIC_REPEAT ="pref_is_music_repeat"
    val PREF_IS_MUSIC_SHUFFLE ="pref_is_music_shuffle"
    val PREF_REST_TIME ="pref_rest_time"
    val PREF_READY_TO_GO_TIME ="pref_ready_to_go_time"
    val PREF_IS_FIRST_TIME ="pref_is_first_time"
    val IS_FIRST_TIME ="IS_FIRST_TIME"
    val PREF_WHATS_YOUR_GOAL ="pref_whats_your_goal"
    val PREF_IS_REMINDER_SET ="pref_is_reminder_set"
    val PREF_IS_KEEP_SCREEN_ON ="pref_is_keep_screen_on"

    const val PREF_WEIGHT_UNIT = "PREF_WEIGHT_UNIT"
    const val PREF_HEIGHT_UNIT = "PREF_HEIGHT_UNIT"
    const val PREF_IN_CM_UNIT = "PREF_IN_CM_UNIT"
    val CENTI_METER: String = "CENTI_METER"
    const val PREF_KG_LB_UNIT = "PREF_KG_LB_UNIT"
    const val PREF_LAST_INPUT_WEIGHT = "PREF_LAST_INPUT_WEIGHT"
    const val PREF_LAST_INPUT_FOOT = "PREF_LAST_INPUT_FOOT"
    const val PREF_LAST_INPUT_INCH = "PREF_LAST_INPUT_INCH"
    const val PREF_DOB = "PREF_DOB"
    const val PREF_IS_MUSIC_SELECTED = "PREF_IS_MUSIC_SELECTED"
    const val PREF_MUSIC = "PREF_MUSIC"
    const val PREF_GENDER = "PREF_GENDER"
    const val PREF_IS_WEEK_GOAL_DAYS_SET = "PREF_IS_WEEK_GOAL_DAYS_SET"
    const val PREF_WEEK_GOAL_DAYS = "PREF_WEEK_GOAL_DAYS"
    const val PREF_FIRST_DAY_OF_WEEK = "PREF_FIRST_DAY_OF_WEEK"
    const val PREF_RANDOM_DISCOVER_PLAN = "PREF_RANDOM_DISCOVER_PLAN"
    const val PREF_RANDOM_DISCOVER_PLAN_DATE = "PREF_RANDOM_DISCOVER_PLAN_DATE"

    const val PREF_KEY_PURCHASE_STATUS = "KeyPurchaseStatus"

    const val PlanTypeWorkout = "Workouts"
    const val PlanTypeWorkoutNew = "Home Workouts"

    const val PlanLvlTitle = "Title"
    const val PlanLvlBeginner = "Beginner"
    const val PlanLvlIntermediate = "Intermediate"
    const val PlanLvlAdvanced = "Advanced"

    const val PlanTypeWorkouts = "Workouts"
    const val PlanTypeSubPlan = "SubPlan"
    const val PlanTypeMyTraining = "MyTraining"

    const val MyTrainingThumbnail = "icon_thumbnail_my_training"
    const val MyTrainingTypeImage = "ic_type_my_training"

    const val PlanDaysYes = "YES"
    const val PlanDaysNo = "NO"

    const val extra_day_id = "extra_day_id"
    const val workout_type_step = "s"

    const val WORKOUT_TIME_FORMAT = "mm:ss"

    const val SEC_DURATION_CAL = 0.08
    const val DEFAULT_REST_TIME = 15L
    const val DEFAULT_READY_TO_GO_TIME = 15L
    const val CapDateFormatDisplay = "yyyy-MM-dd HH:mm:ss"

    const val extraReminderId = "extraReminderId"

    const val DEF_KG = "KG"
    const val DEF_LB = "LB"
    const val DEF_IN = "IN"
    const val DEF_FT = "FT"
    const val DEF_CM = "CM"

    const val MALE = "Male"
    const val FEMALE = "Female"

    const val WEIGHT_TABLE_DATE_FORMAT = "yyyy-MM-dd"
    const val DATE_TIME_24_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT = "yyyy-MM-dd"

    const val Discover_Pain_Relief = "Pain Relief"
    const val Discover_Training = "Training"
    const val Discover_Flexibility = "Flexibility"
    const val Discover_ForBeginner = "ForBeginner"
    const val Discover_PostureCorrection = "PostureCorrection"
    const val Discover_FatBurning = "FatBurning"
    const val Discover_BodyFocus = "BodyFocus"
    const val Discover_Duration = "Duration"

    const val FROM_SETTING = "from setting"

    const val RESPONSE_FAILURE_CODE = 901
    const val RESPONSE_SUCCESS_CODE = 200
    const val VALIDATION_FAILED_CODE = 903
    const val USER_NOT_FOUND = 333

    fun getSuccessCode(): Int {
        return RESPONSE_SUCCESS_CODE
    }

    fun getFailureCode(): Int {
        return RESPONSE_FAILURE_CODE
    }

    fun getUserNotFoundCode(): Int {
        return USER_NOT_FOUND
    }







    var AD_TYPE_FB_GOOGLE = "AD_TYPE_FB_GOOGLE"
    var GOOGLE_BANNER = "GOOGLE_BANNER"
    var GOOGLE_INTERSTITIAL = "GOOGLE_INTERSTITIAL"
    var GOOGLE_REWARDED_VIDEO = "GOOGLE_REWARDED_VIDEO"

    var FB_BANNER = "FB_BANNER"
    var FB_BANNER_RECTANGLE_AD = "FB_BANNER_RECTANGLE_AD"
    var FB_INTERSTITIAL = "FB_INTERSTITIAL"


    var EXTRA_REMINDER_ID = "Reminder_ID"
    var SPLASH_SCREEN_COUNT = "splash_screen_count"
    var START_BTN_COUNT = "start_btn_count"
    var EXIT_BTN_COUNT = "exit_btn_count"


    var STATUS_ENABLE_DISABLE = "STATUS_ENABLE_DISABLE"


    //Test Keys
    const val GOOGLE_ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val GOOGLE_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val GOOGLE_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    //Real Keys
//    const val GOOGLE_ADMOB_APP_ID = "ca-app-pub-4921234158243313~5286900329"
//    const val GOOGLE_BANNER_ID = "ca-app-pub-4921234158243313/3973818656"
//    const val GOOGLE_INTERSTITIAL_ID = "ca-app-pub-4921234158243313/2660736981"


    const val FB_BANNER_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
    const val FB_INTERSTITIAL_ID = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"


    var AD_FACEBOOK = "facebook"
    var AD_GOOGLE = "google"
    val AD_TYPE_FACEBOOK_GOOGLE: String = AD_GOOGLE


    const val ENABLE = "Enable"
    const val DISABLE = "Disable"
    val ENABLE_DISABLE: String = ENABLE


//    var DB_PATH = "data/data/com.ninetynineapps.femalestretchingexercises1/databases/"
    var DB_PATH = "data/data/fitnessapp.workout.homeworkoutapps.homeworkoutexercises/databases/"
    var DATABASE_NAME = "StretchingEx.db";
}