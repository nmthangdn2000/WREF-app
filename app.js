require('dotenv').config();

const express = require('express')
const app = express()
const bodyParser = require('body-parser')
var path = require('path');

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }))
app.use(express.static(path.join(__dirname, 'public')))
// passport
const passport = require('passport')
const passportConfig = require('./middlewares/passport')
// api 
const apiPosts = require('./router/api/posts.api')
const apiUser = require('./router/api/user.api')
const apiLogin = require('./router/api/login.api')
const apiComment = require('./router/api/comment.api')
const apiLocation = require('./router/api/location.api')
// router api passport.authenticate('jwt', { session: false })
app.use('/api', apiLogin)
app.use('/api', apiPosts)
app.use('/api', apiComment)
app.use('/api', apiLocation)
module.exports = app