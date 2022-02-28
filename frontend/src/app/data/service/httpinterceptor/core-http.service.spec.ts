import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';

import { CoreHttpService } from './core-http.service';

describe('CoreHttpService', () => {
  let service: CoreHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ]
    });
    service = TestBed.inject(CoreHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
