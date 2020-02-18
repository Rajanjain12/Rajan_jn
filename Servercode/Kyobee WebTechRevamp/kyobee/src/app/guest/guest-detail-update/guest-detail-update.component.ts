import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { GuestDTO } from 'src/app/core/models/guest.model';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/operators';
import { GuestService } from 'src/app/core/services/guest.service';

@Component({
  selector: 'app-guest-detail-update',
  templateUrl: './guest-detail-update.component.html',
  styleUrls: ['./guest-detail-update.component.scss']
})
export class GuestDetailUpdateComponent implements OnInit {
  guest: GuestDTO = new GuestDTO();
  id$: Observable<string>;
  id: string;

  constructor(private route: ActivatedRoute, private guestService: GuestService) {}

  ngOnInit() {
    this.id$ = this.route.paramMap.pipe(map(paramMap => paramMap.get('id')));
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id !== null) {
      this.guestService.fetchGuestDetail(this.id).subscribe(res => {
        if (res.success == 1) {
          this.guest = res.serviceResult;
          console.log(res.serviceResult);
        } else {
          this.guest = new GuestDTO();
          this.guest.uuid = null;
          console.log('id is absent');
        }
      });
    }
  }

  onFormSubmit(form: NgForm) {
    console.log('TCL: GuestDetailUpdateComponent -> onFormSubmit -> form', form);
  }
}
