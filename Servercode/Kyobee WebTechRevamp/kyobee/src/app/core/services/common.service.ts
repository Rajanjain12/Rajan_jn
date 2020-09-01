import { Injectable } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { SignUpService } from 'src/app/core/services/signup.service';
import { TimezoneDTO } from 'src/app/core/models/timezone.model';
import { CountryDTO } from 'src/app/core/models/countryDTO.model';
import { LocalStorage } from '@ngx-pwa/local-storage';

@Injectable({
  providedIn: 'root'
})
export class CommonService {
  countryList: Array<CountryDTO>;
  timezoneList: Array<TimezoneDTO>;
  organizationTypeList: any;

  constructor(
    private userService: UserService,
    private signupService: SignUpService,
    private localStorage: LocalStorage
  ) {}

  // Purpose : for fetching country list
  fetchCountryList() {
    this.userService.fetchCountryList().subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.countryList = res.serviceResult;
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : for fetching timezone list
  fetchTimezoneList() {
    this.signupService.fetchTimezoneList().subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.timezoneList = res.serviceResult;
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : for fetching organization type
  fetchOrganizationType() {
    this.signupService.fetchOrganizationType().subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.organizationTypeList = res.serviceResult;
        // Sorting list
        this.organizationTypeList.sort((a, b) => a.typeName.localeCompare(b.typeName));
        // Move element to last position
        this.organizationTypeList.push(
          this.organizationTypeList.splice(
            this.organizationTypeList.findIndex(x => x.typeName === 'Other'),
            1
          )[0]
        );
      } else {
        alert(res.message);
      }
    });
  }

  // sets data in local storage
  setLocalStorageData(name, data): void {
    localStorage.setItem(name, data);
    console.log('Active tab' + localStorage.getItem(name));
  }
}
