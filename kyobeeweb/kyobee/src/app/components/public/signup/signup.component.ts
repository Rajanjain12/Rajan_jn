import { Component, OnInit } from '@angular/core';
import { HttpService } from 'src/app/services/http.service';
import { DataService } from 'src/app/services/data.service';
import { SignupDTO } from 'src/app/DTO/SignupDTO';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit  {

  constructor(private httpService: HttpService, private dataService: DataService,private activatedRoute: ActivatedRoute) { }

  public subdomain: string;
  public loading = false;
  public errorMsg: string = "";
  public signupObjDTO : SignupDTO =new SignupDTO();

  ngOnInit() {
   
    this.subdomain = this.dataService.getData().subdomain;
  }
   phonePattern(e)
  {
    var x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
    e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
  }

  signup(invalid){
    if(invalid){
      return;
    }
    else
    {
      this.signupObjDTO.selectedPlan =this.activatedRoute.snapshot.params['planName'];
    }
  }
}
