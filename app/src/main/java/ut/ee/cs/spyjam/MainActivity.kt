package ut.ee.cs.spyjam

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private var smsArr = ArrayList<String>()
    private var nameList = ArrayList<String>()
    private var numberList = ArrayList<String>()
    var randomID = UUID.randomUUID()
    var randomIDtext = randomID.toString()
    val database = FirebaseDatabase.getInstance()
    var storage = FirebaseStorage.getInstance()
    val myRef = database.getReference("userData").child("$randomID")
    val REQ_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_main)
        //alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("This application is only for educational purposes. It can read personal data such as contacts,SMS,calendar events,location coordinates etc. All permissions have to be granted to use the application!")
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

        }
        button2.setOnClickListener {
           namesThread.start()
            button2.isClickable=false
            button2.alpha=0.5f
            val text = "Uloaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
        location.setOnClickListener {
            location.isClickable=false
            location.alpha=0.5f
            getLocation()
        }
        account.setOnClickListener {
            getAccName()
            account.isClickable=false
            account.alpha=0.5f
        }
        events.setOnClickListener {
            getCalendarEvents()
            events.isClickable = false
            events.alpha = 0.5f
            val text = "Uloaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration).show()
        }
        logs.setOnClickListener {
            logs.isClickable = false
            logs.alpha = 0.5f
            getLogs()
            val text = "Uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration).show()
        }
        userid.setOnClickListener {
            userid.text = randomIDtext
        }
        //permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED
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
        val cursorSMS = contentResolver.query(Uri.parse("content://sms/"), null, null, null)
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
        val nameCursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null
        )
        val NumberCursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null
        )
        var number = ""
        var nameStep=0
        var numberStep=0
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
    fun getLastPicture() {
        val storageRef = storage.reference
        var file = Uri.fromFile(File("/document/image:32"))
        val picRef = storageRef.child("images/${file.lastPathSegment}")
        var uploadTask = picRef.putFile(file)
        TODO("implement getpicture")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    fun getLogs(){
        var callMap = HashMap<String, String>()
        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null)
        while (cursor!!.moveToNext()){
           var numberIndex = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
           var durationIndex = cursor!!.getColumnIndex(CallLog.Calls.DURATION)
           var number = cursor.getString(numberIndex)
           var callDuration = cursor.getString(durationIndex)
           callMap.put(number, callDuration)
        }
        cursor.close()
        myRef.child("CallDuration").updateChildren(callMap as Map<String, Any>)
    }

     @SuppressLint("MissingPermission", "NewApi", "SimpleDateFormat")
     fun getCalendarEvents(){
          var eventArr = ArrayList<String>()
         var uri = CalendarContract.Events.CONTENT_URI;
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
}

