package fitnessapp.workout.homeworkout.stretching.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import fitnessapp.workout.homeworkout.stretching.objects.ResultModel
import fitnessapp.workout.homeworkout.stretching.objects.UserModel
import java.io.File

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    var profileObeservable: MutableLiveData<ResultModel>? = null
    /*   var db: FirebaseFirestore? = null
       lateinit var storageRef: StorageReference
       lateinit var uploadTask: UploadTask*/

    init {
        profileObeservable = MutableLiveData()
    }

    /* fun updateProfile(db: FirebaseFirestore,storageRef: StorageReference): LiveData<ResultModel>? {
         this.db = db
         this.storageRef = storageRef
         return profileObeservable
     }*/

    fun updtaeUserInfo(user: UserModel) {
        /*  db!!.collection(FirestoreDatabase.USER).document(user.uid!!)
              .set(user)
              .addOnSuccessListener(object : OnSuccessListener<Void> {
                  override fun onSuccess(p0: Void?) {

                      val resultModel = ResultModel(Constant.STATUS_SUCCESS_CODE, user!!)
                      profileObeservable!!.value = resultModel

                  }
              }).addOnFailureListener(object : OnFailureListener {
                  override fun onFailure(p0: Exception) {
                      profileObeservable!!.value = getErrorResult()
                  }
              })*/
    }

    fun uploadProfilePhoto(user: UserModel, fileUri: File) {
        /*  val fileName = user.uid;
          val profileRef =
              storageRef.child(FirestoreDatabase.URL_PROFILES + fileName)
          uploadTask = profileRef.putFile(Uri.fromFile(fileUri))
          uploadTask.addOnFailureListener(object : OnFailureListener {
              override fun onFailure(p0: java.lang.Exception) {
              }
          }).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
              override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                  var filePath: String? = null
                  profileRef.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri> {
                      override fun onSuccess(uri: Uri?) {
                          filePath = uri.toString()

                          user.pic=filePath
                          updtaeUserInfo(user)

                      }
                  })
              }
          })
      }*/
    }}