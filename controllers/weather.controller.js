const Weather = require('../models/weather.model')
const GetData = require('../service/getDataWeather')

const getWeather = async (req, res) => {
    await Weather.findOne({idLocation: req.res})
        .then(data => 
            res.send(data)
        )
        .catch(err => 
            res.json({
                success: false,
                msg: "getWeather failed"
            })
        )
}

const getDetailWeather = async (req, res) =>{
    console.log(req.body);
    const data = GetData.getWeatehrWithLocation(req.body.lat, req.body.long, res)
}
const getWeatehr24h = async (req, res) => {
    console.log(req.body);
    const data = GetData.getWeatehr24h(req.body.lat, req.body.long, res)
}
module.exports = {
    getWeather,
    getDetailWeather,
    getWeatehr24h
}