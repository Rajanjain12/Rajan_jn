import { Component, OnInit } from '@angular/core';
import { HttpService } from 'src/app/services/http.service';
import { DataService } from 'src/app/services/data.service';
import { signupDTO } from 'src/app/DTO/signupDTO';
import { SignupPlanAndPricingComponent } from '../signup-plan-and-pricing/signup-plan-and-pricing.component';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit  {

  constructor(private httpService: HttpService, private dataService: DataService) { }

  public subdomain: string;
  public loading = false;
  public errorMsg: string = "";
  public signupDTO : signupDTO;

  ngOnInit() {
    this.subdomain = this.dataService.getData().subdomain;
  }
   phonePattern(e)
  {
    var x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
    e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
  }
}
