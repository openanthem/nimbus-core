/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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
import {
  HashLocationStrategy,
  Location,
  LocationStrategy
} from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { FormControl, FormGroup } from '@angular/forms';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { Subject, Subscription } from 'rxjs';
import { ConfigService } from './../../../../services/config.service';
import { CustomHttpClient } from './../../../../services/httpclient.service';
import { LoaderService } from './../../../../services/loader.service';
import { LoggerService } from './../../../../services/logger.service';
import { PageService } from './../../../../services/page.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from './../../../../services/session.store';
import { EventPropagationDirective } from './event-propagation.directive';

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

class MockPageService {
  public postResponseProcessing$: Subject<any>;

  constructor() {
    this.postResponseProcessing$ = new Subject();
  }

  logError(res) {
    this.postResponseProcessing$.next(res);
  }
}

let directive, elementRef, pageService, loggerService;

describe('EventPropagationDirective', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventPropagationDirective],
      providers: [
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: PageService, useClass: MockPageService },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        Location,
        CustomHttpClient,
        SessionStoreService,
        LoaderService,
        ConfigService
      ],
      imports: [HttpClientModule, StorageServiceModule]
    }).compileComponents();
    pageService = TestBed.get(PageService);
    loggerService = TestBed.get(LoggerService);
    directive = new EventPropagationDirective(pageService, loggerService);
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
  });

  it('ngOnInit() shpould remove the attribute if form is valid', () => {
    const postResponseProcessingEvent =
      '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
    directive.path =
      '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
    directive.form = new FormGroup({
      question123: new FormControl()
    });
    directive.srcElement = {
      removeAttribute: () => {}
    };
    spyOn(directive.srcElement, 'removeAttribute').and.callThrough();
    directive.ngOnInit();
    pageService.logError(postResponseProcessingEvent);
    expect(directive.form.valid).toBeTruthy();
    expect(directive.srcElement.removeAttribute).toHaveBeenCalledWith(
      'disabled'
    );
  });

  it('ngOnInit() shpould remove the attribute even if form is not available', () => {
    const postResponseProcessingEvent =
      '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
    directive.path =
      '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
    directive.srcElement = {
      removeAttribute: () => {}
    };
    spyOn(directive.srcElement, 'removeAttribute').and.callThrough();
    directive.ngOnInit();
    pageService.logError(postResponseProcessingEvent);
    expect(directive.form).toBeFalsy();
    expect(directive.srcElement.removeAttribute).toHaveBeenCalledWith(
      'disabled'
    );
  });

  it('clickEvent() should create an instance', () => {
    const event = {
      preventDefault: () => {},
      stopPropagation: () => {},
      srcElement: {
        setAttribute: () => {}
      }
    };
    spyOn(event, 'preventDefault').and.callThrough();
    spyOn(event, 'stopPropagation').and.callThrough();
    spyOn(event.srcElement, 'setAttribute').and.callThrough();
    spyOn(directive.clicksubject, 'next').and.callThrough();
    directive.path = 'test';
    directive.clickEvent(event);
    expect(event.preventDefault).toHaveBeenCalled();
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(event.srcElement.setAttribute).toHaveBeenCalledWith(
      'disabled',
      true
    );
    expect(directive.clicksubject.next).toHaveBeenCalledWith(event);
  });

  it('ngOnDestroy() should unsubscribe the subscription and postProcessing', () => {
    directive.subscription = new Subscription();
    directive.postProcessing = new Subscription();
    spyOn(directive.subscription, 'unsubscribe').and.callThrough();
    spyOn(directive.postProcessing, 'unsubscribe').and.callThrough();
    directive.ngOnDestroy();
    expect(directive.subscription.unsubscribe).toHaveBeenCalled();
    expect(directive.postProcessing.unsubscribe).toHaveBeenCalled();
  });
});
