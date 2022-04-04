import { Component, OnInit ,Inject} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
import { DiamsgComponent } from '../diamsg/diamsg.component';
@Component({
  selector: 'app-dialogin',
  templateUrl: './dialogin.component.html',
  styleUrls: ['./dialogin.component.css']
})
export class DialoginComponent implements OnInit {
  reportForm = new FormGroup({
    reported: new FormControl()
  })
  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<DialoginComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }
   
  ngOnInit() {
    this.dataUser;
  }
  addRepoForm() {
    this.reportForm = this.fb.group({
      
      
    });

}
cancel()
{
  
    this.dialogRef.close();
}
get  dataUser() :string

  {
    
   return  this.crudService.loginid;
  }
reportProperty()
{
  console.log("Resend the Verification Link");
  console.log(this.crudService.loginid);
  
this.crudService.loginid.sendEmailVerification().then(() => {
  this.dialogRef.close();
  let dialogRed1 = this.dialog.open(DiamsgComponent);
 //alert("Check Your Inbox Verification Link is Send Again")
 
}).catch(function(error) {
  window.alert(error)
});


}


}
