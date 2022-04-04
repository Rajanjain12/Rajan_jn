import { Injectable } from '@angular/core';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { Property } from './property';
import { auth } from 'firebase';
import * as firebase from 'firebase';
@Injectable({
    providedIn: 'root'
})

export class DisplayProperty {
    
    Addprop :string;
    private dbPath = '/property';
    private dbPathA = '/user';
    userId: any;
    customerRef: AngularFireList<Property> = null;
    
    topUserPostsRef: AngularFireList<Property> = null;
    //var userId = firebase.auth().currentUser.uid;
    //console.log(userId);
    

    constructor(private db: AngularFireDatabase) {
        
        
        this.userId = firebase.auth().currentUser.uid;
        this.customerRef = db.list(this.dbPath, ref => ref.orderByChild('id').equalTo(this.userId));
        
        
        // orderByChild('id').equalTo(this.userId);
    }
    
    
    
    getPropertyList(): AngularFireList<Property> {
        // console.log(this.customerRef);
        console.log("DISPLAYPROP",this.customerRef);
       // var userId = firebase.auth().currentUser.uid;
        
        // this.customerRef = topUserPostsRef;
         return this.customerRef;
        //return this.topUserPostsRef;
    }
   



}