package fitnessapp.workout.homeworkout.stretching.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fitnessapp.workout.homeworkout.stretching.objects.BaseModel
import fitnessapp.workout.homeworkout.stretching.objects.ResultModel
import fitnessapp.workout.homeworkout.stretching.utils.Constant


abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {


    fun getError(): BaseModel {
        val baseModel = BaseModel()
        baseModel.code = Constant.STATUS_ERROR_CODE
        return baseModel
    }

    fun getErrorResult(): ResultModel {
        val resultModel = ResultModel()
        resultModel.code = Constant.STATUS_ERROR_CODE
        return resultModel
    }

    fun getEmptyResult(): ResultModel {
        val resultModel = ResultModel()
        resultModel.code = Constant.STATUS_SUCCESS_EMPTY_LIST_CODE
        return resultModel
    }

    fun getExistsResult(): ResultModel {
        val resultModel = ResultModel()
        resultModel.code = Constant.STATUS_SUCCESS_EXISTS_CODE
        return resultModel
    }

    fun getNotExistsResult(): ResultModel {
        val resultModel = ResultModel()
        resultModel.code = Constant.STATUS_SUCCESS_NOT_EXISTS_CODE
        return resultModel
    }

    fun getSuccessResult(): ResultModel {
        return ResultModel(Constant.STATUS_SUCCESS_CODE)
    }



}