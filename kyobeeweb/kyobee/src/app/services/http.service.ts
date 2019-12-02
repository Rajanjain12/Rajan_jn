import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, from } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { DataService } from './data.service';
import { IResponse } from '../DTO/Response';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private router: Router, private http: HttpClient, private dataService: DataService) { }

  changeView(path:string){
    this.router.navigate([path]);
  }
  changeViewWithPara(path:string,para:string){
    this.router.navigate([path,para]);
  }

  postService(url, postBody): Observable<IResponse>{
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let params = {};
    return this.http.post<IResponse>(environment.serverUrl+url, postBody, {headers,params}).pipe(catchError(this.errorHandler));
  }

  getDataService(url, requestParams): Observable<IResponse>{
    let headers = new HttpHeaders({ 'Content-Type': 'application/json'});
    let params = requestParams;
    return this.http.get<IResponse>(environment.serverUrl+url, {headers, params}).pipe(catchError(this.errorHandler));
  }

  errorHandler(error: HttpErrorResponse){
    return Observable.throw(error.message || "Server Error");
    
  }
}
