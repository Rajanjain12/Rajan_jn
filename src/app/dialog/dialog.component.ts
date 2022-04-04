import { Component, OnInit, Inject } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit {
mess:string;

  reportRef: AngularFireList<any>;
  
  reportForm = new FormGroup({
    reported: new FormControl()
  })
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    
    public dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }

   ngOnInit() {
     this.dataSeller;
     this.dataService;
   
  }
  addRepoForm() {
    this.reportForm = this.fb.group({
      reported: ['', Validators.required],
      
    });
  }
  get  dataSeller() :string

  {
    console.log("get",this.crudService.ReportSellerID);
   return  this.crudService.ReportSellerID;
  }
  get  dataService() :string

  {
  console.log("get",this.crudService.ReportSerID);
   return  this.crudService.ReportSerID;
  }
  get reported() {
   console.log(this.reportForm.get('reported'));
    return this.reportForm.get('reported').value;
  }
  GetReportS()
  {
    this.reportRef = this.db.list('report/');
    return this.reportRef;
  }
  reportProperty()
{
  console.log("Clear",this.reported);
  firebase.database().ref('report/').child(this.crudService.ReportSellerID).child("services").child(this.crudService.ReportSerID).push(

    {
      serviceid: this.crudService.ReportSerID,
      userid: firebase.auth().currentUser.uid,
      report: this.reported,
    }
);
this.dialogRef.close();
    alert("Property Reported");
}

}
