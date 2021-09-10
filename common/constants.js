const constants = {
  ERROR: {
    InternalServerError: 101,
    //account 111 - 120
    CanNotSignUp: 111,
    UserDoesNotExist: 112,
    // comment 121 - 130
    CanNotGetComment: 121,
    CanNotCreateComment: 122,
    CanNotUpdateComment: 123,
    CanNotCreateRefComment: 124,
  },
  RESPONSE: {
    SUCCESS: 'success',
    SIGNUPSUCCESS: 'Sign Up Success',
    SIGNINSUCCESS: 'Sign In Success',
    UPDATECOMMENTSUCCESS: 'Update Comment Success',
    DELETECOMMENTSUCCESS: 'Delete Comment Success',
  },
};

module.exports = constants;
