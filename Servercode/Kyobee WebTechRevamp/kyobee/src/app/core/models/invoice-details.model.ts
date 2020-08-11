export class InvoiceDetailsDTO {
  invoiceID: number;
  invoiceDate: Date;
  title: Array<string>;
  amount: number;
  status: number;
  invoiceUrl: string;
}
