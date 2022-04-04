import { Component, OnInit } from '@angular/core';
import { Injectable, NgZone } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AngularFireDatabase, AngularFireList } from '@angular/fire/database';
import { Subject, BehaviorSubject,Observable } from 'rxjs';
import { PropertiesService } from '../../properties.service';
import { SearchService }  from '../../seller/services/search.service';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { CrudService} from '../../crud.service';
import { auth } from 'firebase';
import {Property} from '../../seller/property';
import * as firebase from 'firebase';
import { SwiperOptions } from 'swiper';
import {DialogPComponent} from '../../dialog-p/dialog-p.component';
import {DialogIntComponent} from '../../dialog-int/dialog-int.component';

@Component({
  selector: 'app-buyer-dashboard',
  templateUrl: './buyer-dashboard.component.html',
  styleUrls: ['./buyer-dashboard.component.css']
})
export class BuyerDashboardComponent implements OnInit {
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

  address: Object;
  establishmentAddress: Object;

  formattedAddress: string;
  formattedEstablishmentAddress: string;

  phone: string;
  properties:any;
  prop : any;
  startAt1 = new Subject();
  endAt1 = new Subject();

  lastkeypress: number = 0;
  property$: Observable<any[]>;
  startAt: BehaviorSubject<string | null> = new BehaviorSubject('');
  filterForm = new FormGroup({
    f1: new FormControl(),
    f2: new FormControl(),
    f3: new FormControl(),
    f4: new FormControl(),
    f5: new FormControl()
  })
  property: Property[];
  pess: Property[] = [];

  area: string = "area";
  type: string = "type"
  type11: string = "type11";
  BHK: string = "BHK";
  price: string = "price";

  areaVal = 0;
  typeVal = 0;
  type11Val = 0;
  BHKVal = 0;
  priceVal = 0;


  constructor(private propsvc:PropertiesService,private searchser: SearchService,public crudService: CrudService,
    public fb: FormBuilder, public zone: NgZone,public dialog: MatDialog,public dialog1:MatDialog) {
    
      this.dataS;

   }
  

  ngOnInit() {
   
    let date: Date = new Date(); 
    console.log(date);
      this.search();
      let s = this.crudService.GetProperties(this.searchser.sharedData);
    s.snapshotChanges().subscribe(data => {
      this.property = [];
      data.forEach(item => {
        let a = item.payload.toJSON();
        console.log(a);
        a['$key'] = item.key;
        var d = new Date(a['Date']);
        var n = new Date();
        console.log(Date.now());
        console.log("current",d.getDate())
        console.log("STAMP - LAST EDITED",d);
        console.log("CURRENT STAMP",n);
if(a['verify'] === "true")                
{
if(n.getMonth() == d.getMonth())
{
if(n.getDate()-d.getDate() > 10) 
{
a['verify'] =="false";
firebase.database().ref('property').child(a['$key']).update({

verify:"false",

});
}
}
else
{
if((n.getMonth()+1)-(d.getMonth()+1) >= 1)
{
if(((d.getMonth()+1) %2) != 0 )
{
  var x = 31 - d.getDate() + 1;
  if(n.getDate() + x > 10)
  {
    a['verify'] ="false";
    firebase.database().ref('property').child(a['$key']).update({

      verify:"false",
      
    });
  }
}
if((d.getMonth()+1) == 2)
{
if(((d.getFullYear() % 4 == 0) && (d.getFullYear() % 100 != 0)) || (d.getFullYear() % 400 == 0))
{
  var x = 29 - d.getDay() +1;
  if(n.getDate() + x > 10)
  {
    a['verify'] ="false";
    firebase.database().ref('property').child(a['$key']).update({

      verify:"false",
      
    });
  }
}
}
else
{
  var x = 30 - d.getDate() + 1;
  if(n.getDate() + x > 10)
  {
    a['verify'] ="false";
    firebase.database().ref('property').child(a['$key']).update({

      verify:"false",
      
    });
  }
}

}

}
}
        this.property.push(a as Property);
      })
    })
    this.filterFunction();
      
  }
  get  dataS() :string
  {
    console.log("get",this.searchser.sharedData);
   return  this.searchser.sharedData;
  }
  openDialog(key1:string,key2:string) {
    
    this.crudService.ReportPropID = key1;
    this.crudService.ReportSellPID = key2;
    let dialogRed = this.dialog.open(DialogPComponent);
  }
   search() {
    var searchText = this.dataS;     
    console.log(searchText);  
        this.startAt.next(searchText);
        this.property$ = this.propsvc.getproperties(this.startAt);
      }

      Interested(key1:string,key2:string)
      {
       
        console.log("IN INterested Functions");
        var s = firebase.auth().currentUser.uid;
        console.log("ADDED");
          firebase.database().ref('interested').child(s).child("property").push(

            {  
              propid: key1,

            }
          );
          console.log("Added in interested");
          firebase.database().ref('responses').child(key2).child("properties").child(key1).push(

            {  
              propid: key1,
              userid: s,

            }
          );
          console.log("Added in responses");
          this.crudService.SETINT = key2;
          let dialogRed1 = this.dialog.open(DialogIntComponent);
          return;
        }


      addArea() {
        this.areaVal = 1;
      }
    
      addType() {
        this.typeVal = 1;
      }
    
      addType11() {
        this.type11Val = 1;
      }
    
      addBHK() {
        this.BHKVal = 1;
      }
    
      addPrice() {
        this.priceVal = 1;
      }
    
      removeFilters() {
        this.filterForm.reset();
        this.filterFunction();
      }
    
      filterFunction() {
        this.areaVal = 0;
        this.typeVal = 0;
        this.type11Val = 0;
        this.BHKVal = 0;
        this.priceVal = 0;
    
        if(this.filterForm.value.f1 !== null) {
          this.addArea();
        }
    
        if(this.filterForm.value.f2 !== null) {
          this.addType();
        }
    
        if(this.filterForm.value.f3 !== null) {
          this.addPrice();
        }
    
        if(this.filterForm.value.f4 !== null) {
          this.addBHK();
        }
    
        if(this.filterForm.value.f5 !== null) {
          this.addType11();
        }
        console.log(this.areaVal + " " + this.typeVal + " " + this.priceVal + " " + this.BHKVal + " " + this.type11Val);
        let size = this.areaVal + this.typeVal + this.type11Val + this.BHKVal + this.priceVal;
        console.log(size);
    
        switch(size) {
    
          // When zero filters are selected this case is selected and all properties will be displayed
          case 0:
            
            let s = this.crudService.GetProperties(this.searchser.sharedData);
            s.snapshotChanges().subscribe(data => {
              this.pess = [];
              data.forEach(item => {
                let a = item.payload.toJSON();
                a['$key'] = item.key;
                console.log("key",a['$key']);
                var d = new Date(a['Date']);
                var n = new Date();
                console.log(Date.now());
                console.log("current",d.getDate())
                console.log("STAMP - LAST EDITED",d);
                console.log("CURRENT STAMP",n);
if(a['verify'] === "true")                
{
if(n.getMonth() == d.getMonth())
{
   if(n.getDate()-d.getDate() > 10) 
  {
    a['verify'] =="false";
    firebase.database().ref('property').child(a['$key']).update({

      verify:"false",
      
    });
  }
}
else
{
  if((n.getMonth()+1)-(d.getMonth()+1) >= 1)
  {
      if(((d.getMonth()+1) %2) != 0 )
      {
          var x = 31 - d.getDate() + 1;
          if(n.getDate() + x > 10)
          {
            a['verify'] ="false";
            firebase.database().ref('property').child(a['$key']).update({

              verify:"false",
              
            });
          }
      }
      if((d.getMonth()+1) == 2)
      {
        if(((d.getFullYear() % 4 == 0) && (d.getFullYear() % 100 != 0)) || (d.getFullYear() % 400 == 0))
        {
          var x = 29 - d.getDay() +1;
          if(n.getDate() + x > 10)
          {
            a['verify'] ="false";
            firebase.database().ref('property').child(a['$key']).update({

              verify:"false",
              
            });
          }
        }
      }
      else
      {
          var x = 30 - d.getDate() + 1;
          if(n.getDate() + x > 10)
          {
            a['verify'] ="false";
            firebase.database().ref('property').child(a['$key']).update({

              verify:"false",
              
            });
          }
      }

  }
  
}
}

                if(a['location'] === this.searchser.sharedData && a['verify']==="true")
                {
                this.pess.push(a as Property);
                }
              })
            })
            break;
    
          // When any one filter is selected this case will be selected and properties will be displayed accordingly.
          case 1:
            if(this.areaVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.area, this.filterForm.value.f1);
              s.snapshotChanges().subscribe(data => {
                this.pess = [];
                data.forEach(item => {
                  let a = item.payload.toJSON();
                  var d = new Date(a['Date']);
                  var n = new Date();
                  console.log(Date.now());
                  console.log("current",d.getDate())
                  console.log("STAMP - LAST EDITED",d);
                  console.log("CURRENT STAMP",n);
  if(a['verify'] === "true")                
  {
  if(n.getMonth() == d.getMonth())
  {
     if(n.getDate()-d.getDate() > 10) 
    {
      a['verify'] =="false";
      firebase.database().ref('property').child(a['$key']).update({
  
        verify:"false",
        
      });
    }
  }
  else
  {
    if((n.getMonth()+1)-(d.getMonth()+1) >= 1)
    {
        if(((d.getMonth()+1) %2) != 0 )
        {
            var x = 31 - d.getDate() + 1;
            if(n.getDate() + x > 10)
            {
              a['verify'] ="false";
              firebase.database().ref('property').child(a['$key']).update({
  
                verify:"false",
                
              });
            }
        }
        if((d.getMonth()+1) == 2)
        {
          if(((d.getFullYear() % 4 == 0) && (d.getFullYear() % 100 != 0)) || (d.getFullYear() % 400 == 0))
          {
            var x = 29 - d.getDay() +1;
            if(n.getDate() + x > 10)
            {
              a['verify'] ="false";
              firebase.database().ref('property').child(a['$key']).update({
  
                verify:"false",
                
              });
            }
          }
        }
        else
        {
            var x = 30 - d.getDate() + 1;
            if(n.getDate() + x > 10)
            {
              a['verify'] ="false";
              firebase.database().ref('property').child(a['$key']).update({
  
                verify:"false",
                
              });
            }
        }
  
    }
    
  }
  }
                  if(a['location'] === this.searchser.sharedData && a['verify']==="true")
                  {
                    a['$key'] = item.key;
                  this.pess.push(a as Property);
                  }
                })
              })
            } else if(this.typeVal === 1) {
              console.log(this.filterForm.value.f2);
              let s = this.crudService.GetPropertyByFilter(this.type, this.filterForm.value.f2);
              s.snapshotChanges().subscribe(data => {
                this.pess = [];
                data.forEach(item => {
                  let a = item.payload.toJSON();
                 
                  if(a['location'] === this.searchser.sharedData && a['verify']==="true")
                  {
                    a['$key'] = item.key;
                  this.pess.push(a as Property);
                  }
                })
              })
            } else if(this.priceVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.price, this.filterForm.value.f3);
              s.snapshotChanges().subscribe(data => {
                this.pess = [];
                data.forEach(item => {
                  let a = item.payload.toJSON();
                  
                  if(a['location'] === this.searchser.sharedData && a['verify']==="true")
                  {
                    a['$key'] = item.key;
                  this.pess.push(a as Property);
                  }
                })
              })
            } else if(this.BHKVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.BHK, this.filterForm.value.f4);
              s.snapshotChanges().subscribe(data => {
                this.pess = [];
                data.forEach(item => {
                  let a = item.payload.toJSON();
                 
                  if(a['location'] === this.searchser.sharedData && a['verify']==="true")
                  {
                    a['$key'] = item.key;
                  this.pess.push(a as Property);
                  }
                })
              })
            } else if(this.type11Val === 1) {
              let s = this.crudService.GetPropertyByFilter(this.type11, this.filterForm.value.f5);
              s.snapshotChanges().subscribe(data => {
                this.pess = [];
                data.forEach(item => {
                  let a = item.payload.toJSON();
                 
                  if(a['location'] === this.searchser.sharedData && a['verify']==="true")
                  {
                    a['$key'] = item.key;
                  this.pess.push(a as Property);
                  }
                })
              })
            }
            break;
    
          // When any two filters will be selected randomly this case will be selected and the properties will be displayed accordingly.
          case 2:
            if(this.areaVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.area, this.filterForm.value.f1);
              if(this.typeVal === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['type'] === this.filterForm.value.f2 && a['location'] === this.searchser.sharedData && a['verify']==="true") {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              } else if(this.priceVal === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['location'] === this.searchser.sharedData) {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              } else if(this.BHKVal === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['BHK'] === this.filterForm.value.f4 && a['location'] === this.searchser.sharedData && a['verify']==="true") {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              } else if(this.type11Val === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData && a['verify']==="true") {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              }
            }
    
            if(this.typeVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.type, this.filterForm.value.f2);
              if(this.priceVal === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['price'] >= this.filterForm.value.f3 && a['location'] === this.searchser.sharedData && a['verify']==="true") {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              } else if(this.BHKVal === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['BHK'] === this.filterForm.value.f4 && a['location'] === this.searchser.sharedData && a['verify']==="true") {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              } else if(this.type11Val === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['type11'] === this.filterForm.value.f5  && a['verify']==="true" && a['location'] === this.searchser.sharedData) {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              }
            }
    
            if(this.priceVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.price, this.filterForm.value.f3);
              if(this.BHKVal === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['BHK'] === this.filterForm.value.f4 && a['verify']==="true" && a['location'] === this.searchser.sharedData) {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              } else if(this.type11Val === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['type11'] === this.filterForm.value.f5  && a['verify']==="true" && a['location'] === this.searchser.sharedData) {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              }
            }
    
            if(this.BHKVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.BHK, this.filterForm.value.f4);
              if(this.type11Val === 1) {
                s.snapshotChanges().subscribe(data => {
                  this.pess = [];
                  data.forEach(item => {
                    let a = item.payload.toJSON();
                    if(a['type11'] === this.filterForm.value.f5 && a['verify']==="true" && a['location'] === this.searchser.sharedData) {
                      a['$key'] = item.key;
                      this.pess.push(a as Property);
                    }
                  })
                })
              }
            }
            break;
    
          // When any three filters are selected randomly this case will be selected and the properties will be displayed accordingly.
          case 3:
            if(this.areaVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.area, this.filterForm.value.f1);
              if(this.typeVal === 1) {
                if(this.priceVal === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['price'] >= this.filterForm.value.f3 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                } else if(this.BHKVal === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['BHK'] === this.filterForm.value.f4 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }else if(this.type11Val === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }
              } else if(this.priceVal === 1) {
                if(this.BHKVal === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['BHK'] === this.filterForm.value.f4 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                } else if(this.type11Val === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }
              } else if(this.BHKVal === 1) {
                if(this.type11Val === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['BHK'] === this.filterForm.value.f4 && a['verify']==="true" && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }
              }
            }
    
            if(this.typeVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.type, this.filterForm.value.f2);
              if(this.priceVal === 1) {
                if(this.BHKVal === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['BHK'] === this.filterForm.value.f4 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                } else if(this.type11Val === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }
              } else if(this.BHKVal === 1) {
                if(this.type11Val === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['BHK'] === this.filterForm.value.f4 && a['verify']==="true" && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }
              }
            }
    
            if(this.priceVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.price, this.filterForm.value.f3);
              if(this.BHKVal === 1) {
                if(this.type11Val === 1) {
                  s.snapshotChanges().subscribe(data => {
                    this.pess = [];
                    data.forEach(item => {
                      let a = item.payload.toJSON();
                      if(a['BHK'] === this.filterForm.value.f4 && a['verify']==="true" && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                        a['$key'] = item.key;
                        this.pess.push(a as Property);
                      }
                    })
                  })
                }
              }
            }
    
            break;
          
          // When any four filters are selected randomly this case will be selected and the properties will be displayed accordingly.
          case 4:
            if(this.areaVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.area, this.filterForm.value.f1);
              if(this.typeVal === 1) {
                if(this.priceVal === 1) {
                  if(this.BHKVal === 1) {
                    s.snapshotChanges().subscribe(data => {
                      this.pess = [];
                      data.forEach(item => {
                        let a = item.payload.toJSON();
                        if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['price'] >= this.filterForm.value.f3 && a['BHK'] === this.filterForm.value.f4 && a['location'] === this.searchser.sharedData) {
                          a['$key'] = item.key;
                          this.pess.push(a as Property);
                        }
                      })
                    })
                  } else if(this.type11Val === 1) {
                    s.snapshotChanges().subscribe(data => {
                      this.pess = [];
                      data.forEach(item => {
                        let a = item.payload.toJSON();
                        if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['price'] >= this.filterForm.value.f3 && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                          a['$key'] = item.key;
                          this.pess.push(a as Property);
                        }
                      })
                    })
                  }
                } else if(this.BHKVal === 1) {
                  if(this.type11Val === 1) {
                    s.snapshotChanges().subscribe(data => {
                      this.pess = [];
                      data.forEach(item => {
                        let a = item.payload.toJSON();
                        if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['BHK'] === this.filterForm.value.f4 && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                          a['$key'] = item.key;
                          this.pess.push(a as Property);
                        }
                      })
                    })
                  }
                }
              } else if(this.priceVal === 1) {
                if(this.BHKVal === 1) {
                  if(this.type11Val === 1) {
                    s.snapshotChanges().subscribe(data => {
                      this.pess = [];
                      data.forEach(item => {
                        let a = item.payload.toJSON();
                        if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['BHK'] === this.filterForm.value.f4 && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                          a['$key'] = item.key;
                          this.pess.push(a as Property);
                        }
                      })
                    })
                  }
                }
              }
            }
    
            if(this.typeVal === 1) {
              let s = this.crudService.GetPropertyByFilter(this.type, this.filterForm.value.f2);
              if(this.priceVal === 1) {
                if(this.BHKVal === 1) {
                  if(this.type11Val === 1) {
                    s.snapshotChanges().subscribe(data => {
                      this.pess = [];
                      data.forEach(item => {
                        let a = item.payload.toJSON();
                        if(a['price'] >= this.filterForm.value.f3 && a['verify']==="true" && a['BHK'] === this.filterForm.value.f4 && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                          a['$key'] = item.key;
                          this.pess.push(a as Property);
                        }
                      })
                    })
                  }
                }
              }
            }
            break;
    
          // When any five filters are selected randomly this case will be selected and the properties will be displayed accordingly.
          case 5:
            let s5 = this.crudService.GetPropertyByFilter(this.area, this.filterForm.value.f1);
            s5.snapshotChanges().subscribe(data => {
              this.pess = [];
              data.forEach(item => {
                let a = item.payload.toJSON();
                if(a['type'] === this.filterForm.value.f2 && a['verify']==="true" && a['price'] >= this.filterForm.value.f3 && a['BHK'] === this.filterForm.value.f4 && a['type11'] === this.filterForm.value.f5 && a['location'] === this.searchser.sharedData) {
                  a['$key'] = item.key;
                  this.pess.push(a as Property);
                }
              })
            })
            break;
        }
      }
}
