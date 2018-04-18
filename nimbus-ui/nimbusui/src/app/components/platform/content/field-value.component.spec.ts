'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { FieldValue } from './field-value.component';

describe('FieldValue', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          FieldValue
       ],
       imports: [
           HttpModule,
           HttpClientModule
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(FieldValue);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});