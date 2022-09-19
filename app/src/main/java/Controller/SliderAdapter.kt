package Controller
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.my.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(var mycontext: Context, private val userList: ArrayList<ScreenshotPhotos>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    override fun getCount(): Int {

        return userList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.custom_photo, null)

        return SliderViewHolder(inflate)
    }

    // on below line we are calling on bind view holder method to set the data to our image view.
    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {

        val game : ScreenshotPhotos = userList[position]
        if (viewHolder != null) {
            Glide.with(mycontext).load(game.shortscreenshot).into(viewHolder.imageView)
        }

    }

    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {


        var imageView: ImageView = itemView!!.findViewById(R.id.myimage)
    }
}