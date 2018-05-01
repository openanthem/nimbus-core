'use strict';
import { TestBed, async } from '@angular/core/testing';
import { AccordionGroup } from './accordion-group.component';
import { ElementRef } from '@angular/core';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { Accordion } from './accordion.component';
import { WebContentSvc } from '../../services/content-management.service';

let fixture, app, elementRef, webContentSvc;

class MockElementRef {
}

describe('Accordion', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Accordion
       ],
       imports: [
        HttpModule, 
        HttpClientTestingModule 
       ],
       providers: [
        {provide: ElementRef, useClass: MockElementRef},
        WebContentSvc
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(Accordion);
    app = fixture.debugElement.componentInstance;
    elementRef = TestBed.get(ElementRef);
    webContentSvc = TestBed.get(WebContentSvc);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('addGroup() should update the app.groups', async(() => {
      const aGroup = new AccordionGroup(app, webContentSvc, elementRef);
      const groupsLength = app.groups.length;
      app.addGroup(aGroup);
      expect(groupsLength < app.groups.length).toBeTruthy();
  }));

  it('closeOthers() should not app.groups', async(() => {
    app.groups = [{state: 'test'}];
    app.closeOthers(app.groups[0]);
    expect(app.groups[0].state).not.toEqual('closedPanel');
  }));

  it('closeOthers() should not app.groups', async(() => {
    const tAccGroup = new AccordionGroup(app, webContentSvc, elementRef);
    app.groups = [{state: 'test'}];
    app.closeOthers(tAccGroup);
    expect(app.groups[0].state).toEqual('closedPanel');
  }));

  it('openAll() should update the app.groups', async(() => {
    const tAccGroup = new AccordionGroup(app, webContentSvc, elementRef);
    app.groups = [{state: 'test'}];
    app.openAll(tAccGroup);
    expect(app.groups[0].state).toEqual('openPanel');
  }));

  it('openAll() should not update the app.groups', async(() => {
    app.groups = [{state: 'test'}];
    app.openAll(app.groups[0]);
    expect(app.groups[0].state).toEqual('test');
  }));

  it('closeOthers() should not update the app.groups', async(() => {
    app.groups = [{state: 'test'}];
    const aGroups = {state: 'test'};
    app.closeOthers(aGroups);
    expect(app.groups[0].state).not.toEqual('openPanel');
  }));

  it('removeGroup() should not update the app.groups', async(() => {
    app.groups = [{state: 'test'}];
    const aGroups = {state: 'test'};
    app.removeGroup(aGroups);
    expect(app.groups.length).toEqual(1);
  }));

  it('removeGroup() should update the app.groups', async(() => {
    app.groups = [{state: 'test'}];
    const aGroups = {state: 'test'};
    app.removeGroup(app.groups[0]);
    expect(app.groups.length).toEqual(0);
  }));

  it('7should create the app', async(() => {
    app.groups = [{state: 'test'}];
    const aGroups = {state: 'test1'};
    app.removeGroup(aGroups);
    expect(app.groups).toEqual([{state: 'test'}]);
  }));

});