package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.profile.SecurityFragment
import com.emproto.hoabl.feature.profile.data.SettingsData
import com.emproto.hoabl.model.RecyclerViewItem

class SecurityAdapter(private val context: Context, private val list: ArrayList<RecyclerViewItem>,val helpItemInterface: SecurityFragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                SecurityAuthenticateViewHolder(
                    SecurityView1Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                SecurityCommunicationViewHolder(
                    SecurityView2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_SECURITY_LOCATION -> {
                SecurityLocationViewHolder(
                    SecurityView3Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                SecurityItemViewHolder(
                    FragmentSettingsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            VIEW_SECURITY_AUTHENTICATE -> {
                (holder as SecurityAuthenticateViewHolder).bind(position)
            }
            VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                (holder as SecurityCommunicationViewHolder).bind(position)
            }
            VIEW_SECURITY_LOCATION -> {
                (holder as SecurityLocationViewHolder).bind(position)
                holder.itemView.setOnClickListener {
                    helpItemInterface.onClickItem(holder.layoutPosition)
                }
            }
            VIEW_SETTINGS_ALL_OPTIONS -> {
                (holder as SecurityItemViewHolder).bind(position)
            }
        }

    }

    private inner class SecurityItemViewHolder(private val binding: FragmentSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.recyclerview1.layoutManager = LinearLayoutManager(context)
            settingsAdapter = SettingsAdapter(context, initData())
            binding.recyclerview1.adapter = settingsAdapter
        }
    }

    private fun initData(): ArrayList<SettingsData> {
        val newsList: ArrayList<SettingsData> = ArrayList<SettingsData>()
        newsList.add(SettingsData("Location", " Control location access here"))
        newsList.add(SettingsData("Read SMS", "Control location access here"))
        newsList.add(SettingsData("Send Push Notifications", "Control location access here"))
        newsList.add(SettingsData("Assistance Access", "Control location access here"))

        return newsList
    }

    private inner class SecurityLocationViewHolder(private val binding: SecurityView3Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }

    private inner class SecurityCommunicationViewHolder(private val binding: SecurityView2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
        }
    }

    private inner class SecurityAuthenticateViewHolder(private val binding: SecurityView1Binding) :
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

    interface HelpItemInterface {
        fun onClickItem(position: Int)
    }
}
