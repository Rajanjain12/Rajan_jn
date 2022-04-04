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

@Component({
  selector: 'app-view-responses',
  templateUrl: './view-responses.component.html',
  styleUrls: ['./view-responses.component.css']
})
export class ViewResponsesComponent implements OnInit {

  customers: any;

  constructor(private displayService: DisplayService) { }

  ngOnInit() {
    this.getServiceList();
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
