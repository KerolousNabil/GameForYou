package Controller

import BackendClass.Helper
import android.content.Context
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
import com.example.my.R
class PlatformAdapter (var mycontext : Context,var helper:Helper) : RecyclerView.Adapter<PlatformAdapter.MyViewHolder>() {
    var Platformlist = helper.platformlist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_platform_photo,parent , false)
        return MyViewHolder(itemView)
    }



    override fun getItemCount(): Int {
        return Platformlist.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


        val poster_platform : ImageView = itemView.findViewById(R.id.imageviewofplatform)
        val text_of_platform: TextView = itemView.findViewById(R.id.textplatform)
        val card: CardView = itemView.findViewById(R.id.card1)
        val constraintLayout : ConstraintLayout = itemView.findViewById(R.id.constraint)
        val gamecount :TextView = itemView.findViewById(R.id.gamecount)




    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val platform : PlatformsData = Platformlist[position]
        holder.text_of_platform.text = platform.title_platform
        Glide.with(mycontext).load(platform.photo_platform).into(holder.poster_platform)
        holder.poster_platform.alpha = 0.2F
        holder.card.startAnimation(AnimationUtils.loadAnimation(mycontext,R.anim.custom_animation))
        holder.gamecount.text ="Games count \n " + platform.game_count.toString()

        holder.constraintLayout.setOnClickListener(View.OnClickListener {

                Toast.makeText(mycontext,Platformlist[position].title_platform,Toast.LENGTH_LONG).show()

            helper.adaper = GamesAdapter(mycontext,helper.gamelist)
           helper.GameByPlatform(Platformlist[position].Id )


        })





    }
}