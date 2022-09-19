package com.example.my

import Controller.*
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smarteist.autoimageslider.SliderView


class DetailGames : AppCompatActivity() {

    private lateinit var detail_platform_pc:TextView
    private lateinit var detail_geners:TextView
    private lateinit var detail_released:TextView
    private lateinit var detail_rating:TextView
    private lateinit var name_game:TextView
    private lateinit var poster_game:ImageView
    private lateinit var store:TextView
    private lateinit var tags:TextView
    private lateinit var imageUrl: ArrayList<ScreenshotPhotos>
    private  lateinit var sliderView: SliderView
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var savecomments:ImageButton
    private lateinit var person:ImageView
    private lateinit var write_comment:EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentdata: ArrayList<Comments>
    private lateinit var dbref : DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_games)

        recyclerView = findViewById(R.id.recyclerview_comments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        commentdata = arrayListOf<Comments>()
        window.statusBarColor = this.resources.getColor(R.color.backgrounds)
        window.navigationBarColor = this.resources.getColor(R.color.backgrounds)



        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        imageUrl = ArrayList()
        sliderView = findViewById(R.id.poster_image)
        detail_geners = findViewById(R.id.d_geners)
        detail_released = findViewById(R.id.d_released)
        detail_platform_pc = findViewById(R.id.d_platform)
        detail_rating = findViewById(R.id.raiting)
        name_game = findViewById(R.id.nameofgame)
        poster_game = findViewById(R.id.background)
        store = findViewById(R.id.d_stores)
        tags = findViewById(R.id.d_tages)
        savecomments = findViewById(R.id.comment_submet)
        person = findViewById(R.id.person_show)
        write_comment = findViewById(R.id.commenttext)




        val bundle = intent.extras
        val poster = bundle!!.getString("poster")
        val name = bundle!!.getString("name")
        val ratingg = bundle!!.getDouble("rating")
        val generess = bundle.getString("genres")
        val relesded = bundle.getString("released")
        val platform =bundle.getString("platform")
        val tag =bundle.getString("tags")
        val id = bundle.getString("id")
        savecomments.setOnClickListener {

            if (name!!.isNotEmpty())
            {

                val c = write_comment.text.toString()
                val mydatabas = FirebaseDatabase.getInstance().getReference("Comments")


                val user = FirebaseAuth.getInstance().currentUser
                val email = user!!.email
                var CommentId = mydatabas.push().key

                val comment = Comments(CommentId,id.toString(),c,email)
                mydatabas.child(CommentId.toString()).setValue(comment).addOnCompleteListener {
                    Toast.makeText(this , "Comment added " , Toast.LENGTH_SHORT).show()
                }

            }


        }

        val short = bundle.getString("shortimage")?.split(",")?.toTypedArray()
       short?.forEach { i->
            imageUrl.add(ScreenshotPhotos(i.trim()
            ))

        }

        val stores = bundle.getString("stores")



        detail_geners.text = generess
        detail_released.text = "Released : $relesded"
        detail_platform_pc.text = platform
        detail_rating.text ="Rating : " +ratingg.toString() + "/5"
        name_game.text = name
        Glide.with(this).load(poster).into(poster_game)
        store.text = stores
        tags.text = tag

        if(tags.text.isEmpty())
        {
            tags.text = "Sorry there is no record"
        }


        sliderAdapter = SliderAdapter( this@DetailGames,imageUrl)
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.scrollTimeInSec = 2
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()

        getUserData()
    }

    private fun getUserData()
    {
        dbref = FirebaseDatabase.getInstance().getReference("Comments")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                   commentdata.clear()

                    for (userSnapshot in snapshot.children){
                        val comment = userSnapshot.getValue(Comments::class.java)
                            var v =  comment!!.id_name

                        if (intent.extras!!.getString("id") ==v.toString())
                        {

                            comment!!.id_name
                            commentdata.add(comment!!)
                            Log.d("name", comment!!.id_name.toString())

                        }





                    }

                    recyclerView.adapter = CommentsAdapter(this@DetailGames,commentdata)
                }}

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }





    }





