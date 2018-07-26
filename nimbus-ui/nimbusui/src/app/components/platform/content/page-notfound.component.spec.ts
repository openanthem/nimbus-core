'use strict';
import { TestBed, async } from '@angular/core/testing';

import { PageNotfoundComponent } from './page-notfound.component';
import { LoggerService } from '../../../services/logger.service';

describe('PageNotfoundComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PageNotfoundComponent
       ],
       providers: [
        LoggerService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(PageNotfoundComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});