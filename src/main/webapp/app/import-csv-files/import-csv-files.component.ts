import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ImportCsvFilesService } from './import-csv-files.service';

@Component({
  selector: 'jhi-import-csv-files',
  templateUrl: './import-csv-files.component.html',
  styleUrls: ['./import-csv-files.component.scss'],
})
export class ImportCsvFilesComponent implements OnInit {
  myProductCsvFile = '';
  myStockCsvFile = '';
  isSaving = false;
  csvUploadForm = this.fb.group({
    productCsvFile: [],
  });
  constructor(
    private fb: FormBuilder,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected importCsvService: ImportCsvFilesService
  ) {}

  ngOnInit(): void {}

  previousState(): void {
    window.history.back();
  }

  setFileData(event: any, fileName: string): void {
    if (fileName === 'productCsvFile') {
      this.myProductCsvFile = event.target!.files[0];
    }
    if (fileName === 'StockCsvFile') {
      this.myStockCsvFile = event.target!.files[0];
    }
  }

  save(): void {
    if (this.myProductCsvFile) {
      this.isSaving = true;
      const formData = new FormData();
      formData.append('file', this.myProductCsvFile);
      this.importCsvService.importProducts(formData).subscribe(
        () => {
          this.isSaving = false;
        },
        err => {
          this.isSaving = false;
          console.error(err);
        }
      );
    }
    if (this.myStockCsvFile) {
      this.isSaving = true;
      const formData = new FormData();
      formData.append('file', this.myStockCsvFile);
      this.importCsvService.importStocks(formData).subscribe(
        () => {
          this.isSaving = false;
        },
        err => {
          this.isSaving = false;
          console.error(err);
        }
      );
    }
  }
}
