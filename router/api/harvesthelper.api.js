const express = require("express");
const router = express.Router();
const controller = require("../../controllers/harvesthelper.controller");

router.get("/harvesthelper/:id", controller.getByID);

module.exports = router;
