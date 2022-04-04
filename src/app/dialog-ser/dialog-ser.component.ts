import { Component, OnInit ,Inject} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
import { DisplayProperty} from '../seller/display-property.service';
import { Profile } from '../seller/profile';
@Component({
  selector: 'app-dialog-ser',
  templateUrl: './dialog-ser.component.html',
  styleUrls: ['./dialog-ser.component.css']
})
export class DialogSerComponent implements OnInit {
  DisplayForm = new FormGroup({
    Images: new FormControl()
  })
  property01: Profile[];
  servRef1: AngularFireList<any>;
  pessUO: Profile[] = [];
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    public DisplayProp : DisplayProperty,
    public dialogRef: MatDialogRef<DialogSerComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }
   addRepoForm() {
    this.DisplayForm = this.fb.group({
      
      
    });
  }
  Cancel()
  {
    this.dialogRef.close();
  }
  getListServiceReport()
  {
    console.log("Service-Report List Fetch");
     this.servRef1 = this.db.list('user', ref => ref.orderByKey().equalTo(this.crudService.SETINTS));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef1);
    return this.servRef1;
  }
  
  ngOnInit() {
    
    let s = this.getListServiceReport();
  s.snapshotChanges().subscribe(data => {
    this.property01 = [];
    data.forEach(item => {
      let a = item.payload.toJSON();
     console.log(a);
     console.log(a['email']);
     console.log(a['name']);
     
      // a['$key'] = item.key;
      // console.log(a['name']);
      // console.log(a['email']);

      this.pessUO.push(a as Profile);
    })
  })
}

}
