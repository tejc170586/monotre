package com.example.monotre

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.RemoteException
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*


class MainActivity : AppCompatActivity(), BeaconConsumer, SingleDialogFragment.NoticeDialogListener {

    companion object{
        const val PERMISSIONS_REQUEST_CODE = 1000
    }
    private lateinit var beaconManager: BeaconManager
    private var isScanning = false
    
    /**
     * test
     */
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    
    private var inputItemName : String = ""
    private var inputUUID     : String = ""
    private var inputMajor    : String = ""
    private var inputMinor    : String = ""
    private var cont = 1
    
    /**
     * /test
     */
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    
        /**
         * test
         */
//        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            floatingActionButtonOnToast("登録したいデバイスを選択してください")
        }
        /**
         * /test
         */
    
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconUtil.IBEACON_FORMAT))

        checkPermission()
        
        Log.d("create", "deteruyo^")
    
//        val data = arrayOf("パンダ", "ダチョウ", "ウミガメ", "メダカ")
//
//        val adapter = ArrayAdapter(
//                this,
//                android.R.layout.simple_list_item_1,
//                data
//        )
//
//        listView1.adapter = adapter
        
    }
    
    /**
     * test
     */
    private fun floatingActionButtonOnToast(str: String) {
        toastDisplay(str)
        SingleDialogFragment().show(supportFragmentManager, "missiles")
    }
    
        override fun toastDisplay(str: String) {
            Toast.makeText(applicationContext, str, Toast.LENGTH_LONG).show()
        }
    
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }
    
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            return when (item.itemId) {
                R.id.action_settings -> true
                else -> super.onOptionsItemSelected(item)
            }
        
        }
    
        override fun onDialogPositiveClick(dialog: DialogFragment) {
            Log.d("mainActivity", "onDialogPositiveClick")
            val dialogView: Dialog = dialog.dialog ?: run {
                Log.d("err", "dialogView is error")
                return
            }
        
            val itemNameEt = dialogView.findViewById(R.id.itemName) as EditText? ?: run {
                Log.d("err", "itemNameDialogView is error")
                return
            }
        
            val uuidEt = dialogView.findViewById(R.id.UUID) as EditText? ?: run {
                Log.d("err", "itemNameDialogView is error")
                return
            }
            Log.d("uuidEt", "UUID : ${uuidEt.text}")
        
            val majorEt = dialogView.findViewById(R.id.major) as EditText? ?: run {
                Log.d("err", "itemNameDialogView is error")
                return
            }
        
            val minorEt = dialogView.findViewById(R.id.minor) as EditText? ?: run {
                Log.d("err", "itemNameDialogView is error")
                return
            }
        
            inputItemName = itemNameEt.text.toString()
            inputUUID     = uuidEt.text.toString()
            inputMajor    = majorEt.text.toString()
            inputMinor    = minorEt.text.toString()
        
            Log.d("normally", " itemName: $inputItemName UUID: $inputUUID " +
                    "major: $inputMajor minor: $inputMinor")
            
//            nzn氏にpass
            beaconManager.bind(this)
//            nznCode(inputItemName, inputUUID, inputMajor, inputMinor)
        }
    
        fun nznCode(name: String, uuid: String, major: String, minor: String) {
            //nzn氏の書いたコードを記述する
        }
    
        override fun onDialogNegativeClick(dialog: DialogFragment) {
            Log.d("mainActivity", "onDialogNegativeClick")
        }
    /**
     * /test
     */

    // Check Permission.
//    private fun checkAndRequestPermissions() {
//        val permissions = arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        val permissionsNotGranted =
//            permissions
//                .filter {
//                    ActivityCompat.checkSelfPermission(this, it) ==
//                            PackageManager.PERMISSION_GRANTED
//                }.toTypedArray()
//        ActivityCompat.requestPermissions(this, permissionsNotGranted, PERMISSIONS_REQUEST_CODE)
//    }

    private fun checkPermission(){
        if((ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_CODE)
        }
    }


    private fun externalStoragePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }


    // Start Service.
    override fun onResume(){
        super.onResume()
//        beaconManager.bind(this)
        isScanning = true
    }


    // Service termination.
    override fun onPause(){
        super.onPause()
        beaconManager.unbind(this)
    }


    override fun onBeaconServiceConnect(){

        beaconManager.addMonitorNotifier(object : MonitorNotifier {
    
            override fun didEnterRegion(region: Region) {
                Log.d("iBeacon", "Enter Region")
                beaconManager.startRangingBeaconsInRegion(region)
            }
    
            override fun didExitRegion(region: Region) {
                beaconManager.stopRangingBeaconsInRegion(region)
            }
    
            override fun didDetermineStateForRegion(i: Int, region: Region) {
                Log.d("MainActivity", "Determine State $i")
            }
        })

        // 表示用データクラス
        data class BeaconDetail(val id1: Identifier, val id2: Identifier, val id3: Identifier, val distance: Double){
//            val uuid  = id1.toString()
//            val major = id2.toString()
//            val minor = id3.toString()
//            val distance = ((distance * 100).roundToInt() / 100).toString()
//            val category = if(distance < 1) "NEAR" else "FAR"
        }

        val map2 = mutableMapOf<String, String>()
        val list2 = ArrayList(map2.values)
        
        val beaconDetails = mutableListOf<BeaconDetail>()
        val beaconDetailsMap = mutableMapOf<String, String>()
        val list = mutableListOf<String>()
        beaconManager.addRangeNotifier { beacons, region ->
            if(beacons.count() > 0){
                beacons
                        .map{
                            val beaconDetail = BeaconDetail(it.id1, it.id2, it.id3, it.distance).toString().replace(it.id1.toString(), "")
                            // ↑ .replaceでUUIDを削除
                            beaconDetailsMap += it.id1.toString() to beaconDetail
                            it
                        }
                        .map { "UUID:" + it.id1 + " major:" + it.id2 + " minor:" + it.id3 + " RSSI:" + it.rssi + " Distance:" + it.distance + " txPower" + it.txPower }
                        .forEach { Log.d("iBeacon", it)}
                Log.d("iBeacon", "beacon available")
                //ListView設定
                val data = arrayOf("パンダ", "ダチョウ", "ウミガメ", "メダカ")
                val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
//                     data
                        beaconDetailsMap.toList()
                )
                
                listView1.adapter = adapter
                
//                beaconDetails.clear()
                
                Log.d("normally", "listView")
                
//                adapter.clear()
//                adapter.notifyDataSetChanged()


//                val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, beaconDetails)
//                listView1.adapter = arrayAdapter
            }else{
                Log.d("iBeacon", "No beacon available")
            }
        }




//        val major: Identifier = Identifier.parse("0")
//        val mRegion: Region = Region("iBeacon", null, major, null)
    
        Log.d("inputId : ", "inputItemName : $inputItemName, inputUUID : $inputUUID,  inputMajor : $inputMajor, inputMinor : $inputMinor")
        // 入力したinputItemName, inputUUID, inputMajor, inputMinorは正常に取得できていることを確認
        /**
         * ↓ こっちのコードは正常に通って
         */
//        val idUUID:Identifier  = Identifier.parse("12345678-9012-3456-7890-123456789012")
//        val idMajor:Identifier = Identifier.parse("0")
//        val idMinor:Identifier = Identifier.parse("0")
//        val mRegion: Region    = Region("test", idUUID, idMajor, idMinor)
        /**
         * ↓ こっちのコードは通らない
         * java.lang.RuntimeException: Unable to resume activity {com.example.monotre/com.example.monotre.MainActivity}:
         * java.lang.IllegalArgumentException: Unable to parse Identifier. が発生
          */
        val idUUID:Identifier  = Identifier.parse(inputUUID)
        val idMajor:Identifier = Identifier.parse(inputMajor)
        val idMinor:Identifier = Identifier.parse(inputMinor)
        val mRegion: Region    = Region(inputItemName, idUUID, idMajor, idMinor)
//         inputItemName, inputUUID, inputMajor, inputMinorは空文字のStringグローバル変数
        /**
         * ドウシテ・・・
         */
        
        try {
            Log.d("Debug", "Start Monitoring.")
            beaconManager.startMonitoringBeaconsInRegion(mRegion)

        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }
}

