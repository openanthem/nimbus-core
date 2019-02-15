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

import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClientModule, HttpClient, HttpRequest } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { SESSION_STORAGE, StorageServiceModule } from 'angular-webstorage-service';
import { Subject } from 'rxjs/Subject';

import { ControlSubscribers } from './control-subscribers.service';
import { PageService } from './page.service';
import { CustomHttpClient } from './httpclient.service';
import { LoaderService } from './loader.service';
import { ConfigService } from './config.service';
import { LoggerService } from './logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './session.store';
import { AppInitService } from './app.init.service';
import { ValidationUtils } from '../components/platform/validators/ValidationUtils';

let http, backend, service, pageService;

class MockPageService {
    eventUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;
    validationUpdate = {
        unsubscribe: () => {}
    };
    eventUpdate = {
        unsubscribe: () => {}
    };
    constructor() {
        this.eventUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
    }

    logError(data) {
        this.eventUpdate$.next(data);
    }

    notifyErrorEvent(data) {
        this.validationUpdate$.next(data);
    }

    postOnChange(a, b, c) {    }
    processEvent(a, b, c, d) { }
}

describe('ControlSubscribers', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
        imports: [
            HttpModule,
            HttpClientModule,
            HttpClientTestingModule,
            StorageServiceModule
        ],
      providers: [
          { provide: 'JSNLOG', useValue: JL },
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          { provide: PageService, useClass: MockPageService },
          ControlSubscribers,
          CustomHttpClient,
          LoaderService,
          ConfigService,
          LoggerService,
          SessionStoreService,
          AppInitService
        ]
    });
    http = TestBed.get(HttpClient);
    backend = TestBed.get(HttpTestingController);
    service = TestBed.get(ControlSubscribers);
    pageService = TestBed.get(PageService);
  });

    it('should be created', async(() => {
      expect(service).toBeTruthy();
    }));

    it('logError() should call control.form.controls[eve.config.code].setValue()', async(() => {
      const eve = { config: { code: 'a' }, path: '/t', leafState: '' };
      const control = { element: { path: '/t' }, form: { controls: { a: { setValue: a => {}, reset: () => {} } } } };
      spyOn(control.form.controls.a, 'setValue').and.callThrough();
      service.stateUpdateSubscriber(control);
      pageService.logError(eve);
      expect(control.form.controls.a.setValue).toHaveBeenCalled();
    }));

    it('logError() should call control.form.controls[eve.config.code].reset()', async(() => {
      const eve = { config: { code: 'a' }, path: '/t', leafState: null };
      const control = { element: { path: '/t' }, form: { controls: { a: { setValue: a => {}, reset: () => {} } } } };
      spyOn(control.form.controls.a, 'setValue').and.callThrough();
      spyOn(control.form.controls.a, 'reset').and.callThrough();
      service.stateUpdateSubscriber(control);
      pageService.logError(eve);
      expect(control.form.controls.a.setValue).not.toHaveBeenCalled();
      expect(control.form.controls.a.reset).toHaveBeenCalled();
    }));

    it('notifyErrorEvent() should call ValidationUtils.rebindValidations()', async(() => {
      const eve = { path: '/t', config: { code: 'a' }, activeValidationGroups: [1], enabled: true };
      const control = { element: { path: '/t' }, requiredCss: '', disabled: '', form: { controls: { a: { setValidators: a => {} } } } };
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
      spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
      service.validationUpdateSubscriber(control);
      pageService.notifyErrorEvent(eve);
      expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
    }));

    it('notifyErrorEvent() should call ValidationUtils.applyelementStyle(), ValidationUtils.buildStaticValidations() and update disabled', async(() => {
      const eve = { path: '/t', config: { code: 'a' }, activeValidationGroups: [], enabled: true };
      const control = { element: { path: '/t' }, requiredCss: '', disabled: '', form: { controls: { a: { setValidators: a => {} } } } };
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('');
      spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('');
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue('');
      spyOn(ValidationUtils, 'buildStaticValidations').and.returnValue('');
      service.validationUpdateSubscriber(control);
      pageService.notifyErrorEvent(eve);
      expect(ValidationUtils.rebindValidations).not.toHaveBeenCalled();
      expect(ValidationUtils.applyelementStyle).toHaveBeenCalled();
      expect(ValidationUtils.buildStaticValidations).toHaveBeenCalled();
      expect(control.disabled).toBeFalsy();
    }));

    it('onChangeEventSubscriber() should call pageService.postOnChange()', async(() => {
      const control = { element: { config: { uiStyles: { attributes: { postButtonUrl: '/t' } } } } };
      const eve = { leafState: '', config: { uiStyles: { attributes: { postEventOnChange: true } } } };
      service.previouLeafState = 'test';
      service.controlValueChanged = new Subject();
      spyOn(pageService, 'postOnChange').and.callThrough();
      service.onChangeEventSubscriber(control);
      service.controlValueChanged.next(eve);
      expect(pageService.postOnChange).toHaveBeenCalled();
    }));

    it('onChangeEventSubscriber() should call pageService.processEvent()', async(() => {
      const control = { element: { config: { uiStyles: { attributes: { postButtonUrl: '/t' } } } } };
      const eve = { leafState: 'test', config: { uiStyles: { attributes: { postEventOnChange: true, postButtonUrl: true } } } };
      service.resetPreviousLeafState('test');
      service.controlValueChanged = new Subject();
      spyOn(pageService, 'postOnChange').and.callThrough();
      spyOn(pageService, 'processEvent').and.callThrough();
      service.onChangeEventSubscriber(control);
      service.controlValueChanged.next(eve);
      expect(pageService.postOnChange).not.toHaveBeenCalled();
      expect(pageService.processEvent).toHaveBeenCalled();
    }));

});