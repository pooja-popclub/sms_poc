package com.avdt.readinboxsms
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyListAdapter(sampleList1: ArrayList<UserDetailsDto>?) :
    RecyclerView.Adapter<MyListAdapter.ViewHolder>() {
    var sampleList: ArrayList<UserDetailsDto>? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myListData = sampleList!![position]
        holder.msgId.text = sampleList!![position].msg_number
        holder.msgDate.text =  getDateFromMilliseconds(sampleList!![position].msg_date.toLong())
        holder.msgBody.text = sampleList!![position].msg_body
        holder.msgCategory.text = sampleList!![position].category
        holder.msgPlatform.text = sampleList!![position].platform
        holder.msgAmount.text = ""+sampleList!![position].msg_amount
        holder.msgTypee.text = ""+sampleList!![position].msg_type
        holder.msgBank.text = ""+sampleList!![position].msg_bank
        holder.msgPaymentMode.text = ""+sampleList!![position].msg_payment_mode
        holder.relativeLayout.setOnClickListener { view ->
            Toast.makeText(
                view.context,
                 myListData.msg_body,
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun getDateFromMilliseconds(millis: Long): String {
        val dateFormat = "dd-MMM-yyyy hh:mm aa"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }
    override fun getItemCount(): Int {
        return sampleList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null
        var msgId: TextView
        var msgDate: TextView
        var msgBody: TextView
        var msgCategory: TextView
        var msgPlatform: TextView
        var msgAmount: TextView
        var msgTypee: TextView
        var msgBank: TextView
        var msgPaymentMode: TextView
        var relativeLayout: LinearLayout

        init {
            msgId = itemView.findViewById<View>(R.id.msgId) as TextView
            msgDate = itemView.findViewById<View>(R.id.msgDate) as TextView
            msgBody = itemView.findViewById<View>(R.id.msgBody) as TextView
            msgCategory = itemView.findViewById<View>(R.id.msgCategory) as TextView
            msgPlatform = itemView.findViewById<View>(R.id.msgPlatform) as TextView
            msgAmount = itemView.findViewById<View>(R.id.msgAmount) as TextView
            msgTypee = itemView.findViewById<View>(R.id.msgTypee) as TextView
            msgBank = itemView.findViewById<View>(R.id.msgBank) as TextView
            msgPaymentMode = itemView.findViewById<View>(R.id.msgPaymentMode) as TextView
            relativeLayout = itemView.findViewById<View>(R.id.relativeLayout) as LinearLayout
        }
    }

    // RecyclerView recyclerView;
    init {
        sampleList = sampleList1
    }
}