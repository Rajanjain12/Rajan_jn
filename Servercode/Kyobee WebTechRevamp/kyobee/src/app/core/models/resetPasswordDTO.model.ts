export class ResetpasswordDTO {
  userId: number;
  password: String;
  authcode: String;

  ResetpasswordDTO() {
    this.userId = 0;
    this.password = '';
    this.authcode = '';
  }
}
