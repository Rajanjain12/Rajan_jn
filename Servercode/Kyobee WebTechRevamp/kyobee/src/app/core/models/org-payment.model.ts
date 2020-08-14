export class OrgPaymentDTO {
  organizationSubscriptionID: number;
  organizationCardDetailID: number;
  invoiceID: number;
  orgID: number;
  amount: number;
  paymentNonce: string;
  renewalType: string;
}
