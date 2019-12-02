export class SignupDTO{
    firstName : string;
    lastName : string;
    userName : string;
    email : string;
    storeName : string;
    mobileNumber : Int32Array;
    address: string;
    password :string;
    retypePassword : string;
    selectedPlan : string;
    constructor(){
        this.firstName = "";
        this.lastName = "";
        this.userName = "";
        this.email = "";
        this.storeName = "";
        this.mobileNumber = null;
        this.address = "";
        this.password = "";
        this.retypePassword = "";
        this.selectedPlan = "";
    }
}