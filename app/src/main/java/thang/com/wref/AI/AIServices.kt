package thang.com.wref.AI

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions
import java.nio.MappedByteBuffer
import kotlin.math.*

class AIServices(
        private val app: Context
) {

    private lateinit var inputImage: TensorImage;
    private lateinit var imageProcessor: ImageProcessor;
    private val MODEL_NAME: String = "plant_disease_model.tflite";

    companion object {
        val TAG: String = AIServices::class.java.simpleName;
    }

    private fun loadImage(bitmap: Bitmap) {
        Log.d(TAG, "Load bitmap image...");

        inputImage = TensorImage.fromBitmap(bitmap);

        val width: Int = inputImage.width;
        val height: Int = inputImage.height;
        val croppedSize: Int = min(width, height);

        imageProcessor = ImageProcessor.Builder()
                .add(ResizeWithCropOrPadOp(croppedSize, croppedSize))
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(127.5f, 127.5f))
                .build();

        inputImage = imageProcessor.process(inputImage);
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

        return imageClassifier.classify(inputImage);
    }

    public fun predict(bitmap: Bitmap): ArrayList<HashMap<String, String>> {
        Log.d(TAG, "Begin predict...");

        loadImage(bitmap);
        val categories: List<Category> = classify()[0].categories;

        Log.d(TAG, "Predict done!");

        val result: ArrayList<HashMap<String, String>> = ArrayList();
        for (category: Category in categories) {
            val prediction: HashMap<String, String> = HashMap();
            prediction["diseaseName"] = category.label;
            prediction["score"] = category.score.toString();

            result.add(prediction);
        }

        return result;
    }
}