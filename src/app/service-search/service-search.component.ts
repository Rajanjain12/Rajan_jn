import { Component, OnInit, NgZone } from '@angular/core';

import { Subject, BehaviorSubject } from 'rxjs';
import { SearchService } from '../seller/services/search.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as firebase from 'firebase';
@Component({
  selector: 'app-service-search',
  templateUrl: './service-search.component.html',
  styleUrls: ['./service-search.component.css']
})
export class ServiceSearchComponent implements OnInit {
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
  
  startAt: BehaviorSubject<string | null> = new BehaviorSubject('');


  serviceSearch: FormGroup;
  error_messages = {
    'f1': [
      { type: 'required', message: ' Please Select the Category.' },
    ],
    'f2': [
      { type: 'required', message: ' Please Complete the Field.' },
    ],
  }
  constructor(private zone:NgZone, private fb: FormBuilder,private ser: SearchService) { 
    this.createForm();
  }
  
  createForm() {
    this.serviceSearch = this.fb.group({
      f1: ['',[Validators.required ]],
      f2: ['',[Validators.required ]],
      
      
    });
  }
  getAddress(place: object) {
    this.address = place['formatted_address'];
    //this.phone = this.getPhone(place);
    this.formattedAddress = place['formatted_address'];
    this.zone.run(() => this.formattedAddress = place['formatted_address']);
    this.zone.run(()=>this.search1 = this.formattedAddress);
    this.zone.run(()=>console.log(this.search1));
    
     this.zone.run(()=> this.ser.sharedDataS = this.search1);
    this.zone.run(()=>this.ser.sharedDataC = this.serviceSearch.value.f1);
  }
submit()
{
  console.log("Search Value",this.serviceSearch.value.f1);
    this.ser.sharedDataC = this.serviceSearch.value.f1;
}
  ngOnInit() {
  }

}
