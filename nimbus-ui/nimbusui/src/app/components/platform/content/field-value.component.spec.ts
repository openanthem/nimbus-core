'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { FieldValue } from './field-value.component';
import { FormErrorMessage } from '../form-error-message.component';
import { InputLabel } from '../form/elements/input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { Param } from '../../../shared/param-state';
import { fieldValueParam } from 'mockdata';

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
let fixture, hostComponent;
describe('FieldValue', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldValue);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam as Param;
  });

  it('should create the FieldValue', async(() => {    
    expect(hostComponent).toBeTruthy();
  }));

});