import { TestBed } from '@angular/core/testing';

import { BackgroundUnreadService } from './background-unread.service';

describe('BackgroundUnreadService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BackgroundUnreadService = TestBed.get(BackgroundUnreadService);
    expect(service).toBeTruthy();
  });
});
