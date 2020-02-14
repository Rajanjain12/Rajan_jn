import { Component, OnInit } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { GuestService } from 'src/app/core/services/guest.service';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {

  constructor(private guestService: GuestService) { }
  statusOptions = ["All", "Not Present", "Incomplete"];
  selectedStatus: string;
  orgId;
  pageNo;
  pageSize;
  searchText;
  guestDTOList:Array<GuestDTO>;
  
  ngOnInit() {
    this.selectedStatus = this.statusOptions[0];
    this.orgId = 2;
    this.pageNo = 0;
    this.pageSize = 10;
    this.searchText = null;
    this.pageNo=this.pageNo+1;
    this.fetchGuestHistory();
  }

  optionChange(status) {
    this.selectedStatus = status;
  }

  pad(value) {
    return value < 10 ? '0' + value : value;
  }
  createOffset(date) {
    var sign = (date.getTimezoneOffset() > 0) ? "-" : "+";
    var offset = Math.abs(date.getTimezoneOffset());
    var hours = this.pad(Math.floor(offset / 60));
    var minutes = this.pad(offset % 60);
    alert(sign + hours + ":" + minutes);
    return sign + hours + ":" + minutes;
  }

  fetchGuestHistory(){
    var d = new Date(); 
		var	tzName = this.createOffset(d).toString();
    var params = new HttpParams()
    .set('orgId',  this.orgId)
    .set('pageSize', this.pageSize)
    .set('pageNo', this.pageNo)
    .set('searchText', this.searchText)
    .set('clientTimezone',tzName.toString())
    .set('sliderMaxTime',"0")
    .set('sliderMinTime',"24")
    .set('statusOption',this.selectedStatus);
    alert(params);
    this.guestService.fetchGuestHistoryList(params).subscribe((res: any)=>{
      if(res.success == 1){
        this.guestDTOList=res.serviceResult.records;
        console.log("user=="+JSON.stringify(res.serviceResult));
       // this.selectedGuest=new GuestDTO();
      }
      else{
        alert(res.message);
      }
    });
  }


}
