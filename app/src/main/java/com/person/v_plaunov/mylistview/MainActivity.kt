package com.person.v_plaunov.mylistview

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Environment
//import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import org.xml.sax.InputSource
import org.xmlpull.v1.XmlPullParser
import java.io.*
import java.nio.channels.FileChannel
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
//private const val TAG = "AUDIO_QUERY"

class MainActivity : AppCompatActivity() {
    private var myDataBase: SQLiteDatabase? = null
    var myArray: Array<out Coin?>? = null
    var adapter: ArrayAdapter<Coin?>? = null
    //val LOG_TAG = "myLogs"
    val FILENAME = "file"

    //Поиск EditText
    var inputSearch: EditText? = null
    var myArrayList = ArrayList<Coin?>()
    //var myArrayList = MutableCollection<Coin>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lvMain = findViewById<View>(R.id.lv) as ListView
        //        final TextView txt = (TextView)findViewById(R.id.txt);
        val txtNum = findViewById<View>(R.id.textNum) as TextView
        inputSearch = findViewById<View>(R.id.inputSearch) as EditText
        val mContext: Context
        mContext = applicationContext
        initializeData()
        //      Комментарий для GitHub
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.day_of_weeks, android.R.layout.simple_list_item_1);
//        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myArray);
//        myArrayList = Arrays.asList(myArray);
        Collections.addAll(myArrayList, *myArray as Array<out Coin?>)
        val Num = myArrayList.size
        val sNum: CharSequence = Num.toString() //.subSequence(0,4);
        txtNum.text = sNum

        //adapter = new CustomAdapter(this, R.layout.coin_item, R.id.coin, myArrayList);
        adapter = CustomAdapter(this, android.R.layout.simple_list_item_1, myArrayList)
        //        adapter = new CustomAdapter(this, R.layout.coin_item, myArrayList);
        lvMain.adapter = adapter
        //enables filtering for the contents of the given ListView
        lvMain.isTextFilterEnabled = true
        lvMain.onItemClickListener =
            OnItemClickListener { parent, view, position, id -> //                AlertDialog.Builder adb = new AlertDialog.Builder(
//                        MainActivity.this);
//                adb.setTitle("Лист клик");
//                adb.setMessage("Выбран элемент = " + lvMain.getItemAtPosition(position));
//                adb.setPositiveButton("Ok", null);
//                adb.show();
//                txtNum.setText(myArrayList.size());
                val intent = Intent(mContext, CoinViewActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                //                intent.putExtra("coin_id", position + 1);
                val coinObj: Any
                coinObj = lvMain.getItemAtPosition(position)
                val coin = coinObj as Coin
                //val objName = coin.javaClass.name
                val idC = coin.coinId!!.toInt()
                //Coin coin = new Coin();
                intent.putExtra("coin_id", idC)
                //                intent.putExtra("coin_nominal", coins.get(i).nominal);
//                intent.putExtra("coin_state", coins.get(i).state);
//                intent.putExtra("coin_img", coins.get(i).img);
                mContext.startActivity(intent)
            }
        inputSearch!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                //Когда пользователь вводит какой-нибудь текст:
                adapter?.getFilter()?.filter(cs.toString())
                //                    adapter.notifyDataSetChanged();
                    ?: Log.d("filter", "no filter available")

                //MainActivity.this.adapter.getFilter().filter(cs);
            }

            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
            }

            override fun afterTextChanged(arg0: Editable) {}
        })
    }

    //@RequiresApi(Build.VERSION_CODES.Q)
    fun onClick(v: View) {
        when (v.id) {
            R.id.button_LoadFile -> {
                // Очистим базу монет
                //Наш ключевой хелпер
                val dbOpenHelper = DBOpenHelper(this, DB_NAME)
                myDataBase = dbOpenHelper.openDataBase()
                myDataBase?.delete("coins", null, null)

                // 19.10.2023 Изменён механизм получения разрешений на доступ к файловой системе
                if (!PermissionUtils.hasPermissions(this@MainActivity))
                    PermissionUtils.requestPermissions(this@MainActivity, PERMISSION_STORAGE)

//                // Получаем разрешение на запись файла
//                // Check whether this app has write external storage permission or not.
//                val writeExternalStoragePermission = ContextCompat.checkSelfPermission(
//                    this@MainActivity,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                // If do not grant write external storage permission.
//                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
//                    // Request user to grant write external storage permission.
//                    ActivityCompat.requestPermissions(
//                        this@MainActivity,
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION
//                    )
//                }
                // 19.10.2023 Изменён механизм получения разрешений на доступ к файловой системе

                //val xpp: XmlPullParser? = null
                //InputStream document = readFile();
                //xpp.setInput(document, null);
                // Получаем путь к файлу
                //val tv = findViewById<View>(R.id.textFilePath) as TextView
                val extStore = Environment.getExternalStorageDirectory()
                val strDirPath = extStore.absolutePath
                //val strDirPath = tv.text.toString()
                val strFilePath = (strDirPath + resources.getString(R.string.str_xml_path) + FILENAME).replace("//", "/")
                verifyStoragePermissions(this)
                //XmlPullParser xpp = getResources().getXml(R.xml.products);
                // Проверим существование каталога is (для изображений монет) в каталоге databases
                val directoryPath = String.format(this.getString(R.string.str_db_path), packageName) + "is//"
                val file = File(directoryPath)
                if (file.isDirectory) {
                    println("File is a Directory")
                } else {
                    println("Directory doesn't exist!!")
                    file.mkdir()
                }

//                val projection = arrayOf(
//                    MediaStore.Downloads.TITLE,
//                    MediaStore.Downloads.ALBUM
//                )
//
//                val selection = null //not filtering out any row.
//                val selectionArgs = null //this can be null because selection is also null
//                val sortOrder = null //sorting order is not needed
//
//                applicationContext.contentResolver.query(
//                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
//                    projection,
//                    selection,
//                    selectionArgs,
//                    sortOrder
//                )?.use { cursor ->
//
//                    val titleColIndex = cursor.getColumnIndex(MediaStore.Downloads.TITLE)
//                    val albumColIndex = cursor.getColumnIndex(MediaStore.Downloads.ALBUM)
//
//                    Log.d(TAG, "Query found ${cursor.count} rows")
//
//                    while (cursor.moveToNext()) {
//                        val title = cursor.getString(titleColIndex)
//                        val album = cursor.getString(albumColIndex)
//
//                        Log.d(TAG, "$title - $album")
//                    }
//                }
                val parser = CoinFileParser()
                if (parser.parse(strFilePath)) {
                    for (coin in parser.coins) {
                        // Загрузить монеты в базу данных sqlite
                        // База уже открыта!
                        // переменные для query
//                        val columns = arrayOf(
//                            "_id",
//                            "Nominal",
//                            "State",
//                            "Img",
//                            "Year",
//                            "Theme",
//                            "Description"
//                        )
                        var insquery: String
                        val sb = StringBuilder()
                        // Объявляем переменные для значений для вставки в БД
                        var valCoinId: String? = null
                        var valCoinNominal: String? = null
                        var valCoinState: String? = null
                        var valCoinImg: String? = null
                        var valCoinTheme: String? = null
                        var valCoinMint: String? = null
                        var valCoinYear: String? = null
                        var valCoinDiam: String? = null
                        var valCoinWeight: String? = null
                        var valCoinHeight: String? = null
                        var valCoinPrice: String? = null
                        var valCoinPriceForSale: String? = null
                        var valCoinPriceBuy: String? = null
                        var valCoinDate: String? = null
                        var valCoinSeller: String? = null
                        var valCoinMetal: String? = null
                        var valCoinQuality: String? = null
                        var valCoinCreated: String? = null
                        var valCoinStorage: String? = null
                        var valCoinEdge: String? = null
                        var valCoinLoO: String? = null
                        var valCoinLoR: String? = null
                        var valCoinDescription: String? = null
                        sb.append("INSERT INTO " + "COINS" + " Values (")
                        if (!TextUtils.isEmpty(coin!!.coinId)) {
                            valCoinId = "'" + coin.coinId + "'"
                        }
                        sb.append(valCoinId)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinNominal)) {
                            valCoinNominal = "'" + coin.coinNominal + "'"
                        }
                        sb.append(valCoinNominal)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinState)) {
                            valCoinState = "'" + coin.coinState + "'"
                        }
                        sb.append(valCoinState)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinImg)) {
                            valCoinImg = "'" + coin.coinImg + "'"
                            // Скопировать файл с изображением из выгруженного каталога в каталог databases
                            //Составим полный путь к базам для вашего приложения
                            val packageName = this.packageName
                            val DB_PATH = String.format(this.getString(R.string.str_db_path), packageName)
                            val destCoinImagePath = DB_PATH + coin.coinImg!!.replace("\\", "//")
                            val destFile = File(destCoinImagePath)
                            val coinImagePath = strDirPath + this.getString(R.string.str_xml_path) + coin.coinImg!!.replace("\\", "//")
                            val sourceFile = File(coinImagePath)
                            try {
//                                val MY_READ_EXTERNAL_REQUEST : Int = 1
//                                if (checkSelfPermission(
//                                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_EXTERNAL_REQUEST)
//                                }
                                if (PermissionUtils.hasPermissions(this@MainActivity)) {
                                    PermissionUtils.requestPermissions(this@MainActivity, PERMISSION_STORAGE)
                                    copyFile(sourceFile, destFile)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        sb.append(valCoinImg)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinTheme)) {
                            valCoinTheme = "'" + coin.coinTheme + "'"
                        }
                        sb.append(valCoinTheme)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinMint)) {
                            valCoinMint = "'" + coin.coinMint + "'"
                        }
                        sb.append(valCoinMint)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinYear)) {
                            valCoinYear = "'" + coin.coinYear + "'"
                        }
                        sb.append(valCoinYear)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinDiam)) {
                            valCoinDiam = "'" + coin.coinDiam + "'"
                        }
                        sb.append(valCoinDiam)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinWeight)) {
                            valCoinWeight = "'" + coin.coinWeight + "'"
                        }
                        sb.append(valCoinWeight)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinHeight)) {
                            valCoinHeight = "'" + coin.coinHeight + "'"
                        }
                        sb.append(valCoinHeight)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinPrice)) {
                            valCoinPrice = "'" + coin.coinPrice + "'"
                        }
                        sb.append(valCoinPrice)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinPriceForSale)) {
                            valCoinPriceForSale = "'" + coin.coinPriceForSale + "'"
                        }
                        sb.append(valCoinPriceForSale)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinPriceBuy)) {
                            valCoinPriceBuy = "'" + coin.coinPriceBuy + "'"
                        }
                        sb.append(valCoinPriceBuy)
                        sb.append(",")
                        if (coin.coinDate != null) {
                            valCoinDate = "'" + coin.coinDate + "'"
                        }
                        sb.append(valCoinDate)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinSeller)) {
                            valCoinSeller = "'" + coin.coinSeller + "'"
                        }
                        sb.append(valCoinSeller)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinMetal)) {
                            valCoinMetal = "'" + coin.coinMetal + "'"
                        }
                        sb.append(valCoinMetal)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinQuality)) {
                            valCoinQuality = "'" + coin.coinQuality + "'"
                        }
                        sb.append(valCoinQuality)
                        sb.append(",")
                        if (coin.coinCreated != null) {
                            valCoinCreated = "'" + coin.coinCreated + "'"
                        }
                        sb.append(valCoinCreated)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinStorage)) {
                            valCoinStorage = "'" + coin.coinStorage + "'"
                        }
                        sb.append(valCoinStorage)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinEdge)) {
                            valCoinEdge = "'" + coin.coinEdge + "'"
                        }
                        sb.append(valCoinEdge)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinLoO)) {
                            valCoinLoO = "'" + coin.coinLoO + "'"
                        }
                        sb.append(valCoinLoO)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinLoR)) {
                            valCoinLoR = "'" + coin.coinLoR + "'"
                        }
                        sb.append(valCoinLoR)
                        sb.append(",")
                        if (!TextUtils.isEmpty(coin.coinDescription)) {
                            valCoinDescription = "'" + coin.coinDescription + "'"
                        }
                        sb.append(valCoinDescription)
                        sb.append(");")
                        insquery = sb.toString()
                        try {
                            myDataBase?.execSQL(insquery)
                        } catch (e: Exception) {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Ошибка!", Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                        //Log.d("XML", coin.toString());
                    }
                }
                val toast = Toast.makeText(
                    applicationContext,
                    "База монет загружена!", Toast.LENGTH_SHORT
                )
                toast.show()
                this.recreate()
            }
        }
    }

    fun readFile(): InputStream? {
        // открываем поток для чтения
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    openFileInput(strFilePath)));
//        val permissionStatus =
//            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        PermissionUtils.requestPermissions(this@MainActivity, PERMISSION_STORAGE)
        if (PermissionUtils.hasPermissions(this@MainActivity)) {

            // Получаем путь к файлу
            val tv = findViewById<View>(R.id.textFilePath) as TextView
            var strFilePath = tv.text.toString()
            strFilePath = strFilePath + FILENAME
            val document: InputStream?
            val factory = DocumentBuilderFactory.newInstance()
            factory.isNamespaceAware = true
            document = try {
                //val db = factory.newDocumentBuilder()
                //val inputSource = InputSource(FileReader(strFilePath))
                FileInputStream(strFilePath) // db.parse(inputSource);
            } catch (e: ParserConfigurationException) {
                Log.e("Error: ", e.message!!)
                return null
            } /*catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }*/ catch (e: IOException) {
                Log.e("Error: ", e.message!!)
                return null
            }
            return document
        } else {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                REQUEST_CODE_PERMISSION_READ_CONTACTS
//            )
            //if (PermissionUtils.hasPermissions(this@MainActivity))
                PermissionUtils.requestPermissions(this@MainActivity, PERMISSION_STORAGE)
        }
        return null
    }

    private fun initializeData() {
        // переменные для query
        val columns = arrayOf("_id", "Nominal", "State", "Img", "Year", "Theme", "Description")

        //Наш ключевой хелпер
        val dbOpenHelper = DBOpenHelper(this, DB_NAME)
        myDataBase = dbOpenHelper.openDataBase()
        //Все, база открыта!

//        Cursor cursor = myDataBase.query("coins", columns, selection, selectionArgs, null, null, null);
        val cursor = myDataBase?.query("coins", columns, null, null, null, null, null)
        val num = cursor?.count
        myArray = arrayOfNulls(num!!)
        var i = 0
        while (cursor.moveToNext()) {
            val _id = cursor.getInt(0)
            val nominal = cursor.getString(1)
            val state = cursor.getString(2)
            //val img = cursor.getString(3)
            val year = cursor.getString(4)
            val theme = cursor.getString(5)
            val description = cursor.getString(6)

            //myArray[i] = nominal + " " + state + " " + year;
            val coin = Coin(_id.toString(), nominal, state, year, theme, description)
            (myArray as Array<Coin?>)[i] = coin
            i = ++i
        }
        cursor.close()
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File?, destFile: File) {
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }

    companion object {
        //private static String DB_PATH = "/data/data/com.person.v_plaunov.mylistview/databases/";
        //private static String DB_PATH = "/storage/self/primary/My Documents/";
        // Storage Permissions
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private const val PERMISSION_STORAGE = 101
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )

        /**
         * Checks if the app has permission to write to device storage
         *
         * If the app does not has permission then the user will be prompted to grant permissions
         *
         * @param activity
         */
        fun verifyStoragePermissions(activity: Activity?) {

            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }

        private const val DB_NAME = "myDB"
//        private const val REQUEST_CODE_PERMISSION_READ_CONTACTS = 1
//        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
//        private const val REQUEST_CODE_MANAGE_EXTERNAL_STORAGE_PERMISSION = 1
    }
}