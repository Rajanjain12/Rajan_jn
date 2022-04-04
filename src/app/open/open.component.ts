import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {DialogAddComponent} from '../dialog-add/dialog-add.component';
@Component({
  selector: 'app-open',
  templateUrl: './open.component.html',
  styleUrls: ['./open.component.css']
})
export class OpenComponent implements OnInit {

  constructor(public dialog: MatDialog) { }

  ngOnInit() {
  }
  openDialog() {
    
    
    let dialogRed = this.dialog.open(DialogAddComponent);
  }
}
