const User = require('../models/user.model')
const jwt = require('jsonwebtoken')
const { JWT_SECRET } = require('../config/appconfig')

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
    await newUser.save().then(data => res.send(data)).catch(err => console.log(err))
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
        data,
        token: token
    })
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
}