'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { SubHeaderCmp } from './sub-header.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { Param } from '../../shared/param-state';

describe('SubHeaderCmp', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        SubHeaderCmp,
        DateTimeFormatPipe
       ],
       imports: [
           HttpClientModule,
           HttpModule
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(SubHeaderCmp);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should call loadLabelConfig()', async(() => {
    const fixture = TestBed.createComponent(SubHeaderCmp);
    const app = fixture.debugElement.componentInstance;
    app.loadLabelConfig = (a:any) => {    }
    spyOn(app, 'loadLabelConfig').and.callThrough();
    app.ngOnInit();
    expect(app.loadLabelConfig).toHaveBeenCalled();
  }));

});
