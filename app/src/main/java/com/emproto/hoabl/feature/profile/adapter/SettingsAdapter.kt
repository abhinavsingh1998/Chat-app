package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.profile.data.SettingsData
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference
import javax.inject.Inject


class SettingsAdapter(
    private val context: Context,
    private val settingsList: ArrayList<SettingsData>,
    private val showPushNotifications: Boolean,
    private val itemClickListener: ItemClickListener

) : RecyclerView.Adapter<SettingsAdapter.MyViewHolder>() {
    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.setting_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = settingsList[position]
        holder.tvHeading.text = currentItem.heading
        holder.desc.text = currentItem.desc
        if (holder.adapterPosition == 0) {
            when (showPushNotifications) {
                true -> holder.switch.isChecked = true
                false -> holder.switch.isChecked = false
            }
            holder.switch.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        eventTrackingSendPushNotifications()
                        itemClickListener.onItemClicked(
                            holder.switch,
                            position,
                            isChecked.toString()
                        )
                    }
                    false -> {
                        itemClickListener.onItemClicked(
                            holder.switch,
                            position,
                            isChecked.toString()
                        )
                    }
                }
            }
        }
        if (holder.adapterPosition == 1) {
            holder.switch.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        itemClickListener.onItemClicked(
                            holder.switch,
                            position,
                            "Voice Command"
                        )
                    }
                    false -> {

                    }
                }
            }
        }
    }

    private fun eventTrackingSendPushNotifications() {
        Mixpanel(context).identifyFunction(appPreference.getMobilenum(), Mixpanel.SENDPUSHNOTIFICATIONS)
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeading: TextView = itemView.findViewById(R.id.tvHeading)
        val desc: TextView = itemView.findViewById(R.id.desc)
        val switch: SwitchCompat = itemView.findViewById(R.id.setting_switch)
    }

}