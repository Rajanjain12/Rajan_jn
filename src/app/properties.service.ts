import { Injectable } from '@angular/core';
import { AngularFireDatabase } from '@angular/fire/database';
import { BehaviorSubject, Observable } from 'rxjs';
import {
  map,
  switchMap,
  debounceTime,
  distinctUntilChanged
} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PropertiesService {

  constructor(private db: AngularFireDatabase) {}
  getproperties(start: BehaviorSubject<string>): Observable<any[]> {
    return start.pipe(
      switchMap(startText => {
        const endText = startText + '\uf8ff';
        return this.db
          .list('/property', ref =>
            ref
              .orderByChild('location')

              .startAt(startText)
              .endAt(endText)
          )
          .snapshotChanges()
          .pipe(
            debounceTime(500),
            distinctUntilChanged(),
            map(changes => {
              return changes.map(c => {
                return { key: c.payload.key, ...c.payload.exportVal()};
              });
            })
          );
      })
    );
  }
  


  // constructor(private db:AngularFireDatabase) {}

  // // getproperties(start1:string,end1:string): AngularFireList<any>{

  // //   return this.db.list('/property', ref => ref.orderByChild('location').startAt(start1).endAt(end1).limitToFirst(10));
  // // }

}
