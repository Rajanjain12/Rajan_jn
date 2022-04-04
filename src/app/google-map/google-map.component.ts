import { Component, OnInit } from '@angular/core';
import { GeoService } from '../geo.service';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { AuthService } from '../core/auth.service';
import * as firebase from 'firebase';
@Component({
  selector: 'app-google-map',
  templateUrl: './google-map.component.html',
  styleUrls: ['./google-map.component.css']
})
export class GoogleMapComponent implements OnInit {

  lat: number;
  lng: number;

  markers :any;
  userId: string;
  constructor(private geo:GeoService) { 
    //this.userId = firebase.auth().currentUser.uid;
  }

  ngOnInit() {
    this.getUserLocation()
    this.geo.hits.subscribe(hits => this.markers =hits)
    
  }
  private getUserLocation()
  {
    if(navigator.geolocation)
    {
      
      navigator.geolocation.getCurrentPosition(position =>{
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
      console.log(this.lat);
       console.log(this.lng);
        this.geo.getlocations(5, [this.lat,this.lng])
        this.geo.setlocation("A",[this.lat,this.lng])
      });
    }
  }

}
