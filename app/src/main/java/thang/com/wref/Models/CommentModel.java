package thang.com.wref.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class CommentModel {
    @SerializedName("_id")
    private String id;
    @SerializedName("idUser")
    private UsersModel idUser;
    @SerializedName("idPosts")
    private String idPosts;
    @SerializedName("content")
    private String content;
    @SerializedName("media")
    private String media;
    @SerializedName("Like")
    private UserLikeModel[] Like;
    @SerializedName("create_at")
    private long create_at;
    @SerializedName("update_at")
    private long update_at;

    public CommentModel(String id, UsersModel idUser, String idPosts, String content, String media, UserLikeModel[] like, long create_at, long update_at) {
        this.id = id;
        this.idUser = idUser;
        this.idPosts = idPosts;
        this.content = content;
        this.media = media;
        Like = like;
        this.create_at = create_at;
        this.update_at = update_at;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "id='" + id + '\'' +
                ", idUser=" + idUser +
                ", idPosts=" + idPosts +
                ", content='" + content + '\'' +
                ", media='" + media + '\'' +
                ", Like=" + Arrays.toString(Like) +
                ", create_at=" + create_at +
                ", update_at=" + update_at +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UsersModel getIdUser() {
        return idUser;
    }

    public void setIdUser(UsersModel idUser) {
        this.idUser = idUser;
    }

    public String getIdPosts() {
        return idPosts;
    }

    public void setIdPosts(String idPosts) {
        this.idPosts = idPosts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public UserLikeModel[] getLike() {
        return Like;
    }

    public void setLike(UserLikeModel[] like) {
        Like = like;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(long update_at) {
        this.update_at = update_at;
    }
}
