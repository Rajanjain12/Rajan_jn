import { Injectable,NgZone } from '@angular/core';
import { AngularFireDatabase, AngularFireList, AngularFireObject } from '@angular/fire/database';
import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';

import { AngularFirestore } from '@angular/fire/firestore';
import { Property } from './seller/property';
import {Service } from './seller/services/service';
import {Profile} from './seller/profile';
@Injectable({
    providedIn: 'root'
  })

  export class CrudService {
    SETINTPP:any;
    SETINT : any;
    SETINTS : any;
    SETPROPID:any;
    SETSERVICEID:any;
    SETReportPView:any;
    SETReportView:any;
      ReportSerID: any;
      ReportSellerID: any;
      ReportPropID: any;
      ReportSellPID:any;
      Newuserid : any;
      loginid : any;


    selectedFile :Array<File> = [];
    x : any;
  Search: String;
  propRef: AngularFireList<any>;
  propRef1: AngularFireList<any>;
  IntRef1: AngularFireList<any>;
  IntRef3: AngularFireList<any>;
  IntRef11: AngularFireList<any>;
  
  IntRef2: AngularFireList<any>;
  IntRef5: AngularFireList<any>;
  IntRef6: AngularFireList<any>;
  IntRef7: AngularFireList<any>;
  IntRef8: AngularFireList<any>;
  IntRef9: AngularFireList<any>;IntRef10: AngularFireList<any>;
  IntRef4: AngularFireList<any>;
    propertiesRef: AngularFireList<any>;
    propertiesRefC: AngularFireList<any>;
    propertyRef: AngularFireObject<any>;
    propertyRefS: AngularFireObject<any>;
    servicesRef: AngularFireList<any>;
    serviceRef: AngularFireObject<any>;
    profilesRef: AngularFireList<any>;
    profileRef: AngularFireObject<any>;
    constructor( private db: AngularFireDatabase , public zone: NgZone,private storage: AngularFirestore,
      public afAuth: AngularFireAuth) { }
  
      GetPropertyByFilter(arrName, arrVal) {
        if(arrName == "price" || arrName=="area")
        {
            
           this.propRef = this.db.list('property', ref => ref.orderByChild(arrName).endAt(arrVal));
          // this.propRef = this.propRef.query(ref => ref.orderByChild(arrName).endAt(arrVal));
          
        }
        else{
          
        this.propRef = this.db.list('property', ref => ref.orderByChild(arrName).equalTo(arrVal));
        
      }
        return this.propRef;
      }
    // Get Single Property
    GetProperty(id: string) {
     // this.propertyRefS = this.db.object('property/'+id);
      this.propertyRef = this.db.object('property/' + id);
      return this.propertyRef;
    }
    GetProfile(id: string) {
      this.profileRef = this.db.object('user/' + id);
      
      return this.profileRef;
    }
  GetService(id: string) {
      this.serviceRef = this.db.object('services/' + id);
      return this.serviceRef;
    }
    
    // Fetch All Properties
    GetProperties(startText:string) {
      const endText = startText + '\uf8ff';
      this.propertiesRef = this.db.list('property', ref =>
      ref
        .orderByChild('location')

        .startAt(startText)
        .endAt(endText)
    );
      return this.propertiesRef;
    }
  
    GetServices() {
      this.servicesRef = this.db.list('services');
      return this.servicesRef;
    }
    GetServiceByFilter(arrName, arrVal) {
      this.propRef1 = this.db.list('services', ref => ref.orderByChild(arrName).endAt(arrVal));
      return this.propRef1;
    }
    GetInterstedProperty(arrVal)
    {

      this.IntRef1 = this.db.list('property',ref => ref.orderByKey().equalTo(arrVal));
      return this.IntRef1; 
    }
    GetResponsesProperty(arrVal)
    {

      this.IntRef5 = this.db.list('property',ref => ref.orderByKey().equalTo(arrVal));
      return this.IntRef5; 
    }
    GetReportedProperty(arrVal)
    {

      this.IntRef10 = this.db.list('property',ref => ref.orderByKey().equalTo(arrVal));
      return this.IntRef10; 
    }
    GetResponsesPUsers(arrVal)
    {

      this.IntRef6 = this.db.list('user',ref => ref.orderByKey().equalTo(arrVal));
      return this.IntRef6; 
    }
    GetReportUsers(arrVal)
    {

      this.IntRef7 = this.db.list('user',ref => ref.orderByKey().equalTo(arrVal));
      return this.IntRef7; 
    }
    GetReportUsersP(arrVal)
    {

      this.IntRef9 = this.db.list('user',ref => ref.orderByKey().equalTo(arrVal));
      return this.IntRef9; 
    }
    GetReportServices(arrVal1)
    {

      this.IntRef8 = this.db.list('services',ref => ref.orderByKey().equalTo(arrVal1));
      return this.IntRef8; 
    }
    GetInterstedServices(arrVal1)
    {

      this.IntRef2 = this.db.list('services',ref => ref.orderByKey().equalTo(arrVal1));
      return this.IntRef2; 
    }
    GetResponsesServices(arrVal1)
    {

      this.IntRef3 = this.db.list('services',ref => ref.orderByKey().equalTo(arrVal1));
      return this.IntRef3; 
    }
    GetResponsesUsers(arrVal1)
    {

      this.IntRef4 = this.db.list('user',ref => ref.orderByKey().equalTo(arrVal1));
      return this.IntRef4; 
    }

  
    GetProfiles() {
      this.profilesRef = this.db.list('user');
      return this.profilesRef;
    }
  
    onfileselect(event) {
      this.x = event.length;
      
      this.selectedFile = <Array<File>>event.target.files;
      
    }
    // Update properties.
    UpdateProperty(property: Property) {
      // const files: Array<File> = this.selectedFile;
      // console.log(files);
      this.propertyRef.update({

        type: property.type,
        type11: property.type11,
        price: property.price,
        location: property.location,
        id: property.id,
        BHK : property.BHK,
        features: property.features,
        Date: Date.now(),
        pdate: property.pdate,
        verify: property.verify,
        area: property.area,
        unit: property.unit,
        Images: property.Images,
        contact: property.contact
      })
      
    //   let returnVal;
     
    //   for(let i =0; i < files.length; i++){
    //   var uploadTask = this.propertyRefS.update(function (snapshot) {
  
    //     returnVal = snapshot.ref.getDownloadURL().then(function (downloadURL) {
    //       console.log('File available at', downloadURL);
          
    //       this.propertyRef.child('Images').update(
    //         { imageLink: downloadURL }
    //       );
        
    //     });
    //   });
    // }

    }
   
    UpdateProfile(profile: Profile) {

      
      this.profileRef.update({

        name: profile.name,
        email: profile.email,
         contactNo : profile.contactNo,
        
         //Date: Date.now(),
        
      })
    }
    getAddress(Search: String) {
      
     this.Search = Search;
      
    }

    UpdateServices(service: Service) {
      
      
      
      this.serviceRef.update({
          serviceName: service.serviceName,
        location: service.location,
        price : service.price,
       contactNo: service.contactNo,
        Date: Date.now(),
        id: service.id,     
      })
    }
  
    }
  
  