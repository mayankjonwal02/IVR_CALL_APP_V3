package com.example.ivr_call_app_v3.android.FunctionalComponents.Storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.Patient

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "patient_database"
        private const val DATABASE_VERSION = 1

        private const val TABLE_PATIENT = "patients"

        // Column names
        private const val COLUMN_ENGAGEMENT_ID = "engagementid"
        private const val COLUMN_PATIENT_ID = "patientid"
        private const val COLUMN_PATIENT_NAME = "patientname"
        private const val COLUMN_PATIENT_CNO = "patientcno"
        private const val COLUMN_OPERATION_TYPE = "operationtype"
        private const val COLUMN_LANGUAGE = "language"
        private const val COLUMN_CALLED_ON = "calledon"
        private const val COLUMN_NUMBER_OF_INTERACTIONS = "NumberOfInteractions"
        private const val COLUMN_DUE_DATE = "duedate"
        private const val COLUMN_DAY_7 = "day7"
        private const val COLUMN_DAY_6 = "day6"
        private const val COLUMN_DAY_5 = "day5"
        private const val COLUMN_DAY_4 = "day4"
        private const val COLUMN_DAY_3 = "day3"
        private const val COLUMN_DAY_2 = "day2"
        private const val COLUMN_DAY_1 = "day1"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PATIENT_TABLE = """
            CREATE TABLE $TABLE_PATIENT (
                $COLUMN_ENGAGEMENT_ID TEXT PRIMARY KEY,
                $COLUMN_PATIENT_ID TEXT,
                $COLUMN_PATIENT_NAME TEXT,
                $COLUMN_PATIENT_CNO TEXT,
                $COLUMN_OPERATION_TYPE TEXT,
                $COLUMN_LANGUAGE TEXT,
                $COLUMN_CALLED_ON TEXT,
                $COLUMN_NUMBER_OF_INTERACTIONS INTEGER,
                $COLUMN_DUE_DATE TEXT,
                $COLUMN_DAY_7 TEXT,
                $COLUMN_DAY_6 TEXT,
                $COLUMN_DAY_5 TEXT,
                $COLUMN_DAY_4 TEXT,
                $COLUMN_DAY_3 TEXT,
                $COLUMN_DAY_2 TEXT,
                $COLUMN_DAY_1 TEXT
            )
        """.trimIndent()
        db.execSQL(CREATE_PATIENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PATIENT")
        onCreate(db)
    }

    // Insert a single patient into the database
    fun insertPatient(patient: Patient) {
        val db = this.writableDatabase
        val query = """
            INSERT INTO $TABLE_PATIENT (
                $COLUMN_ENGAGEMENT_ID, $COLUMN_PATIENT_ID, $COLUMN_PATIENT_NAME, 
                $COLUMN_PATIENT_CNO, $COLUMN_OPERATION_TYPE, $COLUMN_LANGUAGE, 
                $COLUMN_CALLED_ON, $COLUMN_NUMBER_OF_INTERACTIONS, $COLUMN_DUE_DATE, 
                $COLUMN_DAY_7, $COLUMN_DAY_6, $COLUMN_DAY_5, $COLUMN_DAY_4, 
                $COLUMN_DAY_3, $COLUMN_DAY_2, $COLUMN_DAY_1
            ) VALUES (
                '${patient.engagementId}', '${patient.patientId}', '${patient.patientName}', 
                '${patient.patientCno}', '${patient.operationType}', '${patient.language}', 
                '${patient.calledOn}', ${patient.numberOfInteractions}, '${patient.dueDate}', 
                '${patient.day7}', '${patient.day6}', '${patient.day5}', '${patient.day4}', 
                '${patient.day3}', '${patient.day2}', '${patient.day1}'
            )
        """.trimIndent()
        db.execSQL(query)
        db.close()
    }

    // Insert a list of patients into the database
    fun insertPatientsList(patients: List<Patient>) {
        val db = this.writableDatabase
        db.beginTransaction()  // Start a transaction to improve performance

        try {
            patients.forEach { patient ->
                val query = """
                    INSERT INTO $TABLE_PATIENT (
                        $COLUMN_ENGAGEMENT_ID, $COLUMN_PATIENT_ID, $COLUMN_PATIENT_NAME, 
                        $COLUMN_PATIENT_CNO, $COLUMN_OPERATION_TYPE, $COLUMN_LANGUAGE, 
                        $COLUMN_CALLED_ON, $COLUMN_NUMBER_OF_INTERACTIONS, $COLUMN_DUE_DATE, 
                        $COLUMN_DAY_7, $COLUMN_DAY_6, $COLUMN_DAY_5, $COLUMN_DAY_4, 
                        $COLUMN_DAY_3, $COLUMN_DAY_2, $COLUMN_DAY_1
                    ) VALUES (
                        '${patient.engagementId}', '${patient.patientId}', '${patient.patientName}', 
                        '${patient.patientCno}', '${patient.operationType}', '${patient.language}', 
                        '${patient.calledOn}', ${patient.numberOfInteractions}, '${patient.dueDate}', 
                        '${patient.day7}', '${patient.day6}', '${patient.day5}', '${patient.day4}', 
                        '${patient.day3}', '${patient.day2}', '${patient.day1}'
                    )
                """.trimIndent()
                db.execSQL(query)
            }
            db.setTransactionSuccessful()  // Mark the transaction as successful
        } finally {
            db.endTransaction()  // End the transaction
            db.close()
        }
    }

    // Get all patients from the database
    fun getAllPatients(): List<Patient> {
        val patientList = mutableListOf<Patient>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PATIENT", null)

        if (cursor.moveToFirst()) {
            do {
                val engagementId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENGAGEMENT_ID))
                val patientId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATIENT_ID))
                val patientName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATIENT_NAME))
                val patientCno = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATIENT_CNO))
                val operationType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPERATION_TYPE))
                val language = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGE))
                val calledOn = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CALLED_ON))
                val numberOfInteractions = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_OF_INTERACTIONS))
                val dueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE))
                val day7 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_7))
                val day6 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_6))
                val day5 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_5))
                val day4 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_4))
                val day3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_3))
                val day2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_2))
                val day1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_1))

                patientList.add(
                    Patient(
                        engagementId, patientId, patientName, patientCno, operationType,
                        language, calledOn, numberOfInteractions, dueDate,
                        day7, day6, day5, day4, day3, day2, day1
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return patientList
    }


    fun updatePatientByEngagementId(patient: Patient) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PATIENT_ID, patient.patientId)
            put(COLUMN_PATIENT_NAME, patient.patientName)
            put(COLUMN_PATIENT_CNO, patient.patientCno)
            put(COLUMN_OPERATION_TYPE, patient.operationType)
            put(COLUMN_LANGUAGE, patient.language)
            put(COLUMN_CALLED_ON, patient.calledOn)
            put(COLUMN_NUMBER_OF_INTERACTIONS, patient.numberOfInteractions)
            put(COLUMN_DUE_DATE, patient.dueDate)
            put(COLUMN_DAY_7, patient.day7)
            put(COLUMN_DAY_6, patient.day6)
            put(COLUMN_DAY_5, patient.day5)
            put(COLUMN_DAY_4, patient.day4)
            put(COLUMN_DAY_3, patient.day3)
            put(COLUMN_DAY_2, patient.day2)
            put(COLUMN_DAY_1, patient.day1)
        }

        val rowsUpdated = db.update(
            TABLE_PATIENT,
            contentValues,
            "$COLUMN_ENGAGEMENT_ID = ?",
            arrayOf(patient.engagementId)
        )

        db.close()
    }

    // Clear all patients from the database
    fun clearPatients() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_PATIENT")
        db.close()
    }



}
