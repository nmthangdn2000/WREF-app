package thang.com.wref.Main;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import thang.com.wref.AI.AIServices;
import thang.com.wref.Main.Prediction.DiseaseDetail;
import thang.com.wref.camera.CameraUtilities;
import thang.com.wref.Login.LoginActivity;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.R;

import static android.app.Activity.RESULT_OK;

public class CameraPredictFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = "CameraPredictFragment";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int CAMERA_PERM_CODE = 101;

    private View view;
    private List<Uri> mSelected;
    private LinearLayout layout_chuan_doan_benh;
    private RelativeLayout layout_choose_img_plant;
    private ImageView img_choose_plant;
    private RelativeLayout rltChooseImage, rltCamera, rltArrowBack;
    private LinearLayout lnlContainer;
    private SharedPreferencesManagement sharedPreferencesManagement;
    private String realPathfile="";
    private CameraUtilities camUtils;
    private AIServices AI;
    public CameraPredictFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // lấy ảnh khi ứng hoạt động trở lại
        if(!realPathfile.equals("")){
            if(layout_chuan_doan_benh.getVisibility() != View.VISIBLE){
                layout_chuan_doan_benh.setVisibility(View.VISIBLE);
                layout_choose_img_plant.setVisibility(View.INVISIBLE);
            }
            Glide.with(getContext()).load(realPathfile).into(img_choose_plant);
            rltArrowBack.setVisibility(View.VISIBLE);
        } else if(camUtils.getPhotoUri() != null){

            Bitmap bitmapImg = showImg(camUtils.getPhotoUri());
            ArrayList<HashMap<String, String>> predictionsList = AI.predict(bitmapImg);
            addDataPlant(predictionsList);

            rltArrowBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getContext().getResources().getColor(R.color.colorStatusBar));
        }

        sharedPreferencesManagement = new SharedPreferencesManagement(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera_predict, container, false);

        mappingView();
        this.camUtils = new CameraUtilities(this.getActivity());
        this.AI = new AIServices(this.getActivity());

        if (savedInstanceState != null) {
            String imageUriString = savedInstanceState.getString("imgUri");
            camUtils.setPhotoUri(Uri.parse(imageUriString));
        }

        return view;
    }

    private void mappingView() {
        rltChooseImage = (RelativeLayout) view.findViewById(R.id.rltChooseImage);
        layout_chuan_doan_benh = (LinearLayout) view.findViewById(R.id.layout_chuan_doan_benh);
        layout_choose_img_plant = (RelativeLayout) view.findViewById(R.id.layout_choose_img_plant);
        img_choose_plant = (ImageView) view.findViewById(R.id.img_choose_plant);
        rltCamera = (RelativeLayout) view.findViewById(R.id.rltCamera);
        rltArrowBack = (RelativeLayout) view.findViewById(R.id.rltArrowBack);
        lnlContainer = (LinearLayout) view.findViewById(R.id.lnlContainer);

        rltChooseImage.setOnClickListener(this);
        rltCamera.setOnClickListener(this);
        rltArrowBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rltChooseImage:
                getImage();
                break;
            case R.id.rltCamera:
                camUtils.dispatchTakePictureIntent();
                break;
            case R.id.rltArrowBack:
                clickArrowBack();
            default:
                break;
        }
    }

    private void clickArrowBack() {
        realPathfile = "";
        camUtils.setPhotoUri(null);
        if(layout_chuan_doan_benh.getVisibility() == View.VISIBLE){
            layout_chuan_doan_benh.setVisibility(View.INVISIBLE);
            layout_choose_img_plant.setVisibility(View.VISIBLE);
            rltArrowBack.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: "+realPathfile);
        outState.putString("imgFromCamera", realPathfile);

        if (camUtils.getPhotoUri() != null)
            outState.putString("imgUri", camUtils.getPhotoUri().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "" + requestCode);
        if (resultCode == RESULT_OK && data != null){
            if(requestCode == IMAGE_PICK_CODE){
                mSelected = new ArrayList<>();
                mSelected = Matisse.obtainResult(data);
                Bitmap bitmapImg = showImg(mSelected.get(0));
                ArrayList<HashMap<String, String>> predictionsList = AI.predict(bitmapImg);
                addDataPlant(predictionsList);
            }
        }
    }

    private Bitmap showImg(Uri uriImg) {
        Log.d(TAG, "Show img: " + uriImg.getPath());

        if(layout_chuan_doan_benh.getVisibility() != View.VISIBLE){
            layout_chuan_doan_benh.setVisibility(View.VISIBLE);
            layout_choose_img_plant.setVisibility(View.INVISIBLE);
        }

        rltArrowBack.setVisibility(View.VISIBLE);

        camUtils.setPhotoUri(uriImg);
        Bitmap bitmapImg = camUtils.loadBitmap();

        img_choose_plant.setImageBitmap(bitmapImg);

        return bitmapImg;
    }
    private void addDataPlant(ArrayList<HashMap<String, String>> predictionsList){
        // Remove all old views
        lnlContainer.removeAllViews();

        for (HashMap<String, String> prediction : predictionsList) {
            lnlContainer.addView(generatePredictionLayout(
                    prediction.get("diseaseName"),
                    prediction.get("score")
            ));

            lnlContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent diseaseDetailIntent = new Intent(getContext(), DiseaseDetail.class);
                    startActivity(diseaseDetailIntent);
                }
            });
        }
    }

    private CardView generatePredictionLayout(String diseaseName, String score) {

        // prediction layout
        CardView cvContainer = new CardView(this.getContext());

        CardView.LayoutParams cvContainerLayout = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cvContainerLayout.setMargins(24, 0, 24, 16);

        cvContainer.setLayoutParams(cvContainerLayout);
        cvContainer.setCardElevation(0);
        cvContainer.setRadius(8);
        cvContainer.setCardBackgroundColor(ResourcesCompat.getColor(
                getResources(),
                R.color.cs_light_danger,
                null
        ));

        cvContainer.setPadding(24, 16, 24, 16);

        LinearLayout lnlPredictionContainer = new LinearLayout(this.getContext());
        lnlPredictionContainer.setOrientation(LinearLayout.HORIZONTAL);

        lnlPredictionContainer.setLayoutParams(cvContainerLayout);

        // text layout
        LinearLayout lnlText = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams lnlTextLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );

        lnlText.setLayoutParams(lnlTextLayout);
        lnlText.setOrientation(LinearLayout.VERTICAL);
        lnlText.setPadding(16, 8, 16,8);

        TextView tvDiseaseName = new TextView(this.getContext());
        TextView tvScore = new TextView(this.getContext());
        ImageView ivStatus = new ImageView(this.getContext());

        Typeface typeface = ResourcesCompat.getFont(this.getContext(), R.font.lato);
        tvDiseaseName.setTypeface(typeface, Typeface.BOLD);
        tvDiseaseName.setTextColor(
                ResourcesCompat.getColor(getResources(),
                        R.color.cs_danger,
                        null
                ));
        tvScore.setTypeface(typeface);

        tvDiseaseName.setTextSize(24);
        tvScore.setTextSize(16);

        ivStatus.setImageResource(R.drawable.ic_baseline_close);
        ivStatus.setMinimumWidth(24);
        ivStatus.setMinimumHeight(24);
        ivStatus.setPadding(8, 8, 8,8);

        lnlText.addView(tvDiseaseName);
        lnlText.addView(tvScore);

        tvDiseaseName.setText(diseaseName);
        tvScore.setText(score);

        lnlPredictionContainer.addView(lnlText);
        lnlPredictionContainer.addView(ivStatus);

        cvContainer.addView(lnlPredictionContainer);

        return cvContainer;
    }

    private void getImage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            }else{
                pickImgFromGalley();
            }
        }else{
            pickImgFromGalley();
        }
    }
    private void pickImgFromGalley() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .imageEngine(new GlideEngine())
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showPreview(false) // Default is `true`
                .forResult(IMAGE_PICK_CODE);
    }

    private void logOut(){
        sharedPreferencesManagement.clearData();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}