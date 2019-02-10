'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { SubHeaderCmp } from './sub-header.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { Param } from '../../shared/param-state';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { fieldValueParam } from 'mockdata';

const declarations = [
  SubHeaderCmp,
  DateTimeFormatPipe
 ];
const imports = [
     HttpClientModule,
     HttpModule
 ];
const providers = [];

let fixture, hostComponent;

describe('SubHeaderCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubHeaderCmp);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
  });

  it('should create the SubHeaderCmp',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // it('ngOnInit() should call loadLabelConfig()',  async(() => {
  //   (hostComponent as any).loadLabelConfig = (a: any) => {    }
  //   const spy = spyOn((hostComponent as any), 'loadLabelConfig').and.callThrough();
  //   hostComponent.ngOnInit();
  //   expect(spy).toHaveBeenCalled();  
  // }));

});
