package View

import BackendClass.Helper
import Controller.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.my.R
import org.json.JSONArray
import org.json.JSONException

class PlatformMenu : AppCompatActivity() {
    var help = Helper()

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestQueue: RequestQueue
    private lateinit var  adapter_help : PlatformAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_platform_menu)


        if (supportActionBar != null) { supportActionBar!!.hide() }
        window.statusBarColor = this.resources.getColor(R.color.backgrounds)
        window.navigationBarColor = this.resources.getColor(R.color.backgrounds)

        recyclerView = findViewById(R.id.recyvlerview_platform)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {


                    help.getNextPage()

                }
            }

        })

        requestQueue = VolleySingleton.getmInstance(this).requestQueue
        help.requestQueue = VolleySingleton.getmInstance(this).requestQueue
        adapter_help =PlatformAdapter(this@PlatformMenu, help)
        help.Init(recyclerView,adapter_help , this@PlatformMenu)

        help.platformlist.clear()
        help.myArguments.clear()
        help.commit_arguments()
        help.fetcplatform()
    }



}