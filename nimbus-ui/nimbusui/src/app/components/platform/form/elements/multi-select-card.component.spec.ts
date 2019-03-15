/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Param } from './../../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Subject } from 'rxjs';
import { of as observableOf,  Observable } from 'rxjs';

import { MultiselectCard } from './multi-select-card.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { FormGroup, ValidatorFn, Validators, FormControl } from '@angular/forms';
import { multiselectCardElement } from 'mockdata';
import { By } from '@angular/platform-browser';

let param, pageService;

class MockPageService {
    eventUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
    }
    postOnChange(a, b, c) { }
    logError(a) {
        this.eventUpdate$.next(a);
    }
}

const declarations = [  MultiselectCard];
const imports = [
   HttpModule,
   HttpClientTestingModule,
   StorageServiceModule
];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  {provide: PageService, useClass: MockPageService},
   CustomHttpClient,
   LoaderService,
   ConfigService,
   LoggerService,
   AppInitService,
   SessionStoreService
];
let fixture, hostComponent;

describe('MultiselectCard', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MultiselectCard);
    hostComponent = fixture.debugElement.componentInstance;
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(multiselectCardElement.config.code, new FormControl(multiselectCardElement.leafState, checks));
    hostComponent.form = fg;
    hostComponent.element = multiselectCardElement;
    pageService = TestBed.get(PageService);
  });

  it('should create the MultiselectCard', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('span to display code should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allSpanEles = debugElement.queryAll(By.css('span'));
    expect(allSpanEles[0].nativeElement.innerText).toEqual('testingmulticard');
  }));

  it('span to display value should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allSpanEles = debugElement.queryAll(By.css('span'));
    expect(allSpanEles[1].nativeElement.innerText).toEqual('testing multi select card label');
  }));

  it('onClick of anchor tag should call selectOption()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const anchorEle = debugElement.query(By.css('a'));
    spyOn(hostComponent, 'selectOption').and.callThrough()
    anchorEle.nativeElement.click();
    expect(hostComponent.selectOption).toHaveBeenCalledWith('testingmulticard', hostComponent);
  }));

  it('set value() should update the value property', async(() => {
    hostComponent.value = 'test';
    expect(hostComponent.value).toEqual('test');
  }));

  it('registerOnChange() should update the onChange property', async(() => {
    hostComponent.registerOnChange('test');
    expect(hostComponent.onChange).toEqual('test');
  }));

  it('writeValue() should update the value property', async(() => {
    hostComponent.writeValue('test');
    hostComponent.writeValue('test');
    expect(hostComponent.value).toEqual('test');
  }));

  it('registerOnTouched() should update the onTouched property', async(() => {
    hostComponent.registerOnTouched('test');
    expect(hostComponent.onTouched).toEqual('test');
  }));

  it('toggleChecked() should return true', async(() => {
    (hostComponent as any).selectedOptions[0] = 'test';
    expect(hostComponent.toggleChecked('test')).toBeTruthy();
  }));

  it('toggleChecked() should return false', async(() => {
    (hostComponent as any).selectedOptions[0] = 'test';
    expect(hostComponent.toggleChecked('test1')).toBeFalsy();
  }));

  it('selectOption() should update the value property based on valid argumnet', async(() => {
    const selectedOptions = (hostComponent as any).selectedOptions;
    selectedOptions[0] = 'test';
    hostComponent.selectOption('test', '');
    expect(hostComponent.value).toEqual(selectedOptions);
  }));

  it('selectOption() should update the value property', async(() => {
    const selectedOptions = (hostComponent as any).selectedOptions;
    selectedOptions[0] = 'test';
    hostComponent.selectOption('123', '');
    expect(hostComponent.value).toEqual(selectedOptions);
  }));

  it('setState() should call the pageService.postOnChange()', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.leafState = '';
      hostComponent.element.config.uiStyles.attributes.postEventOnChange = true;
      spyOn(pageService, 'postOnChange').and.callThrough();
      hostComponent.setState('t');
      expect(pageService.postOnChange).toHaveBeenCalled();
    });
  });

  it('ngOnInit() should update the selectedOptions and call form.controls.a.setValue()', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.leafState = '';
      hostComponent.element.path = 'test';
      spyOn(hostComponent, 'setState').and.returnValue('');
      spyOn(hostComponent.form.controls.firstName, 'setValue').and.callThrough();
      const eve = { config: { code: 'firstName' }, path: 'test', leafState: '' };
      hostComponent.ngOnInit();
      pageService.logError(eve);
      expect((hostComponent as any).selectedOptions).toEqual('');
      expect(hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();
    });
  });
    
});

