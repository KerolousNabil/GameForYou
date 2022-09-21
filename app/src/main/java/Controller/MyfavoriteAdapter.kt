package Controller

import BackendClass.Helper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my.DetailGames
import com.example.my.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyfavoriteAdapter (var mycontext : Context, private val userList : ArrayList<GamesFavorite>) : RecyclerView.Adapter<MyfavoriteAdapter.MyViewHolder>() {
    var helper = Helper()


    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.custom_myfavorite_photo,
            parent,false)
        return MyViewHolder(itemView)

    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val GamenameMyfavorite : TextView = itemView.findViewById(R.id.GamenameMyfavorite)
        val rating_favorite : TextView = itemView.findViewById(R.id.rating_favorite)
        val imageviewOfgamemyfavorite : ImageView = itemView.findViewById(R.id.imageviewOfgamemyfavorite)
        val deletefromdatabase: CheckBox = itemView.findViewById(R.id.cb_checkhart)
        val constraintLayout1 : ConstraintLayout = itemView.findViewById(R.id.list_layout_favorite)







    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        helper.requestQueue = VolleySingleton.getmInstance(mycontext).requestQueue

        val game : GamesFavorite = userList[position]
        holder.GamenameMyfavorite.text = game.name
        holder.rating_favorite.text = game.rating.toString()
        Glide.with(mycontext).load(game.poster).into(holder.imageviewOfgamemyfavorite)


       holder.deletefromdatabase.setOnCheckedChangeListener{checkbox , ischecked->


           val myDatabase = FirebaseDatabase.getInstance().getReference("GamerFavorite")
           val user = FirebaseAuth.getInstance().currentUser
           val uid = user!!.uid
           val root : DatabaseReference = myDatabase.ref.child("$uid")
           root.child(game.ID.toString()).removeValue()
           Toast.makeText(mycontext,"Item has been deleted successfully", Toast.LENGTH_LONG).show()
       }

        holder.constraintLayout1.setOnClickListener(View.OnClickListener {
            val intent = Intent(mycontext, DetailGames::class.java)
            val bundle = Bundle()
            bundle.putDouble("rating", game.rating!!)
            bundle.putString("genres",game.genres)
            bundle.putString("poster",game.poster)
            bundle.putString("released",game.released)
            bundle.putString("platform",game.platform)
            bundle.putString("shortimage",game.short_screenshot)
            bundle.putString("stores",game.stores)
            bundle.putString("tags",game.tags)
            intent.putExtras(bundle)
            mycontext.startActivity(intent)
        })



    }

   private fun deleteInfo(g:GamesFavorite){

        val myDatabase = FirebaseDatabase.getInstance().getReference("GamerFavorite")
       val user = FirebaseAuth.getInstance().currentUser
       val uid = user!!.uid
       val root : DatabaseReference = myDatabase.ref.child("$uid")
       root.child(g.ID.toString()).removeValue()

    }
}