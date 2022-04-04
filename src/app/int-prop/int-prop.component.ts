import { Component, OnInit } from '@angular/core';
import { CrudService } from '../crud.service';
import * as firebase from 'firebase';
import { AuthService } from '../core/auth.service';
import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireDatabase, AngularFireList, AngularFireObject } from '@angular/fire/database';
import {Property} from '../seller/property';
import { SwiperOptions } from 'swiper';
import { Router, Params } from '@angular/router';
import {Service} from '../seller/services/service';
import { ɵNgNoValidate } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { DiaforinterestedComponent } from '../diaforinterested/diaforinterested.component';
@Component({
  selector: 'app-int-prop',
  templateUrl: './int-prop.component.html',
  styleUrls: ['./int-prop.component.css']
})
export class IntPropComponent implements OnInit {
  propRef: AngularFireList<any>;
  servRef: AngularFireList<any>;
  servRefX: AngularFireList<any>;
  serviceDel: AngularFireList<any>;
  propDel: AngularFireList<any>;
  propertyRef: AngularFireObject<any>;
  property: Property[];
  property1: Service[];
  demo = [];
  demos = [];
  x1 = "";
  x2="";
  pess: Property[] = [];
  pessS: Service[] = [];
  config: SwiperOptions = {
  
    
    autoplay: true, // Autoplay option having value in milliseconds
// initialSlide: 1, // Slide Index Starting from 0
    zoom: true,
    
//  observer: true,  
//    observeParents: true,
slidesPerView: 1, // Slides Visible in Single View Default is 1
pagination:  { el: '.swiper-pagination', clickable: true },
navigation: {
  nextEl: '.swiper-button-next',
  prevEl: '.swiper-button-prev'
},
 spaceBetween: 30 // Space between each Item

}; 

  constructor(
    private crudService: CrudService,
    private router: Router,
    public dialog: MatDialog,
    private db: AngularFireDatabase
    ) { }

  ngOnInit() {
   //this.getKeys();
   console.log("STILL WORKING");
    let s = this.getlist();
    this.demo=[];
    console.log("NGONIT-1");
    s.snapshotChanges().subscribe(data => {
      this.property = [];
      data.forEach(item => {
        let a = item.payload.toJSON();
        console.log(a);
        a['$key'] = item.key;
        console.log(a);
        this.demo.push(a);
        this.getKeys(a['propid']);
      
      })
    })
    let s1 = this.getListService();
    console.log("NGONIT-2");
    console.log(":::");
    s1.snapshotChanges().subscribe(data => {
      this.property1 = [];
      console.log(":::");
      data.forEach(item => {
        console.log(":::");
        let a1 = item.payload.toJSON();
        console.log(a1);
        
        a1['$key'] = item.key;
        console.log(a1);
        this.demos.push(a1);
       this.getKeysS(a1['serviceid']);
      
      })
    })
  }
  
  openDialog(key1:string) {
    
    this.crudService.SETINTPP =key1;
    
    let dialogRed = this.dialog.open(DiaforinterestedComponent);
  }
 
    getKeys(a:string)
    {
      
     let s= this.crudService.GetInterstedProperty(a);
     this.pess= [];
     s.snapshotChanges().subscribe(data => {
      this.property = [];
      data.forEach(item => {
        let as = item.payload.toJSON();
        
        console.log("AS",as);
        as['$key'] = item.key;
        console.log(as['$key']);
        this.pess.push(as as Property);
      
      })
    })
    
    }
    getKeysS(ad:string)
    {
      
     let s1= this.crudService.GetInterstedServices(ad);
     this.pessS = []; 
     s1.snapshotChanges().subscribe(data => {
      this.property1 = [];
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        console.log(as1);
        as1['$key'] = item.key;
        console.log(as1['$key']);
        this.pessS.push(as1 as Service);
      console.log("Pushing Service",this.pessS.length);
      
      })
    })
    }
    getlist()
    {
      console.log("LIST");
      // this.propRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2');
      this.propRef = this.db.list('interested', ref => ref.child(firebase.auth().currentUser.uid).child('property'));
      return this.propRef;
    }
    getListService()
    {
      console.log("Service List Fetch");
       this.servRef = this.db.list('interested', ref => ref.child(firebase.auth().currentUser.uid).child('service'));
      //this.servRef = this.db.list('interested/E9wAKhKB74Rl3DXnO80VonFolYE2/service');
      console.log(this.servRef);
      return this.servRef;
    }
    removex(id:string)
    {
      console.log("STILL WORKING");
      console.log("Remove - ID->",id);

      for(let i=0;i<this.demo.length;i++)
      {
        console.log ("Block statement execution no." + i);
        console.log(this.demo[i]['propid']);
          if(this.demo[i]['propid'] === id)
          {
            
            console.log("IN IF");
            // var d = this.demo[i];
            // console.log(d);
            console.log("id to delete",this.demo[i]['propid']);
            console.log("in if"); 
            console.log("match");
            var x = this.demo[i]['$key'];
            //firebase.database().ref('interested').child(firebase.auth().currentUser.uid).child('property').child(a1['$key']).remove();
           
                // alert("Prop Removed"+a1['$key']);
                var adaRef = firebase.database().ref('interested').child(firebase.auth().currentUser.uid).child('property').child(x);
                adaRef.remove();

        
                const a = new Promise((resolve, reject) => {  
       let s= this.crudService.GetInterstedProperty(this.demo[i]['propid']);
                s.snapshotChanges().subscribe(data => {
                  
            data.forEach(item => {
              let as1 = item.payload.toJSON();
              console.log(as1);
              //as1['$key'] = item.key;
              console.log(as1['id']);
              this.x1 = as1['id'];
              resolve();
            })
          })
          console.log("Bf return");
          
        })
        
        a.then(()=>{console.log("AwS");
    console.log(this.x1);
    console.log(id);
    let s2= this.db.list('responses',ref => ref.child(this.x1).child('properties').child(id));
    console.log(s2);
    s2.snapshotChanges().subscribe(data => {
      
      data.forEach(item => {
        let as1 = item.payload.toJSON();
        as1['$key'] = item.key; 
        console.log(as1);
        console.log(as1['$key']);
        
        if(as1['propid'] === id)
        {
          //x1
         firebase.database().ref('responses').child(this.x1).child('properties').child(id).child(as1['$key']).remove();
         console.log("Removed From Responses");
          id="";
          
        }
        
        
        })
      })
      
                console.log("Remove succeeded.")
    


    });
      
          
         
                  if(this.pess.length = 1)
                  {
                    this.pess = [];
                  }
                  this.demo[i].delete;
                  console.log("Updated Len",this.demo.length);
                  // this.router.navigate(['/int-prop']);
                  //this.ngOnInit();
                console.log("Removed-1-Prop");
                console.log("X1",this.x1);
                  console.log("ID",id);
             
                  break;
          }
        }
      
      console.log("demo",this.demo.length);
      console.log("RETUrn");
      console.log("STILL WORKING");
      
     return false;
    }



    remove(id:string)
    {
      var u = firebase.auth().currentUser.uid;
        console.log("Remove - ID->",id);
        console.log("remoce");
     for(let i=0;i<this.demos.length;i++)
     {
      console.log ("Block statement execution no." + i);
      console.log(this.demos[i]['serviceid']);
      
        if(this.demos[i]['serviceid'] === id)
        {
          console.log("in if");
          console.log("match");
          var d =this.demos[i]['$key'];
       
         var adaRef = firebase.database().ref('interested').child(u).child('service').child(d).remove();
         const a = new Promise((resolve, reject) => {  
          let s1= this.db.list('services',ref => ref.orderByKey().equalTo(this.demos[i]['serviceid']));
                s1.snapshotChanges().subscribe(data => {
            data.forEach(item => {
              let as11 = item.payload.toJSON();
              console.log(as11);
              //as1['$key'] = item.key;
              console.log(as11['id']);
              this.x2 = as11['id'];
            resolve();
            })
          })
        });
        a.then(()=>{
          let s2= this.db.list('responses',ref => ref.child(this.x2).child('services').child(id));
          s2.snapshotChanges().subscribe(data => {
            console.log("REMOVE");
            data.forEach(item => {
              let as2 = item.payload.toJSON();
              as2['$key'] = item.key; 
              console.log(as2);
              console.log(as2['$key']);
              if(as2['serviceid'] === id)
              {
               firebase.database().ref('responses').child(this.x2).child('services').child(id).child(as2['$key']).remove();
               console.log("Removed From Responses");
               console.log("REMOVE");
               id="";
              }
              
              
              })
            })
          



        })
          
          // let s2= this.db.list('responses',ref => ref.child(x1).child('services').child(id));
          // s2.snapshotChanges().subscribe(data => {
          //   console.log("REMOVE");
          //   data.forEach(item => {
          //     let as2 = item.payload.toJSON();
          //     as2['$key'] = item.key; 
          //     console.log(as2);
          //     console.log(as2['$key']);
          //     if(as2['serviceid'] === id)
          //     {
          //      firebase.database().ref('responses').child(x1).child('services').child(id).child(as2['$key']).remove();
          //      console.log("Removed From Responses");
          //      console.log("REMOVE");
          //     }
              
              
          //     })
          //   })

          if(this.pessS.length = 1)
          {
            console.log("FULL");
            this.pessS = [];
            console.log("EMPTY");
          }
            
              console.log("Removed - 1 - Service"); 
            // a1['$key']="";
            // item.key="";
                console.log("match");
                break;
        }
      }
       
    console.log("RETUrn");
    return false;
    }
 
 
}
       

//Trash From Removx
      // let s1 = this.getlist();
      // console.log("removex");
      // s1.snapshotChanges().subscribe(data => {
      //   this.demo = [];
      //   data.forEach(item => {
      //     let a1 = item.payload.toJSON();
      //     console.log(a1);  
      //     a1['$key'] = item.key;
      //     console.log("Before If");
      //     console.log(a1['propid']);
      //     this.demo.push(a1);
      //     console.log(this.demo);
          
      //     console.log("END");      
      
        
      //   })
      // })