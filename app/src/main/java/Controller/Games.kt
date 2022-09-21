package Controller

import org.json.JSONArray
import org.json.JSONObject

data class Games(
    var id:String?=null, val name:String?=null, val platform:String?=null, val released:String?=null, val rating: Double?=null,
    val genres:String?=null, val poster:String?=null, val updated:String?=null,
    val short_screenshot:String?=null, val stores:String?=null, val tags:String?=null, val id_name:String?=null,
    var fav_state:Boolean?=false
){

}