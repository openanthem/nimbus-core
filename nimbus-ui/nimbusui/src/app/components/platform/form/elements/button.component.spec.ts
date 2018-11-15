'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Location } from '@angular/common';
import { RouterTestingModule } from '@angular/router/testing';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { of as observableOf,  Observable } from 'rxjs';

import { Button } from './button.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { FileService } from '../../../../services/file.service';
import { Subject, observable } from 'rxjs';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { UiAttribute } from '../../../../shared/param-config';
import { FormGroup, ValidatorFn, Validators, FormControl } from '@angular/forms';
import { PrintDirective } from '../../../../directives/print.directive';
import { PrintService } from '../../../../services/print.service';

let location, pageService, param, fileService;

class MockLocation {
    back() { }
}

class MockPageService {
    validationUpdate$: Subject<any>

    constructor() {
        this.validationUpdate$ = new Subject();
    }
    processEvent(a, b, c, d) {

    }

    logError(a) {
        this.validationUpdate$.next(a);
    }
}

class MockFileService {
  test = new Subject();
  uploadFile(a, b) {
    this.test.next('');
    return this.test;
  }
}

const declarations = [
  Button,
  SvgComponent,
  Image,
  PrintDirective
];
const imports = [
   HttpModule,
   HttpClientTestingModule,
   RouterTestingModule,
   StorageServiceModule,
   AngularSvgIconModule
];
const providers = [
   {provide: Location, useClass: MockLocation},
   {provide: PageService, useClass: MockPageService},
   { provide: 'JSNLOG', useValue: JL },
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   {provide: FileService, useClass: MockFileService},
   CustomHttpClient,
   LoaderService,
   ConfigService,
   LoggerService,
   AppInitService,
   SessionStoreService,
   PrintService
];

describe('Button', () => {

  configureTestSuite();
  setup(Button, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<Button>){
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    this.hostComponent.form = fg;
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
    location = TestBed.get(Location);
    fileService = TestBed.get(FileService);
  });

  it('should create the Button', async function (this: TestContext<Button>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('emitEvent() should update the location', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.browserBack = true;
      spyOn(location, 'back').and.callThrough();
      this.hostComponent.emitEvent('eve');
      expect(location.back).toHaveBeenCalled();
    });
  });

  it('emitEvent() should emit buttonClickEvent', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.browserBack = null;
      spyOn(this.hostComponent.buttonClickEvent, 'emit').and.callThrough();
      this.hostComponent.emitEvent('eve');
      expect(this.hostComponent.buttonClickEvent.emit).toHaveBeenCalled();
    });
  });

  it('ngOnInit() should update disabled property as false', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.enabled = true;
      this.hostComponent.ngOnInit();
      expect((this.hostComponent as any).disabled).toBeFalsy();
    });
  });

  it('ngOnInit() should update call pageService.processEvent()', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.enabled = true;
      const attributes = new UiAttribute();
      attributes.b = 'a';
      attributes.method = 'method';
      this.hostComponent.element.path = 'test';
      this.hostComponent.element.config.uiStyles.attributes = attributes;
      const eve = { element: { config: { uiStyles: { attributes: { b: 'a', method: 'method' } } }, path: 'test' } };
      (this.hostComponent as any).buttonClickEvent = new Subject();
      spyOn(pageService, 'processEvent').and.callThrough();
      this.hostComponent.ngOnInit();
      this.hostComponent.buttonClickEvent.next(eve);
      expect(pageService.processEvent).toHaveBeenCalled();
    });
  });

  it('ngOnInit() should update disabled property as true based on pageService.validationUpdate$ subject', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.enabled = false;
      const eve = { path: 'test', enabled: false };
      this.hostComponent.ngOnInit();
      pageService.logError(eve);
      expect((this.hostComponent as any).disabled).toBeTruthy();
    });
  });

  it('ngOnInit() should update disabled property as false based on pageService.validationUpdate$ subject', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.enabled = true;
      const eve = { path: '1test', enabled: false };
      this.hostComponent.ngOnInit();
      pageService.logError(eve);
      expect((this.hostComponent as any).disabled).toBeFalsy();
    });
  });

  it('checkObjectType() should return false', async function (this: TestContext<Button>) {
      expect(this.hostComponent.checkObjectType('test', { test: 'test' })).toBeFalsy();
  });

  it('checkObjectType() should return true', async function (this: TestContext<Button>) {
    expect(this.hostComponent.checkObjectType('Object', {ttttesttest1 : 'testtest1'})).toBeTruthy();
  });

  it('getFileParameter() should call checkObjectType()', async function (this: TestContext<Button>) {
    const item: any = { a: [1] };
    spyOn(this.hostComponent, 'checkObjectType').and.returnValue(true);
    const key = this.hostComponent.getFileParameter(item);
    expect(this.hostComponent.checkObjectType).toHaveBeenCalled();
    expect(key).toEqual('a');
  });

  it('getFileParameter() should return null', async function (this: TestContext<Button>) {
    const item: any = {};
    spyOn(this.hostComponent, 'checkObjectType').and.returnValue(true);
    const key = this.hostComponent.getFileParameter(item);
    expect(key).toBeFalsy();
  });

  it('getFileParameter() should call checkObjectType() and return null', async function (this: TestContext<Button>) {
    const item: any = { a: [1] };
    spyOn(this.hostComponent, 'checkObjectType').and.returnValue(false);
    const key = this.hostComponent.getFileParameter(item);
    expect(this.hostComponent.checkObjectType).toHaveBeenCalled();
    expect(key).toBeFalsy();
  });

  it('getFileParameter() should call checkObjectType() and return null on empty array', async function (this: TestContext<Button>) {
    const item: any = { a: [] };
    spyOn(this.hostComponent, 'checkObjectType').and.returnValue(true);
    const key = this.hostComponent.getFileParameter(item);
    expect(this.hostComponent.checkObjectType).toHaveBeenCalled();
    expect(key).toBeFalsy();
  });

  it('getFileParameter() should return false if a is not File', async function (this: TestContext<Button>) {
    const item: any = { a: [1] };
    this.hostComponent.checkObjectType = (a, b) => {
      if (a === 'File') {
        return false;
      }
      return true;
    };
    const key = this.hostComponent.getFileParameter(item);
    expect(key).toBeFalsy();
  });

  it('onSubmit() should call pageService.processEvent() without form.value.formControl', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      spyOn(pageService, 'processEvent').and.returnValue('');
      this.hostComponent.onSubmit();
      expect(pageService.processEvent).toHaveBeenCalled();
    });
  });

  it('onSubmit() should call pageService.processEvent() with form.value.formControl', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      const files = [{} as File];
      this.hostComponent.form.value.fileControl = files;
      spyOn(pageService, 'processEvent').and.returnValue('');
      this.hostComponent.element.config.uiStyles.attributes.formReset = false;
      this.hostComponent.onSubmit();
      fileService.uploadFile('', '');
      expect(pageService.processEvent).toHaveBeenCalled();
    });
  });

  it('reset() should call form.reset()', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.formReset = true;
      spyOn(this.hostComponent.form, 'reset').and.callThrough();
      this.hostComponent.reset();
      expect(this.hostComponent.form.reset).toHaveBeenCalled();
    });
  });

  it('reset() should not call form.reset()', async function (this: TestContext<Button>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.formReset = false;
      spyOn(this.hostComponent.form, 'reset').and.callThrough();
      this.hostComponent.reset();
      expect(this.hostComponent.form.reset).not.toHaveBeenCalled();
    });
  });

  it('getAllURLParams() should return null', async function (this: TestContext<Button>) {
    expect(this.hostComponent.getAllURLParams('www.test.com')).toBeFalsy();
  });

});