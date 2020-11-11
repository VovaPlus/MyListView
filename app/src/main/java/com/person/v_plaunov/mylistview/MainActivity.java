package com.person.v_plaunov.mylistview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    //private static String DB_PATH = "/data/data/com.person.v_plaunov.mylistview/databases/";
    //private static String DB_PATH = "/storage/self/primary/My Documents/";

    private static final String DB_NAME = "myDB";
    private static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private SQLiteDatabase myDataBase;
    public Coin[] myArray;
    public ArrayAdapter<Coin> adapter;
    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    //Поиск EditText
    EditText inputSearch;
    ArrayList<Coin> myArrayList = new ArrayList<Coin>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView lvMain = (ListView)findViewById(R.id.lv);
//        final TextView txt = (TextView)findViewById(R.id.txt);
        final TextView txtNum = (TextView)findViewById(R.id.textNum);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        final Context mContext;
        mContext = getApplicationContext();

        initializeData();
//      Комментарий для GitHub
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.day_of_weeks, android.R.layout.simple_list_item_1);
//        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myArray);
//        myArrayList = Arrays.asList(myArray);
        Collections.addAll(myArrayList, myArray);
        Integer Num = myArrayList.size();
        CharSequence sNum = Num.toString(); //.subSequence(0,4);

        txtNum.setText(sNum);

        //adapter = new CustomAdapter(this, R.layout.coin_item, R.id.coin, myArrayList);
        adapter = new CustomAdapter(this, android.R.layout.simple_list_item_1, myArrayList);
//        adapter = new CustomAdapter(this, R.layout.coin_item, myArrayList);
        lvMain.setAdapter(adapter);
        //enables filtering for the contents of the given ListView
        lvMain.setTextFilterEnabled(true);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                AlertDialog.Builder adb = new AlertDialog.Builder(
//                        MainActivity.this);
//                adb.setTitle("Лист клик");
//                adb.setMessage("Выбран элемент = " + lvMain.getItemAtPosition(position));
//                adb.setPositiveButton("Ok", null);
//                adb.show();
//                txtNum.setText(myArrayList.size());

                Intent intent = new Intent(mContext, CoinViewActivity.class);
//                intent.putExtra("coin_id", position + 1);
                Object coinObj = new Object();
                coinObj = lvMain.getItemAtPosition(position);
                Coin coin = (Coin)coinObj;
                String objName = coin.getClass().getName();
                int idC = Integer.parseInt(coin.coinId);
                //Coin coin = new Coin();
                intent.putExtra("coin_id", idC);
//                intent.putExtra("coin_nominal", coins.get(i).nominal);
//                intent.putExtra("coin_state", coins.get(i).state);
//                intent.putExtra("coin_img", coins.get(i).img);
                mContext.startActivity(intent);
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //Когда пользователь вводит какой-нибудь текст:
                if (adapter != null) {
                    adapter.getFilter().filter(cs.toString());
//                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("filter", "no filter available");
                }

                //MainActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button_LoadFile:
                // Очистим базу монет
                //Наш ключевой хелпер
                DBOpenHelper dbOpenHelper = new DBOpenHelper(this, DB_NAME);
                myDataBase = dbOpenHelper.openDataBase();
                myDataBase.delete("coins",null,null);

                // Получаем разрешение на чтение файла
                // Check whether this app has write external storage permission or not.
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                // If do not grant write external storage permission.
                if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
                {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
                XmlPullParser xpp = null;
                //InputStream document = readFile();
                //xpp.setInput(document, null);
                // Получаем путь к файлу
                TextView tv = (TextView) findViewById(R.id.textFilePath);
                String strDirPath = tv.getText().toString();
                String strFilePath = strDirPath + FILENAME;

                //XmlPullParser xpp = getResources().getXml(R.xml.products);
                CoinFileParser parser = new CoinFileParser();
                if(parser.parse(strFilePath))
                {
                    for(Coin coin: parser.getCoins()){
                        // Загрузить монеты в базу данных sqlite
                        // База уже открыта!
                        // переменные для query
                        String [] columns = {"_id", "Nominal", "State", "Img", "Year", "Theme", "Description"};
                        String insquery = "";
                        StringBuilder sb = new StringBuilder();
                        // Объявляем переменные для значений для вставки в БД
                        String valCoinId = null;
                        String valCoinNominal = null;
                        String valCoinState = null;
                        String valCoinImg = null;
                        String valCoinTheme = null;
                        String valCoinMint = null;
                        String valCoinYear = null;
                        String valCoinDiam = null;
                        String valCoinWeight = null;
                        String valCoinHeight = null;
                        String valCoinPrice = null;
                        String valCoinPriceForSale = null;
                        String valCoinPriceBuy = null;
                        String valCoinDate = null;
                        String valCoinSeller = null;
                        String valCoinMetal = null;
                        String valCoinQuality = null;
                        String valCoinCreated = null;
                        String valCoinStorage = null;
                        String valCoinEdge = null;
                        String valCoinLoO = null;
                        String valCoinLoR = null;
                        String valCoinDescription = null;

                        sb.append("INSERT INTO " + "COINS" + " Values (");
                        if (!TextUtils.isEmpty(coin.coinId)) {
                            valCoinId = "'" + coin.coinId + "'";
                        }
                        sb.append(valCoinId);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinNominal)) {
                            valCoinNominal = "'" + coin.coinNominal + "'";
                        }
                        sb.append(valCoinNominal);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinState)) {
                            valCoinState = "'" + coin.coinState + "'";
                        }
                        sb.append(valCoinState);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinImg)) {
                            valCoinImg = "'" + coin.coinImg + "'";
                            // Скопировать файл с изображением из выгруженного каталога в каталог databases
                            //Составим полный путь к базам для вашего приложения

                            String packageName = this.getPackageName();

                            String DB_PATH = String.format(this.getString(R.string.str_db_path), packageName);
                            String destCoinImagePath = DB_PATH + coin.coinImg.replace("\\", "//");
                            File destFile = new File(destCoinImagePath);
                            String coinImagePath = strDirPath + coin.coinImg.replace("\\", "//");
                            File sourceFile = new File(coinImagePath);
                            try {
                                copyFile(sourceFile, destFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        sb.append(valCoinImg);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinTheme)) {
                            valCoinTheme = "'" + coin.coinTheme + "'";
                        }
                        sb.append(valCoinTheme);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinMint)) {
                            valCoinMint = "'" + coin.coinMint + "'";
                        }
                        sb.append(valCoinMint);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinYear)) {
                            valCoinYear = "'" + coin.coinYear + "'";
                        }
                        sb.append(valCoinYear);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinDiam)) {
                            valCoinDiam = "'" + coin.coinDiam + "'";
                        }
                        sb.append(valCoinDiam);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinWeight)) {
                            valCoinWeight = "'" + coin.coinWeight + "'";
                        }
                        sb.append(valCoinWeight);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinHeight)) {
                            valCoinHeight = "'" + coin.coinHeight + "'";
                        }
                        sb.append(valCoinHeight);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinPrice)) {
                            valCoinPrice = "'" + coin.coinPrice + "'";
                        }
                        sb.append(valCoinPrice);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinPriceForSale)) {
                            valCoinPriceForSale = "'" + coin.coinPriceForSale + "'";
                        }
                        sb.append(valCoinPriceForSale);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinPriceBuy)) {
                            valCoinPriceBuy = "'" + coin.coinPriceBuy + "'";
                        }
                        sb.append(valCoinPriceBuy);
                        sb.append(",");

                        if (!(coin.coinDate == null)) {
                            valCoinDate = "'" + coin.coinDate + "'";
                        }
                        sb.append(valCoinDate);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinSeller)) {
                            valCoinSeller = "'" + coin.coinSeller + "'";
                        }
                        sb.append(valCoinSeller);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinMetal)) {
                            valCoinMetal = "'" + coin.coinMetal + "'";
                        }
                        sb.append(valCoinMetal);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinQuality)) {
                            valCoinQuality = "'" + coin.coinQuality + "'";
                        }
                        sb.append(valCoinQuality);
                        sb.append(",");

                        if (!(coin.coinCreated == null)) {
                            valCoinCreated = "'" + coin.coinCreated + "'";
                        }
                        sb.append(valCoinCreated);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinStorage)) {
                            valCoinStorage = "'" + coin.coinStorage + "'";
                        }
                        sb.append(valCoinStorage);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinEdge)) {
                            valCoinEdge = "'" + coin.coinEdge + "'";
                        }
                        sb.append(valCoinEdge);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinLoO)) {
                            valCoinLoO = "'" + coin.coinLoO + "'";
                        }
                        sb.append(valCoinLoO);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinLoR)) {
                            valCoinLoR = "'" + coin.coinLoR + "'";
                        }
                        sb.append(valCoinLoR);
                        sb.append(",");

                        if (!TextUtils.isEmpty(coin.coinDescription)) {
                            valCoinDescription = "'" + coin.coinDescription + "'";
                        }
                        sb.append(valCoinDescription);
                        sb.append(");");

                        insquery = sb.toString();
                        try {
                            myDataBase.execSQL(insquery);
                        }
                        catch (Exception e) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Ошибка!", Toast.LENGTH_SHORT);
                        }
                        //Log.d("XML", coin.toString());
                    }
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        "База монет загружена!", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }
    public InputStream readFile() {
        // открываем поток для чтения
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    openFileInput(strFilePath)));
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            // Получаем путь к файлу
            TextView tv = (TextView) findViewById(R.id.textFilePath);
            String strFilePath = tv.getText().toString();
            strFilePath = strFilePath + FILENAME;
            InputStream document = null;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            try {
                DocumentBuilder db = factory.newDocumentBuilder();
                InputSource inputSource = new InputSource(new FileReader(strFilePath));
                document = new FileInputStream(strFilePath); // db.parse(inputSource);


            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } /*catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }*/ catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
            return document;

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_READ_CONTACTS);
        }
        return null;
    }

    private void initializeData(){
        // переменные для query
        String [] columns = {"_id", "Nominal", "State", "Img", "Year", "Theme", "Description"};

        //Наш ключевой хелпер
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this, DB_NAME);
        myDataBase = dbOpenHelper.openDataBase();
        //Все, база открыта!

//        Cursor cursor = myDataBase.query("coins", columns, selection, selectionArgs, null, null, null);
        Cursor cursor = myDataBase.query("coins", columns, null, null, null, null, null);
        int num = cursor.getCount();
        myArray = new Coin[num];
        int i = 0;

        while (cursor.moveToNext()){
            int _id = cursor.getInt(0);
            String nominal = cursor.getString(1);
            String state = cursor.getString(2);
            String img = cursor.getString(3);
            String year = cursor.getString(4);
            String theme = cursor.getString(5);
            String description = cursor.getString(6);

            //myArray[i] = nominal + " " + state + " " + year;
            Coin coin = new Coin(String.valueOf(_id), nominal, state, year, theme, description);
            myArray[i] = coin;
            i = ++i;
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }


}