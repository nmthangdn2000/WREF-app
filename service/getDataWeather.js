require('dotenv').config();
const rp = require('request-promise')
const Weather = require('../models/weather.model')

async function potsWeather(arr, id){
    await Weather.findOne({idLocation: id}, async (err, data) => {
        if(!data) {
            const newWeather = await new Weather({
                idLocation: id,
                infor:arr
            })
            await newWeather.save()
                .then(data => console.log("oki"))
                .catch(err=> console.log("", err))
            return;
        }
        else{
            await Weather.updateOne({idLocation: id}, {$set:{
                infor:arr
            }}).then(data => {console.log("cập nhật")}).catch(err => console.log("", err))
            return;
        }
    })
    
}

module.exports.restApi = function restApi(latiude, longitude, id){
    const URL = 'https://api.openweathermap.org/data/2.5/onecall?lat='+latiude+'&lon='+longitude+'&exclude=hourly&appid='+process.env.KEY_API
    rp(URL)
        .then(async (data) =>{
            const api = JSON.parse(data)
            const array = []
            array.push(api.current)
            api.daily.forEach(element => {
                array.push(element)
            });
            console.log(array.length);
            await potsWeather(array, id)
        })
        .catch(err => console.log("", err))
}