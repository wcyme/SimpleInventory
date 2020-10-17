export interface IProduct {
  id?: number;
  name?: string;
  code?: string;
  weight?: number;
  totalStock?: number;
}

export class Product implements IProduct {
  constructor(public id?: number, public name?: string, public code?: string, public weight?: number, public totalStock?: number) {}
}
