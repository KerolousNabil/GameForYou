package Controller

import org.json.JSONArray
import org.json.JSONObject

data class Games(
    var id:String?=null, val name:String, val platform:String, val released:String, val rating: Double,
    val genres:String, val poster:String, val updated:String,
    val short_screenshot:String, val stores:String , val tags:String , val id_name:String
){

}