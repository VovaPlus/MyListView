package com.person.v_plaunov.mylistview

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DBOpenHelper(val context: Context, databaseName: String) : SQLiteOpenHelper(
    context, databaseName, null, 1
) {
    var db: SQLiteDatabase? = null

    init {

        //Составим полный путь к базам для вашего приложения
        val packageName = context.packageName
        DB_PATH = String.format(context.getString(R.string.str_db_path), packageName)
        DB_NAME = databaseName
        openDataBase()
    }

    //Создаст базу, если она не создана
    fun createDataBase() {
        val dbExist = checkDataBase()
        if (!dbExist) {
            this.readableDatabase
            try {
                copyDataBase()
            } catch (e: IOException) {
                Log.e(this.javaClass.toString(), "Copying error")
                throw Error("Error copying database!")
            }
        } else {
            Log.i(this.javaClass.toString(), "Database already exists")
        }
    }

    //Проверка существования базы данных
    private fun checkDataBase(): Boolean {
        var checkDb: SQLiteDatabase? = null
        try {
            val path = DB_PATH + DB_NAME
            checkDb = SQLiteDatabase.openDatabase(
                path, null,
                SQLiteDatabase.OPEN_READONLY
            )
        } catch (e: SQLException) {
            Log.e(this.javaClass.toString(), "Error while checking db")
        }

        //Андроид не любит утечки ресурсов, все должно закрываться
        checkDb?.close()
        return checkDb != null
    }

    //Метод копирования базы
    @Throws(IOException::class)
    private fun copyDataBase() {

        // Открываем поток для чтения из уже созданной нами БД

        //источник в assets
        val externalDbStream = context.assets.open(DB_NAME)

        // Путь к уже созданной пустой базе в андроиде
        val outFileName = DB_PATH + DB_NAME

        // Теперь создадим поток для записи в эту БД побайтно
        val localDbStream: OutputStream = FileOutputStream(outFileName)

        // Собственно, копирование
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (externalDbStream.read(buffer).also { bytesRead = it } > 0) {
            localDbStream.write(buffer, 0, bytesRead)
        }

        // Мы будем хорошими мальчиками (девочками) и закроем потоки
        localDbStream.close()
        externalDbStream.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): SQLiteDatabase? {
        val path = DB_PATH + DB_NAME
        if (db == null) {
            createDataBase()
            db = SQLiteDatabase.openDatabase(
                path, null,
                SQLiteDatabase.OPEN_READWRITE
            )
        }
        return db
    }

    @Synchronized
    override fun close() {
        if (db != null) {
            db!!.close()
        }
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        //Путь к папке с базами на устройстве
        lateinit var DB_PATH: String

        //Имя файла с базой
        lateinit var DB_NAME: String
    }
}