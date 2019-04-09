import { Component, OnInit } from '@angular/core';
import { DataService } from 'src/app/services/data.service';
import { HttpService } from 'src/app/services/http.service';
import imgLinks from '../../../../assets/data/imgLinks.json';

@Component({
  selector: 'app-forgotpwd',
  templateUrl: './forgotpwd.component.html',
  styleUrls: ['./forgotpwd.component.scss']
})
export class ForgotpwdComponent implements OnInit {

  constructor(private dataService: DataService, private httpService: HttpService) { }

  public forgotPwdPageImageSrc: string = imgLinks.forgotPwdPageImg;
  public correctSignImageSrc: string = imgLinks.correctSignImg;
  public loading: boolean = false;
  public email: string;
	public successMsg: string = null;
  public errorMsg: string = null;
  public mailSent: boolean = false;

  ngOnInit() {
  }

  forgotPwd(invalid){
    alert("email is: "+this.email);
    if(invalid){
      alert("invalid");
      return;
    }

    this.loading = true;
    var postBody = {};
    var url = '/kyobee/rest/forgotPwd/V2?email=' + this.email;

    this.httpService.getDataService(url, '').subscribe(data=>{

        if(data.status == "SUCCESS"){
          this.mailSent = true;
          this.successMsg = data.serviceResult;
          this.loading = false;
        }else if(data.status == "FAILURE"){
          this.errorMsg = data.serviceResult;
          this.loading = false;
        }
    },
    error => {
      alert('Session Timed Out');
        this.loading=false;
    })
  }

  changeView(path: string){
    this.httpService.changeView(path);
  }
}
