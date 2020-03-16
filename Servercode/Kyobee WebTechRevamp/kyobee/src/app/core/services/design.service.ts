import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DesignService {
  loadTheme: string;

  constructor() {}

  setTheme(themeName) {
    this.loadTheme = themeName;
  }

  getTheme() {
    return this.loadTheme;
  }
}
