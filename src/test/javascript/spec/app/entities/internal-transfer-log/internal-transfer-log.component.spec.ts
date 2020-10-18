import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { SimpleInventoryTestModule } from '../../../test.module';
import { InternalTransferLogComponent } from 'app/entities/internal-transfer-log/internal-transfer-log.component';
import { InternalTransferLogService } from 'app/entities/internal-transfer-log/internal-transfer-log.service';
import { InternalTransferLog } from 'app/shared/model/internal-transfer-log.model';

describe('Component Tests', () => {
  describe('InternalTransferLog Management Component', () => {
    let comp: InternalTransferLogComponent;
    let fixture: ComponentFixture<InternalTransferLogComponent>;
    let service: InternalTransferLogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SimpleInventoryTestModule],
        declarations: [InternalTransferLogComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(InternalTransferLogComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InternalTransferLogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InternalTransferLogService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new InternalTransferLog(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.internalTransferLogs && comp.internalTransferLogs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new InternalTransferLog(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.internalTransferLogs && comp.internalTransferLogs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
