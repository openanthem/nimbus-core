'use strict';
import { TestBed, async } from '@angular/core/testing';

import { Value } from './value.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';

const declarations = [Value];
const imports = [];
const providers = [];

describe('Value', () => {

  configureTestSuite();
  setup(Value, declarations, imports, providers);

  beforeEach(async function(this: TestContext<Value>) {
  });

  it('should create the Value', async function (this: TestContext<Value>) {
    expect(this.hostComponent).toBeTruthy();
  });

});
