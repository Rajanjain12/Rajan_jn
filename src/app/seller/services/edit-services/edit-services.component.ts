import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as firebase from 'firebase';
import { Service } from '../service';
import { DisplayService } from '../display-service.service';
import { map } from 'rxjs/operators';
import { AngularFireDatabase } from '@angular/fire/database';

@Component({
  selector: 'app-edit-services',
  templateUrl: './edit-services.component.html',
  styleUrls: ['./edit-services.component.css']
})
export class EditServicesComponent implements OnInit {

  customers: any;
  customerRef1: any;

  constructor(private displayService: DisplayService,private db: AngularFireDatabase ) {
    this.customerRef1 = db.list('/services');
   }

  ngOnInit() {
    this.getServiceList();
  }
  
  deleteRecord(ke:string){
    
    
  
    this.customerRef1.remove(ke);
  
   // firebase.database().ref().child('/property').remove(ke);
}
  getServiceList() {
    this.displayService.getServiceList().snapshotChanges().pipe(
      map(changes => 
        changes.map(c => 
          ({ key:c.payload.key, ...c.payload.val() })
        )
      )
    ).subscribe(customers => {
      this.customers = customers;
    });
  }
}
