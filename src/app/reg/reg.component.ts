import { Component, OnInit } from '@angular/core';
import { AuthService } from '../core/auth.service'
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators, EmailValidator } from '@angular/forms';
import * as firebase from 'firebase';
import { ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-reg',
  templateUrl: './reg.component.html',
  styleUrls: ['./reg.component.css']
})
export class RegComponent  {
age: any;
  registerForm: FormGroup;
   errorMessage: string = '';
   error_messages = {

    'name': [
      { type: 'required', message: ' Name is required.' },
    ],
    
    'email': [
      { type: 'required', message: 'Email is required.' },
      { type: 'minlength', message: 'Email length.' },
      { type: 'maxlength', message: 'Email length.' },
      { type: 'required', message: 'please enter a valid email address.' }
    ],
    'password': [
      { type: 'required', message: 'password is required.' },
      { type: 'minlength', message: 'password length.' },
      { type: 'maxlength', message: 'password length.' }
    ],
    'mobile': [
    { type: 'required', message: 'mobile No is required.' },
    { type: 'minlength', message: 'mobile No ~ length.' },
    { type: 'maxlength', message: 'mobile No length.' }
    ]}

   
  successMessage: string = '';


  constructor(
    public authService: AuthService,
    private router: Router,
    private fb: FormBuilder

  ) 
  { this.createForm(); }
  createForm() {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-.]+$') ]],
      // password: ['',Validators.required],
      password: ['', Validators.compose([
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(30)
      ])],
       mobile: ['',Validators.compose([
       
       Validators.required,
       Validators.pattern('^[0-9]+$'),
       Validators.maxLength(10),
       Validators.minLength(10)])],
        name: ['',[Validators.required, Validators.minLength(3) ]],
       
    });
  }
  
  get f() { return this.registerForm.controls; }

  tryFacebookLogin(){
    this.authService.doFacebookLogin()
    .then(res =>{
      this.router.navigate(['/registration']);
    }, err => console.log(err)
    )
  }

  tryGoogleLogin(){
    this.authService.doGoogleLogin()
    .then(res =>{
      console.log(res);
      this.errorMessage = "";
      this.successMessage = "Your account has been created";
      this.authService.doLogout();
      console.log("Pls Entry");
     
      this.router.navigate(['']);
    }, err => console.log(err)
    )
  }



  tryRegister(value){
    this.authService.doRegister(value)
    .then(res => {
      console.log(res);
      this.errorMessage = "";
      this.successMessage = "Your account has been created";
      this.authService.doLogout();
      this.tryVerify();
console.log(res.user.emailVerified);
var userId = firebase.auth().currentUser.uid;
firebase.database().ref('user/' + userId).set({
  name: value.name,
  email: value.email, contactNo: value.mobile
});
      // firebase.auth().onAuthStateChanged(function(user) { 
      //   if (user.emailVerified) {
          
      //     console.log('Email is verified');
      //     var userId = firebase.auth().currentUser.uid;
     
      //   }
      //   else {
      //     console.log('Email is not verified');
      //     //firebase.auth().currentUser.delete();
      //   }
      // }); 
      
      // if(firebase.auth().currentUser.emailVerified)
      // {
      //   console.log("Verified");
        

      // }
      // else{
      //   console.log(" Not Verified");
      //   window.alert("Please Verify Email To move Forward");
      // //  this.authService.doLogout();
      //   firebase.auth().currentUser.delete;
      // }
      
      
      this.authService.doLogout();
      
        this.router.navigate(['']);
       
    
    //   this.router.navigate(['']);
       var userId = firebase.auth().currentUser.uid;
    
    // firebase.database().ref('user/' + userId).set({
    //   name: value.name,
    //   email: value.email, contactNo: value.mobile
    // });
    firebase.database().ref('D-PROP/'+ userId).set({

        
    });
     
    }, err => {
      console.log(err);
      this.errorMessage = err.message;
      this.successMessage = "";
    })
    
    
  
  }
  tryVerify()
  {
    var user = firebase.auth().currentUser;

user.sendEmailVerification().then(() => {
  // Email sent.
  window.alert("Verify Email Address on link sent to your Email Id! Please Verify it Fast.");
}).catch(function(error) {
  window.alert(error)
});
  }
  clearForm() {
    this.registerForm.reset();
   }
   
    asc(){
      setInterval(function(){  }, 50000);
    
    }
  


  ngOnInit() {
  }

}
