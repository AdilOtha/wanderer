import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';

import { PinService } from './pin.service';

describe('PinService', () => {
  let service: PinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ]
    });
    service = TestBed.inject(PinService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
