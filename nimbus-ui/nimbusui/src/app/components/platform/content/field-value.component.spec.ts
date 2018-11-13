'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { FieldValue } from './field-value.component';
import * as data from '../../../payload.json';
import { FormErrorMessage } from '../form-error-message.component';
import { InputLabel } from '../form/elements/input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { Param } from '../../../shared/param-state';

let param: Param;

const declarations = [
  FieldValue,
  FormErrorMessage,
  InputLabel,
  TooltipComponent
];
const imports = [
   HttpModule,
   HttpClientModule
];
const providers = [];

describe('FieldValue', () => {
  configureTestSuite();
  setup(FieldValue, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<FieldValue>){
    this.hostComponent.element = param;
  });

  it('should create the FieldValue', async function (this: TestContext<FieldValue>) {
    expect(this.hostComponent).toBeTruthy();
  });

});