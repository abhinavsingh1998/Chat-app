package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.*
import com.emproto.hoabl.feature.profile.data.SettingsData
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference

class SecurityAdapter(
    private val context: Context,
    private val list: ArrayList<RecyclerViewItem>,
    private val itemClickListener: ItemClickListener,
    private val isWhatsappEnabled: Boolean,
    private val appPreference: AppPreference
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_REPORT = 0
        const val VIEW_SECURITY_AUTHENTICATE = 1
        const val VIEW_SECURITY_WHATSAPP_COMMUNICATION = 2
        const val VIEW_SECURITY_TIPS = 3
        const val VIEW_SIGN_OUT_ALL = 4
        const val VIEW_SETTINGS_ALL_OPTIONS = 5
    }

    private lateinit var settingsAdapter: SettingsAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_REPORT -> {
                SecurityReportViewHolder(
                    ReportSecurityLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_SECURITY_AUTHENTICATE -> {
                MobilePinAuthenticateViewHolder(
                    SecurityView1Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                WhatsappCommunicationViewHolder(
                    SecurityView2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_SECURITY_TIPS -> {
                SecurityTipsViewHolder(
                    SecurityView3Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_SIGN_OUT_ALL -> {
                SignOutAllViewHolder(
                    SecurityView3Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                SettingsViewHolder(
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
            VIEW_REPORT -> {
                (holder as SecurityReportViewHolder).bind(position)
            }
            VIEW_SECURITY_AUTHENTICATE -> {
                (holder as MobilePinAuthenticateViewHolder).bind()
            }
            VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                (holder as WhatsappCommunicationViewHolder).bind(position)
            }
            VIEW_SECURITY_TIPS -> {
                (holder as SecurityTipsViewHolder).bind(position)
            }
            VIEW_SIGN_OUT_ALL -> {
                (holder as SignOutAllViewHolder).bind(position)
            }
            VIEW_SETTINGS_ALL_OPTIONS -> {
                (holder as SettingsViewHolder).bind()
            }
        }

    }

    private fun initData(): ArrayList<SettingsData> {

        val newsList: ArrayList<SettingsData> = ArrayList()
        newsList.add(SettingsData(context.getString(R.string.send_notification), context.getString(R.string.allow_notification)))

        return newsList
    }

    private inner class SecurityReportViewHolder(private val binding: ReportSecurityLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.buttonView.setOnClickListener {
                itemClickListener.onItemClicked(it, position, position.toString())
            }
        }
    }

    private inner class MobilePinAuthenticateViewHolder(binding: SecurityView1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
        }
    }

    private inner class WhatsappCommunicationViewHolder(private val binding: SecurityView2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.switch1.isChecked= appPreference.getWhatsappStatus()

            binding.switch1.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> {
                        appPreference.whatsappStatus(true)
                        itemClickListener.onItemClicked(
                            binding.switch1,
                            position,
                            isChecked.toString()
                        )
                    }
                    false -> {
                        appPreference.whatsappStatus(false)
                        itemClickListener.onItemClicked(
                            binding.switch1,
                            position,
                            isChecked.toString()
                        )
                    }
                }
            }
        }
    }

    private inner class SecurityTipsViewHolder(private val binding: SecurityView3Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvLoc.text= context.getString(R.string.secure_tips)
            binding.clSecurityTips.setOnClickListener {
                itemClickListener.onItemClicked(binding.clSecurityTips, position, "Security Tips")
            }
        }
    }

    private inner class SignOutAllViewHolder(private val binding: SecurityView3Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                Security.text = context.getString(R.string.log_out_all_text)
                tvLoc.text= context.getString(R.string.manage_your_device_access)
                clSecurityTips.setOnClickListener {
                    itemClickListener.onItemClicked(
                        binding.clSecurityTips,
                        position,
                        Constants.SIGN_OUT_FROM_ALL_DEVICES
                    )
                }
            }

        }
    }

    private inner class SettingsViewHolder(private val binding: FragmentSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.recyclerview1.layoutManager = LinearLayoutManager(context)
            settingsAdapter =
                SettingsAdapter(initData(), itemClickListener, appPreference)
            binding.recyclerview1.adapter = settingsAdapter
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}
