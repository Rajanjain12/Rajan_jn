import { InvoiceDTO } from './invoice.model';

export class DiscountDTO {
  amount: number;
  promoCode: string;
  invoiceDTO: InvoiceDTO;
}
