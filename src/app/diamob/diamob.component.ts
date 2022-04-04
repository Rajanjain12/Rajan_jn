import { Component, OnInit, Inject } from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
@Component({
  selector: 'app-diamob',
  templateUrl: './diamob.component.html',
  styleUrls: ['./diamob.component.css']
})
export class DiamobComponent implements OnInit {
  reportRef: AngularFireList<any>;
  error_messages = {
    'reported': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
      { type: 'minlength', message: ' Please enter the Valid, It is required.' },
      { type: 'maxlength', message: ' Please enter the Valid, It is required.' },
    ],
  }
  reportForm = new FormGroup({
    reported: new FormControl()
  })
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    
    public dialogRef: MatDialogRef<DiamobComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }
   addRepoForm() {
    this.reportForm = this.fb.group({
      reported: ['', [Validators.required,Validators.maxLength(10),Validators.minLength(10)]],
      
    });
  }get reported() {
    console.log(this.reportForm.get('reported'));
     return this.reportForm.get('reported').value;
   }
  reportProperty()
  {
    
  console.log("Clear");
  firebase.database().ref('user/').child(this.crudService.Newuserid).update(

    {
      contactNo : this.reported,
      
    }
);
this.dialogRef.close();
    //alert("Property Reported");
  }
  get  dataUser() :string

  {
    
   return  this.crudService.Newuserid;
  }
  ngOnInit() {
    this.dataUser;
    
  }

}
