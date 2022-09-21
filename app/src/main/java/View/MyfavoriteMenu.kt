package View

import BackendClass.Helper
import Controller.*
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.example.my.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MyfavoriteMenu : AppCompatActivity() {

    var help = Helper()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbref : DatabaseReference
    private lateinit var requestQueue: RequestQueue

    private lateinit var game_favorite : ArrayList<Games>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myfavorite_menu)
        this.intent.getBooleanExtra("checkBoxValue", false)

        if (supportActionBar != null) { supportActionBar!!.hide() }
        window.statusBarColor = this.resources.getColor(R.color.backgrounds)
        window.navigationBarColor = this.resources.getColor(R.color.backgrounds)
        setSupportActionBar(toolbar)

        requestQueue = VolleySingleton.getmInstance(this).requestQueue
        help.requestQueue = VolleySingleton.getmInstance(this).requestQueue

        recyclerView = findViewById(R.id.recyclerview_favorite)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        game_favorite = arrayListOf<Games>()

        help.Init(recyclerView , GamesAdapter(this@MyfavoriteMenu,game_favorite) , this@MyfavoriteMenu)
        getUserData()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        val search = menu.findItem(R.id.appSearchBarfavorit)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search for any game ......"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                val searchtext = query!!.lowercase(Locale.getDefault())

                if (searchtext.isNotEmpty())
                {


                  // help.getPageBySearch(searchtext)


                }


                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

            //    help.getPageBySearch(newText!!)
              /*  game_favorite.clear()
                val searchtext = newText!!.lowercase(Locale.getDefault())
                game_favorite.forEach {
                    if (it.name!!.toLowerCase(Locale.getDefault()).contains(searchtext))
                    {
                        game_favorite.add(it)
                    }
                }
                recyclerView.adapter!!.notifyDataSetChanged()
*/

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    private fun getUserData()
    {


        dbref = FirebaseDatabase.getInstance().getReference("GamerFavorite")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val email = user!!.email
        val root :DatabaseReference = dbref.ref.child("$uid")

        help.getFavorites_id {


            root.get().addOnSuccessListener {
                if (it.exists()) {
                    game_favorite.clear()

                    for (userSnapshot in it.children) {
                        if(userSnapshot.key != "favorites"){
                            val game = userSnapshot.getValue(Games::class.java)
                            Log.d("game", help.favorit_list.toString())
                           if (help.favorit_list.contains(game!!.id_name))
                            {
                                game.fav_state = true

                            }
                            game_favorite.add(game!!)
                        }




                    }

                    recyclerView.adapter = GamesAdapter(this@MyfavoriteMenu, game_favorite)

                }


            }
        }
    }


}