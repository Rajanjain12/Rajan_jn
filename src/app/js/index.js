import { $ } from "protractor";

  // Your web app's Firebase configuration
  // var firebaseConfig = {
  //   apiKey: "AIzaSyAqGP2qrlpH0Vcfxw8lwu7DsI_zfXK5vIw",
  //   authDomain: "homeeasy-d3526.firebaseapp.com",
  //   databaseURL: "https://homeeasy-d3526.firebaseio.com",
  //   projectId: "homeeasy-d3526",
  //   storageBucket: "homeeasy-d3526.appspot.com",
  //   messagingSenderId: "910157629035",
  //   appId: "1:910157629035:web:a4aa8585382ec3fb53932e",
  //   measurementId: "G-K3S57E5PJS"
  // };
  // // Initialize Firebase
  // firebase.initializeApp(firebaseConfig);
  firebase.auth.Auth.Persistence.LOCAL;
  

  $("#btn-login").click(function()

  {
    window.alert("Please Fill Out All Fields!");
    var email= $("#email").val();
    var password = $("#password").val();

      if(email != "" && password !="")
      {
         var result = firebase.auth().signInWithEmailAndPassword(email,password);
         result.catch(function(error)
         {
            var errorCode =error.code;
            var errorMessage= error.message;

            console.log(errorCode);
            console.log(errorMessage);
            window.alert(errorMessage);
         }
         );

      }
      else
      {
        window.alert("Please Fill Out All Fields!");
      }
  }
  );