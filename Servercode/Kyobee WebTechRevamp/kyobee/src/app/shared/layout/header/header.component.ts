import { Component, OnInit } from '@angular/core';
import { DesignService } from 'src/app/core/services/design.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  loadTheme: string;

  constructor(private designService: DesignService) {
    this.loadTheme = this.designService.getTheme();
  }

  ngOnInit() {}
}
