import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { StockService } from 'app/entities/stock/stock.service';
import { IStock } from 'app/shared/model/stock.model';

@Component({
  selector: 'jhi-inventory-external-transfer',
  templateUrl: './inventory-external-transfer.component.html',
  styleUrls: ['./inventory-external-transfer.component.scss'],
})
export class InventoryExternalTransferComponent implements OnInit {
  stock?: IStock | null = null;
  isSaving = false;
  isSaved = false;
  stockTransfer = '';
  externalTransferForm = this.fb.group({
    productCode: [],
    location: [],
    originQuantity: [],
    isOut: [null, [Validators.required]],
    quantity: [null, [Validators.required, Validators.min(0)]],
  });

  constructor(protected activatedRoute: ActivatedRoute, protected stockService: StockService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.loadStock();
  }

  loadForm(): void {
    this.externalTransferForm.patchValue({
      productCode: this.stock?.product?.code,
      location: this.stock?.location,
      originQuantity: this.stock?.quantity,
    });
  }

  loadStock(): void {
    this.activatedRoute.data.subscribe(({ stock }) => {
      this.stock = stock;
      this.loadForm();
    });
  }

  disableAllField(): void {
    this.externalTransferForm.get('isOut')?.disable();
    this.externalTransferForm.get('quantity')?.disable();
  }

  save(): void {
    const formData = new FormData();
    formData.append('id', this.stock?.id?.toString() || '0');
    formData.append('quantity', this.externalTransferForm.get(['quantity'])?.value);
    switch (this.stockTransfer) {
      case 'Stock Out':
        this.stockService.stocksOut(formData).subscribe(
          res => {
            this.isSaving = false;
            this.isSaved = true;
            this.disableAllField();
            this.stock = res.body;
            this.loadForm();
          },
          () => {
            this.isSaving = false;
          }
        );
        break;
      case 'Stock In':
        this.stockService.stocksIn(formData).subscribe(
          res => {
            this.isSaving = false;
            this.isSaved = true;
            this.disableAllField();
            this.stock = res.body;
            this.loadForm();
          },
          () => {
            this.isSaving = false;
          }
        );
        break;
    }
  }

  previousState(): void {
    window.history.back();
  }

  updateValidator(): void {
    let myField = this.externalTransferForm.get(['quantity']);
    switch (this.stockTransfer) {
      case 'Stock Out':
        myField?.clearValidators();
        myField?.setValidators([Validators.required, Validators.max(this.stock?.quantity || 0)]);
        myField?.updateValueAndValidity();
        break;
      case 'Stock In':
        myField = this.externalTransferForm.get(['quantity']);
        myField?.clearValidators();
        myField?.setValidators([Validators.required, Validators.min(0)]);
        myField?.updateValueAndValidity();
        break;
    }
  }
}
