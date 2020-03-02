import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { GuestService } from 'src/app/core/services/guest.service';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  constructor(private authService: AuthService, private guestService: GuestService) {}
  orgid: string;

  ngOnInit() {}

  onClick() {
    var user = this.authService.getUser();
    console.log('user==' + JSON.stringify(user.organizationID));
    //window.alert("user=="+JSON.stringify(user.organizationID));

    this.orgid = user.organizationID;
    var params = new HttpParams().set('orgid', this.orgid);
    this.guestService.resetUser(params).subscribe((res: any) => {
      if (res.success == 1) {
        //window.alert('');
        console.log('user==' + JSON.stringify(res));
      }
    });
  }
}
