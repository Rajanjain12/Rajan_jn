import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DesignService {
  loadTheme: string;
  headerStyle: string;

  headerStyleChange: Subject<string> = new Subject<string>();

  constructor() {}

  setTheme(themeName) {
    this.loadTheme = themeName;
  }

  getTheme() {
    return this.loadTheme;
  }

  setHeaderStyle(type: string) {
    if (this.headerStyle !== type) {
      this.headerStyle = type;
      this.headerStyleChange.next(this.headerStyle);
    }
  }

  getHeaderStyle() {
    console.log('header style = ' + this.headerStyle);
    return this.headerStyle;
  }
}
