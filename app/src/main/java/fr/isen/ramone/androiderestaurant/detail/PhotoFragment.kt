package fr.isen.ramone.androiderestaurant.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import fr.isen.ramone.androiderestaurant.R
import fr.isen.ramone.androiderestaurant.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString(URL)

        if(url?.isNotEmpty() == true) {
            Picasso.get().load(url).placeholder(R.drawable.logo_resto).into(binding.photo)
        }
    }

    companion object {
        fun newInstance(url: String) = PhotoFragment().apply { arguments = Bundle().apply {  putString(URL, url) } }

        const val URL = "URL"
    }
}