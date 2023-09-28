package com.person.v_plaunov.mylistview

import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class CoinViewActivity : AppCompatActivity() {
    private var myDataBase: SQLiteDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coinview)
        val intent = intent
        val bundle = intent!!.extras
        val coinId = bundle!!.getInt("coin_id")
        //        String coinState = bundle.getString("coin_state");
//        String coinNominal = bundle.getString("coin_nominal");
//        String coinImgName = bundle.getString("coin_img");

        // переменные для query
        val columns = arrayOf("_id", "Nominal", "State", "Img", "Year", "Theme", "Description")
        val selection: String
        val selectionArgs: Array<String>

        //coins.clear();
        //Наш ключевой хелпер
        val dbOpenHelper = DBOpenHelper(this, DB_NAME)
        myDataBase = dbOpenHelper.openDataBase()
        //Все, база открыта!

//        Cursor cursor = myDataBase.query("coins", columns, selection, selectionArgs, null, null, null);
        selection = "_id = ?"
        selectionArgs = arrayOf(coinId.toString())
        val cursor = myDataBase?.query("coins", columns, selection, selectionArgs, null, null, null)
        var coinImagePath = ""
        cursor!!.moveToFirst()
        val coinState = cursor.getString(cursor.getColumnIndexOrThrow("State"))
        val cState = findViewById<TextView>(R.id.coin_state)
        cState.text = coinState
        val coinNominal = cursor.getString(cursor.getColumnIndexOrThrow("Nominal"))
        val cNominal = findViewById<TextView>(R.id.coin_nominal)
        cNominal.text = coinNominal
        var coinImgName = cursor.getString(cursor.getColumnIndexOrThrow("Img"))
        if (!TextUtils.isEmpty(coinImgName)) coinImgName = coinImgName.replace("\\", "//")
        val coinYear = cursor.getString(cursor.getColumnIndexOrThrow("Year"))
        val cYear = findViewById<TextView>(R.id.coin_year)
        cYear.text = coinYear
        val coinTheme = cursor.getString(cursor.getColumnIndexOrThrow("Theme"))
        val cTheme = findViewById<TextView>(R.id.coin_theme)
        cTheme.text = coinTheme
        val coinDescr = cursor.getString(cursor.getColumnIndexOrThrow("Description"))
        val cDescr = findViewById<TextView>(R.id.coin_description)
        cDescr.text = coinDescr
        cursor.close()
        val packageName = this.packageName
        val DB_PATH = String.format(this.getString(R.string.str_db_path), packageName)
        if (!TextUtils.isEmpty(coinImgName)) coinImagePath = DB_PATH + coinImgName
        try {

            val inputStream: InputStream = FileInputStream(coinImagePath)
            val num = inputStream.available()
            //Drawable.createFromStream(inputStream, null)
            val d = Drawable.createFromPath(coinImagePath)

            val cImg = findViewById<TouchImageView>(R.id.coin_pic)
            cImg.setImageDrawable(d)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        //private static String DB_PATH = "/data/data/com.person.v_plaunov.mylistview/databases/";
        //private static String DB_PATH = "/storage/self/primary/My Documents/";
        private const val DB_NAME = "myDB"
    }
}