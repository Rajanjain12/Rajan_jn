import { Component, OnInit } from '@angular/core';
import imgLinks from '../../../../assets/data/imgLinks.json';

@Component({
  selector: 'app-signup-verification',
  templateUrl: './signup-verification.component.html',
  styleUrls: ['./signup-verification.component.scss']
})
export class SignupVerificationComponent implements OnInit {

  constructor() { }

  public verificationPageImgSrc: string = imgLinks.verificationCodePageImg;

  ngOnInit() {
  }

  moveOnPrevious(field) { 
    var id = field.id;
    var arr = id.split("_");
    var decr = parseInt(arr[1]) - 1;
    var prviousFieldID = "code_" + decr;
    if (field.value.length <= 0) { 
      document.getElementById(prviousFieldID).focus(); 
    } 
  }

  moveOnNext(field){
      var id = field.id;
    var arr = id.split("_");
    var incr = parseInt(arr[1]) + 1;
    var nextFieldID = "code_"+ incr;
    if (field.value.length >= field.maxLength) { 
      document.getElementById(nextFieldID).focus(); 
    } 
    else{
      this.moveOnPrevious(field)
    }
  }

}
