import { PlanFeatureDTO } from './plan-feature.model';
export class PlanTermDTO {
  termID: number;
  termName: string;
  featureList: Array<PlanFeatureDTO>;
}
