import { Component, OnInit } from '@angular/core';
import imgLinks from '../../../../assets/data/imgLinks.json';
import { HttpService } from 'src/app/services/http.service';

@Component({
  selector: 'app-resetpwd-thanks',
  templateUrl: './resetpwd-thanks.component.html',
  styleUrls: ['./resetpwd-thanks.component.scss']
})
export class ResetpwdThanksComponent implements OnInit {

  constructor(private httpService: HttpService) { }

  public resetPwdPageImageSrc: string = imgLinks.resetPwdPageImg;

  ngOnInit() {
  }

  changeView(path: string){
    this.httpService.changeView(path);
  }
}
