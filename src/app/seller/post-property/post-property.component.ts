import { Component, OnInit } from '@angular/core';



import { AngularFireModule } from  '@angular/fire';
import { AuthService } from '../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore'
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { auth } from 'firebase';
import * as firebase from 'firebase';
import { ReactiveFormsModule } from '@angular/forms';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-post-property',
  templateUrl: './post-property.component.html',
  styleUrls: ['./post-property.component.css']
})
export class PostPropertyComponent implements OnInit {
  propertyForm: FormGroup;
  errorMessage: string = '';

  constructor( public authService: AuthService,
    private router: Router,
    private fb: FormBuilder
   ) {

   
    this.createForm();
   }
   createForm() {
    this.propertyForm = this.fb.group({
      
      name: ['', Validators.required],
      email: ['', Validators.required],
      message: ['', Validators.required],
      subject: ['',Validators.required]
    });
  }

  onSubmit(value) {
    var userId = firebase.auth().currentUser.uid;
    firebase.database().ref('property/' + userId).set({
      basic: value.basic,
      location: value.location, details: value.details,price: value.price,
      features:value.features,contact: value.contact
    });
  }
  clearForm() {
    this.propertyForm.reset();
   }


  ngOnInit() {
  }

}
