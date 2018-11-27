import { Param } from './../../../../shared/param-state';
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
 let fixture, hostComponent;

describe('InputSwitch', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(InputSwitch);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InputSwitch', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('orientation should be left', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.orientation = 'LEFT';
      expect(hostComponent.orientation).toEqual('left');
    });
  });

  it('orientation should be right', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.orientation = 'RIGHT';
      expect(hostComponent.orientation).toEqual('right');
    });
  });

  it('orientation should be empty string', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.orientation = '';
      expect(hostComponent.orientation).toEqual('');
    });
  });

});