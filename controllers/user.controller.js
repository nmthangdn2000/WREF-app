const User = require('../models/user.model')
const jwt = require('jsonwebtoken')
const { JWT_SECRET } = require('../config/appconfig')
const { find } = require('../models/user.model')

const endCodeToken = (inforUser) => {
    return jwt.sign(inforUser, JWT_SECRET, {expiresIn: '30d'})
}
// đăng ký
const postSignUp = async (req, res) => {
    const newUser = new User({
        userName: req.body.userName, 
        email: req.body.email,
        passWord: req.body.passWord,
        create_at: new Date,
        update_at: new Date
    })
    await newUser.save().then(data => {
        res.json({
            success: true,
            msg: "SignUp success"
        })
    }).catch(err => 
        res.json({
            success: false,
            msg: "SignUp failed"
        })
    )
}
// đăng nhập
const postSignIn = async (req, res) => {
    const data = req.user
    const inforUser = {
        id: data._id,
        userName: data.userName
    }
    const token = endCodeToken(inforUser)
    res.json({
        _id: data._id,
        userName: data.userName,
        email: data.email,
        avata: data.avata,
        token: token
    })
}
// check login
const checkLogin = async (req, res) => {
    await User.findById(req.user._id)
        .then(data =>
            res.send(data)
        )
        .catch(err =>
            res.json({
                success: false,
                msg: "Failed"
              })
        )
}
function getInfUser(req, res){
    
}
function editUser(req, res){
    
}
function deleteUser(req, res){

}

module.exports = {
    // sign in
    postSignIn: postSignIn,
    // sign up
    postSignUp: postSignUp,
    // get infor user
    getInfUser: getInfUser,
    // edit infor user
    editUser: editUser,
    // delete user
    deleteUser: deleteUser,
    //check login
    checkLogin: checkLogin
}