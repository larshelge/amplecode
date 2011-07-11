
var validationRules = {
rules: {
  user: {
    username: {
      required: true,
      email: true,
      remote: "usernameAvailable" },
    password: {
      required: true,
      minlength: 8,
      maxlength: 20 },
    passwordRepeat: {
      equalTo: "#password" },
    firstname: {
      required: true,
      minlength: 2,
      maxlength: 60 },
    lastname: {
      required: true,
      minlength: 2,
      maxlength: 60 }
  },
  userRestore: {
    password: {
      required: true,
      minlength: 8,
      maxlength: 20 },
    passwordRepeat: {
      equalTo: "#password" }
  },
  userRestoreRequest: {
    username: {
      required: true,
      email: true }
  },
  category: {
  	code: {
  	  required: true,
      minlength: 2,
      maxlength: 20 },
    name: {
      required: true,
      minlength: 2,
      maxlength: 60 }
  },
  team: {
  	code: {
  	  required: true,
      minlength: 2,
      maxlength: 20 },
    name: {
      required: true,
      minlength: 2,
      maxlength: 60 }
  },
  event: {
  	location: {
  	  required: true,
      minlength: 2,
      maxlength: 60 },
    date: {
      required: true,
      date: true }
  },
  person: {
  	code: {
  	  required: true,
      minlength: 2,
      maxlength: 20 },
    name: {
      required: true,
      minlength: 2,
      maxlength: 60 }
  }
},
messages: {
  user: {
  	username: {
      remote: "E-post adressen er allerede registert" }
  }
} };