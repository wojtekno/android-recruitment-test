package dog.snow.androidrecruittest.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dog.snow.androidrecruittest.MyApplication
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.databinding.DetailsFragmentBinding
import dog.snow.androidrecruittest.ui.MainViewModel

class DetailsFragment : Fragment(R.layout.details_fragment) {

    private lateinit var mainVm: MainViewModel
    private lateinit var detailsVM: DetailsViewModel
    private lateinit var binding: DetailsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        mainVm = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val detailsVmFactory = (requireActivity().application as MyApplication).appGraph.detailsVmFactory
        detailsVmFactory.setListItem(mainVm.getClickedItem())
        detailsVM = ViewModelProvider(viewModelStore, detailsVmFactory).get(DetailsViewModel::class.java)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = detailsVM
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPhotoTitle.transitionName = "title"
        binding.ivPhoto.transitionName = "photo"
        binding.tvAlbumTitle.transitionName = "album"
        detailsVM.getDetails().observe(viewLifecycleOwner, Observer { it ->
            if (it.url.isNotEmpty()) {
                Picasso.get().load(it.url)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(binding.ivPhoto, object : Callback {
                            override fun onSuccess() {
                                startPostponedEnterTransition()
                            }

                            override fun onError(e: Exception?) {
                                startPostponedEnterTransition()
                            }

                        })
            }
        })
    }
}