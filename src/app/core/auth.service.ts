import { Injectable } from "@angular/core";
import {Observable} from 'rxjs';   
import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';
import { AngularFireList,AngularFireDatabase, AngularFireObject } from '@angular/fire/database';
import { CrudService} from '../crud.service';
import { Profile } from '../seller/profile';
import { DiamobComponent } from '../diamob/diamob.component';
import { MatDialog } from '@angular/material/dialog';

@Injectable()
export class AuthService {
  age: any;
  servRef: AngularFireList<any>;
  D:any;
  property01: Profile[];
  x: any;
  propertyRef: AngularFireObject<any>;
  constructor(
   public afAuth: AngularFireAuth,
   public dialog: MatDialog,public crudService: CrudService,
   private db: AngularFireDatabase,
 ){}

  doFacebookLogin(){
    return new Promise<any>((resolve, reject) => {
      let provider = new firebase.auth.FacebookAuthProvider();
      this.afAuth.auth
      .signInWithPopup(provider)
      .then(res => {
        resolve(res);
      }, err => {
        console.log(err);
        reject(err);
      })
    })
  }

  doTwitterLogin(){
    return new Promise<any>((resolve, reject) => {
      let provider = new firebase.auth.TwitterAuthProvider();
      this.afAuth.auth
      .signInWithPopup(provider)
      .then(res => {
        resolve(res);
      }, err => {
        console.log(err);
        reject(err);
      })
    })
  }

  doGoogleLogin(){
    return new Promise<any>((resolve, reject) => {
      let provider = new firebase.auth.GoogleAuthProvider();
      provider.addScope('profile');
      provider.addScope('email');
      this.afAuth.auth
      .signInWithPopup(provider)
      .then(res => {
        resolve(res);
//         var user = res.user;
//         var s = "00";
// console.log("AS");
//         this.servRef = this.db.list('user')
//          let s1= this.servRef;
//          let userexist = false;
//          console.log(userexist);
//         //  , ref => ref.child('email')
//         s1.snapshotChanges().subscribe(data => {
         
//          data.forEach(item => {
//            let as1 = item.payload.toJSON();
//           //  console.log(as1);
//           // console.log(as1['email']);
//            //as1['$key'] = item.key;
//            //console.log(as1['$key']);
//            if(as1['email'] === res.user.email)
//            {
//              console.log("OLD");
//              userexist = true;
             
//            }
          
//          });
//          console.log(userexist);
//          if(userexist == false)
//          {
//            console.log("Updating !!")
//          firebase.database().ref('user/').child(user.uid).set({
//            name: user.displayName,
//            email: user.email,
//             contactNo: s,
//          });
//          console.log("Add Contact Details");
//          this.crudService.Newuserid = user.uid;
//           let dialogRed1 = this.dialog.open(DiamobComponent);
       
//        }
//         }); 
        
       

    

      }, err => {
        console.log(err);
        reject(err);
      })
    })
    
  }

  // getNumber(user)
  // {
  //   this.age= prompt("Welcome,Please enter your Contact Details:", "xxxxx-xxxxx");
  //   if (this.age == null)
  //   {
  //     alert("You Pressed Cancel. You chose not to answer this question. So Please Update It after Going in Profile Section.");
  //     this.getNumber(user);
  //   }
  //   else{
  //     var x  =  this.age.length;
  //    this.age = parseInt(this.age);
  //    if ( isNaN(this.age) || (x != 10)){ 
  //     alert("You entered invalidly.So Please Enter Again in Acceptable Format.");
  //     this.getNumber(user);
  //   }
  //   else if(this.age) {
  //     alert("You are Contact Details Will be Updated.");
  //     // user.updatePhoneNumber(this.age);
  //     console.log(this.age);
  //     firebase.database().ref('user/').child(user.uid).update({
        
  //       contactNo: this.age
  //     });
  //   }
    
  //   }
  // }

  doRegister(value){
    return new Promise<any>((resolve, reject) => {
      //  var e = "myemail@email.com";      
      firebase.auth().createUserWithEmailAndPassword(value.email,value.password)
      .then(res => {
        resolve(res);
      }, err => reject(err))
    })
  }

  doLogin(value: { email: string; password: string; }){
    return new Promise<any>((resolve, reject) => {
      
      firebase.auth().signInWithEmailAndPassword(value.email, value.password)
      .then(res => {
        resolve(res);
        
      }, err => reject(err))
    })
  }

  doLogout(){
    return new Promise<void>((resolve, reject) => {
      if(firebase.auth().currentUser){
        // var x = firebase.auth().currentUser.emailVerified;
        // console.log("EMAIL-vER",x);
        this.afAuth.auth.signOut();
        resolve();
      }
      else{
        reject();
      }
    });
  }


}
