import { Injectable } from '@angular/core';
// import * as GeoFire from "geofire" 
import { GeoFire } from 'geofire';
import {AngularFireDatabase} from '@angular/fire/database'
import { AuthService } from './core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore'
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { auth } from 'firebase';
import * as firebase from 'firebase';

import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class GeoService {
  dbRef : any;
  geoFire: any;
  hits = new BehaviorSubject([])

  constructor(private db:AngularFireDatabase) { 
    this.dbRef = this.db.list('/locations');
    this.geoFire = new GeoFire(this.dbRef.query.ref);

  }
  setlocation(key:string, coords:Array<number>)
  {
    this.geoFire.set(key,coords)
      .then(_=> console.log('location Updated'))
      .catch(err => console.log(err))
  }
  getlocations(radius: number,coords: Array<number>)
  {
    this.geoFire.query({
      center:coords,
      radius:radius
    })
    .on('key_entered',(key,location,distance)=>{
      let hit = {
        location: location,
        distance: distance
      }
      let currentHits = this.hits.value
      currentHits.push(hit)
      this.hits.next(currentHits)
    })
  }
}
