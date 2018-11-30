'use strict';
import { TestBed, async } from '@angular/core/testing';

import { Value } from './value.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';

const declarations = [Value];
const imports = [];
const providers = [];
let fixture, hostComponent;
describe('Value', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(Value);
    hostComponent = fixture.debugElement.componentInstance;
  });

  it('should create the Value', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

});
