import { TestBed, inject } from '@angular/core/testing';

import { KubecheckService } from './kubecheck.service';

describe('KubecheckService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [KubecheckService]
    });
  });

  it('should be created', inject([KubecheckService], (service: KubecheckService) => {
    expect(service).toBeTruthy();
  }));
});
