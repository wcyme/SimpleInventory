import { IProduct } from 'app/shared/model/product.model';

export interface IStock {
  id?: number;
  location?: string;
  quantity?: number;
  product?: IProduct;
}

export class Stock implements IStock {
  constructor(public id?: number, public location?: string, public quantity?: number, public product?: IProduct) {}
}
