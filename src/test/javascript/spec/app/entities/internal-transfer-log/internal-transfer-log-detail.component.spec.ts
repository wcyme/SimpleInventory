import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SimpleInventoryTestModule } from '../../../test.module';
import { InternalTransferLogDetailComponent } from 'app/entities/internal-transfer-log/internal-transfer-log-detail.component';
import { InternalTransferLog } from 'app/shared/model/internal-transfer-log.model';

describe('Component Tests', () => {
  describe('InternalTransferLog Management Detail Component', () => {
    let comp: InternalTransferLogDetailComponent;
    let fixture: ComponentFixture<InternalTransferLogDetailComponent>;
    const route = ({ data: of({ internalTransferLog: new InternalTransferLog(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SimpleInventoryTestModule],
        declarations: [InternalTransferLogDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(InternalTransferLogDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InternalTransferLogDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load internalTransferLog on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.internalTransferLog).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
