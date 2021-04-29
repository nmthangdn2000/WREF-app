package thang.com.wref.Main.Prediction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import thang.com.wref.Adapter.PesticideAdapter
import thang.com.wref.databinding.ActivityDiseaseDetailBinding

class DiseaseDetail : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseDetailBinding;
    private lateinit var pesticideAdapter: PesticideAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiseaseDetailBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val data = ArrayList<HashMap<String, String>>();
        val pesticide1 = HashMap<String, String>();
        pesticide1["name"] = "NiTOX";
        pesticide1["quality"] = "Tốt";
        pesticide1["price"] = "150.000 VNĐ";
        pesticide1["weight"] = "100g";

        data.add(pesticide1);

        pesticideAdapter = PesticideAdapter(data);

        val layoutManager = LinearLayoutManager(this);
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL;
        binding.rvPesticidesList.layoutManager = layoutManager;

        binding.rvPesticidesList.adapter = pesticideAdapter;

        binding.txtDiseaseName.text = "Bệnh vàng lá";
        binding.txtPlantName.text = "Dưa leo";
    }
}