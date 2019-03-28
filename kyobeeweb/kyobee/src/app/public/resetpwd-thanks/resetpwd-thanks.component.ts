import { Component, OnInit } from '@angular/core';
import { imgLinks } from '../../app.component';
import { DataService } from 'src/app/services/data.service';

@Component({
  selector: 'app-resetpwd-thanks',
  templateUrl: './resetpwd-thanks.component.html',
  styleUrls: ['./resetpwd-thanks.component.scss']
})
export class ResetpwdThanksComponent implements OnInit {

  constructor(private dataService: DataService) { }

  public resetPwdPageImageSrc: string = imgLinks.resetPwdPageImg;

  ngOnInit() {
  }

  changeView(path: string){
    this.dataService.changeView(path);
  }
}
