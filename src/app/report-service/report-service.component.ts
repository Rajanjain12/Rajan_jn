import { Component, OnInit,Inject } from '@angular/core';
import { CrudService } from '../crud.service';
import * as firebase from 'firebase';
import { AuthService } from '../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import {Property} from '../seller/property';
import { SwiperOptions } from 'swiper';
import {Service} from '../seller/services/service';
import { Profile } from '../seller/profile';
import { Report } from '../report';
import {Try } from '../try';
import { MatDialog } from '@angular/material/dialog';

import { DialogVResServiceComponent } from '../dialog-v-res-service/dialog-v-res-service.component';
import { DiaviewreportComponent } from '../diaviewreport/diaviewreport.component';
@Component({
  selector: 'app-report-service',
  templateUrl: './report-service.component.html',
  styleUrls: ['./report-service.component.css']
})
export class ReportServiceComponent implements OnInit {
  property1: Service[];
  property01: Service[];

  servRef: AngularFireList<any>;
  servRef1: AngularFireList<any>;
  servRef2: AngularFireList<any>;
  Property2 : Service[];
  pessS: Service[] = [];
  pessST: Service[] = [];
  pessSR: Service[] = [];
  //pessU: Profile[] = [];
  pessU: Profile[];
  o: Try[]= [];
  pessUR: Profile[] = [];
  pessRR: Report[] = [];

  constructor(private crudService: CrudService,public dialog: MatDialog,private db: AngularFireDatabase) { }

  ngOnInit() {
    
    let s1 = this.getListService();
    console.log("SSSS");
    s1.snapshotChanges().subscribe(data => {
      console.log("SSSS");
     // this.property1 = [];
      data.forEach(item => {
        let a1 = item.payload.toJSON();
        console.log("SSSS");
        console.log("SSSS");
        console.log("A1",a1);
        a1['$key'] = item.key;
        console.log("KEY",a1['$key']);
       // console.log(a1['serviceid']);
        //console.log(a1['userid']);
      this.getKeysS(a1['$key']);
     // this.getKeysinU(a1['$key']);
       //this.getKeysU(a1['userid']);

       
        
      })
    })
    let s2 = this.getListServiceReport();
    s2.snapshotChanges().subscribe(data => {
      this.property01 = [];
      data.forEach(item => {
        let a2 = item.payload.toJSON();
        console.log("A2",a2);
        a2['$key'] = item.key;
        console.log(a2['$key']);
        // console.log(a2['serviceid']);
        // console.log(a2['userid']);
        // console.log(a2['report']);
    //  this.getKeysReportS(a2['$key']);
       //this.getKeysReportU(a2['userid']);
      // this.pessRR.push(a2['report'] as Report );
     // this.getKeysReportMess(a2['report']);
     //   console.log(this.pessRR);
     this.getinS(a2['$key']);

          
      })
    })
  }
  getinS(ad:string)
  {
  // let s1= this.GetReportServicesRef(ad);
   let s1 = this.crudService.GetReportServices(ad);
   s1.snapshotChanges().subscribe(data => {
    this.property1 = [];
    data.forEach(item => {
     
      let as1 = item.payload.toJSON();
      console.log("Service",as1);
      
      as1['$key'] = item.key;
      console.log(as1['$key']);
      this.pessSR.push(as1 as Service);
      //as1['$key'] = item.key;
      // console.log("IN",as1['report']);console.log("IN",as1['serviceid']);
      // console.log("IN",as1['userid']);
      
      //this.getKeysReportS(as1['$key']);
      //this.getKeysReportU(as1['userid']);
     // this.pessRR.push(as1['report']);
      //this.pessUR.push(as1 as Service);
    
    })
  })
  }
  GetReportServicesRef(id:string)
  {
    console.log("Service-Report List Fetch");
     this.servRef2 = this.db.list('report', ref => ref.child(firebase.auth().currentUser.uid).child('services').child(id));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef2);
    return this.servRef2;
  }
  GetReportServicesRefIN(id:string)
  {
    console.log("Service-Report List Fetch");
     this.servRef2 = this.db.list('responses', ref => ref.child(firebase.auth().currentUser.uid).child('services').child(id));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef2);
    return this.servRef2;
  }
  getKeysReportS(ad:string)
  {
   let s1= this.crudService.GetReportServices(ad);
   s1.snapshotChanges().subscribe(data => {
    this.property1 = [];
    data.forEach(item => {
      let as1 = item.payload.toJSON();
      console.log("Service",as1);
      as1['$key'] = item.key;
      this.pessSR.push(as1 as Service);
    
    })
  })
  }
  openDialogforReport(id:string)
  {
      console.log(id);
      this.crudService.SETReportView = id;
      let dialogRed = this.dialog.open(DiaviewreportComponent);


  }

  getKeysReportU(ad:string)
    {
      console.log("S");
     let s1= this.crudService.GetReportUsers(ad);
     s1.snapshotChanges().subscribe(data => {
      this.pessUR = [];
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        console.log("User",as1);
        
        this.pessUR.push(as1 as Profile);
      
      })
    })
    } 
  getKeysS(ad:string)
    {
     let s1= this.crudService.GetResponsesServices(ad);
     s1.snapshotChanges().subscribe(data => {
      this.property1 = [];
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        console.log("Service",as1);
        console.log("CHAHSHAH");
        as1['$key'] = item.key;
        console.log(as1['$key']);
        this.pessS.push(as1 as Service);
      
      })
    })
    }
    getKeysU(a:string,ad:string)
    {
     let s1= this.crudService.GetResponsesUsers(ad);
     
     s1.snapshotChanges().subscribe(data => {
      this.pessU = [];
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        console.log("User",as1);
        console.log(a);
        
        this.pessU.push(as1 as Profile);
        //Do some Setting over here such that multiple things can be pushed in one array
        //console.log("UPDATE",this.pessU);
      
      })
    })
    } 
    getKeysinU(ad:string)
    {
     let s1= this.GetReportServicesRefIN(ad);
     s1.snapshotChanges().subscribe(data => {
      this.property1 = [];
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        console.log("INSIDE LOOP",as1);
        console.log("USERID",as1['userid']);
      // this.pessST[ad].push(as1 as Profile);
      this.getKeysU(ad,as1['userid']);
      
      })
    })
    }

    getListService()
  {
    console.log("Service List Fetch");
     this.servRef = this.db.list('responses', ref => ref.child(firebase.auth().currentUser.uid).child('services'));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef);
    return this.servRef;
  }
  getListServiceReport()
  {
    console.log("Service-Report List Fetch");
     this.servRef1 = this.db.list('report', ref => ref.child(firebase.auth().currentUser.uid).child('services'));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef1);
    return this.servRef1;
  }
  openDialog(key1:string) {
    
    
    this.crudService.SETSERVICEID = key1;
    let dialogRed = this.dialog.open(DialogVResServiceComponent);
  }

}
