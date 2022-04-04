import { Component, OnInit, NgZone } from '@angular/core';
import { PropertiesService } from '../../properties.service';
import { Subject, BehaviorSubject } from 'rxjs';

import { PipeTransform, Pipe } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as firebase from 'firebase';
import { Property } from '../property';
import { DisplayProperty } from '../display-property.service';
import { map } from 'rxjs/operators';

import {SearchService } from '../../seller/services/search.service';
import {CommonModule} from "@angular/common";
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { FirebaseListObservable } from '@angular/fire/database-deprecated';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {
  address: Object;
  establishmentAddress: Object;

  formattedAddress: string;
  formattedEstablishmentAddress: string;

  phone: string;
search1: string;
  properties:any;
  prop : any;
  startAt1 = new Subject();
  endAt1 = new Subject();

  lastkeypress: number = 0;
  property$: Observable<any[]>;
  startAt: BehaviorSubject<string | null> = new BehaviorSubject('');


  constructor(private propsvc:PropertiesService, private searchserv: SearchService,public zone: NgZone) {
    

   }

  ngOnInit() {
  }
  

  getAddress(place: object) {
    this.address = place['formatted_address'];
    //this.phone = this.getPhone(place);
    this.formattedAddress = place['formatted_address'];
    this.zone.run(() => this.formattedAddress = place['formatted_address']);
    this.zone.run(()=>this.search1 = this.formattedAddress);
    this.zone.run(()=>console.log(this.search1));
    
    this.zone.run(()=> this.searchserv.sharedData = this.search1);
    
  }


 

  // search(searchText: string) {
  //   // searchText = this.formattedAddress
  //   console.log(searchText);  
  //   this.startAt.next(searchText);
  //   this.property$ = this.propsvc.getproperties(this.startAt);
  // }

//   search($event){

// if($event.timeStamp - this.lastkeypress > 200){


//     let q = $event.target.value
//     this.startAt1.next(q)
//     this.endAt1.next(q+"\uf8ff")
// }
// this.lastkeypress = $event.timeStamp;
//   }

}
