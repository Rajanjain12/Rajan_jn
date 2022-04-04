import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogSerComponent } from '../dialog-ser/dialog-ser.component';
import { DialogComponent } from '../dialog/dialog.component';
import { Injectable, NgZone } from '@angular/core';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { Subject, BehaviorSubject,Observable } from 'rxjs';
import { PropertiesService } from '../properties.service';
import { SearchService }  from '../seller/services/search.service';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { CrudService} from '../crud.service';
import { auth } from 'firebase';
import {Service} from '../seller/services/service';;
import * as firebase from 'firebase';

import { AuthService } from '../core/auth.service';
import { Router, Params } from '@angular/router';
@Component({
  selector: 'app-service-dash',
  templateUrl: './service-dash.component.html',
  styleUrls: ['./service-dash.component.css']
})
export class ServiceDashComponent implements OnInit {

  address: Object;
  establishmentAddress: Object;

  formattedAddress: string;
  formattedEstablishmentAddress: string;

  phone: string;
  properties:any;
  prop : any;
  startAt1 = new Subject();
  endAt1 = new Subject();

  lastkeypress: number = 0;
  property$: Observable<any[]>;
  startAt: BehaviorSubject<string | null> = new BehaviorSubject('');
  errorMessage: string = '';
  
  
  filterForm = new FormGroup({
    f1: new FormControl(),
    
  })
  services: Service[];
  service: Service[] = [];
  price: string = "price";
  priceVal = 0;
  constructor(public authService: AuthService,public dialog: MatDialog,private router: Router,private propsvc:PropertiesService,private searchser: SearchService,public crudService: CrudService,
    public fb: FormBuilder, public zone: NgZone) { 

      this.dataC;
      this.dataS;
    }

  ngOnInit() {
    let s = this.crudService.GetServices();
    s.snapshotChanges().subscribe(data => {
      this.services = [];
      data.forEach(item => {
        let a = item.payload.toJSON();
        console.log(a);
        a['$key'] = item.key;
        
        this.services.push(a as Service);
      })
    })
    this.filterFunction();
  }
  get  dataS() :string

  {
    console.log("get",this.searchser.sharedDataS);
   return  this.searchser.sharedDataS;
  }
  get dataC():string
  {
    console.log("get",this.searchser.sharedDataC);
      return this.searchser.sharedDataC;
  }

  addPrice() {
    this.priceVal = 1;
  }
  removeFilters() {
    this.filterForm.reset();
    this.filterFunction();
  }
  filterFunction() {
    this.priceVal = 0;
    if(this.filterForm.value.f1 !== null) {
      this.addPrice();
    }
    
    let size = this.priceVal;
    console.log(size);

    switch(size) {
      case 0:
            
        let s = this.crudService.GetServices();
        s.snapshotChanges().subscribe(data => {
          this.service = [];
          data.forEach(item => {
            let a = item.payload.toJSON();
           
            if(a['location'] === this.searchser.sharedDataS && a['serviceName'] === this.searchser.sharedDataC )
           {
            a['$key'] = item.key;
            this.service.push(a as Service);
            }
          })
        })
        break;
        case 1:
          if(this.priceVal === 1) {
            let s = this.crudService.GetServiceByFilter(this.price, this.filterForm.value.f1);
            s.snapshotChanges().subscribe(data => {
              this.service = [];
              data.forEach(item => {
                let a = item.payload.toJSON();
               console.log("price");
                if(a['location'] === this.searchser.sharedDataS && a['serviceName'] === this.searchser.sharedDataC)
                {
                  a['$key'] = item.key;
                this.service.push(a as Service);
                }
              })
            })
          }
          break;

    }
  }
  Interested(key1:string,key2:string)
      {
        var s = firebase.auth().currentUser.uid;
          firebase.database().ref('interested').child(s).child("service").push(

            {  serviceid: key1,

            }
          );
          firebase.database().ref('responses').child(key2).child("services").child(key1).push(

              {
                serviceid: key1,
                userid: s,
              }
          );
          this.crudService.SETINTS = key2;
          let dialogRed1 = this.dialog.open(DialogSerComponent);
          
        }
openDialog(key1:string,key2:string) {
          //key1-serviceID key2:SellerID
 this.crudService.ReportSerID = key1;
 this.crudService.ReportSellerID =key2;

          let dialogRed = this.dialog.open(DialogComponent);
        }

  tryLogout(){
    this.authService.doLogout()
    .then(res => {
      this.router.navigate(['']);
    }, err => {
      
      this.errorMessage = err.message;
      console.log(this.errorMessage);
    })
  }

}
