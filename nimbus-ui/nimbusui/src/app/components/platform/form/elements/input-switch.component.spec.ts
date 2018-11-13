'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Subject } from 'rxjs';
import { of as observableOf,  Observable } from 'rxjs';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
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
import * as data from '../../../../payload.json';

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

const declarations = [
  InputSwitch,
  InputLabel,
  TooltipComponent
 ];
const imports = [
     HttpModule,
     HttpClientTestingModule,
     StorageServiceModule,
     DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule,
FormsModule
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

describe('InputSwitch', () => {
  configureTestSuite();
  setup(InputSwitch, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<InputSwitch>){
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InputSwitch', async function (this: TestContext<InputSwitch>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('orientation should be left', async function (this: TestContext<InputSwitch>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.orientation = 'LEFT';
      expect(this.hostComponent.orientation).toEqual('left');
    });
  });

  it('orientation should be right', async function (this: TestContext<InputSwitch>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.orientation = 'RIGHT';
      expect(this.hostComponent.orientation).toEqual('right');
    });
  });

  it('orientation should be empty string', async function (this: TestContext<InputSwitch>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.orientation = '';
      expect(this.hostComponent.orientation).toEqual('');
    });
  });

});