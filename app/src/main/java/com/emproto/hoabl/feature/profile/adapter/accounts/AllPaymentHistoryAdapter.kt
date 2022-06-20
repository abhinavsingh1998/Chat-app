package com.emproto.hoabl.feature.profile.adapter.accounts



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsPaymentBinding
import com.emproto.hoabl.feature.profile.AllPaymentHistoryFragment
import com.emproto.networklayer.response.profile.AccountsResponse

class AllPaymentHistoryAdapter(
    private var mContext: Context?,
    private var accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,


) : RecyclerView.Adapter<AllPaymentHistoryAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsPaymentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(accountsPaymentList[position].paidAmount.toString()!=null){
            holder.tvPaidAmount.text = "₹" + accountsPaymentList[position].paidAmount.toString()
        }
        if(accountsPaymentList[position].projectName!=null){
            holder.tvProjectName.text = accountsPaymentList[position].projectName
        }
        if(accountsPaymentList[position].paymentDate.substring(0, 10)!=null) {
            holder.tvPaymentDate.text = accountsPaymentList[position].paymentDate.substring(0, 10)
        }
        if (accountsPaymentList[position].investment.crmInventory.id.toString()!=null) {
            holder.tvLandId.text = "Land id:" + "" + accountsPaymentList[position].investment.crmInventory.id.toString()
        }
    }

    override fun getItemCount(): Int {
        return accountsPaymentList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvProjectName: TextView = itemView.findViewById(R.id.tvProjectName)
        val tvPaidAmount: TextView = itemView.findViewById(R.id.tvPaidAmount)
        val tvPaymentDate: TextView = itemView.findViewById(R.id.tvPaymentDate)
        val tvSeeReceipt: TextView = itemView.findViewById(R.id.tvSeeReceipt)
        val tvLandId: TextView = itemView.findViewById(R.id.tvLandId)


    }
}
