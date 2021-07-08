package thang.com.wref.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Adapter.CommentAdapter;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Models.CommentModel;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.NewsRetrofit;

import static thang.com.wref.util.SocketIO.socket;

public class CommentFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "CommentFragment";
    private View view;
    private ImageView btnSendComment;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private NewsRetrofit newsRetrofit;
    private ArrayList<CommentModel> commentModels;
    private SharedPreferencesManagement sharedPreferencesManagement;
    private String idPost="";
    private CommentAdapter commentAdapter;
    private RecyclerView rcvComment;
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
        rcvComment = (RecyclerView) view.findViewById(R.id.rcvComment);
        rcvComment.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvComment.setLayoutManager(linearLayoutManager);

        btnSendComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendComment:

                break;
            default:
                break;
        }
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
}