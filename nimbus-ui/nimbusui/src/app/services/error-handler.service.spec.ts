import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TrackJsErrorHandler } from './error-handler.service';

let sut;

describe('TrackJsErrorHandler', () => {
  beforeEach(() => {
    sut = new TrackJsErrorHandler();
  });

  it('TrackJsErrorHandler should be created', async(() => {
    sut.handleError(null);
    expect(sut).toBeTruthy();
  }));

});