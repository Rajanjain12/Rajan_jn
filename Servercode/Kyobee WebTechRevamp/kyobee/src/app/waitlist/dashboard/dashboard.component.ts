import { Component, OnInit } from '@angular/core';
import { GuestService } from 'src/app/core/services/guest.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor(private guestService: GuestService, private authService: AuthService) { }
  orgId;
  pageNo;
  pageSize;
  searchText;

  dashboardIlluImageSrc="../../../assets/images/dashboard-illu.png";
  notPresentImg="../../../assets/images/not-present.png";
  noteIconImg="../../../assets/images/note-icon.png";
  deleteIconImg="../../../assets/images/delete-icon.png";
  msgIconImg="../../../assets/images/msg-icon.png";

  ngOnInit() {
    var user =  this.authService.getUser();
    this.orgId = user.organizationID;
    this.pageNo = 1;
    this.pageSize = 10;
    this.searchText = null;

    var params = new HttpParams()
      .set('orgId', "2")
      .set('pageNo', this.pageNo)
      .set('pageSize', this.pageSize)
      .set('searchText', this.searchText); 
    
    this.guestService.fetchGuestList(params).subscribe((res: any)=>{
      if(res.success == 1){
        console.log("user=="+JSON.stringify(res));
      }
    })
  }
}
