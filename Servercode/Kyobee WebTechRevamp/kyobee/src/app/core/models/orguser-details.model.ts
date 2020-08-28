import { OrganizationDTO } from './organization.model';
import { LoginDTO } from './loginDTO.model';

export class OrgUserDetailsDTO {
  orgDTO: OrganizationDTO;
  credDTO: LoginDTO;
  active: number;
}
