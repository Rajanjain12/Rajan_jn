import { Component, OnInit } from '@angular/core';
import { CommonService } from 'src/app/core/services/common.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { HttpParams } from '@angular/common/http';
import { UserService } from 'src/app/core/services/user.service';
import { OrganizationDTO } from 'src/app/core/models/organization.model';
import { CountryDTO } from 'src/app/core/models/countryDTO.model';
import { AddressDTO } from 'src/app/core/models/address.model';
import { AccountService } from 'src/app/core/services/account.service';

declare var $: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: User = new User(); // for storing user details
  organization: OrganizationDTO = new OrganizationDTO(); // for storing organization details
  countryCode = 'us'; // default country code
  country: CountryDTO = new CountryDTO(); // country object
  imgUrl: any = ''; // selected img url
  profileImage: File = null; // used while uploading a profile image.
  temp: any; // used in event for setting the profile image.
  imgSizeExceed: boolean; // TO show error when image size exceeded than limit.
  oldPwd;
  newPwd;
  confPwd;
  successMsg = null;

  constructor(
    public commonService: CommonService,
    private authService: AuthService,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.organization.addressDTO = new AddressDTO();
    this.user = this.authService.getUser();
    this.imgUrl = this.user.logoPath;
    this.commonService.fetchCountryList();
    this.fetchOrganization();
    this.commonService.fetchOrganizationType();
    this.commonService.fetchTimezoneList();
  }
  // Purpose : For fetching organization details
  fetchOrganization() {
    const params = new HttpParams().set('orgId', this.user.organizationID.toString());

    this.accountService.fetchAccountDetails(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.organization = res.serviceResult;
        this.country = this.commonService.countryList.find(x => x.countryName == this.organization.addressDTO.country);
        this.countryCode = this.country.isocode.toLowerCase();
      } else {
        alert(res.message);
      }
    });
  }

  onImgChange(event) {
    let fileToUpload: File = null;
    fileToUpload = event.target.files[0];
    if (fileToUpload.size > 2097152) {
      console.log('Image Size Exceeded');
      this.imgSizeExceed = true;
      return;
    } else {
      this.imgSizeExceed = false;
      // let temp :any;
      if (fileToUpload) {
        const reader = new FileReader();
        reader.readAsDataURL(fileToUpload); // read file as data url
        reader.onload = (event: ProgressEvent) => {
          // called once readAsDataURL is completed
          this.temp = event.target;
          this.imgUrl = this.temp.result;
          this.profileImage = fileToUpload; // Organization profile Image Set
        };
      }
    }
  }

  updateProfileSetting(invalid) {
    if (invalid) {
      return;
    }

    this.country = this.commonService.countryList.find(x => x.isocode == this.countryCode.toUpperCase());
    this.organization.addressDTO.country = this.country.countryName;

    const formData: FormData = new FormData();
    formData.append('imageFile', this.profileImage);
    formData.append('userDTO', JSON.stringify(this.user));
    formData.append('orgDTO', JSON.stringify(this.organization));

    this.accountService.updateAccountDetails(formData).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        this.authService.setSessionData(this.user);
        this.successMsg = res.serviceResult;
      } else {
        alert(res.message);
      }
    });
  }

  changePassword(invalid) {
    if (invalid) {
      return;
    }

    const params = new HttpParams()
      .set('oldPwd', this.oldPwd)
      .set('newPwd', this.newPwd)
      .set('userId', this.user.userID.toString());

    this.accountService.changePassword(params).subscribe((res: any) => {
      if (res.success === 1) {
        console.log('response:' + JSON.stringify(res.serviceResult));
        $('#passwordChange').modal('hide');
      } else {
        alert(res.message);
      }
    });
  }

  resetForm(form) {
    form.resetForm();
  }
}
