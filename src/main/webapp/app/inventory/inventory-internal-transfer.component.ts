import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StockService } from 'app/entities/stock/stock.service';
import { IProduct } from 'app/shared/model/product.model';
import { FormBuilder, Validators } from '@angular/forms';

import { IStock } from 'app/shared/model/stock.model';

@Component({
  selector: 'jhi-inventory-internal-transfer',
  templateUrl: './inventory-internal-transfer.component.html',
  styleUrls: ['./inventory-internal-transfer.component.scss'],
})
export class InventoryInternalTransferComponent implements OnInit {
  product: IProduct | null = null;
  stocks?: IStock[] = [];
  selectedLocationFrom: IStock | null = null;
  isSaving = false;
  isSaved = false;

  internalTransferForm = this.fb.group({
    productCode: [],
    locationFrom: [null, [Validators.required]],
    locationTo: [null, [Validators.required]],
    quantity: [null, [Validators.required, Validators.min(0)]],
  });

  constructor(protected activatedRoute: ActivatedRoute, protected stockService: StockService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.loadProduct();
  }

  loadForm(): void {
    this.internalTransferForm.patchValue({
      productCode: this.product?.code,
    });
  }

  loadProduct(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      this.loadStocks(product?.code || '');
      this.loadForm();
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

  previousState(): void {
    window.history.back();
  }

  updateValidator(): void {
    if (this.selectedLocationFrom) {
      this.internalTransferForm.get(['quantity'])?.setValidators(Validators.max(this.selectedLocationFrom?.quantity || 0));
    }
  }

  disableAllField(): void {
    this.internalTransferForm.get('locationFrom')?.disable();
    this.internalTransferForm.get('locationTo')?.disable();
    this.internalTransferForm.get('quantity')?.disable();
  }

  save(): void {
    this.isSaving = true;
    const formData = new FormData();
    formData.append('from', this.internalTransferForm.get(['locationFrom'])?.value?.id);
    formData.append('to', this.internalTransferForm.get(['locationTo'])?.value?.id);
    formData.append('quantity', this.internalTransferForm.get(['quantity'])?.value);
    this.stockService.stocksTransfer(formData).subscribe(
      () => {
        this.isSaving = false;
        this.isSaved = true;
        this.loadStocks(this.product?.code || '');
        this.disableAllField();
      },
      () => {
        this.isSaving = false;
      }
    );
  }
}
