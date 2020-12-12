const express = require('express');
const router = express.Router();
const controller = require('../../controllers/weather.controller')

router.get('/weather', controller.getWeather)

module.exports = router