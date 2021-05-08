const crops = require("../models/crops");

const getByID = async (req, res) => {
  console.log(req.params.id);
  await crops
    .findById(req.params.id)
    .then((data) => res.send(data))
    .catch((err) => console.log(err));
};

module.exports = {
  getByID,
};
