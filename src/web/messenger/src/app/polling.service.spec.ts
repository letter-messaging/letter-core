import { TestBed } from '@angular/core/testing';

import { PollingService } from './polling.service';

describe('PollingService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PollingService = TestBed.get(PollingService);
    expect(service).toBeTruthy();
  });
});
