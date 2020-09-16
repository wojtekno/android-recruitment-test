package dog.snow.androidrecruittest.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dog.snow.androidrecruittest.MyApplication
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.databinding.ListFragmentBinding
import dog.snow.androidrecruittest.ui.MainViewModel
import dog.snow.androidrecruittest.ui.detail.DetailsFragment
import dog.snow.androidrecruittest.ui.model.ListItem

class ListFragment : Fragment(R.layout.list_fragment), ((ListItem, Int, View) -> Unit) {
    private lateinit var binding: ListFragmentBinding
    private lateinit var adapter: ListAdapter
    private lateinit var mainVm: MainViewModel
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainVm = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val vmFactory = (requireActivity().application as MyApplication).appGraph.listVMFactory
        viewModel = ViewModelProvider(viewModelStore, vmFactory).get(ListViewModel::class.java)
        viewModel.listItemsLd().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding = ListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ListAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.rvItems.adapter = adapter
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun invoke(item: ListItem, listId: Int, listItemView: View) {
        mainVm.setClickedItem(listId, item)

        val photoTitleTv: TextView = listItemView.findViewById(R.id.tv_photo_title)
        val photoIv: ImageView = listItemView.findViewById(R.id.iv_thumb)
        val albumTitleTv: TextView = listItemView.findViewById(R.id.tv_album_title)
        val fragment = DetailsFragment()
        requireActivity().supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, fragment)
                .addSharedElement(photoIv, "photo")
                .addSharedElement(photoTitleTv, "title")
                .addSharedElement(albumTitleTv, "album")
                .addToBackStack(null)
                .commit()
    }
}