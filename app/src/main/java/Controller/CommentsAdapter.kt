package Controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CommentsAdapter(var mycontext : Context, private var comment_list : ArrayList<Comments>) : RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_comments, parent, false)
        return MyViewHolder(itemView)
    }



    override fun getItemCount(): Int {
        return comment_list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val comments: TextView = itemView.findViewById(R.id.commenttext)
        val email_comment: TextView = itemView.findViewById(R.id.text_email_name)
        val btnupdate : Button = itemView.findViewById(R.id.edit)
        val btndelete : Button = itemView.findViewById(R.id.delete)


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val comment:Comments = comment_list[position]
        holder.comments.text = comment.comment
        holder.email_comment.text = comment.email

        holder.btndelete.setOnClickListener {
            val mydatabas = FirebaseDatabase.getInstance().getReference("Comments")

            mydatabas.child(comment.id.toString()).removeValue()

        }
    }

}