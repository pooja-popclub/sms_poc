package com.avdt.readinboxsms

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avdt.common.SharedPrefsGetSet
import com.avdt.retorfit.ApiResponse
import com.avdt.retorfit.ApiResponse1
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


var tvInformation: TextView? = null
    var recyclerView: RecyclerView? = null
    var btnFoodOrderList: Button? = null
    var btnShowJson: Button? = null
    var btnPermission: Button? = null
    var btnViewPercentage: Button? = null
    var progressBar: ProgressBar? = null

    var llLogin: LinearLayout? = null
    var llAfterLogin: LinearLayout? = null
    var etMobileNumber: EditText? = null
    var loginDetails: TextView? = null
    var btnLogin: Button? = null
    var btnLogout: Button? = null
    var btnCalCoins: Button? = null

    var creditSmsList: ArrayList<UserDetailsDto> = ArrayList()
    var bankList: ArrayList<String?> = ArrayList()
    var categoriesList: ArrayList<String?> = ArrayList()
    var utilityList: ArrayList<String?> = ArrayList()
    var searchKeywordsList: ArrayList<String?> = ArrayList()
    var groceryPartnerList: ArrayList<String?> = ArrayList()
    var entertainmentPartnerList: ArrayList<String?> = ArrayList()
    var foodPartnerList: ArrayList<String?> = ArrayList()
    var travelPartnerList: ArrayList<String?> = ArrayList()
    var eComPartnerList: ArrayList<String?> = ArrayList()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequestPermissions()



        btnFoodOrderList = findViewById(R.id.btnFoodOrderList)
        btnShowJson = findViewById(R.id.btnShowJson)
        btnPermission = findViewById(R.id.btnPermission)
        btnViewPercentage = findViewById(R.id.btnViewPercentage)
        recyclerView = findViewById(R.id.recyclerView)
        tvInformation = findViewById(R.id.tvInformation)
        progressBar = findViewById(R.id.progressBar)

        llLogin = findViewById(R.id.llLogin)
        llAfterLogin = findViewById(R.id.llAfterLogin)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        loginDetails = findViewById(R.id.loginDetails)
        btnLogin = findViewById(R.id.btnLogin)
        btnLogout = findViewById(R.id.btnLogout)
        btnCalCoins = findViewById(R.id.btnCalCoins)

        GlobalFunctions.getCateInformation(this)

        btnFoodOrderList?.setOnClickListener {
            if (checkAndRequestPermissions()) {
                readSMS()
                recyclerView?.visibility=View.VISIBLE
                tvInformation?.visibility=View.GONE
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                val adapter = MyListAdapter(creditSmsList)
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = adapter
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        LinearLayoutManager.VERTICAL
                    )
                )
                btnPermission?.visibility=View.GONE
            }else{
                tvInformation?.visibility=View.VISIBLE
                btnPermission?.visibility=View.VISIBLE
                tvInformation?.text="Permission denied. Please hit the above button to give the permission. Thanks."
            }
        }
            btnShowJson?.setOnClickListener {
                if (!TextUtils.isEmpty(SharedPrefsGetSet.getToken(this))) {
                if (checkAndRequestPermissions()) {
                    readSMS()
                    tvInformation?.visibility = View.GONE
                    recyclerView?.visibility = View.GONE
                } else {
                    tvInformation?.text =
                        "Permission denied. Please kick me to give the permission. Thanks."
                    tvInformation?.visibility = View.VISIBLE
                    // openAlertDialog(this)
                }
                shareDataToServer()
                }else{
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
                }
            }

            btnPermission?.setOnClickListener {
                if (!TextUtils.isEmpty(SharedPrefsGetSet.getToken(this))) {
                if (!checkAndRequestPermissions()) {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
                }else{
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
                }
            }
            btnViewPercentage?.setOnClickListener {
                if (!TextUtils.isEmpty(SharedPrefsGetSet.getToken(this))) {
                progressBar?.visibility = View.VISIBLE
                GlobalFunctions.getDataFromTheServer(this)
                }else{
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
                }
            }
        if (TextUtils.isEmpty(SharedPrefsGetSet.getToken(this))){
            llLogin?.visibility=View.VISIBLE
            llAfterLogin?.visibility=View.GONE
        }else{
            llLogin?.visibility=View.GONE
            llAfterLogin?.visibility=View.VISIBLE
            loginDetails?.setText("Login status: Already Logged In\n\n"+"Access Token: " + SharedPrefsGetSet.getToken(this)+"\n\n "+"Phone number: "+SharedPrefsGetSet.getPhone(this))
        }

        btnLogin?.setOnClickListener {
            if (!TextUtils.isEmpty(etMobileNumber?.text.toString().trim()) && etMobileNumber?.text.toString().trim().length==10){
                //call api
                GlobalFunctions.callLoginApi(this, etMobileNumber?.text.toString().trim())
            }else{
                Toast.makeText(this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show()

            }
        }

        btnCalCoins?.setOnClickListener {
            val map = HashMap<String, Any>()
            map["os_type"] = "android"
            GlobalFunctions.calculateCoins(this,map)
          }

        btnLogout?.setOnClickListener {
           GlobalFunctions.clearSharedPrefs(this)
            etMobileNumber?.setText("")
            etMobileNumber?.setHint("Enter mobile number")
            loginDetails?.setText("")
            llAfterLogin?.visibility = View.GONE
            recyclerView?.visibility = View.GONE
            recyclerView?.visibility = View.GONE
            tvInformation?.visibility = View.GONE
            llLogin?.visibility = View.VISIBLE
        }

    }

    
    private fun shareDataToServer() {

        //credit list
        val creditEntities: MutableList<JsonObject> = ArrayList()
        val creditedItems: Iterator<UserDetailsDto> = creditSmsList.iterator()
        while (creditedItems.hasNext()) {
            val gs = Gson()
            val element: JsonElement =
                gs.fromJson(gs.toJson(creditedItems.next()), JsonElement::class.java)
            val jsonObject: JsonObject = element.getAsJsonObject()
            creditEntities.add(jsonObject)
        }
        //just for the display
        val map = java.util.HashMap<String, Any>()
        map["source"] = "SMS"
        map["data"] = creditEntities
        map["permission"] = ""+checkAndRequestPermissions()
        tvInformation?.visibility=View.VISIBLE
        tvInformation?.setText(""+map)
       // Log.d("MANISH_JAIN",""+map)
        progressBar?.visibility=View.VISIBLE

        GlobalFunctions.sendDataToServer(this,checkAndRequestPermissions(),creditEntities)
    }

    private fun getDateFromMilliseconds(millis: Long): String {
        val dateFormat = "dd-MMM-yyyy hh:mm aa"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }
    private fun readSMS() {
        try {
        creditSmsList.clear()
        val uriSms = Uri.parse("content://sms/inbox")
            val date: Long = Date(System.currentTimeMillis() - 90L * 24 * 3600 * 1000).getTime()
            Log.d("MANISH_JAIN",""+getDateFromMilliseconds(date))
            val cursor = contentResolver.query(
            uriSms, arrayOf("_id", "address", "date", "body"),
                "date" + ">?",
                arrayOf("" + date), "date" + " COLLATE LOCALIZED ASC")



            if (cursor != null) {
            cursor.moveToLast()
            if (cursor.count > 0) {
                do {
                    //top `10 bank
                    //last sms like 1 month
                    val message = UserDetailsDto()
                    val bankList = bankList
                    val utilityList = utilityList
                    val matchesDebitType = searchKeywordsList

                    val regEx: Pattern =
                        Pattern.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)")

                    val paymentMode = arrayOf("UPI","Net Banking","NetBanking","CREDIT Card","Credit Card","Paytm","PhonePe","Google Pay","OlaMoney","Simpl","Amazon Pay","FreeCharge","JioMoney","BHIM","Mobikwik","Airtel Money")

                    for (bl in bankList) {
                        bl?.let {
                        if (cursor.getString(1).contains(it,ignoreCase = true) || cursor.getString(3).contains(it,ignoreCase = true)) {
                            for (cd in matchesDebitType) {
                                cd?.let {
                                    if (cursor.getString(3).contains(cd,ignoreCase = true)) {

                                        val amt: Matcher = regEx.matcher(cursor.getString(3))
                                        if (amt.find()) {
                                            Log.d("MANISH_JAIN",""+cursor.getString(1))
                                            creditSmsList = getListOfSMS(
                                                bl,
                                                cd,
                                                paymentMode,
                                                amt,
                                                cursor,
                                                message,
                                                "Grocery",
                                                groceryPartnerList
                                            )
                                            creditSmsList = getListOfSMS(
                                                bl,
                                                cd,
                                                paymentMode,
                                                amt,
                                                cursor,
                                                message,
                                                "Entertainment",
                                                entertainmentPartnerList
                                            )
                                            creditSmsList = getListOfSMS(
                                                bl,
                                                cd,
                                                paymentMode,
                                                amt,
                                                cursor,
                                                message,
                                                "Food ordering",
                                                foodPartnerList
                                            )
                                            creditSmsList = getListOfSMS(
                                                bl,
                                                cd,
                                                paymentMode,
                                                amt,
                                                cursor,
                                                message,
                                                "Travel",
                                                travelPartnerList
                                            )
                                            creditSmsList = getListOfSMS(
                                                bl,
                                                cd,
                                                paymentMode,
                                                amt,
                                                cursor,
                                                message,
                                                "Ecom",
                                                eComPartnerList
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                 }

                    for (ul in utilityList) {
                        ul?.let {
                            if (cursor.getString(1).contains(it,ignoreCase = true) ||cursor.getString(3).contains(ul,ignoreCase = true)) {
                                for (cd in matchesDebitType) {
                                    cd?.let {
                                        if (cursor.getString(3).contains(cd,ignoreCase = true)) {
                                            val amt: Matcher = regEx.matcher(cursor.getString(3))
                                            if (amt.find()) {
                                                creditSmsList = getListOfSMS(
                                                    ul,
                                                    cd,
                                                    paymentMode,
                                                    amt,
                                                    cursor,
                                                    message,
                                                    "Grocery",
                                                    groceryPartnerList
                                                )
                                                creditSmsList = getListOfSMS(
                                                    ul,
                                                    cd,
                                                    paymentMode,
                                                    amt,
                                                    cursor,
                                                    message,
                                                    "Entertainment",
                                                    entertainmentPartnerList
                                                )
                                                creditSmsList = getListOfSMS(
                                                    ul,
                                                    cd,
                                                    paymentMode,
                                                    amt,
                                                    cursor,
                                                    message,
                                                    "Food ordering",
                                                    foodPartnerList
                                                )
                                                creditSmsList = getListOfSMS(
                                                    ul,
                                                    cd,
                                                    paymentMode,
                                                    amt,
                                                    cursor,
                                                    message,
                                                    "Travel",
                                                    travelPartnerList
                                                )
                                                creditSmsList = getListOfSMS(
                                                    ul,
                                                    cd,
                                                    paymentMode,
                                                    amt,
                                                    cursor,
                                                    message,
                                                    "Ecom",
                                                    eComPartnerList
                                                )

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                } while (cursor.moveToPrevious())
            }
        }
        cursor?.close()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun getListOfSMS(
        bankName: String, creditDebitType: String, paymentMode: Array<String>, amountType: Matcher, cursor: Cursor,
        message: UserDetailsDto, categoryType: String, partnerList: ArrayList<String?>): ArrayList<UserDetailsDto> {
        for (fl in partnerList) {
            fl?.let {
                if (cursor.getString(3).contains(fl,ignoreCase = true)) {
                    for (pm in paymentMode) {
                        if (cursor.getString(3).contains(pm,ignoreCase = true)) {
                            message.msg_payment_mode = pm
                        }
                    }
                    var amount: String =
                        amountType.group(0).replace("inr", "")
                    amount = getTheAmount(amount)
                    message.msg_amount = amount.toDouble()
                    message.msg_number = (cursor.getString(1))
                    message.msg_date = (cursor.getString(2))
                    message.msg_body = (cursor.getString(3))
                    message.platform = fl
                    message.msg_type = creditDebitType
                    message.msg_bank = bankName
                    message.category = categoryType
                    creditSmsList.add(message)
                }
            }
        }
        return creditSmsList
    }

    private fun getTheAmount(amount1: String): String {
        var amount: String = amount1
        amount = amount.replace("rs".toRegex(), "")
        amount = amount.replace("inr".toRegex(), "")
        amount = amount.replace(" ".toRegex(), "")
        amount = amount.replace(",".toRegex(), "")
        amount = amount.replace("Rs.", "")
        amount = amount.replace("Rs", "")
        amount = amount.replace("INR", "")
        amount = amount.replace("INR.", "")
        amount = amount.replace("MRP", "")
        amount = amount.replace("MRP.", "")
        return amount
    }

    fun showResponse(info: ApiResponse){
        progressBar?.visibility=View.GONE
        recyclerView?.visibility=View.GONE
        tvInformation?.visibility=View.VISIBLE
        tvInformation?.setText("Data send success status: "+info.is_success)
        if (info.is_success){
           btnPermission?.visibility=View.GONE
        }else{
            btnPermission?.visibility=View.VISIBLE
        }
    }

    fun showLoginResponse(info: ApiResponse){
        progressBar?.visibility=View.GONE
        loginDetails?.visibility=View.VISIBLE
        recyclerView?.visibility=View.GONE
        if (!TextUtils.isEmpty(info.access_token)) {
            SharedPrefsGetSet.setToken(this,info.access_token)
            SharedPrefsGetSet.setPhone(this,info.user.phone_number)
            loginDetails?.setText("Login status: Logged In Successfully\n\n"+"Access Token: " + info.access_token+"\n\n "+"Phone number: "+info.user.phone_number)
            llAfterLogin?.visibility = View.VISIBLE
            llLogin?.visibility = View.GONE
        }
    }

    fun showResponseOfGet(info: ApiResponse1){
        progressBar?.visibility=View.GONE
        recyclerView?.visibility=View.GONE
        tvInformation?.visibility=View.VISIBLE
        if (!TextUtils.isEmpty(info.data.message)){
            tvInformation?.setText("Coin's Calculation Status: " + info.data.message)
        }else {
            tvInformation?.setText("My Coin's: " + info.data.coins)
        }
    }

    fun showSettingDetails(info: ApiResponse){
        progressBar?.visibility=View.GONE
        recyclerView?.visibility=View.GONE
        tvInformation?.visibility=View.VISIBLE
        //list of banks
        bankList.clear()
        categoriesList.clear()
        utilityList.clear()
        searchKeywordsList.clear()
        groceryPartnerList.clear()
        entertainmentPartnerList.clear()
        foodPartnerList.clear()
        travelPartnerList.clear()
        eComPartnerList.clear()
        if (info.data != null) {
            info.data.banks_sms_headers.HDFC?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.ICICI?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.AXIS?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.SBI?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.CITI?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.KOTAK?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.IDFC?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.INDUSIND?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.YESBANK?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.PNB?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.BOB?.let { bankList.addAll(it) }
            info.data.banks_sms_headers.BOI?.let { bankList.addAll(it) }

            info.data.categories?.let { categoriesList.addAll(it) }
            info.data.utility_names?.let { utilityList.addAll(it) }
            info.data.search_keywords?.let { searchKeywordsList.addAll(it) }
            info.data.platforms.grocery?.let { groceryPartnerList.addAll(it) }
            info.data.platforms.entertainment?.let { entertainmentPartnerList.addAll(it) }
            info.data.platforms.foodOrdering?.let { foodPartnerList.addAll(it) }
            info.data.platforms.travel?.let { travelPartnerList.addAll(it) }
            info.data.platforms.ecom?.let { eComPartnerList.addAll(it) }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
        if (sms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                1
            )
            //openAlertDialog(this)
            return false
        }
       // readSMS()
        return true
    }

    private fun openAlertDialog(mainActivity: MainActivity) {
        AlertDialog.Builder(mainActivity)
            .setTitle("Contact Permisson Required!!!")
            .setMessage("Please provide the permison from the settings.")
            .setPositiveButton("Setting"
            ) { dialog, which ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                )
            }
            .setNegativeButton("Exit") { dialogInterface, which ->
                finish()
            }.setNeutralButton("Dismiss Dialog") { dialogInterface, which ->
                dialogInterface.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        if (checkAndRequestPermissions()){
            btnPermission?.visibility=View.GONE
            readSMS()
        }
        super.onResume()
    }
}