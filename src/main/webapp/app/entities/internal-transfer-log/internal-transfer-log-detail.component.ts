import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInternalTransferLog } from 'app/shared/model/internal-transfer-log.model';

@Component({
  selector: 'jhi-internal-transfer-log-detail',
  templateUrl: './internal-transfer-log-detail.component.html',
})
export class InternalTransferLogDetailComponent implements OnInit {
  internalTransferLog: IInternalTransferLog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ internalTransferLog }) => (this.internalTransferLog = internalTransferLog));
  }

  previousState(): void {
    window.history.back();
  }
}
