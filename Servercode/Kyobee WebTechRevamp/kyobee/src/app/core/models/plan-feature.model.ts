import { PlanFeatureChargeDTO } from './plan-feature-charge.model';

export class PlanFeatureDTO {
  featureId: number;
  featureName: string;
  featureDesc: string;
  featureChargeDetails: Array<PlanFeatureChargeDTO>;
}
