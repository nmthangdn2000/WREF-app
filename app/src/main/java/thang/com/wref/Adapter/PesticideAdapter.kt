package thang.com.wref.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import thang.com.wref.databinding.PesticideItemBinding

class PesticideAdapter(
        private val pesticidesList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<PesticideAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: PesticideItemBinding
    ) : RecyclerView.ViewHolder(binding.root);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PesticideItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false);

        return ViewHolder(binding);
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = pesticidesList[position];

        viewHolder.binding.txtPesticideName.text = item["name"];
        viewHolder.binding.txtPesticideQuality.text = item["quality"];
        viewHolder.binding.txtPesticidePrice.text = item["price"];
        viewHolder.binding.txtPesticideWeight.text = item["weight"];
    }

    override fun getItemCount(): Int {
        return pesticidesList.size;
    }
}