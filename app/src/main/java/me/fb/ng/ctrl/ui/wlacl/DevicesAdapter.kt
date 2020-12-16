package me.fb.ng.ctrl.ui.wlacl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.fb.ng.ctrl.databinding.ItemDeviceBinding
import me.fb.ng.ctrl.model.common.DeviceModel

/**
 * An adapter for a list of devices.
 */
class DevicesAdapter(
   private val delegate: DeviceListDelegate
) : ListAdapter<DeviceModel, DeviceViewHolder>(DeviceDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DeviceViewHolder(
            ItemDeviceBinding.inflate(inflater, parent, false),
            delegate
        )
    }

    override fun onBindViewHolder(
        holder: DeviceViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}

class DeviceViewHolder(
    private val binding: ItemDeviceBinding,
    private val delegate: DeviceListDelegate
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(device: DeviceModel) {
        binding.device = device
        binding.delegate = delegate
        binding.executePendingBindings()
    }
}

object DeviceDiffCallback : DiffUtil.ItemCallback<DeviceModel>() {

    override fun areItemsTheSame(
        oldItem: DeviceModel,
        newItem: DeviceModel
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: DeviceModel,
        newItem: DeviceModel
    ): Boolean = oldItem.name == newItem.name &&
            oldItem.mac == newItem.mac &&
            oldItem.selected == newItem.selected
}
