import { Component, OnInit,NgZone } from '@angular/core';
import { CrudService } from '../../../crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth.service';
import * as firebase from 'firebase';

@Component({
  selector: 'app-editaservice',
  templateUrl: './editaservice.component.html',
  styleUrls: ['./editaservice.component.css']
})
export class EditaserviceComponent implements OnInit {
  editForm: FormGroup;
  address: Object;
  establishmentAddress: Object;

  formattedAddress: string;
  base : string;
  formattedEstablishmentAddress: string;
Search: String;
  error_messages = {

    
    'serviceName': [
      { type: 'required', message: ' Please Select the Category.' },
    ],
    'price': [
      { type: 'required', message: 'Please enter Price, It is required.' },
    ],

    'contactNo': [
       { type: 'required', message: 'Please fill Details.' },
       { type: 'minlength', message: 'Please Elaborate.' },
       { type: 'maxlength', message: 'Please Reduce Details.' }
      ]
  
  }
  constructor(
    private crudApi: CrudService,
    private fb: FormBuilder,
    private actRoute: ActivatedRoute,
    private route: Router,
    public authService: AuthService,
    public zone: NgZone
  ) { }

  ngOnInit() {
    this.updateServiceData();
    const id = this.actRoute.snapshot.paramMap.get('ids');
    console.log(id);
    this.crudApi.GetService(id).valueChanges().subscribe(data => {
      this.editForm.setValue(data);
      console.log(this.editForm.value.location);
       this.formattedAddress = this.editForm.value.location
      this.getAddress(this.editForm.value.location);
      //console.log(this.editForm.get('location').value);
      
      //this.formattedAddress = this.editForm.get('location').value;
      //this.SetAddress(this.formattedAddress);
      
      
    })
    
  }

  
  getAddress(place: object) {
    this.address = place['formatted_address'];
    //this.phone = this.getPhone(place);
    this.formattedAddress = place['formatted_address'];
    this.zone.run(() => this.formattedAddress = place['formatted_address']);
    this.zone.run(()=> console.log(this.formattedAddress));
    this.zone.run(()=> this.Search = this.formattedAddress);
    
  }

  get f() { return this.editForm.controls; }
  
  get location() {

    
    return this.editForm.get('location');
  }

  get contactNo() {
    
    return this.editForm.get('contactNo');
  }
  
  get price() {
    
    return this.editForm.get('price');
  }
  
  get serviceName() {
    
    return this.editForm.get('serviceName');
  }
 
  updateServiceData() {
    this.editForm = this.fb.group({
        
      serviceName: ['',[Validators.required, Validators.minLength(3) ]],
      location: ['',[Validators.required, Validators.minLength(3) ]],
      price: ['',Validators.required],
      contactNo: ['',Validators.compose([
       
        Validators.required, Validators.minLength(5)])],
       
      
      Date:['',Validators.required],
      id: ['',Validators.required],
    })
  }
 

  updateService() {
    this.crudApi.getAddress(this.Search);
    this.crudApi.UpdateServices(this.editForm.value);
    alert("Update Successfull");
    this.route.navigate(['/edit-services']);
  }

}
