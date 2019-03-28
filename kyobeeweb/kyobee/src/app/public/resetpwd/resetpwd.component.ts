import { Component, OnInit } from '@angular/core';
import {imgLinks} from '../../app.component';
import { DataService } from 'src/app/services/data.service';
import { HttpService } from 'src/app/services/http.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-resetpwd',
  templateUrl: './resetpwd.component.html',
  styleUrls: ['./resetpwd.component.scss']
})
export class ResetpwdComponent implements OnInit {

  constructor(private dataService: DataService, private httpService: HttpService, private route: ActivatedRoute) { }

  public resetPwdPageImageSrc: string = imgLinks.resetPwdPageImg;
  public successMsg: string;
  public errorMsg: string;
  public loading: boolean = false;
  public validurl: boolean = false;
  public message: string;
  public userId = null;
  public authcode = null;
  public newPwd: string = null;
  public newConfPwd: string = null;

  ngOnInit() {
    this.validateURL();
  }

  validateURL(){
    this.userId = (this.route.snapshot.paramMap.get("userId") != null && this.route.snapshot.paramMap.get("userId") != 'undefined') ? this.route.snapshot.paramMap.get("userId") : null;
    this.authcode = (this.route.snapshot.paramMap.get("authcode") != null && this.route.snapshot.paramMap.get("authcode") != 'undefined') ? this.route.snapshot.paramMap.get("authcode") : null;
    if(this.userId == null || this.authcode == null){
      this.validurl = false;
    }else{
      this.validateResetPwdURL();
    }
  }

  validateResetPwdURL = function() {
    this.loading = true;
    var postBody = {};
    
    var url = '/kyobee/rest/validateResetPwdurl?userId='+ this.userId+'&authcode='+ this.authcode;
    
    this.httpService.getDataService(url, '').subscribe(data => {
      if(data.status == "SUCCESS"){
        this.validurl = true;
      }else if(data.status == "FAILURE"){
        this.validurl = false;
        this.message = data.serviceResult;
      }
      this.loading = false;
    },
    error => {
      this.validurl = false;
      this.loading = false;
      this.message = "Error occured while validating url. Please contact support or try again later";
    })
  };

  resetPassword(invalid){
    this.successMsg = null;
    this.errorMsg = null;

    if(invalid){
      return;
    }
    if(this.newPwd !== this.newConfPwd){
      this.errorMsg = "Password & Confirm Passord should match";
      return;
    }
    this.loading = true;

    var postBody = {
      "userId" : this.userId,
      "password":this.newPwd
    }
    var params = {};
    var url = "/kyobee/rest/resetPassword";

    this.httpService.postService(url, postBody).subscribe(data => {
      if(data.status == "SUCCESS"){
        this.successMsg = data.serviceResult;
        this.changeView('public/resetpwdThanks');
      }else if(data.status == "FAILURE"){
        this.errorMsg = data.serviceResult;
      }
      this.loading = false;
    },
    error => {
      this.loading = false;
      this.errorMsg = "Error occured while password reset. Please contact support or try again later";
    })
  }

  changeView(path: string){
    this.dataService.changeView(path);
  }
}
