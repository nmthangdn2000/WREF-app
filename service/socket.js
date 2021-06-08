const io = require("socket.io")();

const people = {};

io.on("connection", (socket) => {
  console.log("a user connected");

  socket.on("like", (idPosts, idUser, type) => {
    if (type == true) {
      //   like bài viết
    } else {
      //   hủy like bài viết
    }
  });
  socket.on("comment", (idPosts, idUser, content) => {});

  socket.on("disconnect", () => {
    console.log("user disconnected");
  });
});

function updateNotification() {}

module.exports = io;
