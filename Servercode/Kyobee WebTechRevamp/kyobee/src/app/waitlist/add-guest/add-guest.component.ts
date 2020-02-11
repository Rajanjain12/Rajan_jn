import { Component, OnInit } from '@angular/core';
import { AddGuest } from 'src/app/core/models/add-guest.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';


@Component({
  selector: 'app-add-guest',
  templateUrl: './add-guest.component.html',
  styleUrls: ['./add-guest.component.scss']
})
export class AddGuestComponent implements OnInit {

  addGuest : AddGuest = new AddGuest();
  user : User = new User();
  errorMessage : string;
  public sum : number;
  
  constructor(private authService: AuthService) { }

  counter(i: number) {
    return  Array(i);
  }
  
  ngOnInit() {
  this.user =  this.authService.getUser();
   this.user.seatingpref.map((obj) => {
    obj.selected = false;
  })
   this.user.marketingPref.map((obj) => {
    obj.selected = false;
  })
    console.log("seatingpref=="+JSON.stringify(this.user.seatingpref)); 
    console.log("marketingPref=="+JSON.stringify(this.user.marketingPref));
  }

  get resultSeating() {
    return  this.user.seatingpref.filter(seating => seating.selected);
  }

  get resultMarketing() {
    return  this.user.marketingPref.filter(marketing => marketing.selected);
  }

  validate(invalid){
    if(invalid){
      this.errorMessage = "Please enter proper values " 
      return
    }
    console.log("jaaaam"); 
    console.log("seatingpref=="+JSON.stringify(this.resultSeating)); 
    console.log("marketingPref=="+JSON.stringify(this.resultMarketing)); 
   this.sum = +this.addGuest.adult + +this.addGuest.children;
    if(this.sum > this.user.maxParty){
      this.errorMessage = "More than " +this.user.maxParty+ " people are not allowed";
      return
    } else {
    console.log(this.sum);
    }
   // this.errorMessage = 'More than "+this.user.maxParty+" people are not allowed'
  }   
}
/* for (var i = 0; i < user.seatingpref.length; i++) {
    user.seatingpref[i] = Object.assign(user.seatingpref[i], {
      selected: false, 
  });
  }*/