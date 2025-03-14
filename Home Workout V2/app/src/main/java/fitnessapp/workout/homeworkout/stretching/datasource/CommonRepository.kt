package fitnessapp.workout.homeworkout.stretching.datasource

import androidx.lifecycle.MutableLiveData
import com.admision.network.APIinterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.viewmodel.AdsIdDataResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommonRepository(apiInterface: APIinterface) {
    var apiInterface: APIinterface? = apiInterface

    fun getAdsId():MutableLiveData<AdsIdDataResponse>{
        val data = MutableLiveData<AdsIdDataResponse>()

        val call = apiInterface!!.getAdvertisementID()
        call.enqueue(object :Callback<ArrayList<AdsIdDataResponse>>{
            override fun onFailure(call: Call<ArrayList<AdsIdDataResponse>>, t: Throwable) {
                data.value = AdsIdDataResponse(Constant.getFailureCode())
            }

            override fun onResponse(call: Call<ArrayList<AdsIdDataResponse>>, response: Response<ArrayList<AdsIdDataResponse>>) {
                try {
                    if (response.isSuccessful){
                        val adsData = response.body()
                        data.value = adsData!![0]
                    }else {
                        try {
                            val inputAsString = response.errorBody()!!.source().inputStream().bufferedReader().use { it.readText() }

                            val sb = StringBuilder()
                            sb.append(inputAsString)
                            val adsData = Gson().fromJson<AdsIdDataResponse>(JSONObject(inputAsString).toString(), object : TypeToken<AdsIdDataResponse>() {}.type)
                            data.value = adsData
                        } catch (e: Exception) {
                            e.printStackTrace()
                            data.value = AdsIdDataResponse(Constant.getFailureCode())
                        }
                    }
                } catch (e: Exception) {

                    data.value = AdsIdDataResponse(Constant.getFailureCode())
                }
            }

        })
        return data
    }

}