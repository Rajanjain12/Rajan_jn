import { Component, OnInit } from '@angular/core';

import { AuthService } from '../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore'
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { auth } from 'firebase';
import * as firebase from 'firebase';

@Component({
  selector: 'app-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.css']
})
export class ForgotComponent implements OnInit {
  forgotForm: FormGroup;
  errorMessage: string = '';

  constructor(
    public authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ){
    

      // this.router.navigateByUrl('/registration');
      this.createForm();
    

  }
  createForm() {
    this.forgotForm = this.fb.group({
      email: ['', Validators.required ]
    });
  }
  Reset(value)
  
    {
       
var emailAddress = value.email;
var auth1 = firebase.auth();

auth1.sendPasswordResetEmail(emailAddress).then(function() {
  window.alert("Email has been sent to your email address");
  
}).catch(function(error) {
  // An error happened.
  window.alert("error");
});
      
    }
  ngOnInit() {
  }

}
