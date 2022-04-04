import { Component, OnInit, NgZone } from '@angular/core';
import{ AutocompleteComponent } from '../../google-places.component';
import {HttpClient } from '@angular/common/http';
import { AngularFireModule } from  '@angular/fire';
import { AuthService } from '../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { auth } from 'firebase';
import * as firebase from 'firebase';
import { ReactiveFormsModule } from '@angular/forms';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-post-property-page',
  templateUrl: './post-property-page.component.html',
  styleUrls: ['./post-property-page.component.css']
})
export class PostPropertyPageComponent implements OnInit {
  propertyForm: FormGroup;
  errorMessage: string = '';
  selectedFile :Array<File> = [];
  x : any;
  acd : any;
  s1 : any; 
  address: Object;
  establishmentAddress: Object;

  formattedAddress: string;
  formattedEstablishmentAddress: string;
Search: String;
  error_messages = {
    'type11': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'type4': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'type': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'location': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'area': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'price': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'BHK': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
      { type: 'min', message: 'Error In Your Data' },
      { type: 'max', message: 'There cannot be this much bedrooms.' }
    ],
    'features': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
      { type: 'minlength', message: 'please elaborate the Features' }
    ],
    'Images': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'contact': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
      { type: 'minlength', message: 'Please Elaborate.' },
      { type: 'maxlength', message: 'Please Elaborate.' }
    ],
  }      


  constructor(public authService: AuthService,
    private router: Router,
    private auto : AutocompleteComponent,
    private fb: FormBuilder,
    private http: HttpClient,
    private storage: AngularFirestore,
    private zone: NgZone) {
      this.createForm();
    }
     
     createForm() {
       
      this.propertyForm = this.fb.group({
        
        
        type11: ['', Validators.required],
        type4: ['', Validators.required],
        
        
        type: ['', Validators.required],
        location: ['', Validators.required],
        area: ['', Validators.required],
        price: ['',Validators.required],
      //  BHK: ['',Validators.required],
      BHK:['',[Validators.required,Validators.min(0),Validators.max(9)]],
        contact: ['',[Validators.required,Validators.maxLength(80),Validators.minLength(10)]],
        features: ['',[Validators.required,Validators.minLength(5)]],
        Images: ['',Validators.required],
        imageURL: ['',Validators.required],
        Date:['',Validators.required],
        verify:['',Validators.required],
      });
    }
    getAddress(place: object) {
      this.address = place['formatted_address'];
      //this.phone = this.getPhone(place);
      this.formattedAddress = place['formatted_address'];
      this.zone.run(() => this.formattedAddress = place['formatted_address']);
      this.zone.run(()=> console.log(this.formattedAddress));
      this.zone.run(()=> this.Search = this.formattedAddress);
      
      
    }
    get f() { return this.propertyForm.controls; }
    onSubmit(value: { type: any;type4: any; location: any; area: any; price: any; features: any; contact: any; BHK: any; Images: any; type11:any; }) {
      var userId = firebase.auth().currentUser.uid;
      const formData: any = new FormData();
      const files: Array<File> = this.selectedFile;
      console.log(files);
      console.log("BHK",value.BHK);
      if(value.type =="commercial")
      {
        value.BHK = 0;
        
      }
if(this.Search != "")
{
  value.location = this.Search;
}
let date: Date = new Date(); 

            var s1=firebase.database().ref('property/').push({
             type: value.type,
              type11: value.type11,
              location: value.location,
              area: value.area,
              price: value.price,
              features:value.features,
              contact: value.contact,
              id: userId,
              BHK : value.BHK,
              Images: value.Images,
              Date: Date.now(), 
              pdate:date.toDateString(), 
              unit: value.type4,
              verify:"true",
          });
     
      console.log(s1.key);
      // this.acd = Date.now();
    
      let returnVal;
        // var c = this.x;
        for(let i =0; i < files.length; i++){
        var uploadTask = firebase.storage().ref('PropImages/' + "").child(s1.key).child(Date()).child(files[i]['name']).put(files[i]).then(function (snapshot) {
    
          returnVal = snapshot.ref.getDownloadURL().then(function (downloadURL) {
            console.log('File available at', downloadURL);
            var userId = firebase.auth().currentUser.uid;
            firebase.database().ref('property/').child(s1.key).child('Images').push(
              { imageLink: downloadURL }
            );
            //  firebase.database().ref('properties/').child(userId).child(s1.key).({
            //     propId:
            //  });
          });
        });
      }
      
this.Search = "";
alert("Property Added");
  
    }




      
      
X()
{
  console.log(this.s1);
}
  onfileselect(event) {
    this.x = event.length;
    
    this.selectedFile = <Array<File>>event.target.files;
    
  }
  clearForm() {
    this.propertyForm.reset();
    console.log("AUTO-CALL-AUTO AUTO");
    //this.zone.run(()=>this.auto.searchControl.reset());
    this.auto.autocompleteInput="";
  }

  ngOnInit() {
  }

}
