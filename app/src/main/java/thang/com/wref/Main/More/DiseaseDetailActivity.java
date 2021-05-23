package thang.com.wref.Main.More;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.Buffer;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import thang.com.wref.Adapter.HarvesthelperAdapter;
import thang.com.wref.Models.CropsModel;
import thang.com.wref.Models.DiseaseDetailModel;
import thang.com.wref.R;
import thang.com.wref.ml.Model;
import thang.com.wref.ml.Model1;

public class DiseaseDetailActivity extends AppCompatActivity {
    private static final String TAG = "DiseaseDetailActivity";
    private String pathImg = "";
    private ImageView imgPlant;
    private Bitmap bitmapImg;
    private String stringLable[];
    private RelativeLayout rltArrowBack;
    private RecyclerView rcvDiseaseDetail;
    private TextView txtPlantName, txtDiseaseName;
    private HashMap<Integer, Integer> mHashMap;
    private List<DiseaseDetailModel> diseaseDetail;
    private ArrayList<CropsModel> cropsModels;
    private HarvesthelperAdapter harvesthelperAdapter;

    private int INPUT_SIZE = 256;
    private int PIXEL_SIZE  = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail2);

        mappingView();
        convertData();
        readFileJson();
        readFileTxt();
        getImage();
        getDiseaseDetail();
    }

    private void convertData(){
        mHashMap = new HashMap<>();

        mHashMap.put(0, 0);
        mHashMap.put(2, 1);
        mHashMap.put(3, 2);
        mHashMap.put(5, 3);
        mHashMap.put(6, 4);
        mHashMap.put(7, 5);
        mHashMap.put(8, 6);
        mHashMap.put(9, 7);
        //
        mHashMap.put(1, 8);
        mHashMap.put(4, 8);
        mHashMap.put(10, 8);
    }

    private  String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private void readFileJson(){
        String jsonFileString = getJsonFromAssets(getApplicationContext(), "corn.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<DiseaseDetailModel>>() { }.getType();
        diseaseDetail = gson.fromJson(jsonFileString, listUserType);
    }

    private void readFileTxt(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("lable.txt"), "UTF-8"));

            stringLable = new String[11];
            String mLine;
            int i = 0;
            while ((mLine = reader.readLine()) != null) {
                stringLable[i] = mLine;
                i++;
            }
        } catch (IOException e) {
            Log.d(TAG, "error: "+e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "error: "+e.getMessage());
                }
            }
        }
    }

    private void getImage() {
        pathImg = getIntent().getStringExtra("pathImage");

        bitmapImg = BitmapFactory.decodeFile(pathImg);
        Glide.with(this).load(bitmapImg).into(imgPlant);
    }

    private void mappingView(){
        imgPlant = (ImageView) findViewById(R.id.imgPlant);
        rltArrowBack = (RelativeLayout) findViewById(R.id.rltArrowBack);
        txtPlantName = (TextView) findViewById(R.id.txtPlantName);
        txtDiseaseName = (TextView) findViewById(R.id.txtDiseaseName);
        rcvDiseaseDetail = (RecyclerView) findViewById(R.id.rcvDiseaseDetail);

        rltArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvDiseaseDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        rcvDiseaseDetail.setLayoutManager(linearLayoutManager);
    }

    private void getDiseaseDetail(){
        try {
            Bitmap bitmap = scaleImage(bitmapImg);

            Model1 model = Model1.newInstance(this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);

//            TensorImage byteBuffer = TensorImage.fromBitmap(bitmap);
//            TensorImage tensorImage = TensorImage.createFrom(byteBuffer, DataType.FLOAT32);
            ByteBuffer byteBuffer = convertBitmapToByteBuffer(bitmap);
//            Log.d(TAG, "getDiseaseDetail: " + tensorImage.getWidth()+ " " + tensorImage.getWidth() + " " +tensorImage.getDataType());
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model1.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            for (int i = 0; i < outputFeature0.getFloatArray().length; i++) {
                Log.d(TAG, "getDiseaseDetail: "+ outputFeature0.getFloatArray()[i]);
            }
            int max = getMax(outputFeature0.getFloatArray());
            txtDiseaseName.setText(stringLable[max].split("_")[1]);
            txtPlantName.setText(stringLable[max].split("_")[0]);
            // Releases model resources if no longer used.
            model.close();
            Log.d(TAG, max + " getDiseaseDetail: " + mHashMap.get(max));
            cropsModels = new ArrayList<>();
            for (int i = 0; i < diseaseDetail.size(); i++) {
                if (diseaseDetail.get(i).getId() == mHashMap.get(max)){
                    cropsModels.add(new CropsModel("Triệu chứng", diseaseDetail.get(i).getCause()));
                    cropsModels.add(new CropsModel("Nguyên nhân", diseaseDetail.get(i).getSymptom()));
                    cropsModels.add(new CropsModel("Giải pháp đề xuất", diseaseDetail.get(i).getPrevention()));
                    break;
                }
            }

        } catch (IOException e) {
            // TODO Handle the exception
            Log.d(TAG, "getDiseaseDetail: " + e.getMessage());
        }
        HarvesthelperAdapter harvesthelperAdapter = new HarvesthelperAdapter(cropsModels, this, 1);
        rcvDiseaseDetail.setAdapter(harvesthelperAdapter);
    }

    private int getMax(float arrFloat[]) {
        int ind = 0;
        float min = 0.0f;
        for (int i = 0 ; i < 11; i ++) {
            float number = arrFloat[i];
//            if(i == 0 && arrFloat[i] == 1.0f) {
//                number = 0.1f;
//            }
            if(number > 0 && number < 0.1f) {
                number = number * 1000000 * 1000000;
            }
            if(number > min){
                ind = i;
                min = arrFloat[i];
            }
        }
        return ind;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.rewind();

        int[] pixels = new int[INPUT_SIZE * INPUT_SIZE];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        float mean = 0.0f;
        float std = 1.0f;
        for (int i = 0; i < INPUT_SIZE; ++i) {
            for (int j = 0; j < INPUT_SIZE; ++j) {
                int pixelValue = pixels[i * INPUT_SIZE + j];
                byteBuffer.putFloat((((pixelValue >> 16) & 0xFF) - mean) / std);
                byteBuffer.putFloat((((pixelValue >> 8) & 0xFF) - mean) / std);
                byteBuffer.putFloat(((pixelValue & 0xFF) - mean) / std);
            }
        }
        return byteBuffer;
    }

    private Bitmap scaleImage(Bitmap bitmap) {
        int orignalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth =  ((float) INPUT_SIZE) / orignalWidth;
        float scaleHeight =  ((float) INPUT_SIZE) / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true);
    }
}