package thang.com.wref.AI

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions
import java.nio.MappedByteBuffer
import kotlin.math.*
import kotlin.random.Random

class AIServices(
        private val app: Context
) {

    private lateinit var inputImage: TensorImage;
    private lateinit var imageProcessor: ImageProcessingOptions;
    private val MODEL_NAME: String = "lite-model_disease-classification.tflite";

    private val fakeData: ArrayList<HashMap<String, String>> = ArrayList();

    companion object {
        val TAG: String = AIServices::class.java.simpleName;
    }

    init {
        fakeData.sortedByDescending {
            return@sortedByDescending true;
        }
    }

    private fun generateFakePrediction(plantName: String, diseaseName: String, score: Int?): HashMap<String, String> {
        val data = HashMap<String, String>();
        data["plantName"] = plantName;
        data["diseaseName"] = diseaseName;
        data["score"] = "${(Random.nextFloat() * 100).toString()} %";

        return data;
    }

    private fun loadImage(bitmap: Bitmap) {
        Log.d(TAG, "Load bitmap image...");

        inputImage = TensorImage.fromBitmap(bitmap);

        val width: Int = inputImage.width;
        val height: Int = inputImage.height;
        val croppedSize: Int = min(width, height);

        imageProcessor = ImageProcessingOptions
                .builder()
                .setRoi(Rect(
                        /*left=*/ (width - croppedSize) / 2,
                        /*top=*/ (height - croppedSize) / 2,
                        /*right=*/ (width + croppedSize) / 2,
                        /*bottom=*/ (height + croppedSize) / 2
                ))
                .build();
    }

    private fun loadModelFile(): MappedByteBuffer {
        return FileUtil.loadMappedFile(app, MODEL_NAME);
    }

    private fun classify(): List<Classifications> {
        Log.d(TAG, "Classifying ...");

        val options: ImageClassifierOptions = ImageClassifierOptions
                .builder()
                .setMaxResults(5)
                .build();

        val imageClassifier: ImageClassifier = ImageClassifier
                .createFromBufferAndOptions(
                        loadModelFile(),
                        options
                );

        return imageClassifier.classify(inputImage, imageProcessor);
    }

    public fun predict(bitmap: Bitmap): ArrayList<HashMap<String, String>> {
        Log.d(TAG, "Begin predict...");

        loadImage(bitmap);
        val categories: List<Category> = classify()[0].categories;

        Log.d(TAG, "Predict done!");

        val result: ArrayList<HashMap<String, String>> = ArrayList();
        for (category: Category in categories) {
            val diseaseInfo = category.label.split(" ", ignoreCase = true, limit = 2);

            val prediction: HashMap<String, String> = HashMap();
            prediction["plantName"] = diseaseInfo[0];
            prediction["diseaseName"] = diseaseInfo[1];
            prediction["percent"] = (category.score * 100).toString();

            result.add(prediction);
        }

        return result;
    }
}