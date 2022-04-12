package com.emproto.hoabl.feature.home.promisesUi.adapter//package com.example.hoabelui.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.PromisesViewBinding
import com.emproto.hoabl.feature.home.promisesUi.data.DataModel


class HoabelPromiseAdapter(var context: Context) : RecyclerView.Adapter<HoabelPromiseAdapter.HoabelPromiseViewHolder>() {
    private lateinit var onItemClickListener : View.OnClickListener
    var dataList = emptyList<DataModel>()
    internal fun setDataList(dataList: List<DataModel>) {
        this.dataList = dataList
    }

    inner class HoabelPromiseViewHolder(var binding: PromisesViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoabelPromiseViewHolder {
        val view = PromisesViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HoabelPromiseViewHolder(view)
    }
    override fun onBindViewHolder(holder: HoabelPromiseViewHolder, position: Int) {
        holder.binding.title.setOnClickListener(onItemClickListener)
        holder.binding.fullviewHoabel.setOnClickListener(onItemClickListener)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var arrowImage: ImageView
        var image: ImageView
        var title: TextView
        var desc: TextView



        init {
            arrowImage= itemView.findViewById(R.id.arrowImage)
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            desc = itemView.findViewById(R.id.desc)
        }

    }

    fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var data = dataList[position]
        holder.title.text = data.title
        holder.desc.text = data.desc
        holder.image.setImageResource(data.image)
        holder.arrowImage.setImageResource(data.arrowImage)




    }
    override fun getItemCount() = dataList.size

    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }

}
