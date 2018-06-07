import { TestBed, inject } from '@angular/core/testing';

import { LoaderService } from './loader.service';

describe('LoaderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoaderService]
    });
  });

  it('LoaderService should be created', inject([LoaderService], (service: LoaderService) => {
    expect(service).toBeTruthy();
  }));

  it('show() should update loaderUpdate subject with true value', inject([LoaderService], (service: LoaderService) => {
    let test;
    service.loaderUpdate.subscribe(val => {
        test = val;
    });
    service.show();
    expect(test.show).toEqual(true);
  }));

  it('hide() should update loaderUpdate subject with false value', inject([LoaderService], (service: LoaderService) => {
    let test;
    service.loaderUpdate.subscribe(val => {
        test = val;
    });
    service.hide();
    expect(test.show).toEqual(false);
  }));

});
