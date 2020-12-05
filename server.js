const app = require('./app')
const http = require('http').Server(app)
const port = process.env.PORT
const db = require('./db/connectDB')
const rp = require('./service/apiOpenWeather')

http.listen(port, () => console.log(`Active on ${port} port`))