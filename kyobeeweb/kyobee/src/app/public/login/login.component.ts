import { Component, OnInit } from '@angular/core';
import { HttpService } from 'src/app/services/http.service';
import { DataService } from 'src/app/services/data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private httpService: HttpService, private dataService: DataService) { }

  public signinPageImageSrc: string = "../assets/img/sign-in-illu.png";
  public subdomain: string;
  public username = null;
  public password = null;
  public loading = false;
  public errorMsg: string = null;

  ngOnInit() {
    this.subdomain = this.dataService.getData().subdomain;
  }

  login(invalid){
    if(invalid){
      return;
    }
    else{
      this.loading = true;
      this.errorMsg = null;

      var postBody = {
        username: this.username,
        password: this.password,
        clientBase : this.subdomain
      };
      alert(JSON.stringify(postBody));
      var url = 'http://localhost:8080/kyobee/rest/login';

      this.httpService.postService(url, postBody).subscribe(data => {
        
        if (data.status == "SUCCESS") {
          this.dataService.changeView('public/dashboard');
          this.loading=false;
        } else if (data.status == "FAILURE") {
          this.errorMsg = data.errorDescription;
          this.loading=false;
        }
      },
      error => {
        alert('Session Timed Out');
        this.loading=false;
      });
    }
  }

  hideErrorMsg(){
    this.errorMsg = null;
  }

  changeView(path: string){
    alert("path: "+path);
    this.dataService.changeView(path);
  }
}
