import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { InternalTransferLogService } from 'app/entities/internal-transfer-log/internal-transfer-log.service';
import { IInternalTransferLog, InternalTransferLog } from 'app/shared/model/internal-transfer-log.model';

describe('Service Tests', () => {
  describe('InternalTransferLog Service', () => {
    let injector: TestBed;
    let service: InternalTransferLogService;
    let httpMock: HttpTestingController;
    let elemDefault: IInternalTransferLog;
    let expectedResult: IInternalTransferLog | IInternalTransferLog[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(InternalTransferLogService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new InternalTransferLog(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should return a list of InternalTransferLog', () => {
        const returnedFromService = Object.assign(
          {
            productCode: 'BBBBBB',
            locationFrom: 'BBBBBB',
            locationTo: 'BBBBBB',
            quantity: 1,
            createdDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
