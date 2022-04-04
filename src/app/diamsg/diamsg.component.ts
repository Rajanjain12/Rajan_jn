
import { Component, OnInit ,Inject} from '@angular/core';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';

@Component({
  selector: 'app-diamsg',
  templateUrl: './diamsg.component.html',
  styleUrls: ['./diamsg.component.css']
})
export class DiamsgComponent implements OnInit {
  reportForm = new FormGroup({
    reported: new FormControl()
  })
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    
    public dialogRef: MatDialogRef<DiamsgComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }
   addRepoForm() {
    this.reportForm = this.fb.group({
      
      
    });

}
cancel()
{
  
    this.dialogRef.close();
}
  ngOnInit() {
  }

}
