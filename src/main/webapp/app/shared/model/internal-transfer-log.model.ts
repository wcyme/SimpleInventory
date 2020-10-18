import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IInternalTransferLog {
  id?: number;
  productCode?: string;
  locationFrom?: string;
  locationTo?: string;
  quantity?: number;
  createdDate?: Moment;
  user?: IUser;
}

export class InternalTransferLog implements IInternalTransferLog {
  constructor(
    public id?: number,
    public productCode?: string,
    public locationFrom?: string,
    public locationTo?: string,
    public quantity?: number,
    public createdDate?: Moment,
    public user?: IUser
  ) {}
}
