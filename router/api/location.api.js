const express = require('express');
const router = express.Router();
const controller = require('../../controllers/location.controller')


// router.get('/location', controller.postLocation)

router.post('/location', controller.postLocation)

module.exports = router