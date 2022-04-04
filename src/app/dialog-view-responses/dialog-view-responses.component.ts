import { Component, OnInit,Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
import { DisplayProperty} from '../seller/display-property.service';
import { Profile } from '../seller/profile';
@Component({
  selector: 'app-dialog-view-responses',
  templateUrl: './dialog-view-responses.component.html',
  styleUrls: ['./dialog-view-responses.component.css']
})
export class DialogViewResponsesComponent implements OnInit {
  property01: Profile[];
  pessPU : Profile[] = [];
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    public DisplayProp : DisplayProperty,
    public dialogRef: MatDialogRef<DialogViewResponsesComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }
   Cancel()
   {
     this.dialogRef.close();
   }

  ngOnInit() {
    this.dataSeller;
    let sx = this.db.list('responses', ref => ref.child(firebase.auth().currentUser.uid).child('properties').child(this.dataSeller));
    sx.snapshotChanges().subscribe(data => {
      this.property01 = [];
      data.forEach(item => {
        let ax = item.payload.toJSON();
       console.log("FROM DIALOG OF RESPONSES",ax['userid']);
      //  console.log(ax['email']);
      //  console.log(ax['name']);
       
        // a['$key'] = item.key;
        // console.log(a['name']);
        // console.log(a['email']);
        this.getuser(ax['userid']);
       //this.pessPU.push(ax['userid'] as Profile);
      })
    })
  }
  getuser(id:string)
  {
    let s= this.db.list('user',ref => ref.orderByKey().equalTo(id));
     this.pessPU= [];
     s.snapshotChanges().subscribe(data => {
      
      data.forEach(item => {
        let as = item.payload.toJSON();
        
        console.log("AS",as);
        as['$key'] = item.key;
        console.log(as['$key']);
        this.pessPU.push(as as Profile);
      
      })
    })
  }
  
  get  dataSeller() :string

  {
    console.log("get",this.crudService.SETPROPID);
   return  this.crudService.SETPROPID;
  }

}
