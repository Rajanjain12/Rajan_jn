import { Component, OnInit } from '@angular/core';
import { AddGuest } from 'src/app/core/models/add-guest.model';
import { AuthService } from 'src/app/core/services/auth.service';


@Component({
  selector: 'app-add-guest',
  templateUrl: './add-guest.component.html',
  styleUrls: ['./add-guest.component.scss']
})
export class AddGuestComponent implements OnInit {

  AddGuest: AddGuest = new AddGuest();
   
  

  constructor(private authService: AuthService) { }

 // seatingpref: any = [];


  counter(i: number) {
    return new Array(i);
  }
  
  ngOnInit() {
    var user =  this.authService.getUser();
   user.seatingpref.map((obj) => {
    obj.selected = false;
})
  /* for (var i = 0; i < user.seatingpref.length; i++) {
    user.seatingpref[i] = Object.assign(user.seatingpref[i], {
      selected: false, 
  });
  }*/
    console.log("seatingpref=="+JSON.stringify(user.seatingpref)); 
  }

  addGuest(){
    //console.log("jaaaam"); 
    var user =  this.authService.getUser();
   
    console.log("seatingpref=="+JSON.stringify(user.seatingpref)); 
  }
}
