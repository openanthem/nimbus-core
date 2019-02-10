'use strict';
import { TestBed, async } from '@angular/core/testing';

import { Menu } from './menu.component';
import { Link } from '../platform/link.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { Param } from '../../shared/param-state';
import { fieldValueParam } from 'mockdata';

const declarations = [
  Menu,
  Link
 ];
const imports = [];
const providers = [];

let fixture, hostComponent;

describe('Menu', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach( () => {
    fixture = TestBed.createComponent(Menu);
    hostComponent = fixture.debugElement.componentInstance;
      hostComponent.element = fieldValueParam;
  });

  it('should create the Menu',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

});