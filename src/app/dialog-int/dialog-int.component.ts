import { Component, OnInit ,Inject} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
import { DisplayProperty} from '../seller/display-property.service';
import { Profile } from '../seller/profile';
@Component({
  selector: 'app-dialog-int',
  templateUrl: './dialog-int.component.html',
  styleUrls: ['./dialog-int.component.css']
})
export class DialogIntComponent implements OnInit {
  DisplayForm = new FormGroup({
    Images: new FormControl()
  })
  property01: Profile[];
  servRef1: AngularFireList<any>;
  pessUE: Profile[] = [];
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    public DisplayProp : DisplayProperty,
    public dialogRef: MatDialogRef<DialogIntComponent>,
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
  getDetails()
  {
    var x = this.crudService.SETINT;
    
  }
  getListServiceReport()
  {
    console.log("Service-Report List Fetch");
     this.servRef1 = this.db.list('user', ref => ref.orderByKey().equalTo(this.crudService.SETINT));
    //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
    console.log(this.servRef1);
    return this.servRef1;
  }


  ngOnInit() {
    
      let sx = this.getListServiceReport();
    sx.snapshotChanges().subscribe(data => {
      this.property01 = [];
      data.forEach(item => {
        let ax = item.payload.toJSON();
       console.log(ax);
       console.log(ax['email']);
       console.log(ax['name']);
       
        // a['$key'] = item.key;
        // console.log(a['name']);
        // console.log(a['email']);

        this.pessUE.push(ax as Profile);
      })
    })
  }

}
