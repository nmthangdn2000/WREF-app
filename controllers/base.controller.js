const { ERROR, RESPONSE } = require('../common/constants');

class BaseController {
  responseSuccess(res, message) {
    return res.status(200).json({
      success: true,
      message,
    });
  }

  responseSuccessWithData(res, data, message = RESPONSE.SUCCESS) {
    return res.status(200).json({
      success: true,
      message,
      data,
    });
  }

  responseError(res, errorCode = ERROR.InternalServerError, data = {}) {
    const message = getErrorMessage(code);
    return res.status(200).json({
      success: false,
      message,
      errorCode,
      data,
    });
  }
}

module.exports = new BaseController();

function getErrorMessage(code) {
  const message = getKeyByValue(ERROR, code);
  return message ? message.replace(/([A-Z])/g, ' $1').trim() : `${code}`;
}

function getKeyByValue(object, value) {
  return Object.keys(object).find((key) => object[key] === value);
}
