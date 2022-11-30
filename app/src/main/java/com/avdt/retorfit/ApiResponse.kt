package com.avdt.retorfit

data class ApiResponse(
    val is_success: Boolean,
    val message: String,
    val access_token: String,
    val data:DTO,
    val user:User
)
data class DTO(
    val banks_sms_headers: BankSmsHeaders,
    val platforms: ApiResponse1.Platforms,
    var categories: ArrayList<String?>? = null,
    var utility_names: ArrayList<String?>? = null,
    var search_keywords: ArrayList<String?>? = null

)
data class User(
    val phone_number: String
)

data class BankSmsHeaders(
    var HDFC: ArrayList<String?>? = null,
    var ICICI: ArrayList<String?>? = null,
    var AXIS: ArrayList<String?>? = null,
    var SBI: ArrayList<String?>? = null,
    var CITI: ArrayList<String?>? = null,
    var KOTAK: ArrayList<String?>? = null,
    var IDFC: ArrayList<String?>? = null,
    var INDUSIND: ArrayList<String?>? = null,
    var YESBANK: ArrayList<String?>? = null,
    var PNB: ArrayList<String?>? = null,
    var BOB: ArrayList<String?>? = null,
    var BOI: ArrayList<String?>? = null,
)