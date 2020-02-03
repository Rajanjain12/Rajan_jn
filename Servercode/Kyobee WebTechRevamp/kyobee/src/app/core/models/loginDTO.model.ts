export class LoginDTO { 
    userName: string;
    password: string;
    deviceToken: string;
    deviceType : string;
    clientBase:string;
    
    LoginDTO(){
        this.userName = "";
        this.password = "";
        this.deviceToken = "";
        this.deviceType = "Web";
        this.clientBase="admin";
    }
  }