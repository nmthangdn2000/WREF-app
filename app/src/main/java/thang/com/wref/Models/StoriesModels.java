package thang.com.wref.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class StoriesModels {
    @SerializedName("_id")
    private String id;
//    @SerializedName("iduser")
//    private UsersModel users;
    @SerializedName("file")
    private String[] file;
    @SerializedName("text")
    private String textStory;
//    @SerializedName("numberLike")
//    private UserLike[] like;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    @Override
    public String toString() {
        return "StoriesModels{" +
                "id='" + id + '\'' +
//                ", users=" + users +
                ", file=" + Arrays.toString(file) +
                ", textStory='" + textStory + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public StoriesModels(String id, String[] file, String textStory, String createdAt, String updatedAt) {
        this.id = id;
//        this.users = users;
        this.file = file;
        this.textStory = textStory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public UsersModel getUsers() {
//        return users;
//    }
//
//    public void setUsers(UsersModel users) {
//        this.users = users;
//    }

    public String[] getFile() {
        return file;
    }

    public void setFile(String[] file) {
        this.file = file;
    }

    public String getTextStory() {
        return textStory;
    }

    public void setTextStory(String textStory) {
        this.textStory = textStory;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
