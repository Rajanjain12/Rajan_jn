import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { Observable ,  throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {environment} from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private formatErrors(error: any) {
    return  throwError(error.error);
  }

  constructor(private http: HttpClient) { }

  post(path: string, body): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });   
    return this.http.post(
      `${environment.serverUrl}${path}`,
      JSON.stringify(body),
      {headers}
    ).pipe(catchError(this.formatErrors));
  }

  get(path: string, params: HttpParams = new HttpParams()): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });   
    return this.http.get(`${environment.serverUrl}${path}`, { headers,params })
      .pipe(catchError(this.formatErrors));
  }
}