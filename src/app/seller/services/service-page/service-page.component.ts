import { Component, OnInit, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as firebase from 'firebase';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-service-page',
  templateUrl: './service-page.component.html',
  
  styleUrls: ['./service-page.component.css']
})
export class ServicePageComponent implements OnInit {

  serviceForm: FormGroup;
  errorMessage: string = '';
  address: Object;
  establishmentAddress: Object;

  formattedAddress: string;
  formattedEstablishmentAddress: string;
Search: String;
  error_messages = {

    'location': [
      { type: 'required', message: ' Location is required.' },
      // { type: 'minlength', message:'Please Add Proper Location'}
    ],
    'serviceName': [
      { type: 'required', message: ' Please Select the Category.' },
    ],
    'price': [
      { type: 'required', message: 'Please enter Price is required.' },
    ],
    'contactNo': [
      { type: 'required', message: 'Please Elaborate.' },
      { type: 'minlength', message: 'Please Elaborate.' },
      
      { type: 'maxlength', message: 'Please Elaborate.' }
      ]
  
  }

  constructor(
    public authService: AuthService,
    public router: Router,
    private fb: FormBuilder,
    private http: HttpClient,
    private storage: AngularFirestore,
    public zone: NgZone
  ) { 
    this.createForm();
  }

  createForm() {
    this.serviceForm = this.fb.group({
      serviceName: ['',[Validators.required, Validators.minLength(3) ]],
      location: ['NULL',[Validators.required]],
      price: ['',Validators.required],
      Date:['',Validators.required],
      contactNo: ['',Validators.compose([
       
        Validators.required])],
       
      id:['',Validators.required]
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
  get f() { return this.serviceForm.controls; }
  onSubmit(value : {
    serviceName: any;
    location: any;
    price: any;
    contactNo: any;
  })
  
  {
    if(this.Search != "")
{
  value.location = this.Search;
}

    var userID = firebase.auth().currentUser.uid;
    const formData: any = new FormData();
    firebase.database().ref('services/').push({
      serviceName: value.serviceName,
      location: value.location,
      // location: value.location,
      price: value.price,
      contactNo: value.contactNo,
      Date: Date.now(),  
      id: userID
      
    });
    this.Search = "";
    alert("Service Added!");
  }

  clearForm() {
    this.serviceForm.reset();
    
  }

  ngOnInit() {
  }

}
