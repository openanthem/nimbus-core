'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { SubHeaderCmp } from './sub-header.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { Param } from '../../shared/param-state';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';

let param: Param;

const declarations = [
  SubHeaderCmp,
  DateTimeFormatPipe
 ];
const imports = [
     HttpClientModule,
     HttpModule
 ];
const providers = [];

describe('SubHeaderCmp', () => {

  configureTestSuite();
  setup(SubHeaderCmp, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<SubHeaderCmp>){
      this.hostComponent.element = param;
  });

  it('should create the SubHeaderCmp', function(this: TestContext<SubHeaderCmp>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngOnInit() should call loadLabelConfig()', function(this: TestContext<SubHeaderCmp>) {
    (this.hostComponent as any).loadLabelConfig = (a: any) => {    }
    const spy = spyOn((this.hostComponent as any), 'loadLabelConfig').and.callThrough();
    this.hostComponent.ngOnInit();
    expect(spy).toHaveBeenCalled();  });

});
