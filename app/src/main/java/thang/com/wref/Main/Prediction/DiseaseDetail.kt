package thang.com.wref.Main.Prediction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.Klaxon
import thang.com.wref.Adapter.PesticideAdapter
import thang.com.wref.databinding.ActivityDiseaseDetailBinding
import java.lang.Math

class DiseaseDetail : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseDetailBinding;
    private lateinit var pesticideAdapter: PesticideAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiseaseDetailBinding.inflate(layoutInflater);
        setContentView(binding.root);

        // Pesticides Recycle View
        val pesticidesList = getPesticidesData();
        pesticideAdapter = PesticideAdapter(pesticidesList);

        val layoutManager = LinearLayoutManager(this);
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL;
        binding.rvPesticidesList.layoutManager = layoutManager;

        binding.rvPesticidesList.adapter = pesticideAdapter;

        // Header
        intent.extras?.apply {
            binding.txtDiseaseName.text = getString("diseaseName");
            binding.txtPlantName.text = getString("plantName");

            getDiseaseData(getString("plantName")!!)
        }
    }

    private fun getPesticidesData() : ArrayList<HashMap<String, String>> {
        class PesticideJson(
                val name: String,
                val quality: String,
                val weight: String,
                val price: String,
                val label: String
        )

        val data = Klaxon()
                .parseArray<PesticideJson>(assets.open("pesticides.json"));

        val result = ArrayList<HashMap<String, String>>();

        data?.filter {
            Math.random() > 0.5;
        }?.forEach {
            val pesticide = HashMap<String, String>();

            pesticide["name"] = it.name;
            pesticide["quality"] = it.quality;
            pesticide["price"] = it.price;
            pesticide["weight"] = it.weight;
            pesticide["label"] = it.label;

            result.add(pesticide);
        }

        return result;
    }

    private fun getDiseaseData(plantName: String) {
        class DiseaseJson(
                val diseaseName: String,
                val cause: String,
                val symptom: String,
                val prevention: String,
                val cycle: String
        );

        val data = Klaxon()
                .parseArray<DiseaseJson>(assets.open("corn.json"));

        data?.let {
            val disease: DiseaseJson = it.random();

            binding.txtSymptom.text = disease.symptom;
            binding.txtPrevention.text = disease.prevention;
            binding.txtCause.text = disease.cause;
        }
    }
}