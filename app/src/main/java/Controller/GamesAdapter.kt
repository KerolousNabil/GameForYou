package Controller

import BackendClass.Helper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import kotlin.collections.ArrayList

class GamesAdapter (var mycontext : Context, private var userList : ArrayList<Games>) : RecyclerView.Adapter<GamesAdapter.MyViewHolder>(){
    var helper = Helper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_game,parent , false)
        return MyViewHolder(itemView)

    }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       helper.requestQueue = VolleySingleton.getmInstance(mycontext).requestQueue

        val game : Games = userList[position]

        holder.name_game.text = game.name
        holder.rating_game.text = game.rating.toString()
       Glide.with(mycontext).load(game.poster).into(holder.poster_game)
        holder.card.startAnimation(AnimationUtils.loadAnimation(mycontext,R.anim.custom_animation))

       holder.constraintLayout.setOnClickListener(View.OnClickListener {

          helper.details_game(userList[position].id_name) { res ->

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
          bundle.putDouble("rating", game.rating)
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

       holder.favorite.setOnClickListener{
           val mydatabas = FirebaseDatabase.getInstance().getReference("GamerFavorite")
           val user = FirebaseAuth.getInstance().currentUser
           val email = user!!.email
           val uid = user.uid
           val root :DatabaseReference = mydatabas.ref.child("$uid")

           var Gameid = mydatabas.push().key
           game.id = Gameid



           root.child(Gameid.toString()).setValue(game).addOnCompleteListener {
               Toast.makeText(mycontext , "Added to Favorite List " , Toast.LENGTH_SHORT).show()


           }



           holder.favorite.setBackgroundResource(R.drawable.ic_baseline_turned_in_24)



           //save photo to storage

        /*   val p = ProgressDialog(mycontext)

           p.setMessage("Uploadind .....")
           p.setCancelable(false)
           p.show()

           val storageref = FirebaseStorage.getInstance().getReference("images/")
           storageref.putFile().addOnSuccessListener{
               holder.poster_game.setImageURI(null)


               Toast.makeText(mycontext, "Uploaded Successfully..",Toast.LENGTH_SHORT).show()
               if (p.isShowing) p.dismiss()


           }
               .addOnFailureListener{

                   if (p.isShowing) p.dismiss()

                   Toast.makeText(mycontext, "Failed",Toast.LENGTH_SHORT).show()






               }*/

       }
      /* holder.favorite.setOnClickListener {
           val myDatabase = FirebaseDatabase.getInstance().getReference("Employee")
           val gameid = myDatabase.push().key
           myDatabase.child(gameid.toString()).removeValue()
           holder.favorite.setBackgroundResource(R.drawable.ic_baseline_turned_in_not_24)

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
        val favorite:ImageView = itemView.findViewById(R.id.save_card_to_database) as ImageView


    }


}