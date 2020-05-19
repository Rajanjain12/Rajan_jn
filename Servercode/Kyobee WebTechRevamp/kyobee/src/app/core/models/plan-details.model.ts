import { PlanDTO } from './plan.model';
import { PlanTermDTO } from './plan-term.model';
export class PlanDetailsDTO {
  planList: Array<PlanDTO>;
  termList: Array<PlanTermDTO>;
}
