import { Injectable, NgZone } from '@angular/core';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { Subject, BehaviorSubject,Observable } from 'rxjs';
import { PropertiesService } from '../../properties.service';
import { auth } from 'firebase';
// import { SearchBarComponent } from '../search-bar/search-bar.component';
import * as firebase from 'firebase';
import { StringifyOptions } from 'querystring';
@Injectable({
    providedIn: 'root'
})

export class SearchService{
    
    sharedData: string;
    sharedDataS : string;
    sharedDataC :string;

    constructor(private propsvc:PropertiesService,public zone: NgZone) {

      
      }
   
   

}