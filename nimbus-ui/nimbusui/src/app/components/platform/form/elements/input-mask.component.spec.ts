'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Subject } from 'rxjs';
import { of as observableOf,  Observable } from 'rxjs';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule, InputMaskModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { TooltipComponent } from '../../tooltip/tooltip.component';

import { InputLabel } from './input-label.component';
import { InputSwitch } from './input-switch.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
// import { inputMaskElement } from './../../../../mockdata/input-mask.component.mockdata.spec';
import {inputMaskElement} from 'mockdata';
import { Param } from './../../../../shared/param-state';
import { ServiceConstants } from './../../../../services/service.constants';



import {InputMaskComp} from './input-mask.component';

let pageService;

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

const declarations = [
  InputSwitch,
  InputLabel,
  TooltipComponent,
  InputMaskComp
 ];
const imports = [
     HttpModule,
     HttpClientTestingModule,
     StorageServiceModule,
     DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule,
FormsModule, InputMaskModule
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

describe('InputMaskComp', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputMaskComp);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = inputMaskElement as Param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InputMaskComp', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-input-label should be created if the label is configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeTruthy();
    }));
    
    it('nm-input-label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy();
    }));

    it('p-inputMask should be created', async(() => {
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const pInputMaskElement = debugElement.query(By.css('p-inputMask'));
        expect(pInputMaskElement).toBeTruthy();
        }));

});