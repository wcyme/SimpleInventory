import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StockService } from 'app/entities/stock/stock.service';
import { IProduct, Product } from 'app/shared/model/product.model';
import { IStock } from 'app/shared/model/stock.model';

@Component({
  selector: 'jhi-inventory-product-detail',
  templateUrl: './inventory-product-detail.component.html',
  styleUrls: ['./inventory-product-detail.component.scss'],
})
export class InventoryProductDetailComponent implements OnInit {
  product: IProduct | null = null;
  stocks?: IStock[] = [];

  constructor(protected activatedRoute: ActivatedRoute, protected stockService: StockService) {}

  ngOnInit(): void {
    this.product = new Product();
    this.product.code = '';
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      this.loadStocks(product?.code || '');
    });
  }

  loadStocks(productCode: string): void {
    if (productCode) {
      this.stockService.findByProductCode(productCode).subscribe(
        (res: HttpResponse<IStock[]>) => {
          this.stocks = res.body || [];
        },
        err => {
          console.error(err);
        }
      );
    }
  }
}
