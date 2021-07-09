package thang.com.wref.fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Adapter.CommentAdapter;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Models.CommentModel;
import thang.com.wref.Models.ErrModel;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.NewsRetrofit;

import static android.app.Activity.RESULT_OK;
import static thang.com.wref.util.SocketIO.socket;

public class CommentFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "CommentFragment";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private View view;
    private ImageView btnSendComment, btnChooseImgUpload;
    private RoundedImageView imdUpload;
    private CircleImageView btnCloseImg;
    private EditText edtComment;
    private NetworkUtil networkUtil;
    private RelativeLayout rltOneImg;
    private Retrofit retrofit;
    private NewsRetrofit newsRetrofit;
    private ArrayList<CommentModel> commentModels;
    private SharedPreferencesManagement sharedPreferencesManagement;
    private String idPost="";
    private CommentAdapter commentAdapter;
    private RecyclerView rcvComment;
    private List<Uri> mSelected;
    private String realPathfile="";
    private MultipartBody.Part part;
    public CommentFragment(String idPost) {
        // Required empty public constructor
        this.idPost = idPost;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        sharedPreferencesManagement = new SharedPreferencesManagement(getContext());
        commentModels = new ArrayList<>();
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        mapingView();
        getData();
        return view;
    }
    private void mapingView(){
        btnSendComment = (ImageView) view.findViewById(R.id.btnSendComment);
        btnChooseImgUpload = (ImageView) view.findViewById(R.id.btnChooseImgUpload);
        rcvComment = (RecyclerView) view.findViewById(R.id.rcvComment);
        imdUpload = (RoundedImageView) view.findViewById(R.id.imdUpload);
        btnCloseImg = (CircleImageView) view.findViewById(R.id.btnCloseImg);
        edtComment = (EditText) view.findViewById(R.id.edtComment);
        rltOneImg = (RelativeLayout) view.findViewById(R.id.rltOneImg);
        rcvComment.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvComment.setLayoutManager(linearLayoutManager);

        btnSendComment.setOnClickListener(this);
        btnChooseImgUpload.setOnClickListener(this);
        btnCloseImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendComment:
                sendData();
                break;
            case R.id.btnChooseImgUpload:
                getImage();
                break;
            case R.id.btnCloseImg:
                closeImage();
                break;
            default:
                break;
        }
    }
    private void closeImage(){
        imdUpload.setImageDrawable(null);
        rltOneImg.setVisibility(View.GONE);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            if(requestCode == IMAGE_PICK_CODE){
                mSelected = Matisse.obtainResult(data);
                realPathfile = getPathFromURI(getContext(), mSelected.get(0));
                Glide.with(getContext()).load(mSelected.get(0)).centerCrop().fitCenter().into(imdUpload);
                rltOneImg.setVisibility(View.VISIBLE);
            }
        }
    }
    private void sendData(){
        if(!realPathfile.isEmpty()){
            File file = new File(realPathfile);
            String file_path = file.getAbsolutePath();
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            part = MultipartBody.Part.createFormData("image",file_path, requestBody);
        }
        RequestBody document = RequestBody.create(edtComment.getText().toString(), MultipartBody.FORM);
        newsRetrofit = retrofit.create(NewsRetrofit.class);
        Call<ErrModel> errModelCall =  newsRetrofit.postComments(sharedPreferencesManagement.getTOKEN(), idPost, document, part);
        errModelCall.enqueue(new Callback<ErrModel>() {
            @Override
            public void onResponse(Call<ErrModel> call, Response<ErrModel> response) {
                ErrModel errors = response.body();
                if(!errors.isSuccess()){
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
                else{
                    getData();
                    closeImage();
                    edtComment.setText("");
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<ErrModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                call.cancel();
            }
        });
    }
    private void getData(){
        commentModels.clear();
        newsRetrofit = retrofit.create(NewsRetrofit.class);
        Call<List<CommentModel>> listCall = newsRetrofit.getComments(sharedPreferencesManagement.getTOKEN(), idPost);
        listCall.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(), "lỗi mạng", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<CommentModel> cmtModels = response.body();
                    Log.d(TAG, "onResponse: " +response.body());
                    for (CommentModel cmt : cmtModels){
                        commentModels.add(cmt);
                    }
                    commentAdapter.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                call.cancel();
            }
        });
        commentAdapter = new CommentAdapter(commentModels, getContext());
        rcvComment.setAdapter(commentAdapter);

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