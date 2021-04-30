package thang.com.wref.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import thang.com.wref.R
import thang.com.wref.databinding.PesticideItemBinding

class PesticideAdapter(
        private val pesticidesList: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<PesticideAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: PesticideItemBinding,
            val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PesticideItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false);

        return ViewHolder(binding, parent);
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = pesticidesList[position];

        val pesticideImgDrawableId = viewHolder.parent.let {
            it.resources.getIdentifier(
                    item["label"],
                    "drawable",
                    it.context.packageName
            );
        }

        viewHolder.apply {
            binding.apply {
                txtPesticideName.text = item["name"];
                txtPesticideQuality.text = item["quality"];
                txtPesticidePrice.text = item["price"];
                txtPesticideWeight.text = item["weight"];
                ivPesticideImg.setImageDrawable(parent.context.getDrawable(pesticideImgDrawableId));
            }
        }
    }

    override fun getItemCount(): Int {
        return pesticidesList.size;
    }
}