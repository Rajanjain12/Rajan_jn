import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/core/models/user.model';



@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
  
})
export class SignupComponent implements OnInit {
  password;
  confirmPassword;
  user: User = new User();

  constructor() { }

  ngOnInit() {
    
  }
}
