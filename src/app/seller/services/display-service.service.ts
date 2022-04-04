import { Injectable } from '@angular/core';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as firebase from 'firebase';
import { Service } from './service';

@Injectable({
    providedIn: 'root'
})

export class DisplayService {
    //userID = firebase.auth().currentUser.uid;
    private dbPath = '/services';
    customerRef: AngularFireList<Service> = null;
    
userId: any;
     constructor(private db: AngularFireDatabase){
         this.userId = firebase.auth().currentUser.uid;
         this.customerRef = db.list(this.dbPath, ref => ref.orderByChild('id').equalTo(this.userId));
     }

     getServiceList(): AngularFireList<Service> {
         return this.customerRef;
     }
}