package com.emproto.hoabl.feature.profile.adapter.accounts

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemAccountsKycDocUploadBinding
import com.emproto.networklayer.response.profile.AccountsResponse
import com.emproto.networklayer.response.profile.KycUpload

class AccountKycUploadAdapter(
    private var mContext: Context?,
    private var newList: ArrayList<KycUpload>,
    private var mListener: OnKycItemUploadClickListener,
    private var viewListener: OnKycItemClickListener

) : RecyclerView.Adapter<AccountKycUploadAdapter.ViewHolder>() {

    lateinit var binding: ItemAccountsKycDocUploadBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemAccountsKycDocUploadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding.root)
    }

    interface OnKycItemUploadClickListener {
        fun onUploadClick(
            newList: ArrayList<KycUpload>,
            view: View,
            documentType: Int

        )
    }

    interface OnKycItemClickListener {
        fun onAccountsKycItemClick(
            accountsDocumentList: ArrayList<KycUpload>,
            view: View,
            position: Int,
            name: String,
            path: String?
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDocName.text = newList[position].documentName

        if (newList[position].status == "UPLOAD") {
            holder.tvUploadDoc.text = "Upload"
            holder.tvUploadDoc.isEnabled = true
        } else if (newList[position].status == "View") {
            holder.tvUploadDoc.text = "View"
            holder.tvUploadDoc.isEnabled = true
        } else if(newList[position].status=="Verification Pending"){
            holder.tvUploadDoc.visibility=View.GONE
            holder.viewUnderLine.visibility=View.GONE
            holder.clStatus.visibility=View.VISIBLE
            holder.textStatus.visibility=View.GONE
            holder.tvPreview.setOnClickListener {
                viewListener.onAccountsKycItemClick(
                    newList,
                    it,
                    position,
                    newList[position].name,
                    newList[position].path
                )
            }
            holder.tvReupload.setOnClickListener {
                if (newList[position].documentName == "Address Proof") {
                    mListener.onUploadClick(newList, it, 200110)
                } else if (newList[position].documentName == "PAN Card") {
                    mListener.onUploadClick(newList, it, 200109)
                }
            }



        }
        holder.tvUploadDoc.setOnClickListener {
            Log.d("RRRR", newList[position].name.toString())
            when {
                newList[position].status == "UPLOAD" -> {
                    if (newList[position].documentName == "Address Proof") {
                        mListener.onUploadClick(newList, it, 200110)
                    } else if (newList[position].documentName == "PAN Card") {
                        mListener.onUploadClick(newList, it, 200109)
                    }
                }
                newList[position].status == "View" -> {

                    viewListener.onAccountsKycItemClick(
                        newList,
                        it,
                        position,
                        newList[position].name,
                        newList[position].path
                    )
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        val tvUploadDoc: TextView = itemView.findViewById(R.id.tvUploadDoc)
        val viewUnderLine: View = itemView.findViewById(R.id.viewUnderLine)
        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        val tvVerificationPending: TextView = itemView.findViewById(R.id.tvVerificationPending)
        val tvPreview: TextView = itemView.findViewById(R.id.tvPreview)
        val viewPreview: View = itemView.findViewById(R.id.previewLine)
        val tvReupload: TextView = itemView.findViewById(R.id.tvReupload)
        val reuploadLine: View = itemView.findViewById(R.id.reuploadLine)
        val clStatus: ConstraintLayout = itemView.findViewById(R.id.clStatus)






    }
}