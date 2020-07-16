import { AddressDTO } from './address.model';

export class OrganizationDTO {
  addressDTO: AddressDTO;
  email: string;
  organizationName: string;
  primaryPhone: BigInteger;
  secondaryPhone: BigInteger;
  orgId: number;
  customerId: number;
  orgTypeId: number;
  timezoneId: number;

  OrganizationDTO() {
    this.addressDTO = new AddressDTO();
  }
}
