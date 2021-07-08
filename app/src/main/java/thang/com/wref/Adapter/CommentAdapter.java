package thang.com.wref.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.wref.Models.CommentModel;
import thang.com.wref.R;

import static thang.com.wref.util.Constants.BASE_URL;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHodler>{
    private ArrayList<CommentModel> commentModels;
    private Context context;

    public CommentAdapter(ArrayList<CommentModel> commentModels, Context context) {
        this.commentModels = commentModels;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CommentAdapter.ViewHodler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_comment, parent,false);
        return new CommentAdapter.ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdapter.ViewHodler holder, int position) {
        Glide.with(context).load(BASE_URL+"uploads/"+commentModels.get(position).getIdUser().getAvata()).into(holder.imgUser);
        holder.txtUserName.setText(commentModels.get(position).getIdUser().getUsername());
        holder.txtComment.setText(commentModels.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        private CircleImageView imgUser;
        private TextView txtUserName, txtComment;
        public ViewHodler(@NonNull @NotNull View itemView) {
            super(itemView);
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
        }
    }
}
