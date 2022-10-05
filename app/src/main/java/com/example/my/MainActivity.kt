package com.example.my

import BackendClass.Helper
import Controller.Games
import Controller.GamesAdapter
import Controller.VolleySingleton
import View.MyfavoriteMenu
import View.PlatformMenu
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{


         var help = Helper()

    private lateinit var  adapter_help :GamesAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestQueue: RequestQueue
    private lateinit var ref: DatabaseReference
    var next:Int = 0

    private lateinit var navigationview : NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = this.resources.getColor(R.color.toolbarcolor)
        window.navigationBarColor = this.resources.getColor(R.color.backgrounds)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        requestQueue = VolleySingleton.getmInstance(this).requestQueue
        help.requestQueue = VolleySingleton.getmInstance(this).requestQueue






        navigationview = findViewById(R.id.nav_menu)
        navigationview.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this,Drawablelayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled= true
        Drawablelayout.addDrawerListener(toggle)
        toggle.syncState()


        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        adapter_help =GamesAdapter(this@MainActivity, help.gamelist )
        help.Init(recyclerView,adapter_help , this@MainActivity)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {

                    help.getNextPage()

                }
            }

        })



            help.getDataFromUrl()



        swiperRefresh.setOnRefreshListener {
            help.getDataFromUrl()
            swiperRefresh.isRefreshing = false

        }

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


                    help.getPageBySearch(searchtext)


                }


                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }



    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, id ->

            var myauth = FirebaseAuth.getInstance()
                myauth.signOut()
           myauth.addAuthStateListener {
                if (myauth.currentUser==null) this.finish()
            }
            var progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setTitle("Log out")
            progressDialog.setMessage("Please wait .......")
            progressDialog.show()
            exitProcess(-1)

        }
        builder.setNegativeButton("No") { dialog, id ->

            dialog.dismiss()

        }
        val alert: AlertDialog = builder.create()
        alert.setCancelable(false)
        alert.show()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {

            R.id.home ->
            {
                help.gamelist.clear()
                help.myArguments.clear()
                help.commit_arguments()
                help.getDataFromUrl()

            }
            R.id.last30day ->
            {

                help.gamelist.clear()
                help.myArguments.clear()
                help.commit_arguments()

                //last 30 days
                val sdf1 = SimpleDateFormat("yyyy-MM-dd")
                var date = Date()
                val c = Calendar.getInstance()
                c.time = date
                c.add(Calendar.DATE ,-30)
                var currentdate:Date = c.time
                val last30days = sdf1.format(currentdate)



                // current day
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val current1 =  sdf.format(Date())

                help.getlast30days(last30days,current1)

            }
            R.id.thisweek->{
                help.gamelist.clear()
                help.myArguments.clear()
                help.commit_arguments()
                // current day
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val current1 =  sdf.format(Date())

                //last7 days

                val sdf1 = SimpleDateFormat("yyyy-MM-dd")
                var date = Date()
                val c = Calendar.getInstance()
                c.time = date
                c.add(Calendar.DATE ,-7)
                var currentdate:Date = c.time
                val last7days = sdf1.format(currentdate)

                help.getlast7days(last7days,current1)
            }
            R.id.action ->  help.GameByGenres("action")
            R.id.shooter -> help.GameByGenres("shooter")
            R.id.fighting ->help.GameByGenres("fighting")
            R.id.adventure->help.GameByGenres("adventure")
            R.id.horror ->  help.GameByGenres("indie")
            R.id.sport ->   help.GameByGenres("sports")
            R.id.racing ->  help.GameByGenres("racing")
            R.id.simulation->help.GameByGenres("simulation")
            R.id.puzzle ->  help.GameByGenres("puzzle")
            R.id.arcade ->  help.GameByGenres("arcade")
            R.id.education->help.GameByGenres("educational")
            R.id.cards ->   help.GameByGenres("card")
            R.id.rpg ->   help.GameByGenres("role-playing-games-rpg")
            R.id.strategy ->   help.GameByGenres("strategy")
            R.id.platform -> startActivity(Intent(this, PlatformMenu::class.java))
            R.id.myfavorit -> startActivity(Intent(this, MyfavoriteMenu::class.java))
            R.id.orderbyrating ->help.orderByrating("-metacritic")






        }


        return true
    }


}


