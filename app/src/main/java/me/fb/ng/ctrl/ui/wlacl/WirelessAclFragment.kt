package me.fb.ng.ctrl.ui.wlacl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import me.fb.ng.ctrl.R
import me.fb.ng.ctrl.databinding.FragmentWirelessAclBinding
import me.fb.ng.ctrl.ui.MainActivity

@AndroidEntryPoint
class WirelessAclFragment : Fragment() {

    private val viewModel: WirelessAclViewModel by viewModels()
    private lateinit var binding: FragmentWirelessAclBinding
    private lateinit var devicesAdapter: DevicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWirelessAclBinding.inflate(
            inflater, container, false).apply {
            viewModel = this@WirelessAclFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        devicesAdapter = DevicesAdapter(viewModel)
        binding.listDevices.adapter = devicesAdapter
        binding.listDevices.itemAnimator = null
        // Setup toolbar
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.help -> {
                    //todo: create help dialog and implement displaying help page on it
                    false
                }
                else -> true
            }
        }
        // Listen to view model events
        viewModel.showMessageEvent.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
        viewModel.deviceList.observe(viewLifecycleOwner, Observer {
            devicesAdapter.submitList(it)
        })
        // Control navigational drawer
        binding.toolbar.setNavigationOnClickListener {
            (activity as MainActivity).toggleDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }
}
