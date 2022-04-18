package com.emproto.hoabl.feature.home.profileAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.*
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
            SecurityAdapter.VIEW_SECURITY_AUTHENTICATE -> {
                (holder as SecurityItemViewHolder1).bind(position)
            }
            SecurityAdapter.VIEW_SECURITY_WHATSAPP_COMMUNICATION -> {
                (holder as SecurityItemViewHolder2).bind(position)
            }
            SecurityAdapter.VIEW_SECURITY_LOCATION -> {
                (holder as SecurityItemViewHolder3).bind(position)
            }
            SecurityAdapter.VIEW_SETTINGS_ALL_OPTIONS -> {
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































//    companion object {
//        const val VIEW_SECURITY_ONE = 1
//        const val VIEW_SECURITY_TWO = 2
//        const val VIEW_SECURITY_THREE = 3
//        const val VIEW_SECURITY_FOUR = 4
//    }
//
//    private val context: Context = context
//
//
//    var list: ArrayList<SecurityData> = list
//    lateinit var adapter: SettingsAdapter
//    lateinit var recyclerview1: RecyclerView
//
//    private inner class View1ViewHolder(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var message: TextView = itemView.findViewById(R.id.tvmobile)
//        fun bind(position: Int) {
//            val recyclerViewModel = list[position]
//            message.text = recyclerViewModel.textData
//            message.text = recyclerViewModel.loc
//        }
//    }
//
//    private inner class View2ViewHolder(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var message: TextView = itemView.findViewById(R.id.tvWhatsapp)
//        fun bind(position: Int) {
//            val recyclerViewModel = list[position]
//            message.text = recyclerViewModel.textData
//            message.text = recyclerViewModel.loc
//        }
//    }
//
//    private inner class View3ViewHolder(itemView: View) :
//        RecyclerView.ViewHolder(itemView) {
//        var message: TextView = itemView.findViewById(R.id.Security)
//        var imgarrow: ImageView = itemView.findViewById(R.id.imgarrow)
//        fun bind(position: Int) {
//            val recyclerViewModel = list[position]
//            message.text = recyclerViewModel.textData
//            message.text = recyclerViewModel.loc
//        }
//    }
//
////    private inner class ProjectKeyPillarsViewHolder(private val binding: KeyPillarsLayoutBinding):RecyclerView.ViewHolder(binding.root){
////        fun bind(position: Int){
////            val itemList = arrayListOf<String>("1","2","3","4","5")
////            keyPillarAdapter = KeyPillarAdapter(context,itemList)
////            binding.rvKeyPillars.adapter = keyPillarAdapter
////        }
////    }
//    private inner class View4ViewHolder(private val  binding: FragmentSettingsBinding):RecyclerView.ViewHolder(binding.root)
//       fun bind(position: Int){
////           val recyclerViewModel = list[position]
////           message.text = recyclerViewModel.textData
////           message.text = recyclerViewModel.desc
//           recyclerview1.layoutManager= LinearLayoutManager(context)
//           recyclerview1.layoutManager = LinearLayoutManager(context)
//           adapter = SettingsAdapter(context,initData())
//           recyclerview1.adapter = adapter
//
//
//       }
//
//    private fun initData(): ArrayList<SettingsData> {
//        val newsList: ArrayList<SettingsData> = ArrayList<SettingsData>()
//        newsList.add(SettingsData("Location", " Control location access here", ))
//        newsList.add(SettingsData("Read SMS", "Control location access here", ))
//        newsList.add(SettingsData("Send Push Notifications", "Control location access here", ))
//        newsList.add(SettingsData("Assistance Access", "Control location access here", ))
//
//        return newsList
//
//
//    }
////        RecyclerView.ViewHolder(itemView) {
////        var message: TextView = itemView.findViewById(R.id.tvHeading)
////        var desc :TextView= itemView.findViewById(R.id.desc)
////        fun bind(position: Int) {
////
////            val recyclerViewModel = list[position]
////            message.text = recyclerViewModel.textData
////            message.text = recyclerViewModel.desc
////            recyclerview1.layoutManager= LinearLayoutManager(context)
////            recyclerview1.layoutManager = LinearLayoutManager(context)
////
////             adapter = SettingsAdapter(context,initData())
////           recyclerview1.adapter = adapter
////
////
////        }
//
////        private fun initData(): ArrayList<SettingsData> {
////            val newsList: ArrayList<SettingsData> = ArrayList<SettingsData>()
////            newsList.add(SettingsData("Location", " Control location access here", ))
////            newsList.add(SettingsData("Read SMS", "Control location access here", ))
////            newsList.add(SettingsData("Send Push Notifications", "Control location access here", ))
////            newsList.add(SettingsData("Assistance Access", "Control location access here", ))
////
////            return newsList
////        }
////    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == VIEW_SECURITY_ONE) {
//            return View1ViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.security_view_1, parent, false)
//            )
//        } else if (viewType == VIEW_SECURITY_TWO) {
//            return View2ViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.security_view_2, parent, false)
//            )
//        } else if (viewType == VIEW_SECURITY_THREE) {
//            return View3ViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.security_view_3, parent, false)
//            )
//        }
//        else if (viewType== VIEW_SECURITY_FOUR) {
//            return View4ViewHolder(LayoutInflater.from(context).inflate(R.layout.setting_item,parent,false))
//
//        }
//
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (list[position].viewType === VIEW_SECURITY_ONE) {
//            (holder as View1ViewHolder).bind(position)
//        } else if (list[position].viewType === VIEW_SECURITY_TWO) {
//            (holder as View2ViewHolder).bind(position)
//        } else if (list[position].viewType == VIEW_SECURITY_THREE) {
//            (holder as View3ViewHolder).bind(position)
//        }
//        else if ((list[position].viewType== VIEW_SECURITY_FOUR){
//                (holder as View4ViewHolder).bind(position)
//
//            })
////        else if (list[position].viewType == VIEW_SECURITY_FOUR){
////            (holder as View4ViewHolder).bind(position)
////        }
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return list[position].viewType
//    }
//
//
//}