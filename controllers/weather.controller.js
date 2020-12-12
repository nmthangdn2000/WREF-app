const Weather = require('../models/weather.model')

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
module.exports = {
    getWeather
}