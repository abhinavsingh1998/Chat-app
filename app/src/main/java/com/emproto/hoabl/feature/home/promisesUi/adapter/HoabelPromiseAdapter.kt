package com.emproto.hoabl.feature.home.promisesUi.adapter//package com.example.hoabelui.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.PromisesViewBinding
import com.emproto.hoabl.feature.home.promisesUi.data.DataModel


class HoabelPromiseAdapter(
    var context: Context,
    val dataList: List<DataModel>,
    val itemInterface: PromisedItemInterface
) :
    RecyclerView.Adapter<HoabelPromiseAdapter.HoabelPromiseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoabelPromiseViewHolder {
        val view = PromisesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoabelPromiseViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoabelPromiseViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.title.text = data.title
        holder.binding.desc.text = data.desc
        holder.binding.image.setImageResource(data.image)
        holder.binding.arrowImage.setImageResource(data.arrowImage)
        holder.binding.itemCard.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var arrowImage: ImageView
        var image: ImageView
        var title: TextView
        var desc: TextView
        var itemCard: CardView


        init {
            arrowImage = itemView.findViewById(R.id.arrowImage)
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            desc = itemView.findViewById(R.id.desc)
            itemCard = itemView.findViewById(R.id.item_card)
        }

    }


    override fun getItemCount() = dataList.size

    inner class HoabelPromiseViewHolder(var binding: PromisesViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PromisedItemInterface {
        fun onClickItem(position: Int)
    }

}
