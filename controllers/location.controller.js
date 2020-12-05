const Location = require('../models/location.model')
const getDataWeather = require('../service/getDataWeather')

const postLocation = async (req, res) =>{
    const newLocation = await new Location({
        longitude: req.body.long,
        latiude: req.body.lati,
        name: req.body.name
    })
    await newLocation.save().then(async (data) =>{
        await getDataWeather.restApi(data.latiude, data.longitude, data._id)
        res.json({
            success: true,
            msg: "new Location success"
        })
    }).catch(err =>{
        console.log("",err)
        res.json({
            success: false,
            msg: "new Location failed"
        })
    })
    
}

module.exports = {
    postLocation
}