const io = require("socket.io")();
const Posts = require("../models/posts.model");

const people = {};

io.on("connection", (socket) => {
  console.log("a user connected");

  socket.on("like", async (idPosts, idUser, type) => {
    console.log(idPosts);
    console.log(idUser);
    console.log(type);
    await likePosts(idPosts, idUser, type, socket);
    // update notification
  });
  socket.on("comment", (idPosts, idUser, content) => {});

  socket.on("disconnect", () => {
    console.log("user disconnected");
  });
});

function updateNotification() {}

async function likePosts(idPosts, idUser, type, socket) {
  if (type == true) {
    //   like bài viết
    const like = await Posts.findByIdAndUpdate(idPosts, {
      $addToSet: {
        Like: {
          iduserLike: idUser,
        },
      },
    });
    console.log("Like", like.Like.length + 1);
    if (like) socket.emit("like", like.Like.length + 1);
    else socket.emit("like", null);
  } else {
    const disLike = await Posts.findByIdAndUpdate(idPosts, {
      $pull: {
        Like: {
          iduserLike: idUser,
        },
      },
    });
    console.log("disLike", disLike.Like.length - 1);
    if (disLike) socket.emit("like", disLike.Like.length - 1);
    else socket.emit("like", null);
    //   hủy like bài viết
  }
}
module.exports = io;
