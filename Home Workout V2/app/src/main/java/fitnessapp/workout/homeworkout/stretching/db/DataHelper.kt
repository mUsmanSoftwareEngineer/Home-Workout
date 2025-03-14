package fitnessapp.workout.homeworkout.stretching.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import fitnessapp.workout.homeworkout.stretching.objects.*
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// Database name
//private const val DBName = "databases/StretchingEx.db"
private const val DBName = "StretchingEx.db"

// Home Plan table
private const val PlanTable = "HomePlanTable"
private const val PlanId = "PlanId"
private const val PlanName = "PlanName"
private const val PlanProgress = "PlanProgress"
private const val PlanText = "PlanText"
private const val PlanLvl = "PlanLvl"
private const val PlanImage = "PlanImage"
private const val PlanDays = "PlanDays"
private const val PlanType = "PlanType"
private const val ShortDes = "ShortDes"
private const val Introduction = "Introduction"
private const val PlanWorkouts = "PlanWorkouts"
private const val PlanMinutes = "PlanMinutes"
private const val IsPro = "IsPro"
private const val HasSubPlan = "HasSubPlan"
private const val TestDes = "TestDes"
private const val PlanThumbnail = "PlanThumbnail"
private const val PlanTypeImage = "PlanTypeImage"
private const val ParentPlanId = "ParentPlanId"
private const val PlanSort = "sort"
private const val DefaultSort = "DefaultSort"
private const val DiscoverCatName = "DiscoverCatName"
private const val DiscoverIcon = "DiscoverIcon"

// Plan days table getting by planId
private const val PlanDaysTable = "PlanDaysTable"
private const val DayId = "DayId"
private const val DayName = "DayName"
private const val WeekName = "WeekName"
private const val IsCompleted = "IsCompleted"
private const val DayProgress = "DayProgress"

// DayExTable table getting by DayId
private const val DayExTable = "DayExTable"
private const val HomeExSingleTable = "HomeExSingleTable"
private const val DayExId = "Id"
private const val ExId = "ExId"
private const val ExTime = "ExTime"
private const val UpdatedExTime = "UpdatedExTime"
private const val ReplaceExId = "ReplaceExId"

// ExerciseTable getting
private const val ExerciseTable = "ExerciseTable"
private const val ExName = "ExName"
private const val ExUnit = "ExUnit"
private const val ExPath = "ExPath"
private const val ExVideoPath = "ExVideoPath"
private const val ExDescription = "ExDescription"
private const val ExVideo = "ExVideo"
private const val ExReplaceTime = "ReplaceTime"

// ReminderTable getting
private const val ReminderTable = "ReminderTable"
private const val RId = "RId"
private const val RemindTime = "RemindTime"
private const val Days = "Days"
private const val IsActive = "IsActive"

// WeightTable getting
private const val WeightTable = "WeightTable"
private const val WeightId = "WeightId"
private const val WeightKg = "WeightKg"
private const val WeightLb = "WeightLb"
private const val WeightDate = "WeightDate"
private const val CurrentTimeStamp = "CurrentTimeStamp"

// HistoryTable getting
private const val HistoryTable = "HistoryTable"
private const val HId = "HId"
private const val HPlanName = "HPlanName"
private const val HPlanId = "HPlanId"
private const val HDayName = "HDayName"
private const val HDayId = "HDayId"
private const val HBurnKcal = "HBurnKcal"
private const val HTotalEx = "HTotalEx"
private const val HKg = "HKg"
private const val HFeet = "HFeet"
private const val HInch = "HInch"
private const val HFeelRate = "HFeelRate"
private const val HCompletionTime = "HCompletionTime"
private const val HDateTime = "HDateTime"

//MyTrainingCategoryTable
private const val MyTrainingCategoryTable = "MyTrainingCategoryTable"
private const val CId = "CId"
private const val CName = "CName"

//MyTrainningCatExTable
private const val MyTrainningCatExTable = "MyTrainningCatExTable"
private const val CatExId = "CatExId"
private const val CatId = "CatId"

//MyTrainingExTable
private const val MyTrainingExTable = "MyTrainingExTable"

//MusicTable
private const val MusicTable = "MusicTable"
private const val mId = "id"
private const val mName = "name"
private const val mDuration = "duration"
private const val mFileName = "fileName"
private const val mIsPro = "isPro"
private const val mIsFromRaw = "isFromRaw"


class DataHelper(private val mContext: Context) {


    /*private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            val myPath: String = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(
                myPath,
                null,
                SQLiteDatabase.OPEN_READONLY or SQLiteDatabase.NO_LOCALIZED_COLLATORS
            )
        } catch (e: SQLiteException) {
            //database does't exist yet.
            Log.v(TAG, "[checkDataBase] - Database does not exist... YET!")
        } finally {
            checkDB?.close()
        }
        return if (checkDB != null) true else false
    }*/

    fun checkDBExist(): Boolean {
        var isExist = false
        val dbFile = mContext.getDatabasePath(DBName)

        if (!dbFile.exists()) {
            try {
                if (copyDatabase(dbFile)) {
                    if (dbFile.exists()) {
                        isExist = true
                    }
                } else if (dbFile.delete()) {
                    if (copyDatabase(dbFile)) {
                        if (dbFile.exists()) {
                            isExist = true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return isExist
    }

    private fun getReadWriteDB(): SQLiteDatabase {
        val dbFile = mContext.getDatabasePath(DBName)
        if (!dbFile.exists()) {
            try {
                val checkDB = mContext.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null)
                checkDB?.close()
                copyDataBase()
//                copyDatabase(dbFile)
            } catch (e: Exception) {
                Log.e("TAG", "getReadWriteDB:::::Not Working===>>>  " + e.printStackTrace())
                e.printStackTrace()
                throw RuntimeException("Error creating source database", e)
            }
        }

        val database = SQLiteDatabase.openDatabase(
            dbFile.path,
            null,
            SQLiteDatabase.OPEN_READWRITE,
            object : DatabaseErrorHandler {
                override fun onCorruption(dbObj: SQLiteDatabase?) {
                    Log.e("TAG", "onCorruption::::::===>>> " + dbObj.toString())
                }
            })
        if (database.version == 0) {
            updateTableData(database)
            database.version = 1
        }
        return database
    }

    private fun updateTableData(database: SQLiteDatabase?) {
        updateData(database, "Fast relieve shoulder tension and prevent tightness", 27)
    }

    private fun updateData(database: SQLiteDatabase?, planText: String, id: Int) {
        try {
            database!!.execSQL("UPDATE $PlanTable SET $PlanText='$planText' WHERE $PlanId='$id'")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            database!!.execSQL("UPDATE $PlanTable SET $ShortDes='$planText' WHERE $PlanId='$id'")
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun copyDatabase(dbFile: File): Boolean {
        var isSuccess = false
        var ins: InputStream? = null
        var os: FileOutputStream? = null

        try {
            ins = mContext.assets.open(DBName)
            os = FileOutputStream(dbFile)

            val buffer = ByteArray(1024)
            while (ins.read(buffer) > 0) {
                os.write(buffer)
            }

            isSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (os != null) {
                os.flush()
                os.close()
            }
            if (ins != null) {
                ins.close()
            }
        }

        return isSuccess

    }


    @Throws(IOException::class)
    private fun copyDataBase() {
        val myInput = mContext.assets.open("databases/" + Constant.DATABASE_NAME)
        val outFileName = Constant.DB_PATH + Constant.DATABASE_NAME
        val myOutput = FileOutputStream(outFileName)
        val buffer = ByteArray(1024)
        while (myInput.read(buffer) > 0) {
            myOutput.write(buffer)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

    // Todo Home Plan table Process
    fun getHomePlanList(strPlanType: String): ArrayList<HomePlanTableClass> {

        val arrPlan: ArrayList<HomePlanTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
//            val query =
//                "Select * From $PlanTable where $PlanType = '$strPlanType' order by $PlanSort"//Original QUERY
            val query =
                "Select * From $PlanTable where $PlanType = '$strPlanType' order by $PlanSort desc"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = HomePlanTableClass()
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                    aClass.planProgress =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                    aClass.planText = cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                    aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                    aClass.planImage = cursor.getString(cursor.getColumnIndexOrThrow(PlanImage))
                    Log.d("checkPlanImage", aClass.planImage.toString())
                    aClass.planDays = cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                    aClass.planType = cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                    aClass.planWorkouts =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanWorkouts))
                    aClass.planMinutes = cursor.getString(cursor.getColumnIndexOrThrow(PlanMinutes))
                    aClass.planTestDes = cursor.getString(cursor.getColumnIndexOrThrow(TestDes))
                    aClass.parentPlanId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ParentPlanId))
                    aClass.planThumbnail =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanThumbnail))
                    aClass.planTypeImage =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanTypeImage))
                    aClass.shortDes = cursor.getString(cursor.getColumnIndexOrThrow(ShortDes))
                    aClass.introduction =
                        cursor.getString(cursor.getColumnIndexOrThrow(Introduction))
                    aClass.isPro =
                        cursor.getString(cursor.getColumnIndexOrThrow(IsPro)).equals("true")
                    aClass.hasSubPlan =
                        cursor.getString(cursor.getColumnIndexOrThrow(HasSubPlan)).equals("true")
                    arrPlan.add(aClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }
        return arrPlan
    }

    fun getHomeSubPlanList(parentPlanId: String): ArrayList<HomePlanTableClass> {

        val arrPlan: ArrayList<HomePlanTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            //val query = "Select * From $PlanTable where FIND_IN_SET $ParentPlanId = '$parentPlanId' order by $PlanSort"
            //val query = "SELECT * FROM $PlanTable WHERE $ParentPlanId  LIKE '%$parentPlanId,%' or $ParentPlanId  LIKE '%,$parentPlanId%' or $ParentPlanId  LIKE '$parentPlanId' order by $PlanSort"
            val query = "SELECT * FROM $PlanTable order by $PlanSort"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    var parentPlanIdsStr = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            ParentPlanId
                        )
                    )

                    if (parentPlanIdsStr.isNullOrEmpty().not() && parentPlanIdsStr.equals("0")
                            .not()
                    ) {
                        var parentIdList = parentPlanIdsStr.split(",")

                        if (!parentIdList.isNullOrEmpty() && parentIdList.contains(parentPlanId)) {

                            val aClass = HomePlanTableClass()
                            aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                            aClass.planName =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                            aClass.planProgress =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                            aClass.planText =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                            aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                            aClass.planImage = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    PlanImage
                                )
                            )
                            aClass.planDays =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                            aClass.planType =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                            aClass.planWorkouts =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanWorkouts))
                            aClass.planMinutes = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    PlanMinutes
                                )
                            )
                            aClass.planTestDes = cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    TestDes
                                )
                            )
                            aClass.parentPlanId = parentPlanId
                            aClass.planThumbnail =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanThumbnail))
                            aClass.planTypeImage =
                                cursor.getString(cursor.getColumnIndexOrThrow(PlanTypeImage))
                            aClass.shortDes =
                                cursor.getString(cursor.getColumnIndexOrThrow(ShortDes))
                            aClass.introduction =
                                cursor.getString(cursor.getColumnIndexOrThrow(Introduction))
                            aClass.discoverIcon =
                                cursor.getString(cursor.getColumnIndexOrThrow(DiscoverIcon))
                            aClass.isPro =
                                cursor.getString(cursor.getColumnIndexOrThrow(IsPro)).equals("true")
                            aClass.hasSubPlan =
                                cursor.getString(cursor.getColumnIndexOrThrow(HasSubPlan))
                                    .equals("true")
                            arrPlan.add(aClass)

                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }
        return arrPlan
    }

    fun getPlanByPlanId(planId: Int): HomePlanTableClass? {
        var aClass: HomePlanTableClass? = null
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()
            val query =
                "Select * From $PlanTable where $PlanId = '$planId'"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {

                cursor.moveToFirst()
                aClass = HomePlanTableClass()
                aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                aClass.planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                aClass.planProgress =
                    cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                aClass.planText = cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                aClass.planImage = cursor.getString(cursor.getColumnIndexOrThrow(PlanImage))
                aClass.planDays = cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                aClass.planType = cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                aClass.planWorkouts =
                    cursor.getString(cursor.getColumnIndexOrThrow(PlanWorkouts))
                aClass.planMinutes = cursor.getString(cursor.getColumnIndexOrThrow(PlanMinutes))
                aClass.planTestDes = cursor.getString(cursor.getColumnIndexOrThrow(TestDes))
                aClass.parentPlanId = cursor.getString(cursor.getColumnIndexOrThrow(ParentPlanId))
                aClass.planThumbnail =
                    cursor.getString(cursor.getColumnIndexOrThrow(PlanThumbnail))
                aClass.planTypeImage =
                    cursor.getString(cursor.getColumnIndexOrThrow(PlanTypeImage))
                aClass.shortDes = cursor.getString(cursor.getColumnIndexOrThrow(ShortDes))
                aClass.introduction =
                    cursor.getString(cursor.getColumnIndexOrThrow(Introduction))
                aClass.isPro =
                    cursor.getString(cursor.getColumnIndexOrThrow(IsPro)).equals("true")
                aClass.hasSubPlan =
                    cursor.getString(cursor.getColumnIndexOrThrow(HasSubPlan)).equals("true")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return aClass
    }

    // Todo reset Plan Day
    fun resetPlanDay(strDayId: String) {

        var db: SQLiteDatabase? = null

        val contentValues = ContentValues()
        contentValues.put(UpdatedExTime, "")
        contentValues.put(ReplaceExId, "")

        try {
            db = getReadWriteDB()
            // Todo clear  full body workout progress
            db.update(DayExTable, contentValues, "$DayId = $strDayId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

    }

    fun resetPlanExc(ExcList: ArrayList<HomeExTableClass>) {

        var db: SQLiteDatabase? = null

        val contentValues = ContentValues()
        contentValues.put(UpdatedExTime, "")
        contentValues.put(ReplaceExId, "")

        try {
            db = getReadWriteDB()
            for (item in ExcList) {
                val contentValues = ContentValues()
                contentValues.put(UpdatedExTime, "")
                contentValues.put(ReplaceExId, "")
                contentValues.put(PlanSort, item.defaultPlanSort)
                db.update(HomeExSingleTable, contentValues, "$DayExId = ${item.dayExId}", null)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

    }

    // Todo Edit exercise list
    fun getReplaceExList(strExId: String): ArrayList<ExTableClass> {

        val arrExTableClass: ArrayList<ExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "SELECT * FROM $ExerciseTable WHERE $ExId != $strExId"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = ExTableClass()
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    aClass.exReplaceTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExReplaceTime))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    arrExTableClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return arrExTableClass
    }

    fun getOriginalPlanExTime(dayExId: Int): String? {

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var exTime: String? = null

        val contentValues = ContentValues()
        contentValues.put(UpdatedExTime, "")

        try {
            db = getReadWriteDB()
            // Todo clear  full body workout progress
            val query = "Select $ExTime From $HomeExSingleTable WHERE $DayExId = $dayExId"
            cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return exTime
    }

    fun getOriginalDetailExTime(dayExId: Int): String? {

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var exTime: String? = null

//        val contentValues = ContentValues()
//        contentValues.put(UpdatedExTime, "")

        try {
            db = getReadWriteDB()
            // Todo clear  full body workout progress
            val query = "Select $ExTime From $DayExTable WHERE $DayExId = $dayExId"
            cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return exTime
    }


    fun getOriginalSingleDetailExTime(dayExId: Int): String? {

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var exTime: String? = null

//        val contentValues = ContentValues()
//        contentValues.put(UpdatedExTime, "")

        try {
            db = getReadWriteDB()
            // Todo clear  full body workout progress
            val query = "Select $ExTime From $DayExTable WHERE $DayExId = $dayExId"
            cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return exTime
    }

    fun getOriginalPlanExID(dayExId: Int): String? {

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var exTime: String? = null

        val contentValues = ContentValues()
        contentValues.put(UpdatedExTime, "")

        try {
            db = getReadWriteDB()
            // Todo clear  full body workout progress
            val query = "Select $ExId From $HomeExSingleTable WHERE $DayExId = $dayExId"
            cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExId))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return exTime
    }

    fun updatePlanExTime(dayExId: Int, strExTime: String): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(UpdatedExTime, strExTime)

        try {
            db = getReadWriteDB()

            count = db.update(HomeExSingleTable, cv, "$DayExId = $dayExId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun updateDayExTime(dayExId: Int, strExTime: String): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(UpdatedExTime, strExTime)

        try {
            db = getReadWriteDB()

            count = db.update(DayExTable, cv, "$DayExId = $dayExId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun updatePlanEx(item: HomeExTableClass): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(PlanSort, item.planSort!!.toInt())

        try {
            db = getReadWriteDB()

            count = db.update(HomeExSingleTable, cv, "$DayExId = ${item.dayExId}", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun replaceExercise(strDayExId: String, strExId: String, strExTime: String): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(ReplaceExId, strExId)
        cv.put(UpdatedExTime, strExTime)
        cv.put(ExTime, strExTime)

        try {
            db = getReadWriteDB()

            count = db.update(HomeExSingleTable, cv, "$DayExId = ?", arrayOf(strDayExId))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun getProgressByPlanIdDayID(dayId: String): String {

        var completedCount = " "

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "Select $DayProgress From $PlanDaysTable where $DayId = $dayId"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.moveToFirst()) {

                completedCount = cursor.getString(cursor.getColumnIndexOrThrow(DayProgress))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }
        Log.d("checkproVar", completedCount)
        return completedCount
    }

    // Todo Plan table Process
    fun getCompleteDayCountByPlanId(strId: String): Int {

        var completedCount = 0

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "Select $DayId From $PlanDaysTable where $PlanId = $strId And $IsCompleted = '1'"
            cursor = db.rawQuery(query, null)
            if (cursor != null) {
                completedCount = cursor.count
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }
        Log.d("checkCount", completedCount.toString())
        return completedCount
    }

    // Todo Plan table Process
    fun getPlanNameByPlanId(strId: String): String {

        var planName = ""

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select $PlanName From $PlanTable where $PlanId = $strId"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }
        return planName
    }

    // Todo Getting Home Detail exercise data
    fun getHomeDetailExList(strPlanId: String): ArrayList<HomeExTableClass> {

        val arrDayExClass: ArrayList<HomeExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "SELECT DX.$DayExId, DX.$PlanId," +
                    "       DX.$DayId," +
                    "       DX.$IsCompleted," +
                    "       DX.$UpdatedExTime," +
                    "       DX.$ReplaceExId," +
                    "       DX.$PlanSort," +
                    "       DX.$DefaultSort," +
                    "       CASE WHEN DX.$UpdatedExTime != ''" +
                    "       THEN DX.$UpdatedExTime" +
                    "       ELSE DX.$ExTime" +
                    "       END as $ExTime, " +
                    "       CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END as $ExId, " +
                    "EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $HomeExSingleTable as DX " +
                    "INNER JOIN $ExerciseTable as EX ON " +
                    "(CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END)" +
                    "= EX.$ExId WHERE DX.$PlanId = $strPlanId ORDER BY DX.$PlanSort"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = HomeExTableClass()
                    aClass.dayExId = cursor.getString(cursor.getColumnIndexOrThrow(DayExId))
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndexOrThrow(DayId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.isCompleted = cursor.getString(cursor.getColumnIndexOrThrow(IsCompleted))
                    aClass.updatedExTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(UpdatedExTime))
                    aClass.replaceExId = cursor.getString(cursor.getColumnIndexOrThrow(ReplaceExId))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    aClass.planSort = cursor.getString(cursor.getColumnIndexOrThrow(PlanSort))
                    aClass.defaultPlanSort =
                        cursor.getString(cursor.getColumnIndexOrThrow(DefaultSort))
                    arrDayExClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return arrDayExClass
    }

    fun updateCompleteHomeExByDayExId(strDayExId: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(IsCompleted, "1")

        try {
            db = getReadWriteDB()
            mCount = db.update(HomeExSingleTable, contentValues, "$ExId = $strDayExId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
//                db.close()
                db.releaseReference()
            }
        }

        return mCount
    }


    // Todo Reminder Table methods
    fun getRemindersList(): ArrayList<ReminderTableClass> {
        val arrReminder = ArrayList<ReminderTableClass>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $ReminderTable order by $RId DESC"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val reminderClass = ReminderTableClass()
                    reminderClass.rId = cursor.getString(cursor.getColumnIndexOrThrow(RId))
                    reminderClass.remindTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(RemindTime))
                    reminderClass.days = cursor.getString(cursor.getColumnIndexOrThrow(Days))
                    reminderClass.isActive =
                        cursor.getString(cursor.getColumnIndexOrThrow(IsActive))
                    arrReminder.add(reminderClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrReminder
    }

    fun getRemindersListString(): String {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var reminders = ""
        try {
            db = getReadWriteDB()
            val query = "Select * From $ReminderTable order by $RId DESC"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    if (reminders.isNullOrEmpty()) {
                        reminders = cursor.getString(cursor.getColumnIndexOrThrow(RemindTime))
                    } else {
                        reminders = "$reminders, " + cursor.getString(
                            cursor.getColumnIndexOrThrow(RemindTime)
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return reminders
    }

    fun getReminderById(mid: String): ReminderTableClass {
        val reminderClass = ReminderTableClass()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $ReminderTable where $RId=$mid"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    reminderClass.rId = cursor.getString(cursor.getColumnIndexOrThrow(RId))
                    reminderClass.remindTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(RemindTime))
                    reminderClass.days = cursor.getString(cursor.getColumnIndexOrThrow(Days))
                    reminderClass.isActive =
                        cursor.getString(cursor.getColumnIndexOrThrow(IsActive))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return reminderClass
    }

    fun addReminder(reminderClass: ReminderTableClass): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(RemindTime, reminderClass.remindTime)
        contentValues.put(Days, reminderClass.days)
        contentValues.put(IsActive, reminderClass.isActive)

        try {
            db = getReadWriteDB()
            mCount = db.insert(ReminderTable, null, contentValues).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun deleteReminder(id: String): Boolean {
        var isSuccess = false
        var db: SQLiteDatabase? = null

        try {
            db = getReadWriteDB()
            val mCount = db.delete(ReminderTable, "$RId=?", arrayOf(id))
            if (mCount > 0) {
                isSuccess = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return isSuccess
    }

    fun updateReminder(strReminderId: String, strIsActive: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(IsActive, strIsActive)

        try {
            db = getReadWriteDB()
            mCount = db.update(ReminderTable, contentValues, "$RId = $strReminderId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun updateReminderDays(strReminderId: String, strDays: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(Days, strDays)

        try {
            db = getReadWriteDB()
            mCount = db.update(ReminderTable, contentValues, "$RId = $strReminderId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun updateReminderTimes(strReminderId: String, strTime: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(RemindTime, strTime)

        try {
            db = getReadWriteDB()
            mCount = db.update(ReminderTable, contentValues, "$RId = $strReminderId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    // Todo Weight related data
    fun getWeightList(): ArrayList<WeightTableClass> {

        val arrWeightTableClass: ArrayList<WeightTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()
            val query = "Select * From $WeightTable"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = WeightTableClass()
                    aClass.weightId = cursor.getString(cursor.getColumnIndexOrThrow(WeightId))
                    aClass.weightKg = cursor.getString(cursor.getColumnIndexOrThrow(WeightKg))
                    aClass.weightLb = cursor.getString(cursor.getColumnIndexOrThrow(WeightLb))
                    aClass.weightDate = cursor.getString(cursor.getColumnIndexOrThrow(WeightDate))
                    aClass.currentTimeStamp =
                        cursor.getString(cursor.getColumnIndexOrThrow(CurrentTimeStamp))
                    arrWeightTableClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrWeightTableClass
    }

    // Todo get Max Weight
    @SuppressLint("Range")
    fun getMaxWeight(): String {

        var strMaxWeight = "0"
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val maxkg = "maxkg"
        try {
            db = getReadWriteDB()

            val query = "SELECT MAX(CAST($WeightKg as INTEGER)) as $maxkg from $WeightTable"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    strMaxWeight = cursor.getString(cursor.getColumnIndex(maxkg))

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return strMaxWeight
    }

    // Todo get Min Weight
    @SuppressLint("Range")
    fun getMinWeight(): String {

        var strMinWeight = "0"
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val minkg = "minkg"
        try {
            db = getReadWriteDB()

            val query = "SELECT MIN(CAST($WeightKg as INTEGER)) as $minkg from $WeightTable"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    strMinWeight = cursor.getString(cursor.getColumnIndex(minkg))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return strMinWeight
    }

    // get weight for graph
    fun getUserWeightData(): ArrayList<HashMap<String, String>> {

        //val arrKg = ArrayList<String>()
        val arrDateChange = ArrayList<HashMap<String, String>>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()

            val query =
                "Select * from $WeightTable where $WeightKg != '0' group by $WeightDate order by $WeightDate"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    //                    if (!arrKg.contains(cursor.getString(cursor.getColumnIndexOrThrow(WeightKG)))) {
                    //arrKg.add(cursor.getString(cursor.getColumnIndexOrThrow(WeightKG)))
                    val hashMap = HashMap<String, String>()
                    hashMap.put("KG", cursor.getString(cursor.getColumnIndexOrThrow(WeightKg)))
                    hashMap.put(
                        "DT",
                        cursor.getString(cursor.getColumnIndexOrThrow(WeightDate))
                    )
                    arrDateChange.add(hashMap)
                    //                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrDateChange
    }

    //* Todo Update weight if weight exist *//*
    fun updateWeight(strDate: String, strWeightKG: String, strWeightLB: String): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(WeightKg, strWeightKG)
        cv.put(WeightLb, strWeightLB)

        try {
            db = getReadWriteDB()

            count = db.update(WeightTable, cv, "$WeightDate = ?", arrayOf(strDate))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    //* Todo add user Weight query *//*
    fun addUserWeight(strWeightKG: String, strDate: String, strweightLB: String): Int {
        var count = 0
        var db: SQLiteDatabase? = null

        val row = ContentValues()
//        row.put(Id,"")
        row.put(WeightKg, strWeightKG)
        row.put(WeightDate, strDate)
        row.put(WeightLb, strweightLB)
        row.put(CurrentTimeStamp, Utils.parseTime(Date().time, "yyyy-MM-dd HH:mm:ss"))

        try {
            db = getReadWriteDB()
            count = db.insert(WeightTable, null, row).toInt()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count
    }

    //* Todo Check weight record is exist or not *//*
    fun weightExistOrNot(strDate: String): Boolean {

        var boolResult = false
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()

            val query = "Select * From $WeightTable Where $WeightDate = '$strDate'"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                boolResult = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }

            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return boolResult
    }

    fun getWeightForDate(strDate: String): String {

        var strMinWeight = "0"
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()

            val query = "Select * From $WeightTable Where $WeightDate = '$strDate'"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                strMinWeight = cursor.getString(cursor.getColumnIndexOrThrow(WeightKg))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }

            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return strMinWeight
    }

    //* Todo History *//*
    fun getHistoryList(): ArrayList<HistoryTableClass> {

        val arrWeightTableClass: ArrayList<HistoryTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()
            val query = "Select * From $HistoryTable"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = HistoryTableClass()
                    aClass.hId = cursor.getString(cursor.getColumnIndexOrThrow(HId))
                    aClass.hPlanName = cursor.getString(cursor.getColumnIndexOrThrow(HPlanName))
                    aClass.hPlanId = cursor.getString(cursor.getColumnIndexOrThrow(HPlanId))
                    aClass.hDayName = cursor.getString(cursor.getColumnIndexOrThrow(HDayName))
                    aClass.hBurnKcal = cursor.getString(cursor.getColumnIndexOrThrow(HBurnKcal))
                    aClass.hTotalEx = cursor.getString(cursor.getColumnIndexOrThrow(HTotalEx))
                    aClass.hKg = cursor.getString(cursor.getColumnIndexOrThrow(HKg))
                    aClass.hFeet = cursor.getString(cursor.getColumnIndexOrThrow(HFeet))
                    aClass.hInch = cursor.getString(cursor.getColumnIndexOrThrow(HInch))
                    aClass.hFeelRate = cursor.getString(cursor.getColumnIndexOrThrow(HFeelRate))
                    aClass.hCompletionTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(HCompletionTime))
                    aClass.hDateTime = cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))
                    arrWeightTableClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrWeightTableClass
    }

    @SuppressLint("Range")
    fun getHistoryTotalMinutes(): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totMinutesSum = 0

        try {
            db = getReadWriteDB()
            val query =
                "SELECT SUM(CAST($HCompletionTime as INTEGER)) as $HCompletionTime FROM $HistoryTable"

            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totMinutesSum = cursor.getInt(cursor.getColumnIndex(HCompletionTime))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return totMinutesSum
    }

    @SuppressLint("Range")
    fun getHistoryTotalWorkout(): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val totWorkout = "totCompletionTime"
        var totWorkoutSum = 0

        try {
            db = getReadWriteDB()
            val query = "SELECT SUM(CAST($HTotalEx as INTEGER)) as $totWorkout FROM $HistoryTable"

            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totWorkoutSum = cursor.getInt(cursor.getColumnIndex(totWorkout))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return totWorkoutSum
    }

    @SuppressLint("Range")
    fun getHistoryTotalKCal(): Float {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totKcalSum = 0f

        try {
            db = getReadWriteDB()
            val query = "SELECT SUM(CAST($HBurnKcal as Float)) as $HBurnKcal FROM $HistoryTable"

            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totKcalSum = cursor.getFloat(cursor.getColumnIndex(HBurnKcal))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return totKcalSum
    }

    fun getCompleteExerciseDate(): ArrayList<String> {

        val arrDt = ArrayList<String>()
        val arrDtTemp = ArrayList<String>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()

            val query = "Select * from $HistoryTable"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    if (!arrDtTemp.contains
                            (
                            Utils.parseTime(
                                cursor.getString(cursor.getColumnIndexOrThrow(HDateTime)),
                                Constant.DATE_TIME_24_FORMAT,
                                Constant.DATE_FORMAT
                            )
                        )
                    ) {
                        arrDtTemp.add(
                            Utils.parseTime(
                                cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                        HDateTime
                                    )
                                ), Constant.DATE_TIME_24_FORMAT, Constant.DATE_FORMAT
                            )
                        )
                        arrDt.add(cursor.getString(cursor.getColumnIndexOrThrow(HDateTime)))
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrDt
    }

    // Todo Get Weekly history data
    fun getWeekDayOfHistory(): ArrayList<HistoryWeekDataClass> {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val weekStart = "WeekStart"
        val WeekEnd = "WeekEnd"
        val WeekNumber = "WeekNumber"
        val arrHistoryData = ArrayList<HistoryWeekDataClass>()

        try {
            db = getReadWriteDB()

            val query = "select strftime('%W', $HDateTime) $WeekNumber," +
                    "    max(date($HDateTime, 'weekday 0' ,'-6 day')) $weekStart," +
                    "    max(date($HDateTime, 'weekday 0', '-0 day')) $WeekEnd " +
                    "from $HistoryTable " +
                    "group by $WeekNumber"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val historyWeekDataClass = HistoryWeekDataClass()

                    historyWeekDataClass.weekNumber =
                        cursor.getString(cursor.getColumnIndexOrThrow(WeekNumber))
                    historyWeekDataClass.weekStart =
                        cursor.getString(cursor.getColumnIndexOrThrow(weekStart))
                    historyWeekDataClass.weekEnd =
                        cursor.getString(cursor.getColumnIndexOrThrow(WeekEnd))
                    historyWeekDataClass.totKcal = getTotBurnWeekKcal(
                        historyWeekDataClass.weekStart,
                        historyWeekDataClass.weekEnd
                    )
                    historyWeekDataClass.totTime = getTotWeekWorkoutTime(
                        historyWeekDataClass.weekStart,
                        historyWeekDataClass.weekEnd
                    )

                    historyWeekDataClass.arrHistoryDetail = getWeekHistoryData(
                        historyWeekDataClass.weekStart,
                        historyWeekDataClass.weekEnd
                    )

                    historyWeekDataClass.totWorkout = historyWeekDataClass.arrHistoryDetail.size
                    arrHistoryData.add(historyWeekDataClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrHistoryData
    }

    fun getTotBurnWeekKcal(strWeekStart: String, strWeekEnd: String): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totKcal = 0
        try {
            db = getReadWriteDB()

            val query =
                "SELECT sum($HBurnKcal) as $HBurnKcal from $HistoryTable WHERE date('$strWeekStart') <= date($HDateTime) AND date('$strWeekEnd') >= date($HDateTime)"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totKcal = cursor.getInt(cursor.getColumnIndexOrThrow(HBurnKcal))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return totKcal
    }

    fun getTotWeekWorkoutTime(strWeekStart: String, strWeekEnd: String): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var totCompletionTime = 0
        try {
            db = getReadWriteDB()

            val query =
                "SELECT sum($HCompletionTime) as $HCompletionTime from $HistoryTable WHERE date('$strWeekStart') <= date($HDateTime) AND date('$strWeekEnd') >= date($HDateTime)"
            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                totCompletionTime = cursor.getInt(cursor.getColumnIndexOrThrow(HCompletionTime))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return totCompletionTime
    }

    fun getWeekHistoryData(
        strWeekStart: String,
        strWeekEnd: String
    ): ArrayList<HistoryDetailsClass> {

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val arrHistoryWeekDetails = ArrayList<HistoryDetailsClass>()
        try {
            db = getReadWriteDB()
//            val query = "SELECT * FROM tbl_history WHERE date('$strWeekStart') <= date(datetime) AND date('$strWeekEnd') >= date(datetime)"
            val query =
                "SELECT * FROM $HistoryTable WHERE date('$strWeekStart') <= date($HDateTime) AND date('$strWeekEnd') >= date($HDateTime) Order by $HId Desc "

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val historyDetailsClass = HistoryDetailsClass()
                    historyDetailsClass.PlanId =
                        cursor.getString(cursor.getColumnIndexOrThrow(HPlanId))
                    historyDetailsClass.PlanName =
                        cursor.getString(cursor.getColumnIndexOrThrow(HPlanName))
                    historyDetailsClass.DateTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))
                    historyDetailsClass.CompletionTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(HCompletionTime))
                    historyDetailsClass.BurnKcal =
                        cursor.getString(cursor.getColumnIndexOrThrow(HBurnKcal))
                    historyDetailsClass.TotalWorkout =
                        cursor.getString(cursor.getColumnIndexOrThrow(HTotalEx))
                    historyDetailsClass.Kg = cursor.getString(cursor.getColumnIndexOrThrow(HKg))
                    historyDetailsClass.Feet = cursor.getString(cursor.getColumnIndexOrThrow(HFeet))
                    historyDetailsClass.Inch = cursor.getString(cursor.getColumnIndexOrThrow(HInch))
                    historyDetailsClass.FeelRate =
                        cursor.getString(cursor.getColumnIndexOrThrow(HFeelRate))
                    historyDetailsClass.DayName =
                        cursor.getString(cursor.getColumnIndexOrThrow(HDayName))
                    historyDetailsClass.DayId =
                        cursor.getString(cursor.getColumnIndexOrThrow(HDayId))
                    historyDetailsClass.planDetail = getPlanByPlanId(
                        cursor.getString(cursor.getColumnIndexOrThrow(HPlanId)).toInt()
                    )
//                    historyDetailsClass.WeekName = cursor.getString(cursor.getColumnIndexOrThrow(Week_name))

                    arrHistoryWeekDetails.add(historyDetailsClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrHistoryWeekDetails
    }

    // Todo Check User history available or not
    fun isHistoryAvailable(strDate: String): Boolean {
        var dtIsAvailable = false
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()

//            val query = "Select * from $CompleteWorkoutTable"
            val query = "Select $HId From $HistoryTable " +
                    "Where " +
                    "DateTime(strftime('%Y-%m-%d', DateTime($HDateTime)))" +
                    "= " +
                    "DateTime(strftime('%Y-%m-%d', DateTime('$strDate')));"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                dtIsAvailable = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return dtIsAvailable
    }

    //* Todo History Methods  *//*
    fun addHistory(
        strPlanId: String,
        strPlanName: String,
        strDateTime: String,
        strCompletionTime: String,
        strBurnKcal: String,
        strTotalWorkout: String,
        strKg: String,
        strFeet: String,
        strInch: String,
        strFeelRate: String,
        strDayName: String,
        dayId: String
    ): Int {

        var db: SQLiteDatabase? = null
        var count = 0

        val cv = ContentValues()
        cv.put(HPlanId, strPlanId)
        cv.put(HPlanName, strPlanName)
        cv.put(HDateTime, strDateTime)
        cv.put(HCompletionTime, strCompletionTime)
        cv.put(HBurnKcal, strBurnKcal)
        cv.put(HTotalEx, strTotalWorkout)
        cv.put(HKg, strKg)
        cv.put(HFeet, strFeet)
        cv.put(HInch, strInch)
        cv.put(HFeelRate, strFeelRate)
        cv.put(HDayName, strDayName)
        cv.put(HDayId, dayId)

        try {
            db = getReadWriteDB()
            count = db.insert(HistoryTable, null, cv).toInt()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count
    }

    fun getPlanDayNameByDayId(strId: String): String {

        var dayName = ""

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "Select $DayName,$WeekName From $PlanDaysTable where $DayId = $strId"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val weekName = cursor.getString(cursor.getColumnIndexOrThrow(WeekName))
                if (weekName.toInt() > 1) {
                    dayName = (cursor.getString(cursor.getColumnIndexOrThrow(DayName))
                        .toInt() + ((weekName.toInt() - 1) * 7)).toString()
                } else {
                    dayName = cursor.getString(cursor.getColumnIndexOrThrow(DayName))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return dayName
    }

    fun updateProgressPlanDayCompleteByDayId(strDayId: String, progress: String) {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(DayProgress, progress)

        Log.d("checkProgress", progress.toString())
        try {
            db = getReadWriteDB()
            db.update(PlanDaysTable, contentValues, "$DayId = $strDayId", null)
        } catch (e: Exception) {
            Log.d("exception", e.toString())
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }
    }

    fun updatePlanDayCompleteByDayId(strDayId: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(IsCompleted, "1")

        try {
            db = getReadWriteDB()
            mCount = db.update(PlanDaysTable, contentValues, "$DayId = $strDayId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun getRecentHistory(): HistoryDetailsClass? {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        var historyDetailsClass: HistoryDetailsClass? = null
        try {
            db = getReadWriteDB()
            val query = "Select * From $HistoryTable order by $HId DESC"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                historyDetailsClass = HistoryDetailsClass()
                historyDetailsClass.PlanId =
                    cursor.getString(cursor.getColumnIndexOrThrow(HPlanId))
                historyDetailsClass.PlanName =
                    cursor.getString(cursor.getColumnIndexOrThrow(HPlanName))
                historyDetailsClass.DateTime =
                    cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))
                historyDetailsClass.CompletionTime =
                    cursor.getString(cursor.getColumnIndexOrThrow(HCompletionTime))
                historyDetailsClass.BurnKcal =
                    cursor.getString(cursor.getColumnIndexOrThrow(HBurnKcal))
                historyDetailsClass.TotalWorkout =
                    cursor.getString(cursor.getColumnIndexOrThrow(HTotalEx))
                historyDetailsClass.Kg = cursor.getString(cursor.getColumnIndexOrThrow(HKg))
                historyDetailsClass.Feet = cursor.getString(cursor.getColumnIndexOrThrow(HFeet))
                historyDetailsClass.Inch = cursor.getString(cursor.getColumnIndexOrThrow(HInch))
                historyDetailsClass.FeelRate =
                    cursor.getString(cursor.getColumnIndexOrThrow(HFeelRate))
                historyDetailsClass.DayName =
                    cursor.getString(cursor.getColumnIndexOrThrow(HDayName))
                historyDetailsClass.DayId =
                    cursor.getString(cursor.getColumnIndexOrThrow(HDayId))
                historyDetailsClass.planDetail =
                    getPlanByPlanId(cursor.getString(cursor.getColumnIndexOrThrow(HPlanId)).toInt())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return historyDetailsClass
    }

    fun getRecentHistoryList(): ArrayList<HistoryDetailsClass> {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        val arrRecent: ArrayList<HistoryDetailsClass> = ArrayList()
        try {
            db = getReadWriteDB()
            val query = "SELECT *" +
                    "  FROM HistoryTable AS HI," +
                    "       HomePlanTable AS HP" +
                    " WHERE HI.HPlanId = HP.PlanId" +
                    " GROUP BY HI.HPlanId" +
                    " ORDER BY HI.HId DESC"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val historyDetailsClass = HistoryDetailsClass()
                    historyDetailsClass.PlanId =
                        cursor.getString(cursor.getColumnIndexOrThrow(HPlanId))
                    historyDetailsClass.PlanName =
                        cursor.getString(cursor.getColumnIndexOrThrow(HPlanName))
                    historyDetailsClass.DateTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(HDateTime))
                    historyDetailsClass.CompletionTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(HCompletionTime))
                    historyDetailsClass.BurnKcal =
                        cursor.getString(cursor.getColumnIndexOrThrow(HBurnKcal))
                    historyDetailsClass.TotalWorkout =
                        cursor.getString(cursor.getColumnIndexOrThrow(HTotalEx))
                    historyDetailsClass.Kg = cursor.getString(cursor.getColumnIndexOrThrow(HKg))
                    historyDetailsClass.Feet = cursor.getString(cursor.getColumnIndexOrThrow(HFeet))
                    historyDetailsClass.Inch = cursor.getString(cursor.getColumnIndexOrThrow(HInch))
                    historyDetailsClass.FeelRate =
                        cursor.getString(cursor.getColumnIndexOrThrow(HFeelRate))
                    historyDetailsClass.DayName =
                        cursor.getString(cursor.getColumnIndexOrThrow(HDayName))
                    historyDetailsClass.DayId =
                        cursor.getString(cursor.getColumnIndexOrThrow(HDayId))

                    val aClass = HomePlanTableClass()
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                    aClass.planProgress =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                    aClass.planText = cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                    aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                    aClass.planImage = cursor.getString(cursor.getColumnIndexOrThrow(PlanImage))
                    aClass.planDays = cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                    aClass.planType = cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                    aClass.planWorkouts =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanWorkouts))
                    aClass.planMinutes = cursor.getString(cursor.getColumnIndexOrThrow(PlanMinutes))
                    aClass.planTestDes = cursor.getString(cursor.getColumnIndexOrThrow(TestDes))
                    aClass.parentPlanId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ParentPlanId))
                    aClass.planThumbnail =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanThumbnail))
                    aClass.planTypeImage =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanTypeImage))
                    aClass.shortDes = cursor.getString(cursor.getColumnIndexOrThrow(ShortDes))
                    aClass.introduction =
                        cursor.getString(cursor.getColumnIndexOrThrow(Introduction))
                    aClass.isPro =
                        cursor.getString(cursor.getColumnIndexOrThrow(IsPro)).equals("true")
                    aClass.hasSubPlan =
                        cursor.getString(cursor.getColumnIndexOrThrow(HasSubPlan)).equals("true")

                    historyDetailsClass.planDetail = aClass

                    arrRecent.add(historyDetailsClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrRecent
    }

    // restart progress
    fun restartProgress() {

        var db: SQLiteDatabase? = null

        val contentValues = ContentValues()
        contentValues.put(IsCompleted, "0")

        try {
            db = getReadWriteDB()
            // Todo clear ex plan multiple day table workout progress
            db.update(ExerciseTable, contentValues, null, null)

            // Todo clear ex single day table workout progress
            db.update(DayExTable, contentValues, null, null)

            // Todo clear lower body workout progress
            db.update(PlanDaysTable, contentValues, null, null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

    }

    // Todo Get Weekly Day wise Data
    @SuppressLint("Range")
    fun getWorkoutWeeklyData(strCategoryName: String): ArrayList<PWeeklyDayData> {

        Log.d("checkPlanName", strCategoryName)

        val arrPWeeklyDayData = ArrayList<PWeeklyDayData>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            var query = ""

            query =
//                "SELECT $PlanId, group_concat(DISTINCT(CAST($DayName as INTEGER))) as $DayName, $WeekName, $DayId, $IsCompleted from $PlanDaysTable GROUP BY CAST($WeekName as INTEGER)"
                "SELECT  max($DayId) as DayId, $PlanId, group_concat(DISTINCT(CAST($DayName as INTEGER))) as $DayName, $WeekName, $IsCompleted,$DayProgress from $PlanDaysTable GROUP BY CAST($WeekName as INTEGER)"
//            "SELECT $DayId, $PlanId, $DayName, $WeekName, $IsCompleted from $PlanDaysTable"
//            SELECT  DayId, PlanId,  DayName, WeekName, IsCompleted from PlanDaysTable
            cursor = db.rawQuery(query, null)



            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = PWeeklyDayData()
                    aClass.Workout_id = cursor.getString(cursor.getColumnIndex(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndex(DayId))
                    aClass.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
                    aClass.Week_name = cursor.getString(cursor.getColumnIndex(WeekName))
                    aClass.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))
                    aClass.Day_progress =
                        cursor.getString(cursor.getColumnIndexOrThrow(DayProgress))
                    aClass.categoryName = strCategoryName




                    Log.d("progressName1", aClass.Workout_id)
                    Log.d("progressName2", aClass.dayId)
                    Log.d("progressName3", aClass.Day_name+" day name")
                    Log.d("progressName4", aClass.Week_name)
                    Log.d("progressName5", aClass.Is_completed)
                    Log.d("progressName6", aClass.Day_progress)
                    Log.d("progressName7", aClass.categoryName)
                    Log.d("progressName8", aClass.Week_name)



                    aClass.arrWeekDayData = getWeekDaysData(aClass.Week_name)

                    Log.d("progressName9", aClass.arrWeekDayData.size.toString())

                    val aClass1 = PWeekDayData()
                    aClass1.Day_name = "Cup"


                    if (aClass.Is_completed == "1") {
                        aClass1.Is_completed = "1"
                    } else {
                        aClass1.Is_completed = "0"
                    }



                    aClass.arrWeekDayData.add(aClass1)




                    arrPWeeklyDayData.add(aClass)
                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
            }
        }

        Log.d("progressName10", arrPWeeklyDayData.size.toString())
        return arrPWeeklyDayData
    }

    @SuppressLint("Range")
    fun get30DaysData(): ArrayList<PWeekDayData> {

        val arrWeekDayData = ArrayList<PWeekDayData>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {

            db = getReadWriteDB()
            var query = ""

//            query =
//                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable " +
//                        "WHERE $DayName IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30') " +
//                        "AND $WeekName = '$strWeekName' GROUP by $DayName"

//            query =
//                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable " +
//                        "WHERE $DayName IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30') " +
//                        ""
            query =
                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = PWeekDayData()
                    aClass.Day_id = cursor.getString(cursor.getColumnIndex(DayId))
                    aClass.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
                    aClass.Workouts = cursor.getString(cursor.getColumnIndex(PlanWorkouts))
                    aClass.Minutes = cursor.getString(cursor.getColumnIndex(PlanMinutes))
                    aClass.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))
                    arrWeekDayData.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
            }
        }

        return arrWeekDayData
    }

    @SuppressLint("Range")
    private fun getWeekDaysData(strWeekName: String): ArrayList<PWeekDayData> {

        val arrWeekDayData = ArrayList<PWeekDayData>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {

            db = getReadWriteDB()
            var query = ""

//            query =
//                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable " +
//                        "WHERE $DayName IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30') " +
//                        "AND $WeekName = '$strWeekName' GROUP by $DayName"

            query =
                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable " +
                        "WHERE $DayName IN ('1','2','3','4','5','6','8','9','10','11','12','13','15','16','17','18','19','20','22','23','24','25','26','27','29') " +
                        "AND $WeekName = '$strWeekName'"

//            query =
//                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable " +
//                        "WHERE $DayName IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30') " +
//                        ""

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = PWeekDayData()
                    aClass.Day_id = cursor.getString(cursor.getColumnIndex(DayId))
                    aClass.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
                    aClass.Workouts = cursor.getString(cursor.getColumnIndex(PlanWorkouts))
                    aClass.Minutes = cursor.getString(cursor.getColumnIndex(PlanMinutes))
                    aClass.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))
                    Log.d("checkClass", aClass.Day_name.toString())
                    arrWeekDayData.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
            }
        }



        return arrWeekDayData
    }

//    @SuppressLint("Range")
//    private fun getWeekDaysData(strWeekName: String): ArrayList<PWeekDayData> {
//        //old fun
//        val arrWeekDayData = ArrayList<PWeekDayData>()
//        var db: SQLiteDatabase? = null
//        var cursor: Cursor? = null
//        try {
//
//            db = getReadWriteDB()
//            var query = ""
//
//            query =
//                "select $DayName, $DayId ,$IsCompleted,$PlanMinutes,$PlanWorkouts FROM $PlanDaysTable " +
//                        "WHERE $DayName IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30') " +
//                        "AND $WeekName = '$strWeekName' GROUP by $DayName"
//
//            cursor = db.rawQuery(query, null)
//
//            if (cursor != null && cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    val aClass = PWeekDayData()
//                    aClass.Day_id = cursor.getString(cursor.getColumnIndex(DayId))
//                    aClass.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
//                    aClass.Workouts = cursor.getString(cursor.getColumnIndex(PlanWorkouts))
//                    aClass.Minutes = cursor.getString(cursor.getColumnIndex(PlanMinutes))
//                    aClass.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))
//                    arrWeekDayData.add(aClass)
//                }
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            if (cursor != null && !cursor.isClosed) {
//                cursor.close()
//            }
//            if (db != null && db.isOpen) {
//                //db.close()
//            }
//        }
//
//        return arrWeekDayData
//    }

    @SuppressLint("Range")
    fun getDaysPlanData(strDayId: String): PWeekDayData? {

        var pWeekDayData: PWeekDayData? = null
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {

            db = getReadWriteDB()
            var query = ""

            query = "select * FROM $PlanDaysTable " +
                    "WHERE $DayId = '$strDayId'"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()

                pWeekDayData = PWeekDayData()
                pWeekDayData.Day_id = cursor.getString(cursor.getColumnIndex(DayId))
                pWeekDayData.Day_name = cursor.getString(cursor.getColumnIndex(DayName))
                pWeekDayData.Workouts = cursor.getString(cursor.getColumnIndex(PlanWorkouts))
                pWeekDayData.Minutes = cursor.getString(cursor.getColumnIndex(PlanMinutes))
                pWeekDayData.Is_completed = cursor.getString(cursor.getColumnIndex(IsCompleted))

            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
            }
        }

        return pWeekDayData
    }

    // Todo Getting day exercise data
    fun getDayExList(strDayId: String): ArrayList<HomeExTableClass> {

        val arrDayExClass: ArrayList<HomeExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "SELECT DX.$DayExId, DX.$PlanId," +
                    "       DX.$DayId," +
                    "       DX.$IsCompleted," +
                    "       DX.$UpdatedExTime," +
                    "       DX.$ReplaceExId," +
                    "       CASE WHEN DX.$UpdatedExTime != ''" +
                    "       THEN DX.$UpdatedExTime" +
                    "       ELSE DX.$ExTime" +
                    "       END as $ExTime, " +
                    "       CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END as $ExId, " +
                    "EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExVideoPath,EX.$ExName,Ex.$ExUnit FROM $DayExTable as DX " +
                    "INNER JOIN $ExerciseTable as EX ON " +
                    "(CASE WHEN DX.$ReplaceExId != ''" +
                    "       THEN DX.$ReplaceExId" +
                    "       ELSE DX.$ExId" +
                    "       END)" +
                    "= EX.$ExId WHERE DX.$DayId = $strDayId"

            //            val query =
            //                "SELECT DX.*, EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $DayExTable as DX INNER JOIN $ExerciseTable as EX ON DX.$ExId = EX.$ExId WHERE $DayId = $strDayId"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = HomeExTableClass()
                    aClass.dayExId = cursor.getString(cursor.getColumnIndexOrThrow(DayExId))
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndexOrThrow(DayId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.isCompleted = cursor.getString(cursor.getColumnIndexOrThrow(IsCompleted))
                    aClass.updatedExTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(UpdatedExTime))
                    aClass.replaceExId = cursor.getString(cursor.getColumnIndexOrThrow(ReplaceExId))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exVideoPath = cursor.getString(cursor.getColumnIndexOrThrow(ExVideoPath))
                    aClass.exDescription =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    arrDayExClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrDayExClass
    }


    fun getCompleteDayExList(strDayId: String): Int {

        var count = 0
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "Select $DayExId From $DayExTable where $DayId = $strDayId AND $IsCompleted = 1"

            cursor = db.rawQuery(query, null)
            count = cursor.count

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count
    }


    fun getMyTrainingCategoryList(): ArrayList<MyTrainingCategoryTableClass> {

        val arrPlan: ArrayList<MyTrainingCategoryTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "Select * From $MyTrainingCategoryTable "
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = MyTrainingCategoryTableClass()
                    aClass.cId = cursor.getString(cursor.getColumnIndexOrThrow(CId))
                    aClass.cName = cursor.getString(cursor.getColumnIndexOrThrow(CName))
                    arrPlan.add(aClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }
        return arrPlan
    }

    fun getMyTrainingCategoryExList(catId: String): ArrayList<MyTrainingCatExTableClass> {
        val arrExClass: ArrayList<MyTrainingCatExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null

        try {
            db = getReadWriteDB()
            val query =
                "SELECT CX.$CatExId,CX.$CatId,EX.$ExId, EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit,Ex.$ExTime FROM $MyTrainningCatExTable AS CX,$ExerciseTable AS EX ON CX.$ExId = Ex.$ExId WHERE CX.$CatId = $catId"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = MyTrainingCatExTableClass()
                    aClass.catId = cursor.getString(cursor.getColumnIndexOrThrow(CatId))
                    aClass.catExId = cursor.getString(cursor.getColumnIndexOrThrow(CatExId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    arrExClass.add(aClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }
        return arrExClass
    }

    fun addMyTrainingPlan(
        plan: HomePlanTableClass,
        excList: ArrayList<MyTrainingCatExTableClass>
    ): Int {
        var db: SQLiteDatabase? = null
        var count = 0
        Log.d("checkFun", "Add Trainning Plan")
        val cv = ContentValues()
        cv.put(PlanName, plan.planName)
        cv.put(PlanProgress, plan.planProgress)
        cv.put(PlanDays, plan.planDays)
        cv.put(PlanType, plan.planType)
        cv.put(PlanWorkouts, plan.planWorkouts)
        cv.put(PlanMinutes, plan.planMinutes)
        cv.put(IsPro, plan.isPro)
        cv.put(HasSubPlan, plan.hasSubPlan)
        cv.put(PlanThumbnail, plan.planThumbnail)
        cv.put(ParentPlanId, plan.parentPlanId)
        cv.put(PlanTypeImage, plan.planTypeImage)

        try {
            db = getReadWriteDB()
            count = db.insert(PlanTable, null, cv).toInt()

            if (count != -1) {
                for (i in excList.indices) {
                    addMyTrainingExc(excList.get(i), count, i + 1)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count
    }

    fun addMyTrainingExc(
        item: MyTrainingCatExTableClass,
        planId: Int,
        pos: Int
    ): Int {
        var db: SQLiteDatabase? = null
        var count = 0

        val cv = ContentValues()
        cv.put(PlanId, planId)
        cv.put(DayId, "1")
        cv.put(ExId, item.exId)
        cv.put(ExTime, item.exTime)
        cv.put(IsCompleted, false)
        cv.put(UpdatedExTime, item.exReplaceTime)
        cv.put(PlanSort, pos)
        cv.put(DefaultSort, pos)

        try {
            db = getReadWriteDB()
            count = db.insert(MyTrainingExTable, null, cv).toInt()


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count
    }

    fun updateMyTrainingPlanName(id: String, planName: String): Int {
        var mCount = 0
        var db: SQLiteDatabase? = null
        val contentValues = ContentValues()
        contentValues.put(PlanName, planName)

        try {
            db = getReadWriteDB()
            mCount = db.update(PlanTable, contentValues, "$PlanId = $id", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return mCount
    }

    fun deleteMyTrainingPlan(id: String): Boolean {
        var isSuccess = false
        var db: SQLiteDatabase? = null

        try {
            db = getReadWriteDB()
            val mCount = db.delete(PlanTable, "$PlanId=?", arrayOf(id))
            val count_ = db.delete(MyTrainingExTable, "$PlanId=?", arrayOf(id))
            if (mCount > 0 && count_ > 0) {
                isSuccess = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return isSuccess
    }

    fun deleteMyTrainingEx(id: String): Boolean {
        var isSuccess = false
        var db: SQLiteDatabase? = null

        try {
            db = getReadWriteDB()
            val mCount = db.delete(MyTrainingExTable, "$DayExId=?", arrayOf(id))
            if (mCount > 0) {
                isSuccess = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return isSuccess
    }

    fun updateMyTrainingPlanExcCount(eCount: Int, pId: Int): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(PlanWorkouts, eCount)

        try {
            db = getReadWriteDB()

            count = db.update(PlanTable, cv, "$PlanId = $pId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun updateMyTrainingEx(
        dayExId: Int,
        data: MyTrainingCatExTableClass,
        i: Int
    ): Boolean {
        var count = 0
        var db: SQLiteDatabase? = null

        val cv = ContentValues()
        cv.put(UpdatedExTime, data.exReplaceTime)
        cv.put(PlanSort, i)
        cv.put(DefaultSort, i)

        try {
            db = getReadWriteDB()

            count = db.update(MyTrainingExTable, cv, "$DayExId = $dayExId", null)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return count > 0
    }

    fun getMyTrainingExList(strPlanId: String): ArrayList<HomeExTableClass> {

        val arrDayExClass: ArrayList<HomeExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "SELECT MX.$DayExId, MX.$PlanId," +
                    "       MX.$DayId," +
                    "       MX.$IsCompleted," +
                    "       MX.$UpdatedExTime," +
                    "       MX.$ReplaceExId," +
                    "       MX.$ExTime," +
                    "       MX.$PlanSort," +
                    "       MX.$DefaultSort," +
                    "       CASE WHEN MX.$ReplaceExId != ''" +
                    "       THEN MX.$ReplaceExId" +
                    "       ELSE MX.$ExId" +
                    "       END as $ExId, " +
                    "EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $MyTrainingExTable as MX " +
                    "INNER JOIN $ExerciseTable as EX ON " +
                    "(CASE WHEN MX.$ReplaceExId != ''" +
                    "       THEN MX.$ReplaceExId" +
                    "       ELSE MX.$ExId" +
                    "       END)" +
                    "= EX.$ExId WHERE MX.$PlanId = $strPlanId ORDER BY MX.$PlanSort"


            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = HomeExTableClass()
                    aClass.dayExId = cursor.getString(cursor.getColumnIndexOrThrow(DayExId))
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.dayId = cursor.getString(cursor.getColumnIndexOrThrow(DayId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.isCompleted = cursor.getString(cursor.getColumnIndexOrThrow(IsCompleted))
                    aClass.updatedExTime =
                        cursor.getString(cursor.getColumnIndexOrThrow(UpdatedExTime))
                    aClass.replaceExId = cursor.getString(cursor.getColumnIndexOrThrow(ReplaceExId))
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    aClass.planSort = cursor.getString(cursor.getColumnIndexOrThrow(PlanSort))
                    aClass.defaultPlanSort = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            DefaultSort
                        )
                    )
                    arrDayExClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrDayExClass
    }

    fun getMyTrainingExListForEdit(strDayId: String): ArrayList<MyTrainingCatExTableClass> {
        val arrDayExClass: ArrayList<MyTrainingCatExTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query = "SELECT MX.$DayExId, MX.$PlanId," +
                    "       MX.$DayId," +
                    "       MX.$IsCompleted," +
                    "       MX.$UpdatedExTime," +
                    "       MX.$ReplaceExId," +
                    "       MX.$ExTime," +
                    "       CASE WHEN MX.$ReplaceExId != ''" +
                    "       THEN MX.$ReplaceExId" +
                    "       ELSE MX.$ExId" +
                    "       END as $ExId, " +
                    "EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $MyTrainingExTable as MX " +
                    "INNER JOIN $ExerciseTable as EX ON " +
                    "(CASE WHEN MX.$ReplaceExId != ''" +
                    "       THEN MX.$ReplaceExId" +
                    "       ELSE MX.$ExId" +
                    "       END)" +
                    "= EX.$ExId WHERE MX.$PlanId = $strDayId ORDER BY MX.$PlanSort"

            //            val query =
            //                "SELECT DX.*, EX.$ExDescription, EX.$ExVideo,EX.$ExPath,EX.$ExName,Ex.$ExUnit FROM $DayExTable as DX INNER JOIN $ExerciseTable as EX ON DX.$ExId = EX.$ExId WHERE $DayId = $strDayId"

            cursor = db.rawQuery(query, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = MyTrainingCatExTableClass()
                    aClass.catExId = cursor.getString(cursor.getColumnIndexOrThrow(DayExId))
                    aClass.catId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.exId = cursor.getString(cursor.getColumnIndexOrThrow(ExId))
                    aClass.exTime = cursor.getString(cursor.getColumnIndexOrThrow(ExTime))
                    aClass.exReplaceTime = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            UpdatedExTime
                        )
                    )
                    aClass.exName = cursor.getString(cursor.getColumnIndexOrThrow(ExName))
                    aClass.exUnit = cursor.getString(cursor.getColumnIndexOrThrow(ExUnit))
                    aClass.exPath = cursor.getString(cursor.getColumnIndexOrThrow(ExPath))
                    aClass.exDescription =
                        cursor.getString(cursor.getColumnIndexOrThrow(ExDescription))
                    aClass.exVideo = cursor.getString(cursor.getColumnIndexOrThrow(ExVideo))
                    arrDayExClass.add(aClass)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }

        return arrDayExClass
    }

    fun getDiscoverPlanList(catName: String): ArrayList<HomePlanTableClass> {

        val arrPlan: ArrayList<HomePlanTableClass> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "Select * From $PlanTable where $DiscoverCatName = '$catName' order by $PlanSort"
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = HomePlanTableClass()
                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                    aClass.planProgress =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                    aClass.planText = cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                    aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                    aClass.planImage = cursor.getString(cursor.getColumnIndexOrThrow(PlanImage))
                    aClass.planDays = cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                    aClass.planType = cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                    aClass.planWorkouts =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanWorkouts))
                    aClass.planMinutes = cursor.getString(cursor.getColumnIndexOrThrow(PlanMinutes))
                    aClass.planTestDes = cursor.getString(cursor.getColumnIndexOrThrow(TestDes))
                    aClass.parentPlanId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ParentPlanId))
                    aClass.planThumbnail =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanThumbnail))
                    aClass.planTypeImage =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanTypeImage))
                    aClass.shortDes = cursor.getString(cursor.getColumnIndexOrThrow(ShortDes))
                    aClass.introduction =
                        cursor.getString(cursor.getColumnIndexOrThrow(Introduction))
                    aClass.discoverIcon =
                        cursor.getString(cursor.getColumnIndexOrThrow(DiscoverIcon))
                    aClass.isPro =
                        cursor.getString(cursor.getColumnIndexOrThrow(IsPro)).equals("true")
                    aClass.hasSubPlan =
                        cursor.getString(cursor.getColumnIndexOrThrow(HasSubPlan)).equals("true")
                    arrPlan.add(aClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }
        return arrPlan
    }

    fun getMusicList(): ArrayList<Music> {

        val arrPlan: ArrayList<Music> = ArrayList()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "Select * From $MusicTable "
            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val aClass = Music()
                    aClass.id = cursor.getString(cursor.getColumnIndexOrThrow(mId))
                    aClass.name = cursor.getString(cursor.getColumnIndexOrThrow(mName))
                    aClass.duration = cursor.getString(cursor.getColumnIndexOrThrow(mDuration))
                    aClass.fileName = cursor.getString(cursor.getColumnIndexOrThrow(mFileName))
                    aClass.isPro =
                        cursor.getString(cursor.getColumnIndexOrThrow(mIsPro)).equals("true")
                    aClass.isFromRaw =
                        cursor.getString(cursor.getColumnIndexOrThrow(mIsFromRaw)).equals("true")
                    arrPlan.add(aClass)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }
        return arrPlan
    }

    fun getRandomDiscoverPlan(): HomePlanTableClass {

        val aClass = HomePlanTableClass()

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = getReadWriteDB()
            val query =
                "SELECT * FROM  $PlanTable WHERE $DiscoverCatName != '' AND $HasSubPlan = 'false' ORDER BY RANDOM() LIMIT 1;"


            cursor = db.rawQuery(query, null)
            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {

                    aClass.planId = cursor.getString(cursor.getColumnIndexOrThrow(PlanId))
                    aClass.planName = cursor.getString(cursor.getColumnIndexOrThrow(PlanName))
                    aClass.planProgress =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanProgress))
                    aClass.planText = cursor.getString(cursor.getColumnIndexOrThrow(PlanText))
                    aClass.planLvl = cursor.getString(cursor.getColumnIndexOrThrow(PlanLvl))
                    aClass.planImage = cursor.getString(cursor.getColumnIndexOrThrow(PlanImage))
                    aClass.planDays = cursor.getString(cursor.getColumnIndexOrThrow(PlanDays))
                    aClass.planType = cursor.getString(cursor.getColumnIndexOrThrow(PlanType))
                    aClass.planWorkouts =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanWorkouts))
                    aClass.planMinutes = cursor.getString(cursor.getColumnIndexOrThrow(PlanMinutes))
                    aClass.planTestDes = cursor.getString(cursor.getColumnIndexOrThrow(TestDes))
                    aClass.parentPlanId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ParentPlanId))
                    aClass.planThumbnail =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanThumbnail))
                    aClass.planTypeImage =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlanTypeImage))
                    aClass.shortDes = cursor.getString(cursor.getColumnIndexOrThrow(ShortDes))
                    aClass.introduction =
                        cursor.getString(cursor.getColumnIndexOrThrow(Introduction))
                    aClass.discoverIcon =
                        cursor.getString(cursor.getColumnIndexOrThrow(DiscoverIcon))
                    aClass.isPro =
                        cursor.getString(cursor.getColumnIndexOrThrow(IsPro)).equals("true")
                    aClass.hasSubPlan =
                        cursor.getString(cursor.getColumnIndexOrThrow(HasSubPlan)).equals("true")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            if (db != null && db.isOpen) {
                //db.close()
                db.releaseReference()
            }
        }
        return aClass
    }
}
