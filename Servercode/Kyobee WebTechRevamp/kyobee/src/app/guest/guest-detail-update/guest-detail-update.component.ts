import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { GuestService } from 'src/app/core/services/guest.service';
import { Preference } from 'src/app/core/models/preference.model';

@Component({
  selector: 'app-guest-detail-update',
  templateUrl: './guest-detail-update.component.html',
  styleUrls: ['./guest-detail-update.component.scss']
})
export class GuestDetailUpdateComponent implements OnInit {
  guest: GuestDTO = new GuestDTO();
  id$: Observable<string>;
  id: string;
  listSeatingPref: Array<Preference>;
  listMarketingPref: Array<Preference>;
  listLanguageKeyMap: Array<Map<string, string>>;

  constructor(private route: ActivatedRoute, private guestService: GuestService) {}

  ngOnInit() {
    this.fetchGuest();
  }

  fetchGuest() {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id !== null) {
      this.guestService.fetchGuestDetail(this.id).subscribe(res => {
        if (res.success == 1) {
          this.guest = res.serviceResult;
          this.fetchGuestMetric();
          this.fetchOrgPrefandKeyMap();
          console.log(res.serviceResult);
        } else {
          alert(res.message);
        }
      });
    } else {
      alert('invalid url');
    }
  }

  fetchOrgPrefandKeyMap() {
    const params = new HttpParams().set('orgId', this.guest.organizationID.toString());
    this.guestService.fetchOrgPrefandKeyMap(params).subscribe(res => {
      if (res.success == 1) {
        this.listSeatingPref = res.serviceResult.seatingPreference;
        this.listMarketingPref = res.serviceResult.marketingPreference;
        console.log(JSON.stringify(this.listSeatingPref) + ' --- ' + JSON.stringify(this.listMarketingPref));
      } else {
        alert(res.message);
      }
    });
  }

  fetchGuestMetric() {
    const params = new HttpParams()
      .set('orgId', this.guest.organizationID.toString())
      .set('guestId', this.guest.guestID.toString());
    this.guestService.fetchGuestMetrics(params).subscribe(res => {
      if (res.success == 1) {
        console.log(res.serviceResult);
      } else {
        alert(res.message);
      }
    });
  }

  onFormSubmit(form: NgForm) {
    console.log('TCL: GuestDetailUpdateComponent -> onFormSubmit -> form', form);
  }
}
