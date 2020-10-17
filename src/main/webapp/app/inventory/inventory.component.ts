import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductDeleteDialogComponent } from 'app/entities/product/product-delete-dialog.component';
import { ProductService } from 'app/entities/product/product.service';
import { IProduct } from 'app/shared/model/product.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss'],
})
export class InventoryComponent implements OnInit, OnDestroy {
  products?: IProduct[];
  eventSubscriber?: Subscription;
  constructor(protected productService: ProductService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.productService.queryWithStock().subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProducts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProduct): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProducts(): void {
    this.eventSubscriber = this.eventManager.subscribe('productListModification', () => this.loadAll());
  }

  delete(product: IProduct): void {
    const modalRef = this.modalService.open(ProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.product = product;
  }
}
