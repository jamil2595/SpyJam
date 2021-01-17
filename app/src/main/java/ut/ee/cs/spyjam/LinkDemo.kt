package ut.ee.cs.spyjam

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_link_demo.*


class LinkDemo : AppCompatActivity() {


    @SuppressLint("WrongConstant", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_demo)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("Click to the link to see your data on the web page! Remember your User ID!")
        builder.setPositiveButton("Agree"){ dialog, which ->
            Log.d("msg", "pressed")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        val html = "<a href=\"http://jamilgur.pythonanywhere.com/\">Link to webpage</a>"
        val result = HtmlCompat.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        textView2.text = result
        textView2.movementMethod = LinkMovementMethod.getInstance()
        val bundle = intent.extras
        val message = bundle!!.getString("message")
        textView3.text = "UID: $message"

    }

}