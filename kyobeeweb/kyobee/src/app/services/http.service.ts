import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IResponse } from '../DTO/Response';
import { HttpHeaders, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { DataService } from './data.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private router: Router, private http: HttpClient, private dataService: DataService) { }

  changeView(path:string){
    this.router.navigate([path]);
  }

  postService(url, postBody): Observable<IResponse>{
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    let params = {};
    return this.http.post<IResponse>(this.dataService.serverUrl+url, postBody, {headers,params}).pipe(catchError(this.errorHandler));
  }

  getDataService(url, requestParams): Observable<IResponse>{
    let headers = new HttpHeaders({ 'Content-Type': 'application/json'});
    let params = requestParams;
    return this.http.get<IResponse>(this.dataService.serverUrl+url, {headers, params}).pipe(catchError(this.errorHandler));
  }

  errorHandler(error: HttpErrorResponse){
    return Observable.throw(error.message || "Server Error");
    
  }
}
