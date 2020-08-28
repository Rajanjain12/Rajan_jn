export class LoginDTO {
  userName: string;
  password: string;
  deviceToken: string;
  deviceType: string;
  clientBase: string;
  rem: boolean;
  mainOrganizationLogin: boolean;

  LoginDTO() {
    this.userName = '';
    this.password = '';
    this.deviceToken = '';
    this.deviceType = 'Web';
    this.clientBase = 'admin';
    this.rem = false;
    this.mainOrganizationLogin = false;
  }
}
