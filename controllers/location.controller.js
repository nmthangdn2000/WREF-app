const Location = require('../models/location.model')
const Posts = require('../models/posts.model')
const Weather = require('../models/weather.model')
const getDataWeather = require('../service/getDataWeather')

const getDataLocation = async (req, res) => {
    const dataL = await Location.findOne({name: req.body.name})
    if(dataL){
        const dataP = await Posts.find({idLocation: dataL._id})
        if(dataP){
            const img = []
            const data_img = dataP.map((element) => {
                element.media.forEach(value => {
                    img.push(value)
                })
                return element.media
            })
            const dataW = await Weather.findOne({idLocation: dataL._id})
            if(!dataW){
                await getDataWeather.restApi(dataL.latiude, dataL.longitude, dataL._id)
                    .then(data => {
                        res.json({
                            location: dataL,
                            media: img,
                            weather: dataW
                        })
                        return
                    }).catch(err => {
                        console.log("",err);
                        logErr(res, "get data Location failed")
                    })
            }
            else if(dataL && dataP && dataW){
                res.json({
                    location: dataL,
                    media: img,
                    weather: dataW
                })
                return
            }else{
                logErr(res, "get data Location failed")
                return
            }
        }else{
            logErr(res, "get data Location failed")
            return
        }
    }else{
        logErr(res, "get data Location failed")
        return
    }
}
//
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
        logErr(res, "new Location failed")
    })
    
}
function logErr(res, msg){
    res.json({
        success: false,
        msg: msg
    })
}
module.exports = {
    getDataLocation,
    postLocation
}