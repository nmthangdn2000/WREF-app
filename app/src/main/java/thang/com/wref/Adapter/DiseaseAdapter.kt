package thang.com.wref.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import thang.com.wref.R
import thang.com.wref.databinding.DiseaseItemBinding

abstract class DiseaseAdapter(
) : RecyclerView.Adapter<DiseaseAdapter.ViewHolder>() {

    private var diseasesList: ArrayList<HashMap<String, String>> = ArrayList();

    abstract fun handleItemClick (diseaseName: String, plantName: String);

    companion object {
        val TAG: String = DiseaseAdapter::class.java.simpleName;
    }

    class ViewHolder(
            val binding: DiseaseItemBinding,
            hasDisease: Boolean,
            val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            if (hasDisease) {
                binding.cvDiseaseItem
                        .setCardBackgroundColor(parent.resources
                                .getColor(R.color.cs_light_danger));

                binding.txtDiseaseName.setTextColor(parent.resources
                        .getColor(R.color.cs_danger));
            } else {
                binding.cvDiseaseItem
                        .setCardBackgroundColor(parent.resources
                                .getColor(R.color.cs_light_success));

                binding.txtDiseaseName.setTextColor(parent.resources
                        .getColor(R.color.cs_success));
            }
        }
    };

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DiseaseItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false);

        val hasDisease = when(viewType) {
            1 -> true
            0 -> false
            else -> false
        }

        return ViewHolder(binding, hasDisease, parent);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diseaseInfo: HashMap<String, String> = diseasesList[position];

        holder.binding.txtDiseaseName.text = diseaseInfo["diseaseName"];
        holder.binding.txtPlantName.text = diseaseInfo["plantName"];
        holder.binding.txtPercent.text = "${diseaseInfo["percent"]} %";

        Log.d(TAG, "Add item ${diseaseInfo}");

        holder.binding.root.setOnClickListener { view ->
            handleItemClick(diseaseInfo["diseaseName"]!!, diseaseInfo["plantName"]!!);
            Log.d(TAG, "Item ${position} clicked!");
        }
    }

    override fun getItemCount(): Int {
        return diseasesList.size;
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position);

        return when(diseasesList[position]["diseaseName"]?.contains("healthy", ignoreCase = true)) {
            true -> 0
            false -> 1
            else -> 1
        }
    }

    fun addItem(item: HashMap<String, String>) {
        diseasesList.add(item);
        this.notifyItemInserted(diseasesList.size - 1);
    }

    fun replaceList(newList: ArrayList<HashMap<String, String>>) {
        diseasesList = newList;
        this.notifyDataSetChanged();
    }

    fun clear() {
        diseasesList.clear();
        this.notifyDataSetChanged();
    }
}