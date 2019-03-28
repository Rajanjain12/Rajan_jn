import { Component, OnInit } from '@angular/core';
import { imgLinks } from 'src/app/app.component';
import { HttpService } from 'src/app/services/http.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor(private httpService:HttpService) { }

  public dashboardIlluImageSrc:string = imgLinks.dashboardIlluImg;
  public noteIconImg:string = imgLinks.notPresentImg;
  public deleteIconImg:string = imgLinks.deleteIconImg;
  public msgIconImg:string = imgLinks.msgIconImg;
  public notPresentImg:string = imgLinks.notPresentImg;
  public loading:boolean=true;
  public errorMsg=null;
  public appKey=null;
  public privateKey=null;
  public channel=null;
  ngOnInit() {
    this.loadInfo();
  }

  loadInfo(){
      this.loading = true;
      this.errorMsg = null;
      var params={};
      var url = '/kyobee/web/rest/waitlistRestAction/pusgerinformation';

      this.httpService.getDataService
      this.httpService.getDataService(url, params).subscribe(data => {
        alert(JSON.stringify(data));
        if (data.status == "SUCCESS") {
          this.appKey = data.serviceResult.REALTIME_APPLICATION_KEY;
          this.privateKey = data.serviceResult.REALTIME_PRIVATE_KEY;
          this.channel = data.serviceResult.pusherChannelEnv;
          this.loadFactory();
          this.loading=false;
        } else if (data.status == "FAILURE") {
          this.errorMsg = "Error while fetching user details. Please login again or contact support";
          this.loading=false;
        }
      },
      error => {
        this.errorMsg="Error while fetching user details. Please login again or contact support";
        this.loading=false;
      });
  }

  loadFactory(){

  }
}
