package thang.com.wref.Models;

import com.google.gson.annotations.SerializedName;

public class UserLikeModel {
    @SerializedName("iduserLike")
    private String iduserLike;

    public UserLikeModel(String iduserLike) {
        this.iduserLike = iduserLike;
    }

    @Override
    public String toString() {
        return "UserLikeModel{" +
                "iduserLike=" + iduserLike +
                '}';
    }

    public String getIduserLike() {
        return iduserLike;
    }

    public void setIduserLike(String iduserLike) {
        this.iduserLike = iduserLike;
    }
}
