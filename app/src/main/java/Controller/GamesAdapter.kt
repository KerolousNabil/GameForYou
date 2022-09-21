package Controller

import BackendClass.Helper
import View.MyfavoriteMenu
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my.DetailGames
import com.example.my.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONObject

class GamesAdapter (var mycontext : Context, private var userList : ArrayList<Games>) : RecyclerView.Adapter<GamesAdapter.MyViewHolder>(){
    var helper = Helper()
    var value = mutableMapOf<String,String>()
    val mydatabas = FirebaseDatabase.getInstance().getReference("GamerFavorite")
    val user = FirebaseAuth.getInstance().currentUser
    val email = user!!.email
    val uid = user!!.uid
    val root: DatabaseReference = mydatabas.ref.child("$uid")
    var favorit_list = mutableListOf("")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        root.child("favorites").get().addOnSuccessListener {

            if (it.value !=null)
            {
                value = it.value as MutableMap<String, String>
                Log.d("oncreate",value["id"]!!.split(",").toString())
                favorit_list = value["id"]!!.split(",") as MutableList<String>
            }




        }

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_game,parent , false)
        return MyViewHolder(itemView)


    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
           helper.requestQueue = VolleySingleton.getmInstance(mycontext).requestQueue
       // val  adaper = helper.adaper as GamesAdapter
        Log.d("target game " , userList[position].toString())
        val game : Games = userList[position]
       Log.d("fav_stat",game.fav_state.toString())
            if (game.fav_state== true)
            {
                holder.favorite.isChecked = true
            }else{
                holder.favorite.isChecked = false

            }
        holder.name_game.text = game.name
        holder.rating_game.text = game.rating.toString()
       Glide.with(mycontext).load(game.poster).into(holder.poster_game)
        holder.card.startAnimation(AnimationUtils.loadAnimation(mycontext,R.anim.custom_animation))

       holder.constraintLayout.setOnClickListener(View.OnClickListener {

          helper.details_game(userList[position].id_name!!) { res ->

          val intent = Intent(mycontext, DetailGames::class.java)
          val bundle = Bundle()
           var reruirments =JSONObject()

              var x = res.getJSONArray("platforms")
              for (loop_pc in 0 until x.length())
              {
                  val platorm = x.getJSONObject(loop_pc).getJSONObject("platform")
                    if (platorm.getString("slug") =="pc")
                    {
                        reruirments = x.getJSONObject(loop_pc).getJSONObject("requirements")
                    }

              }
          bundle.putString("desc" ,res.getString("description").replace("<.*?>".toRegex(RegexOption.DOT_MATCHES_ALL), "") )

              var min:String;
              var rec:String = "sorry there is no record"

              min=rec


          if(reruirments.has("recommended"))
          {
              rec = reruirments.getString("recommended")

          }
            if(reruirments.has("minimum")){
                min = reruirments.getString("minimum")

          }

              bundle.putString("min" , min)
              bundle.putString("rec" , rec)
          bundle.putString("name",game.name)
          bundle.putString("link",res.getString("website"))
          bundle.putDouble("rating", game.rating!!)
          bundle.putString("genres", game.genres)
          bundle.putString("poster", game.poster)
          bundle.putString("released", game.released)
          bundle.putString("platform", game.platform)
          bundle.putString("shortimage", game.short_screenshot)
          bundle.putString("stores", game.stores)
          bundle.putString("tags", game.tags)
          bundle.putString("id", game.id_name)
          intent.putExtras(bundle)
          mycontext.startActivity(intent)

       }


       })
       holder.favorite.setOnCheckedChangeListener{checkbox , ischecked->

           if (ischecked) {


               val mydatabas = FirebaseDatabase.getInstance().getReference("GamerFavorite")
               val user = FirebaseAuth.getInstance().currentUser
               val email = user!!.email
               val uid = user.uid
               val root: DatabaseReference = mydatabas.ref.child("$uid")

               var Gameid = mydatabas.push().key
               game.id = Gameid

               root.child(game.id_name.toString()).setValue(game).addOnCompleteListener {
                   Toast.makeText(mycontext, "Added to Favorite List ", Toast.LENGTH_SHORT).show()
                root.get().addOnSuccessListener {

                    Log.d("a7a",it.child("favorites").toString())
                    if(it.child("favorites").value!=null)
                    {
                        value = it.child("favorites").value as MutableMap<String, String>
                        Log.d("",value.toString())
                        var target = value["id"].toString() + "," + game.id_name

                        if(value["id"]!!.split(",").contains(game.id_name)){
                            target = value["id"].toString()
                        }
                        value["id"]= target
                    }
                    else{

                        value["id"] = game.id_name.toString()

                    }
                    root.child("favorites").setValue(value).addOnCompleteListener {
                        Log.d("fav",root.child("favorites").toString())

                    }


                }

               }

            /*   root.child(Gameid.toString()).addValueEventListener(object : ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       if (snapshot.exists()){
                           if (snapshot.child("favorites").value !=null)
                           {
                               value = snapshot.child("favorites").value as MutableMap<String, String>
                               Log.d("",value.toString())
                               value["id"]= value["id"].toString() + "," + game.id_name
                           }
                          else
                           {
                               value["id"] = game.id_name.toString()
                           }

                           root.child("favorites").setValue(value)


                       }}

                   override fun onCancelled(error: DatabaseError) {
                   }

               })*/




           }
           else
           {

               val myDatabase = FirebaseDatabase.getInstance().getReference("GamerFavorite")
               val user = FirebaseAuth.getInstance().currentUser
               val uid = user!!.uid
               val root : DatabaseReference = myDatabase.ref.child("$uid")
               Log.d("idss" , game.id.toString())
               root.child(game.id_name.toString()).removeValue()
                   .addOnSuccessListener {
                       helper.getFavorites_id {
                           Toast.makeText(mycontext,"Item has been deleted successfully", Toast.LENGTH_LONG).show()






                           if(helper.favorit_list.size <= 1 ){

                               value["id"] = ""
                           }else{

                               helper.favorit_list.removeAll { it == game.id_name }
                               value["id"] = helper.favorit_list.joinToString(",")
                           }
                               root.child("favorites").setValue( value)


                       }
                   }



           }

       }

    /*   holder.favorite.setOnClickListener(){
       //    if (holder.favorite.background.toString())
           //if buttn is red
                //delete from firebae
           //else
                //add to firebase

           //notif adapter to change

        /*   holder.favorite.setBackgroundResource(R.drawable.ic_baseline_turned_in_not_24)


           Log.d("before color", holder.favorite.toString())
           holder.favorite.setBackgroundResource(R.drawable.ic_baseline_turned_in_24)

Log.d("after color", holder.favorite.imageTintList?.defaultColor.toString())*/



       }*/



    }



    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name_game: TextView = itemView.findViewById(R.id.Gamename)
        val rating_game: TextView = itemView.findViewById(R.id.rating)
        val poster_game :ImageView = itemView.findViewById(R.id.imageviewOfgame)
        val constraintLayout : ConstraintLayout = itemView.findViewById(R.id.list_layout)
        val card: CardView = itemView.findViewById(R.id.custom_cardview)
        val favorite:CheckBox = itemView.findViewById(R.id.cb_checkhart) as CheckBox


    }




    }



