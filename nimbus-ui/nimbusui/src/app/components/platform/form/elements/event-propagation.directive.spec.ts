import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { EventPropagationDirective } from './event-propagation.directive';
import { LoggerService } from './../../../../services/logger.service';
import { PageService } from './../../../../services/page.service';
import { SessionStoreService, CUSTOM_STORAGE } from './../../../../services/session.store';
import { CustomHttpClient } from './../../../../services/httpclient.service';
import { LoaderService } from './../../../../services/loader.service';
import { ConfigService } from './../../../../services/config.service';
import { Subject, Observable, throwError, of as observableOf, Subscription } from 'rxjs';
import { FormGroup, FormControl } from '@angular/forms';

class MockLoggerService {
    debug() { }
    info() { }
    error() { }
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
          declarations: [
            EventPropagationDirective
           ],
           providers: [
               {provide: LoggerService, useClass: MockLoggerService},
               {provide: PageService, useClass: MockPageService},
               { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
               { provide: LocationStrategy, useClass: HashLocationStrategy },
               Location,
               CustomHttpClient,
               SessionStoreService,
               LoaderService,
               ConfigService
            ],
            imports: [
                HttpClientModule,
                StorageServiceModule
            ]
        }).compileComponents();
        pageService = TestBed.get(PageService);
        loggerService = TestBed.get(LoggerService);
        directive = new EventPropagationDirective(pageService, loggerService)
      });

    it('should create an instance', () => {
        expect(directive).toBeTruthy();
    });

    it('ngOnInit() shpould remove the attribute if form is valid', () => {
        const postResponseProcessingEvent = '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
        directive.path = '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
        directive.form = new FormGroup({
            question123: new FormControl()
        });
        directive.srcElement = {
            'removeAttribute': () => { }
        };
        spyOn(directive.srcElement, 'removeAttribute').and.callThrough();
        directive.ngOnInit();
        pageService.logError(postResponseProcessingEvent);
        expect(directive.form.valid).toBeTruthy();
        expect(directive.srcElement.removeAttribute).toHaveBeenCalledWith('disabled');
    });

    it('ngOnInit() shpould remove the attribute even if form is not available', () => {
        const postResponseProcessingEvent = '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
        directive.path = '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/vbgAddOwnerButtonGrp/cancel';
        directive.srcElement = {
            'removeAttribute': () => { }
        };
        spyOn(directive.srcElement, 'removeAttribute').and.callThrough();
        directive.ngOnInit();
        pageService.logError(postResponseProcessingEvent);
        expect(directive.form).toBeFalsy();
        expect(directive.srcElement.removeAttribute).toHaveBeenCalledWith('disabled');
    });

    it('clickEvent() should create an instance', () => {
        const event = {
            'preventDefault': () => { },
            'stopPropagation': () => { },
            'srcElement': {
                'setAttribute': () => { }
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
        expect(event.srcElement.setAttribute).toHaveBeenCalledWith('disabled', true);
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