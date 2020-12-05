const Comment = require('../models/comment.model')

// get all Comment
const getComment = async (req, res) => {
    await Comment.find()
        .then(data => {
            res.send(data)
        }).catch(err => {
            console.log('', err);
            res.json({
                success: false,
                msg: "Comment update failed"
            })
        })
}
// add new comment
const newComment = async (req, res) => {
    const newCmt = await new Comment({
        idUser: req.user._id,
        idPosts: req.params.id,
        content: req.body.content,
        media: req.file.filename,
        Like:[],
        create_at: new Date,
        update_at: new Date
    })
    await newCmt.save().then(data => {
            res.json({
                success: true,
                msg: "Comment update success"
            })
        }).catch(err => {
            console.log('', err);
            res.json({
                success: false,
                msg: "Comment update failed"
            })
        })
}
// edit comment
const editComment = async (req, res) => {
    await Comment.findByIdAndUpdate(req.params.id, { $set: {
        content: req.body.content,
        update_at: new Date
    }}).then(data => {
        res.json({
            success: true,
            msg: "Comment update success"
        })
    }).catch(err => {
        console.log('' ,err);
        res.json({
            success: false,
            msg: "Comment update failed"
        })
    })
    
}
// ref comment
const refComment = async (req, res) => {
    const newCmt = await new Comment({
        idUser: req.user._id,
        idPosts: req.params.idposts,
        idComment: req.params.idcmt,
        content: req.body.content,
        // media: req.file.filename,
        Like:[],
        create_at: new Date,
        update_at: new Date
    })
    await newCmt.save().then(data => {
        res.json({
            success: true,
            msg: "new RefComment success"
        })
    }).catch(err => {
        console.log('', err);
        res.json({
            success: false,
            msg: "new RefComment failed"
        })
    })
}
// get RefComent
const getRefComment = async (req, res) => {
    await Comment.findOne({idComment: req.params.id})
        .then(data => {
            res.send(data)
        }).catch(err => {
            console.log('', err);
        res.json({
            success: false,
            msg: "get RefComment failed"
        })
        })
}
// delete a Comment
const deleteComment = async (req, res) => {
    // delete file 
    await Comment.findOne({_id: req.params.id})
    .then(data => {
        console.log(typeof data.media);
        if(data.media.length > 0)
            data.media.forEach(element => {
                const path = './public/uploads/'+element
                try {
                    fs.unlinkSync(path)
                } catch (error) {
                    console.log(" "+error);
                }
            })
    })
    .catch(err => console.log(" "+err))
    await Comment.deleteOne({_id: req.params.id})
        .then(data => {
            res.json({
                success: true,
                msg: "Comment delete success"
            })
        }).catch(err => {
            console.log("", err);
            res.json({
                success: false,
                msg: "Comment delete failed"
            })
        })
}
module.exports = {
    // get all Comment
    getComment,
    // new Comment in a Posts
    newComment,
    //edit Comment
    editComment,
    // ref Comment
    refComment,
    // get RefComment
    getRefComment,
    // delete a Comment
    deleteComment
}