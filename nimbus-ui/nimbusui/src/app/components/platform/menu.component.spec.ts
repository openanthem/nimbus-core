'use strict';
import { TestBed, async } from '@angular/core/testing';

import { Menu } from './menu.component';
import { Link } from '../platform/link.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';
import { Param } from '../../shared/param-state';

let param: Param;

const declarations = [
  Menu,
  Link
 ];
const imports = [];
const providers = [];

describe('Menu', () => {

  configureTestSuite();
  setup(Menu, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<Menu>){
      this.hostComponent.element = param;
  });

  it('should create the Menu', function(this: TestContext<Menu>) {
    expect(this.hostComponent).toBeTruthy();
  });

});