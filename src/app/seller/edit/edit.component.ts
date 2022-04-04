import { Component, OnInit } from '@angular/core';
import { CrudService } from '../../crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth.service';
import * as firebase from 'firebase';
@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
userId:any;
  editForm: FormGroup;
  error_messages = {
    'type11': [
      { type: 'required', message: ' Please enter the Type, It is required.' },
    ],
    'unit': [
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
  constructor(
    private crudApi: CrudService,
    private fb: FormBuilder,
    private actRoute: ActivatedRoute,
    private route: Router,
    public authService: AuthService
  ) { }


  ngOnInit() {
    this.updatePropertyData();
    //this.userId = firebase.auth().currentUser.uid;
    const id = this.actRoute.snapshot.paramMap.get('ids');
    console.log(id);
   
    this.crudApi.GetProperty(id).valueChanges().subscribe(data => {
      this.editForm.setValue(data);
      
    })
  }

  get location() {
    return this.editForm.get('location');
  }

  get BHK() {
    return this.editForm.get('BHK');
  }

  get price() {
    return this.editForm.get('price');
  }

  get features() {
    return this.editForm.get('features');
  }

  get contact() {
    return this.editForm.get('contact');
  }
  get type11() {
    return this.editForm.get('type11');
  }
  get type() {
    return this.editForm.get('type');
  }
  get unit() {
    return this.editForm.get('type4');
  }
  get area() {
    return this.editForm.get('area');
  }
  get pdate() {
    return this.editForm.get('pdate');
  }
  get verify() {
    return this.editForm.get('verify');
  }
  updatePropertyData() {
    this.editForm = this.fb.group({
        
      
      type: ['', Validators.required],
      type11: ['', Validators.required],
      unit: ['', Validators.required],
      id: ['', Validators.required],
      //type1: ['', Validators.required],
      location: ['', Validators.required],
      area: ['', Validators.required],
      price: ['',Validators.required  ],
      BHK: ['',[Validators.required,Validators.min(1),Validators.max(10)]],
      contact: ['',[Validators.required,Validators.maxLength(10),Validators.minLength(10)]],
      features: ['',[Validators.required,Validators.minLength(5)]],
       Images: ['',Validators.required],
      //imageURL: ['',Validators.required],
      Date:['',Validators.required],
      pdate:['',Validators.required],
      verify:['',Validators.required]
    })
  }

  updateProperty() {
    this.crudApi.UpdateProperty(this.editForm.value);
    alert("Update Successfull");
    this.route.navigate(['/become-seller']);
  }

}
