package com.person.v_plaunov.mylistview;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CoinViewActivity extends AppCompatActivity {

    //private static String DB_PATH = "/data/data/com.person.v_plaunov.mylistview/databases/";
    //private static String DB_PATH = "/storage/self/primary/My Documents/";
    private static String DB_NAME = "myDB";
    private SQLiteDatabase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coinview);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int coinId = bundle.getInt("coin_id");
//        String coinState = bundle.getString("coin_state");
//        String coinNominal = bundle.getString("coin_nominal");
//        String coinImgName = bundle.getString("coin_img");

        // переменные для query
        String [] columns = {"_id", "Nominal", "State", "Img", "Year", "Description"};
        String selection = null;
        String[] selectionArgs = null;

        //coins.clear();
        //Наш ключевой хелпер
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this, DB_NAME);
        myDataBase = dbOpenHelper.openDataBase();
        //Все, база открыта!

//        Cursor cursor = myDataBase.query("coins", columns, selection, selectionArgs, null, null, null);
        selection = "_id = ?";
        selectionArgs = new String[] {String.valueOf(coinId)};
        Cursor cursor = myDataBase.query("coins", columns, selection, selectionArgs, null, null, null);
        String coinImagePath = "";
        cursor.moveToFirst();
        String coinState = cursor.getString(cursor.getColumnIndex("State"));
        TextView cState = (TextView)findViewById(R.id.coin_state);
        cState.setText(coinState);

        String coinNominal = cursor.getString(cursor.getColumnIndex("Nominal"));
        TextView cNominal = (TextView)findViewById(R.id.coin_nominal);
        cNominal.setText(coinNominal);
        String coinImgName = cursor.getString(cursor.getColumnIndex("Img"));
        if (!TextUtils.isEmpty(coinImgName))
            coinImgName = coinImgName.replace("\\", "/");
        String coinYear = cursor.getString(cursor.getColumnIndex("Year"));
        TextView cYear = (TextView)findViewById(R.id.coin_year);
        cYear.setText(coinYear);
        String coinDescr = cursor.getString(cursor.getColumnIndex("Description"));
        TextView cDescr = (TextView)findViewById(R.id.coin_description);
        cDescr.setText(coinDescr);

        String packageName = this.getPackageName();
        String DB_PATH = String.format(this.getString(R.string.str_db_path), packageName);
        if (!TextUtils.isEmpty(coinImgName))
            coinImagePath = DB_PATH + coinImgName;

        try {
            InputStream inputStream = new FileInputStream(coinImagePath);
            Drawable d = Drawable.createFromStream(inputStream, null);
            ImageView cImg = findViewById(R.id.coin_pic);
            cImg.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
