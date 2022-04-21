package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.profile.data.SettingsData
import com.emproto.hoabl.model.RecyclerViewItem

class SecurityAdapter(private val context: Context, private val list: ArrayList<RecyclerViewItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_SECURITY_AUTHENTICATE = 1
        const val VIEW_SECURITY_WHATSAPP_COMMUNICATION = 2
        const val VIEW_SECURITY_LOCATION = 3
        const val VIEW_SETTINGS_ALL_OPTIONS = 4
    }
    private lateinit var settingsAdapter: SettingsAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_SECURITY_AUTHENTICATE -> {
                SecurityItemViewHolder1(
                    SecurityView1Binding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false))
            }
            VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                SecurityItemViewHolder2(
                    SecurityView2Binding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false))
            }
            VIEW_SECURITY_LOCATION -> {
                SecurityItemViewHolder3( SecurityView3Binding.inflate(LayoutInflater.from(parent.context),
                        parent,
                        false))
            }
            else -> {
                SecurityItemViewHolder4(FragmentSettingsBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false))
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            VIEW_SECURITY_AUTHENTICATE -> {
                (holder as SecurityItemViewHolder1).bind(position)
            }
            VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                (holder as SecurityItemViewHolder2).bind(position)
            }
            VIEW_SECURITY_LOCATION -> {
                (holder as SecurityItemViewHolder3).bind(position)
            }
            VIEW_SETTINGS_ALL_OPTIONS -> {
                (holder as SecurityItemViewHolder4).bind(position)
            }
        }

    }
    private inner class SecurityItemViewHolder4(private val binding: FragmentSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.recyclerview1.layoutManager = LinearLayoutManager(context)
            settingsAdapter = SettingsAdapter(context, initData())
            binding.recyclerview1.adapter = settingsAdapter
        }
    }

    private fun initData(): ArrayList<SettingsData> {
        val newsList: ArrayList<SettingsData> = ArrayList<SettingsData>()
        newsList.add(SettingsData("Location", " Control location access here", ))
        newsList.add(SettingsData("Read SMS", "Control location access here", ))
        newsList.add(SettingsData("Send Push Notifications", "Control location access here", ))
        newsList.add(SettingsData("Assistance Access", "Control location access here", ))

        return newsList
    }

    private inner class SecurityItemViewHolder3(private val binding: SecurityView3Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }

    private inner class SecurityItemViewHolder2(private val binding: SecurityView2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }
    private inner class SecurityItemViewHolder1(private val binding: SecurityView1Binding) :
                RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun getItemViewType(position: Int): Int {
       return list[position].viewType
    }
}
