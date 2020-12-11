const express = require('express');
const router = express.Router();
const controller = require('../../controllers/location.controller')


router.get('/location', controller.getDataLocation)

router.post('/location', controller.postLocation)

module.exports = router