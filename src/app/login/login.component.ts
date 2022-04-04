import { Component, OnInit } from '@angular/core';
import * as firebase from 'firebase';
import { Injectable } from "@angular/core";
import {Observable} from 'rxjs';   
import { AngularFireList,AngularFireDatabase, AngularFireObject } from '@angular/fire/database';
import {NgForm} from '@angular/forms';
import { CrudService} from '../crud.service';
import { FirebaseListObservable, FirebaseObjectObservable } from '@angular/fire/database-deprecated';
import { AngularFireModule } from '@angular/fire';
import { AngularFireAuthModule, AngularFireAuth } from '@angular/fire/auth';
import { AuthService } from '../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore'
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DiamobComponent } from '../diamob/diamob.component';
import { MatDialog } from '@angular/material/dialog';
import { DialoginComponent} from '../dialogin/dialogin.component';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: []
})
export class LoginComponent {
  propertyRef: AngularFireObject<any>;
  loginForm: FormGroup;
  errorMessage: string = '';
  servRef: AngularFireList<any>;
  constructor(
    public authService: AuthService,
    private router: Router,
    public dialogL: MatDialog,
    public dialog: MatDialog,public crudService: CrudService,
    private fb: FormBuilder,
    private db: AngularFireDatabase,
  ) {

    // this.router.navigateByUrl('/registration');
    this.createForm();
  }

  createForm() {
    this.loginForm = this.fb.group({
      email: ['', Validators.required ],
      password: ['',Validators.required]
    });
  }

  tryFacebookLogin(){
    this.authService.doFacebookLogin()
    .then(res => {
      this.router.navigate(['/dashboard']);
    })
  }

  tryTwitterLogin(){
    this.authService.doTwitterLogin()
    .then(res => {
      this.router.navigate(['/dashboard']);
    })
  }

  tryGoogleLogin(){
    this.authService.doGoogleLogin()
    .then(res => {
      var user = res.user;
      console.log("AS");
      var s = "00";
      this.servRef = this.db.list('user')
       let s1= this.servRef;
       let userexist = false;
       console.log(userexist);
      //  , ref => ref.child('email')
      s1.snapshotChanges().subscribe(data => {
       
       data.forEach(item => {
         let as1 = item.payload.toJSON();
        //  console.log(as1);
        // console.log(as1['email']);
         //as1['$key'] = item.key;
         //console.log(as1['$key']);
         if(as1['email'] === res.user.email)
         {
           console.log("OLD");
           userexist = true;
           
         }
        
       });
       console.log(userexist);
       if(userexist)
       {
        this.router.navigate(['/background']);
       }
       if(userexist == false)
       {
         console.log("Updating !!")
       firebase.database().ref('user/').child(user.uid).set({
         name: user.displayName,
         email: user.email,
          contactNo: s,
       });
       console.log("Add Contact Details");
       this.crudService.Newuserid = user.uid;
        let dialogRed1 = this.dialog.open(DiamobComponent);
        this.router.navigate(['/background']);
     }
      }); 
      
      
    })
  }
   


  
  tryLogin(value){
    this.authService.doLogin(value)
    .then(res => {
      console.log("TryLogin");
      if(firebase.auth().currentUser.emailVerified)
      {
        this.router.navigate(['/background']);
      }
      else{

        this.authService.doLogout();
  
        this.crudService.loginid = res.user;
        let dialogRed1 = this.dialog.open(DialoginComponent);
      }
      
    }, err => {
      console.log(err);
      this.errorMessage = err.message;
    })
  }
}


 



