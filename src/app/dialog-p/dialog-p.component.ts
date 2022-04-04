import { Component, OnInit, Inject } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';

@Component({
  selector: 'app-dialog-p',
  templateUrl: './dialog-p.component.html',
  styleUrls: ['./dialog-p.component.css']
})
export class DialogPComponent implements OnInit {
  mess:string;
  reportRef: AngularFireList<any>;
  reportForm = new FormGroup({
    reported: new FormControl()
  })
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    
    public dialogRef: MatDialogRef<DialogPComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }

  ngOnInit() {
    this.dataSeller;
    this.dataProp;
  }
  get  dataSeller() :string

  {
    console.log("get",this.crudService.ReportSellPID);
   return  this.crudService.ReportSellPID;
  }
  get  dataProp() :string

  {
    console.log("get",this.crudService.ReportPropID);
   return  this.crudService.ReportPropID;
  }
  get reported() {
    console.log(this.reportForm.get('reported'));
     return this.reportForm.get('reported').value;
   }
   reportProperty()
{
  console.log("Clear",this.reported);
  firebase.database().ref('report/').child(this.crudService.ReportSellPID).child("property").child(this.crudService.ReportPropID).push(

    {
      propid: this.crudService.ReportPropID,
      userid: firebase.auth().currentUser.uid,
      report: this.reported,
    }
);
this.dialogRef.close();
    alert("Property Reported");
}


}
