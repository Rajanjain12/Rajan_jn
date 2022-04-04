import { Component, OnInit, Inject } from '@angular/core';


import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CrudService } from '../crud.service';
import { AngularFireList, AngularFireDatabase } from '@angular/fire/database';
import * as firebase from 'firebase';
import { DisplayProperty} from '../seller/display-property.service';
@Component({
  selector: 'app-dialog-add',
  templateUrl: './dialog-add.component.html',
  styleUrls: ['./dialog-add.component.css']
})
export class DialogAddComponent implements OnInit {
  selectedFile :Array<File> = [];
  x : any;
  AddForm = new FormGroup({
    Images: new FormControl()
  })


  constructor(
    public crudService: CrudService,
    public fb: FormBuilder,private db: AngularFireDatabase,
    public DisplayProp : DisplayProperty,
    public dialogRef: MatDialogRef<DialogAddComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
   }


  ngOnInit() {
  }
  addRepoForm() {
    this.AddForm = this.fb.group({
      Images: ['', Validators.required],
      
    });
  }
  AddProperty(value)
  {
    console.log("Add Property");
    const files: Array<File> = this.selectedFile;
console.log(this.DisplayProp.Addprop);
var s = this.DisplayProp.Addprop;
   firebase.database().ref('property/').child(s).push({
     
       Images: value.Images,
     
   });
   let returnVal;
        // var c = this.x;
        for(let i =0; i < files.length; i++){
        var uploadTask = firebase.storage().ref('PropImages/' + "").child(s).child(Date()).child(files[i]['name']).put(files[i]).then(function (snapshot) {
    
          returnVal = snapshot.ref.getDownloadURL().then(function (downloadURL) {
            console.log('File available at', downloadURL);
            var userId = firebase.auth().currentUser.uid;
            firebase.database().ref('property/').child(s).child("Images").push(
              { imageLink: downloadURL }
            );
            //  firebase.database().ref('properties/').child(userId).child(s1.key).({
            //     propId:
            //  });
          });
        });
      }

      this.dialogRef.close();
      alert("CLOSE");
  }
  onfileselect(event) {
    this.x = event.length;
    
    this.selectedFile = <Array<File>>event.target.files;
    
  }
  Cancel()
  {
    this.dialogRef.close();
    alert("You Pressed Cancel!");
  }
}
