import { Component, OnInit } from '@angular/core';

import { AngularFireModule } from  '@angular/fire';
import { AuthService } from '../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore'
import { Router, Params } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { auth } from 'firebase';
import * as firebase from 'firebase';
import { ReactiveFormsModule } from '@angular/forms';
import { environment } from 'src/environments/environment';



@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {
  contactForm: FormGroup;
  errorMessage: string = '';


  constructor(
    public authService: AuthService,
    private router: Router,
    private fb: FormBuilder
    
  ) {
    this.createForm();
   }
   createForm() {
    this.contactForm = this.fb.group({
      
      name: ['', Validators.required],
      email: ['', Validators.required],
      message: ['', Validators.required],
      subject: ['',Validators.required]
    });
  }
  // tryContact(value)
  // {
  //     var message = value.message;
  //     var name = value.name;
  //     var email = value.email;
  //     var subject = value.subject;


  // }


//   onSubmit(value)  {
//     this.db.list('/messages').push({ name: value.name1, email: value.email, subject: value.subject, 
//     message: value.message});
// //Popup message
//     alert('Thank you for contacting us, your message has gone through!')
//    }
onSubmit(value) {
  
  firebase.database().ref('messages/').push({
    name: value.name,
    email: value.email, subject: value.subject,message: value.message
  });
}
// Clearing the form after submit
clearForm() {
      this.contactForm.reset();
     }


  ngOnInit() {
  }

}
