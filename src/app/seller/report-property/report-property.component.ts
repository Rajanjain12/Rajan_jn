import { Component, OnInit } from '@angular/core';
import { CrudService } from '../../crud.service';
import * as firebase from 'firebase';
import { AuthService } from '../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import {Property} from '../../seller/property';
import { SwiperOptions } from 'swiper';
import {Service} from '../../seller/services/service';
import { Profile } from '../profile';
import { Report } from '../../report';
import { DialogViewResponsesComponent } from 'src/app/dialog-view-responses/dialog-view-responses.component';
import { MatDialog } from '@angular/material/dialog';
import { DiaPropReportComponent } from 'src/app/dia-prop-report/dia-prop-report.component';
@Component({
  selector: 'app-report-property',
  templateUrl: './report-property.component.html',
  styleUrls: ['./report-property.component.css']
})
export class ReportPropertyComponent implements OnInit {
  propRef: AngularFireList<any>;
  servRef1: AngularFireList<any>;
  property: Property[];
  property1: Property[];
  Property2 : Property[];
  pess: Property[] = [];
  pessR: Property[] = [];
pessRR : Report[]=[];
  pessPU:Profile[] = [];
  pessS: Service[] = [];
  pessU: Profile[] = [];
  pessUR: Profile[] = [];

  config: SwiperOptions = {
    autoplay: true, // Autoplay option having value in milliseconds
    zoom: true,
  slidesPerView: 1, // Slides Visible in Single View Default is 1
  pagination:  { el: '.swiper-pagination', clickable: true },
  navigation: {
    nextEl: '.swiper-button-next',
    prevEl: '.swiper-button-prev'
              },
    spaceBetween: 30 // Space between each Item

    };
  constructor(private crudService: CrudService,public dialog: MatDialog,private db: AngularFireDatabase) { }

  ngOnInit() {

    let s = this.getlist();
    s.snapshotChanges().subscribe(data => {
      this.property = [];
      data.forEach(item => {
        let a = item.payload.toJSON();
        console.log(a);
        a['$key'] = item.key;
        console.log("A",a);
        console.log("ID",a['$key']);
       // console.log("ID",a['userid']);
        this.getKeys(a['$key']);
        //this.getKeysPU(a['userid']);
       
      
      })
    })
    let s1 = this.getListServiceReport();
    s1.snapshotChanges().subscribe(data => {
      this.property1 = [];
      data.forEach(item => {
        let a1 = item.payload.toJSON();
        console.log("SATRT",a1);
        a1['$key'] = item.key;
        console.log("A",a1['$key']);
        //console.log("ID",a1['propid']);
        //console.log("ID",a1['userid']);
      //  console.log("ID",a1['report']);
        this.getKeysReport(a1['$key']);
       // this.getKeysPUR(a1['userid']);
       // this.pessRR.push(a1['report']);
      
      })
    })

    

  }
  openDialogReport(id:string)
  {
      console.log(id);
    this.crudService.SETReportPView = id;
      let dialogRed = this.dialog.open(DiaPropReportComponent);


  }
  getKeysReport(a:string)
    {
     let s= this.crudService.GetReportedProperty(a);
     s.snapshotChanges().subscribe(data => {
      this.property1 = [];
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        console.log("ASP",as1);
        as1['$key'] = item.key;
        this.pessR.push(as1 as Property);
      
      })
    })
    }
  getKeys(a:string)
    {
     let s= this.crudService.GetResponsesProperty(a);
     s.snapshotChanges().subscribe(data => {
      this.property = [];
      data.forEach(item => {
        let as = item.payload.toJSON();
        console.log("ASP",as);
        as['$key'] = item.key;
        this.pess.push(as as Property);
      
      })
    })
    }
    getKeysPU(a:string)
    {
     let s= this.crudService.GetResponsesPUsers(a);
     s.snapshotChanges().subscribe(data => {
      this.property = [];
      data.forEach(item => {
        let as = item.payload.toJSON();
        console.log("ASP",as);
        
        this.pessPU.push(as as Profile);
      
      })
    })
    }
    getKeysPUR(a:string)
    {
     let s= this.crudService.GetReportUsersP(a);
     s.snapshotChanges().subscribe(data => {
      this.property1 = [];
      data.forEach(item => {
        let as = item.payload.toJSON();
        console.log("ASP",as);
        
        this.pessUR.push(as as Profile);
      
      })
    })
    }
  // getKeysS(ad:string)
  //   {
  //    let s1= this.crudService.GetResponsesServices(ad);
  //    s1.snapshotChanges().subscribe(data => {
  //     this.property1 = [];
  //     data.forEach(item => {
  //       let as1 = item.payload.toJSON();
  //       console.log("Service",as1);
        
  //       this.pessS.push(as1 as Service);
      
  //     })
  //   })
  //   }
    // getKeysU(ad:string)
    // {
    //  let s1= this.crudService.GetResponsesUsers(ad);
    //  s1.snapshotChanges().subscribe(data => {
    //   this.property1 = [];
    //   data.forEach(item => {
    //     let as1 = item.payload.toJSON();
    //     console.log("User",as1);
        
    //     this.pessU.push(as1 as Profile);
      
    //   })
    // })
    // }

  // getListService()
  // {
  //   console.log("Service List Fetch");
  //    this.servRef = this.db.list('responses', ref => ref.child(firebase.auth().currentUser.uid).child('services'));
  //   //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
  //   console.log(this.servRef);
  //   return this.servRef;
  // }
  getlist()
  {
    console.log("Property LIST");
    // this.propRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2');
    this.propRef = this.db.list('responses', ref => ref.child(firebase.auth().currentUser.uid).child('properties'));
    return this.propRef;
  }
  getListServiceReport()
  {
    console.log("Property-Report List Fetch");
     this.servRef1 = this.db.list('report', ref => ref.child(firebase.auth().currentUser.uid).child('property'));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef1);
    return this.servRef1;
  }
  openDialog(key1:string) {
    
    
    this.crudService.SETPROPID = key1;
    let dialogRed = this.dialog.open(DialogViewResponsesComponent);
  }


}
