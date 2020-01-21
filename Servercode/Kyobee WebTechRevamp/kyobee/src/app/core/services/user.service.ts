import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private apiService: ApiService) { }

  login(request : object) {
    return this.apiService.post('rest/user/login',request);
   }
}
