package ut.ee.cs.spyjam

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LinkDemo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_demo)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("Click button to see your user ID and then click to the link to see your data on the web page!")
        builder.setPositiveButton("Agree"){ dialog, which ->
            Log.d("msg","pressed")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()


    }
}