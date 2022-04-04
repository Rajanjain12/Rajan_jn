import { Component, OnInit } from '@angular/core';
import { CrudService } from '../../crud.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth.service';
import * as firebase from 'firebase';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userId:any;
  emaile :any;
  emailf: any;
  profileForm: FormGroup;
  tempForm: any;
id: any;
  constructor(
    private crudApi: CrudService,
    private fb: FormBuilder,
    private actRoute: ActivatedRoute,
    private route: Router,
    public authService: AuthService
  ) {
    this.id = firebase.auth().currentUser.uid;
   }
   ngAfterViewInit(){
     this.getPrevEmail();
   }
  ngOnInit() {

    this.updateProfileData();
 
    // const id = this.actRoute.snapshot.paramMap.get('ids');
    console.log(this.id);
     this.crudApi.GetProfile(this.id).valueChanges().subscribe(data => {
       this.profileForm.setValue(data);
      // this.emaile = this.profileForm.get('email');
      // console.log(this.emaile);

    //this.getPrevEmail();      
    this.tempForm = this.profileForm.value;

      })
    }
  getPrevEmail()
  {
    var x = firebase.database().ref('user/').orderByKey().equalTo(firebase.auth().currentUser.uid);
      x.on('value',function(snap){
        var y = snap.val();
        var K = Object.keys(y);
        
        var f = K[0];
        

        console.log(y[f].email);
        console.log("PREV");
       // this.emaile = y[f].email;
        //console.log(y);
  
      })
  }
  get contactNo() {
    
    return this.profileForm.get('contactNo');
  }

  get name() {
    
    return this.profileForm.get('name');
  }

  get email() {
    
    return this.profileForm.get('email');
  }

  // get features() {
  //   return this.editForm.get('features');
  // }
  updateProfileData() {
    this.profileForm = this.fb.group({
        
      name: ['', Validators.required],
      email: ['',Validators.required],
      contactNo: ['',Validators.required],
      //Date:['',Validators.required]
    })
  }
  updateProfile()
  {
    
    // var x = firebase.database().ref('user/').orderByKey().equalTo(firebase.auth().currentUser.uid);
    // x.on('value',function(snap){
    //   var y = snap.val();
    //   var K = Object.keys(y);
    //   var f = K[0];
    //    console.log(y[f].email);
    //   // console.log(this.emailf);
    //   this.emailf = y[f].email;
        
    //   console.log("F");

    // })
      this.crudApi.UpdateProfile(this.profileForm.value);
            alert("Update Successfull");
            this.route.navigate(['/profile']);
    
  }

}  

