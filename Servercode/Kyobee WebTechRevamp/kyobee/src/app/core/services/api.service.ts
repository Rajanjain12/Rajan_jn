import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private formatErrors(error: any) {
    return throwError(error.error);
  }

  constructor(private http: HttpClient) {}

  post(path: string, body): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.append('Accept', 'application/vnd.kyobee.v1+json');
    return this.http
      .post(`${environment.serverUrl}${path}`, JSON.stringify(body), { headers })
      .pipe(catchError(this.formatErrors));
  }
  put(path: string, body): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.append('Accept', 'application/vnd.kyobee.v1+json');
    return this.http
      .put(`${environment.serverUrl}${path}`, JSON.stringify(body), { headers })
      .pipe(catchError(this.formatErrors));
  }
  get(path: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.set('Accept', 'application/vnd.kyobee.v1+json');
    return this.http.get(`${environment.serverUrl}${path}`, { headers }).pipe(catchError(this.formatErrors));
  }
  getParams(path: string, params: HttpParams = new HttpParams()): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.set('Accept', 'application/vnd.kyobee.v1+json');
    return this.http.get(`${environment.serverUrl}${path}`, { headers, params }).pipe(catchError(this.formatErrors));
  }

  postParams(path: string, params: HttpParams): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.append('Accept', 'application/vnd.kyobee.v1+json');
    const postbody = {};
    return this.http
      .post(`${environment.serverUrl}${path}`, postbody, { headers, params })
      .pipe(catchError(this.formatErrors));
  }

  putParams(path: string, params: HttpParams): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.append('Accept', 'application/vnd.kyobee.v1+json');
    const postbody = {};
    return this.http
      .put(`${environment.serverUrl}${path}`, postbody, { headers, params })
      .pipe(catchError(this.formatErrors));
  }
  deleteParams(path: string, params: HttpParams): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers.append('Accept', 'application/vnd.kyobee.v1+json');
    const postbody = {};
    return this.http.delete(`${environment.serverUrl}${path}`, { headers, params }).pipe(catchError(this.formatErrors));
  }
}
