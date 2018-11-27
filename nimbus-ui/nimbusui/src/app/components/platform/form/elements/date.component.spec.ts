// 'use strict';
// import { TestBed, async } from '@angular/core/testing';
// import { HttpClientModule } from '@angular/common/http';
// import { HttpModule } from '@angular/http';
// import { FormsModule } from '@angular/forms';
// import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
// import { JL } from 'jsnlog';
// import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

// import { DateControl } from './date.component';
// import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
// import { PageService } from '../../../../services/page.service';
// import { CustomHttpClient } from '../../../../services/httpclient.service';
// import { LoaderService } from '../../../../services/loader.service';
// import { ConfigService } from '../../../../services/config.service';
// import { LoggerService } from '../../../../services/logger.service';
// import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
// import { AppInitService } from '../../../../services/app.init.service';
// import { InputLabel } from './input-label.component';
// import { configureTestSuite } from 'ng-bullet';
// import { setup, TestContext } from '../../../../setup.spec';
// import * as data from '../../../../payload.json';
// import { Param } from '../../../../shared/param-state';

// let param: Param;

// const declarations = [
//   DateControl,
//   TooltipComponent,
//   InputLabel
// ];
// const imports = [
//    StorageServiceModule,
//    FormsModule,
//    HttpClientModule,
//    HttpModule
// ];
// const providers = [
//    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
//    { provide: 'JSNLOG', useValue: JL },
//    { provide: LocationStrategy, useClass: HashLocationStrategy },
//    Location,
//    SessionStoreService,
//    PageService,
//    CustomHttpClient,
//    LoaderService,
//    ConfigService,
//    LoggerService,
//    AppInitService
// ];

// describe('DateControl', () => {

//   configureTestSuite();
//   setup(DateControl, declarations, imports, providers);
//      let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

//   beforeEach(async function(this: TestContext<DateControl>){
//     hostComponent.element = param;
//   });

//   it('should create the DateControl', async function (this: TestContext<DateControl>) {
//     expect(hostComponent).toBeTruthy();
//   });

// });
