import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { CountryDTO } from 'src/app/core/models/countryDTO.model';
import { HttpParams } from '@angular/common/http';
import { PlaceDTO } from 'src/app/core/models/place.model';
import { LoaderService } from 'src/app/core/services/loader.service';
import { OrganizationDTO } from 'src/app/core/models/organization.model';
import { AddressDTO } from 'src/app/core/models/address.model';
import { SignUpService } from 'src/app/core/services/signup.service';
import { AuthB2BService } from 'src/app/core/services/auth-b2b.service';

@Component({
  selector: 'app-register-business',
  templateUrl: './register-business.component.html',
  styleUrls: ['./register-business.component.scss']
})
export class RegisterBusinessComponent implements OnInit {
  @Input('step') step: number;
  @Output('stepChange') stepChange = new EventEmitter<number>();

  countryList: Array<CountryDTO>;
  countryCode = 'us';
  country: CountryDTO = new CountryDTO();
  searchKeyword: string;
  countryName: any;
  zipCode: any;
  showBusiness = false;
  latLon: string;
  place: any;
  placeList: Array<PlaceDTO>;
  show = true;
  organization: OrganizationDTO = new OrganizationDTO();
  organizationTypeList: any;
  placeResults = false;

  addBusinessFlag: boolean = false;
  addBusiness: {
    businessName: string;
    businessPhoneNumber: string;
    streetAddress: string;
    IndustryInfo: string;
    countryCode: string;
    city: string;
    state: string;
    zipcode: string;
    timezone: string;
  } = {
    businessName: '',
    businessPhoneNumber: '',
    streetAddress: '',
    IndustryInfo: '',
    countryCode: 'us',
    city: '',
    state: '',
    zipcode: '',
    timezone: ''
  };

  constructor(
    private userService: UserService,
    public loaderService: LoaderService,
    private signupService: SignUpService,
    private authb2bService: AuthB2BService
  ) {}

  ngOnInit() {
    this.fetchCountryList();
    this.organization.addressDTO = new AddressDTO();
    this.organization.orgTypeId = 1;
  }

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

  // Purpose : for fetching latLon for given zipcode
  fetchLatLon() {
    var promise = (promise = new Promise((resolve, reject) => {
      const params = new HttpParams().set('zipCode', this.zipCode);
      this.userService.fetchLatLon(params).subscribe((res: any) => {
        if (res.success === 1) {
          this.showBusiness = true;
          console.log('response:' + JSON.stringify(res.serviceResult));
          this.latLon = res.serviceResult.latitude + ',' + res.serviceResult.longitude;
          console.log('this.latLon:' + this.latLon);
          resolve();
        } else {
          alert(res.message);
          this.showBusiness = false;
          reject();
        }
      });
    }));
    return promise;
  }

  // Purpose : for fetching placeList acc to country and zipcode
  fetchPlaceList() {
    this.loaderService.disable = true;

    const params = new HttpParams()
      .set('place', this.searchKeyword)
      .set('latLon', this.latLon)
      .set('countryCode', this.countryCode);
    this.userService.fetchPlaceList(params).subscribe((res: any) => {
      if (res.success === 1) {
        this.placeList = [];
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.placeList = res.serviceResult;
        this.placeResults = true;
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : For clearing search results
  searchCleared() {
    this.placeList = [];
    console.log('placeList cleared:' + this.placeList);
  }

  // Purpose : For fetching place details of selected place
  fetchPlaceDetails(place) {
    console.log('place Id:' + JSON.stringify(place.placeId));
    const params = new HttpParams().set('placeId', place.placeId);
    this.userService.fetchPlaceDetails(params).subscribe((res: any) => {
      if (res.success === 1) {
        this.placeList = [];
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.organization = res.serviceResult;
        this.organization.orgTypeId = 1;
        this.fetchOrganizationType();
        this.addBusinessFlag = true;
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
      } else {
        alert(res.message);
      }
    });
  }

  // Purpose : for adding business
  registerBusiness() {
    this.country = this.countryList.find(x => x.isocode == this.countryCode.toUpperCase());
    this.organization.addressDTO.country = this.country.countryName;
    this.organization.addressDTO.zipcode = this.zipCode;

    console.log('organization:' + JSON.stringify(this.organization));

    this.signupService.registerBusiness(this.organization).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.step = 1;
        this.stepChange.emit(this.step);
        this.organization = res.serviceResult;
        console.log('saved organization:' + JSON.stringify(this.organization));
        this.authb2bService.setOrganizationData(this.organization);
      } else {
        alert(res.message);
      }
    });
  }
  validate(invalidate) {
    if (invalidate) {
      return;
    }
    const promise = this.fetchLatLon();
    promise.then(value => {
      this.fetchPlaceList();
    });
  }
}
