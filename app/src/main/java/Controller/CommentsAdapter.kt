package Controller

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.my.R
import com.google.firebase.auth.FirebaseAuth
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
        val mydatabas = FirebaseDatabase.getInstance().getReference("Comments")
        val user = FirebaseAuth.getInstance().currentUser
        val udi = user!!.uid
        val comment:Comments = comment_list[position]
        holder.comments.text = comment.commentName
        holder.email_comment.text = comment.email
        if (udi != comment.id_user)
        {
            holder.btndelete.isVisible = false
            holder.btnupdate.isVisible = false
        }

        holder.btnupdate.setOnClickListener {

          updateInfo(comment)

        }


        holder.btndelete.setOnClickListener {

            delete_comment(comment)

        }
    }


    private  fun updateInfo(s:Comments)
    {


        val builder = AlertDialog.Builder(mycontext)
        builder.setTitle("Update Info")
        val inflater = LayoutInflater.from(mycontext)
        val view = inflater.inflate(R.layout.comment_update,null)

        val first_name = view.findViewById<TextView>(R.id.comment_name)

        first_name.text = s.commentName
        builder.setView(view)

        builder.setPositiveButton("Update",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                val mydatabas = FirebaseDatabase.getInstance().getReference("Comments")
                val user = FirebaseAuth.getInstance().currentUser
                var udi = user!!.uid
                val comment_name  = first_name.text.toString().trim()
                if (comment_name.isEmpty()){
                    first_name.error = "Please enter your firstname"
                    return
                }

                val c = Comments(s.id,s.id_name,comment_name,s.email,s.id_user)
                mydatabas.child(udi).child(s.id.toString()).setValue(c)
                val pdialog = ProgressDialog(mycontext)
                pdialog.setTitle("Update progress")
                pdialog.setMessage("This progress is done successfully")
                pdialog.show()
                val progressRunnable = Runnable { pdialog.cancel() }

                val pdCanceller = Handler()
                pdCanceller.postDelayed(progressRunnable, 1000)


            }})

        builder.setNegativeButton("cancel",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })

        val alert = builder.create()
        alert.show()

    }

    private fun delete_comment(s:Comments)
    {

        val mydatabas = FirebaseDatabase.getInstance().getReference("Comments")
        val user = FirebaseAuth.getInstance().currentUser
        var udi = user!!.uid
        mydatabas.child(udi).child(s.id.toString()).removeValue()

    }






    }