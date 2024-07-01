import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myweatherapp.R

class ImageViewPager : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_view_pager, container, false)
        viewPager = view.findViewById(R.id.viewPager)

        val imageResIds = listOf(

            R.drawable.almeria,
            R.drawable.burgos,
            R.drawable.madrid,
            R.drawable.sevilla,
        )

        viewPager.adapter = ImagePagerAdapter(imageResIds)
        return view
    }
}