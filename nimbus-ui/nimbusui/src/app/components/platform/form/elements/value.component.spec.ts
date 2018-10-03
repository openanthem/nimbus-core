'use strict';
import { TestBed, async } from '@angular/core/testing';

import { Value } from './value.component';

describe('Value', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Value
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(Value);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});
