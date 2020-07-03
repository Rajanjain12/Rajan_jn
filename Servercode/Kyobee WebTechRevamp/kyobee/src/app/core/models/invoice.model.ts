import { OrganizationDTO } from './organization.model';

export class InvoiceDTO {
  featureChargeIds: Array<number>;
  orgSubscriptionId: number;
  orgDTO: OrganizationDTO;
}
