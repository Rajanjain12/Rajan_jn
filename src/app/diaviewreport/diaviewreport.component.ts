import { Component, OnInit,Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
import { DisplayProperty} from '../seller/display-property.service';
import { Profile } from '../seller/profile';
import {Report} from '../report';
import {Try} from '../try';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { combineAll } from 'rxjs/operators';
@Component({
  selector: 'app-diaviewreport',
  templateUrl: './diaviewreport.component.html',
  styleUrls: ['./diaviewreport.component.css']
})
export class DiaviewreportComponent implements OnInit {
  pessU : Profile[] = [];
  pessPU : Try[]=[];
  pessR: Report[]=[];
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    public DisplayProp : DisplayProperty,
    public dialogRef: MatDialogRef<DiaviewreportComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }
   Cancel()
   {
     this.dialogRef.close();
   }
   get  dataSeller() :string

   {
     console.log("get",this.crudService.SETReportView);
    return  this.crudService.SETReportView;
   }
   fuc()
   {

      this.pessR=[];
    // return new Promise((resolve, reject) => {
    let sx = this.db.list('report', ref => ref.child(firebase.auth().currentUser.uid).child('services').child(this.dataSeller));
 
    sx.snapshotChanges().subscribe(data => {
      //this.property01 = [];
      data.forEach(item => {
        let ax = item.payload.toJSON();
       console.log("FROM DIALOG OF RESPONSES",ax);
      //  console.log(ax['email']);
      //  console.log(ax['name']);
       
        // a['$key'] = item.key;
        // console.log(a['name']);
        // console.log(a['email']);
        this.pessR.push(ax['report'] as Report);
        this.getuser(ax['userid']);
        
       console.log(this.pessR)
      })
    })
    
  //     resolve("Success");
   
  // });
   
   }
   async delay(ms: number) {
    await new Promise(resolve => setTimeout(()=>resolve(), ms)).then(()=>this.combine());
}
  ngOnInit() {


    
      this.dataSeller;
      this.fuc();
      this.delay(1000);
    //   setTimeout(function () {
    //      console.log("STOE");
    // }, 500);
      //this.combine();
      
  
    
  }
  combine()
  {
    console.log("COMBINE");
    for(let i=0;i<this.pessR.length;i++)
    {
      var objectC = {...this.pessU[i],...{report: this.pessR[i]}};
      this.pessPU.push(objectC as Try);
      console.log("Update in PU",this.pessPU);

    }
  }
  getuser(id:string)
  {
    return new Promise((resolve, reject) => {
    let s= this.db.list('user',ref => ref.orderByKey().equalTo(id));
     this.pessU= [];
     s.snapshotChanges().subscribe(data => {
      
      data.forEach(item => {
        let as = item.payload.toJSON();
        
        console.log("AS",as);
        
        
        this.pessU.push(as as Profile);
      console.log(this.pessU);
      // var objectC = {...this.pessU[0],...{report: this.pessR[0]}};
      
      // this.pessPU.push(objectC as Try);
      // console.log();
      // console.log("Update in PU",this.pessPU);
      })
    })
    resolve();
   
  });
   
  
   
  }

 
  
}
  
  


