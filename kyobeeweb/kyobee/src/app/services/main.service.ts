import { Injectable, OnInit } from '@angular/core';
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class MainService{
  constructor(private httpService:HttpService) {}
  public userDTO=null;

  fetchUserDetails(){
    var params = {};
    var url = '/kyobee/rest/userDetails';

    this.httpService.getDataService(url, params).subscribe(data => {
      
      if (data.status == "SUCCESS") {
        this.userDTO = data.serviceResult;
        alert(JSON.stringify(this.userDTO));
				//put("USER_OBJ",JSON.stringify(data.serviceResult));
				//this.loadDataForPage();
      } else if (data.status == "FAILURE") {
        //this.errorMsg = "Error while fetching user details. Please login again or contact support";
        this.logout();
      }
    },
    error => {
      alert('Error while fetching user details. Please login again or contact support');
      //this.loading=false;
    });
  }

  /* loadDataForPage(){
    this.loadSeatingPref();
		this.loadMarketingPref();
  } */

  logout(){
    var postBody = {};
    var url = '/kyobee/web/rest/logout';
    this.httpService.postService(url, postBody).subscribe(data => {
      this.httpService.changeView("logout");
    },
    error => {
      alert('Session Timed Out');
      this.httpService.changeView("logout");
    });
  }
}
