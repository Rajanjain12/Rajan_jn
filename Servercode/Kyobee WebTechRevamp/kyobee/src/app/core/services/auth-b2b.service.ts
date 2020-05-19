import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { HttpParams } from '@angular/common/http';
import { OrganizationDTO } from 'src/app/core/models/organization.model';

@Injectable({
  providedIn: 'root'
})
export class AuthB2BService {
  constructor(private apiService: ApiService) {}

  organization: OrganizationDTO = new OrganizationDTO();

  setOrganizationData(data) {
    this.organization = data;
  }
}
