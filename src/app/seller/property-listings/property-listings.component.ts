import { Component, OnInit, PipeTransform, Pipe } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {DialogAddComponent} from '../../dialog-add/dialog-add.component';

import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as firebase from 'firebase';
import { Property } from '../property';
import { DisplayProperty } from '../display-property.service';
import { map } from 'rxjs/operators';
import { MatSliderModule } from '@angular/material/slider';
import {CommonModule} from "@angular/common";
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { FirebaseListObservable } from '@angular/fire/database-deprecated';
import { Observable } from 'rxjs';
import { ObjNgFor } from 'src/app/ObjNgFor.pipe';
import { Variable } from '@angular/compiler/src/render3/r3_ast';
import { SwiperOptions } from 'swiper';
import { ConstantPool } from '@angular/compiler';



@Component({
  selector: 'app-property-listings',
  
  templateUrl: './property-listings.component.html',
  styleUrls: ['./property-listings.component.css'],
  
})
export class PropertyListingsComponent implements OnInit {


config: SwiperOptions = {
    
    
    autoplay: true, // Autoplay option having value in milliseconds
// initialSlide: 1, // Slide Index Starting from 0
    zoom: true,
    
//  observer: true,  
//    observeParents: true,
slidesPerView: 1, // Slides Visible in Single View Default is 1
pagination:  { el: '.swiper-pagination', clickable: true },
navigation: {
  nextEl: '.swiper-button-next',
  prevEl: '.swiper-button-prev'
},
 spaceBetween: 30 // Space between each Item

}; 

  status_1: string = 'true';
  status_2: string = 'true';
  itemValue = '';
  items: Observable<any[]>;
 userId: any;
  customers: any;
  customer: any;
  a: any;
  customerRef1: AngularFireList<Property> = null;
  customerAct: AngularFireList<Property> = null;
  as: AngularFireList<any> = null;
  A: AngularFireList<any> = null;
public property : AngularFireList<Property> = null;
  //customerRef1: any;
  constructor(private displayProperty: DisplayProperty,public dialog: MatDialog,private storage: AngularFirestore,private db: AngularFireDatabase,public authService: AuthService) {
    this.customerRef1 = db.list('/property');
    this.property = db.list('/property');
    
    this.A = db.list('/user');
   // this.items = db.list('/property/-M2mqsoPFlYBI300-dFh/Images').valueChanges();
    this.userId = firebase.auth().currentUser.uid;

   }

  ngOnInit() {
    this.getPropertyList();
   //this.FILTER();
    
   
  }

  getPropertyList() {
    this.displayProperty.getPropertyList().snapshotChanges().pipe(
      map(changes => 
        changes.map(c => 
          ({ key:c.payload.key, ...c.payload.val() })
        )
      )
    ).subscribe(customers => {
      this.customers = customers;
      
    });
  }
 
  
  
  deleteRecord(ke:string){
    console.log("DK");
    console.log(ke);
    
   this.customerRef1.remove(ke);
   
    
   
}
Activate(propid:string)
{
  firebase.database().ref('property').child(propid).update({

    verify:"true",
    Date: Date.now(),
  });

}
deleteImage(id:string,ke:object,ke1:string)
{
  
  
  // var desertRef = firebase.database().ref('property').child(id).child('Images').child('-M49NrRTgRcbpIzTxdhz');

  // // Delete the file
  // desertRef.remove().then(function() {
      
  // }).catch(function(error) {
  //   console.log("NO");
  // });

    console.log(id);

    console.log(Object.values(ke));
    var keys = Object.keys(ke);
    console.log(keys);
    for(var i=0;i<keys.length;i++)
    {
      var k = keys[i];
      
      console.log("K",k);
      var d = Object.values(ke[k]);
      console.log(d);
      if(d.toString() === ke1)
      {
        var delRef = firebase.database().ref('property').child(id).child('Images').child(k).remove();
        console.log("Matched");
        alert("Removed Successfully");

      }
    }
     console.log(ke1);
}

  
  FILTER() {
    
    var topUserPostsRef = firebase.database().ref().child('property').orderByChild('id').equalTo(this.userId);
    
    
    console.log("TOP-REF=",topUserPostsRef);
    topUserPostsRef.on('value', function(snap){

      var scores = snap.val();
      console.log("scores-",scores);
      var keys = Object.keys(scores);
      console.log("keys",keys);
      
      for(var i=0;i< keys.length;i++)
      {
        var userId = firebase.auth().currentUser.uid;
        var k = keys[i];
        console.log("K-",k);
    //  if (scores[k].id == userId)
      //  {
          var D = scores[k].features;
          var X = scores[k].id;
          console.log(D +"Features & ID"+ X);
       // }
        //else{
        //  console.log("NO");
       // }
      }
    }
    // snap=>{
    //   console.log(snap.val()+"<-");
    //   console.log("D");
    );

  }
  // AddImages(key:string)
  // {
      
  //     firebase.database().ref('property').child(key).push;
  // }
  openDialog(key:string) {
    
    this.displayProperty.Addprop = key;
    let dialogRed = this.dialog.open(DialogAddComponent);
  }
}


