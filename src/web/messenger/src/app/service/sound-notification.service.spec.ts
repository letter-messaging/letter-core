import {TestBed} from '@angular/core/testing';

import {SoundNotificationService} from './sound-notification.service';

describe('SoundNotificationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SoundNotificationService = TestBed.get(SoundNotificationService);
    expect(service).toBeTruthy();
  });
});
