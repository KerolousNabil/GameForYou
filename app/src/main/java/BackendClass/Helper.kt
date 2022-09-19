package BackendClass

import Controller.*
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.browser.trusted.Token
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

val genres_map = mutableMapOf<String,Int>(
    "action" to 4,"indie" to 51, "adventure" to 3, "role-playing-games-rpg" to 5, "strategy" to 10,
    "shooter" to 2, "casual" to 40, "simulation" to 14, "puzzle" to 7, "arcade" to 11,
    "platformer" to 83, "racing" to 1, "massively-multiplayer" to 59, "sports" to 15, "fighting" to 6,
    "family" to 19, "board-games" to 28, "educational" to 34, "card" to 17
)


class Helper {
    val gamelist: ArrayList<Games> = ArrayList()
    val platformlist: ArrayList<PlatformsData> = ArrayList()

    lateinit var requestQueue: RequestQueue
    lateinit var mycontext: Context
    var basi_url = "https://api.rawg.io/api/games?key=774ceaa28aa8481c8cf311f07baa8121"
    var url = basi_url
    val myArguments = mutableMapOf<String,String>("page" to "1")
    lateinit var recyclerView:RecyclerView
    lateinit var adaper:Any
    lateinit var adapterforplatform:PlatformAdapter

    fun Init ( recyclerView: RecyclerView,adaper: Any , mycontext:Context){
     this.mycontext = mycontext
    this.recyclerView = recyclerView
    this.adaper= adaper

        }




      fun commit_arguments(){

          url = basi_url
            if(this.myArguments["page"] == null){
                this.myArguments["page"] = "1"
            }
          myArguments.forEach{
                  (k,v)->
              url += "&${k}=${v}"

          }
      }
      fun getDataFromUrl(){
          val jsonObjctRequest = JsonObjectRequest(
              Request.Method.GET, url, null,
              { response ->
                  for (i in 0 until response.getJSONArray("results").length()) {
                      try {
                          val results = response.getJSONArray("results")
                          val detail  = results.getJSONObject(i)
                          var backgroundimage = detail.getString("background_image")
                          var rate = detail.getDouble("rating")
                          val released = detail.getString("released")
                          val updated = detail.getString("updated")
                          val genres : JSONArray =  detail.getJSONArray("genres")
                          val short_screenshots: JSONArray = detail.getJSONArray("short_screenshots")
                          val id = detail.getInt("id")


                          val platforms: JSONArray = detail.getJSONArray("platforms")
                          val stores:JSONArray = detail.getJSONArray("stores")
                          val tags:JSONArray = detail.getJSONArray("tags")


                          var array_include_geners = arrayOf("")
                          var array_include_nameplatform = arrayOf("")
                          var array_include_screenshots = arrayOf("")
                          var array_include_stores = arrayOf("")
                          var array_include_tags = arrayOf("")

                          for (loop_geners in 0 until genres.length())
                          {

                              array_include_geners+=genres.getJSONObject(loop_geners).getString("name")

                          }

                          for (loop_platforms in 0 until platforms.length())
                          {
                              array_include_nameplatform+=platforms.getJSONObject(loop_platforms).getJSONObject("platform").getString("name")


                          }

                        for (loop_stores in 0 until stores.length())
                          {
                            array_include_stores+= stores.getJSONObject(loop_stores).getJSONObject("store").getString("name")
                          }


                          for (loop_screenshoots in 0 until short_screenshots.length())
                          {
                              array_include_screenshots+=short_screenshots.getJSONObject(loop_screenshoots).getString("image")


                          }
                          for (loop_tags in 0 until tags.length())
                          {
                              array_include_tags+=tags.getJSONObject(loop_tags).getString("name")


                          }
                         var t3 = array_include_stores.contentToString().drop(2)
                          t3= t3.dropLast(1)

                          var t4 = array_include_tags.contentToString().drop(2)
                          t4= t4.dropLast(1)

                          var t = array_include_geners.contentToString().drop(2)
                          t =t.dropLast(1)

                          var t1 = array_include_nameplatform.contentToString().drop(2)
                          t1 =t1.dropLast(1)

                          var t2 =array_include_screenshots.contentToString().drop(2)
                          t2 =t2.dropLast(1)


                          val game = Games(id=null , detail.getString("name"),
                              t1, released, rate, t, backgroundimage,updated , t2  ,
                              t3 ,t4 , id.toString()
                          )

                          gamelist.add(game)


                      } catch (e: JSONException) {
                          e.printStackTrace()
                      }




                      if( myArguments["page"]!!.toInt() > 1){

                         this.recyclerView.adapter!!.notifyDataSetChanged()

                      }else{
                          this.recyclerView.adapter = adaper as GamesAdapter

                      }


                  }

              }
          ) { error -> Toast.makeText(mycontext, "Please check Internet Connection and try again", Toast.LENGTH_LONG).show() }
          requestQueue.add(jsonObjctRequest)
      }


    fun fetcplatform() {
        val url = "https://api.rawg.io/api/platforms?key=774ceaa28aa8481c8cf311f07baa8121"
        val jsonObjctRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.getJSONArray("results").length()) {
                    try {
                        var name = response.getJSONArray("results").getJSONObject(i).getString("name")
                        var imagebackground = response.getJSONArray("results").getJSONObject(i).getString("image_background")
                        var id = response.getJSONArray("results").getJSONObject(i).getInt("id")
                        var games_count = response.getJSONArray("results").getJSONObject(i).getLong("games_count")


                        val platform = PlatformsData(imagebackground , name,id,games_count)
                        platformlist.add(platform)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if( myArguments["page"]!!.toInt() > 1){

                        this.recyclerView.adapter!!.notifyDataSetChanged()

                    }else{
                        this.recyclerView.adapter = adaper as PlatformAdapter

                    }


                }

            }
        ) { error -> Toast.makeText(mycontext, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue.add(jsonObjctRequest)
    }




    fun getNextPage(){

            this.myArguments["page"] = (myArguments["page"]!!.toInt() + 1).toString()
            this.commit_arguments()
            this.getDataFromUrl()
        }


      fun getPageBySearch( searchtext:String){
          this.gamelist.clear()
          this.myArguments["page"]= "1"
          this.myArguments["search"] = searchtext
          this.commit_arguments()
          this.getDataFromUrl()
      }

      fun GameByGenres ( genres:String)
      {
          this.myArguments.clear()
          this.gamelist.clear()
          this.myArguments["genres"]= genres_map[genres].toString()
          this.commit_arguments()
          this.getDataFromUrl()
      }
    fun GameByPlatform ( id:Int )
    {
        this.myArguments.clear()
        this.gamelist.clear()
        this.adaper = this.adaper as GamesAdapter
        this.myArguments["platforms"]= id.toString()
        this.commit_arguments()
        this.getDataFromUrl()
    }

    fun getlast30days(last30day :String , current:String)
    {
        this.myArguments["dates"]= last30day+','+current
        this.commit_arguments()
        this.getDataFromUrl()
    }
    fun getlast7days(last7days:String, currentdate: String)
    {
        this.myArguments["dates"]= last7days+','+currentdate
        this.commit_arguments()
        this.getDataFromUrl()
    }

    fun orderByrating(value:String)
    {
        this.myArguments.clear()
        this.gamelist.clear()
        this.myArguments["ordering"] = value
        this.commit_arguments()
        this.getDataFromUrl()
    }
    fun details_game(id: String, callbackr:(res:JSONObject)->Unit) {

        val url = "https://api.rawg.io/api/games/$id?key=774ceaa28aa8481c8cf311f07baa8121"
        val jsonObjctRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                callbackr(response)

            }



        ) { error -> Toast.makeText(mycontext, error.message, Toast.LENGTH_SHORT).show() }
        requestQueue.add(jsonObjctRequest)



    }

}