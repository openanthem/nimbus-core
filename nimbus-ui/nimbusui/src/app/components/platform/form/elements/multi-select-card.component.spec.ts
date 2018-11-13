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
import * as data from '../../../../payload.json';
import { FormGroup, ValidatorFn, Validators, FormControl } from '@angular/forms';

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

describe('MultiselectCard', () => {

  configureTestSuite();
  setup(MultiselectCard, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<MultiselectCard>) {
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    this.hostComponent.form = fg;
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the MultiselectCard', async function (this: TestContext<MultiselectCard>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('set value() should update the value property', async function (this: TestContext<MultiselectCard>) {
    this.hostComponent.value = 'test';
    expect(this.hostComponent.value).toEqual('test');
  });

    it('registerOnChange() should update the onChange property', async function (this: TestContext<MultiselectCard>) {
      this.hostComponent.registerOnChange('test');
      expect(this.hostComponent.onChange).toEqual('test');
    });

    it('writeValue() should update the value property', async function (this: TestContext<MultiselectCard>) {
      this.hostComponent.writeValue('test');
      this.hostComponent.writeValue('test');
      expect(this.hostComponent.value).toEqual('test');
    });

    it('registerOnTouched() should update the onTouched property', async function (this: TestContext<MultiselectCard>) {
      this.hostComponent.registerOnTouched('test');
      expect(this.hostComponent.onTouched).toEqual('test');
    });

    it('toggleChecked() should return true', async function (this: TestContext<MultiselectCard>) {
      (this.hostComponent as any).selectedOptions[0] = 'test';
      expect(this.hostComponent.toggleChecked('test')).toBeTruthy();
    });

    it('toggleChecked() should return false', async function (this: TestContext<MultiselectCard>) {
      (this.hostComponent as any).selectedOptions[0] = 'test';
      expect(this.hostComponent.toggleChecked('test1')).toBeFalsy();
    });

    it('selectOption() should update the value property based on valid argumnet', async function (this: TestContext<MultiselectCard>) {
      const selectedOptions = (this.hostComponent as any).selectedOptions;
      selectedOptions[0] = 'test';
      this.hostComponent.selectOption('test', '');
      expect(this.hostComponent.value).toEqual(selectedOptions);
    });

    it('selectOption() should update the value property', async function (this: TestContext<MultiselectCard>) {
      const selectedOptions = (this.hostComponent as any).selectedOptions;
      selectedOptions[0] = 'test';
      this.hostComponent.selectOption('123', '');
      expect(this.hostComponent.value).toEqual(selectedOptions);
    });

    it('setState() should call the pageService.postOnChange()', async function (this: TestContext<MultiselectCard>) {
      this.fixture.whenStable().then(() => {
        this.hostComponent.element.leafState = '';
        this.hostComponent.element.config.uiStyles.attributes.postEventOnChange = true;
        spyOn(pageService, 'postOnChange').and.callThrough();
        this.hostComponent.setState('t');
        expect(pageService.postOnChange).toHaveBeenCalled();
      });
    });

    it('ngOnInit() should update the selectedOptions and call form.controls.a.setValue()', async function (this: TestContext<MultiselectCard>) {
      this.fixture.whenStable().then(() => {
        this.hostComponent.element.leafState = '';
        this.hostComponent.element.path = 'test';
        spyOn(this.hostComponent, 'setState').and.returnValue('');
        spyOn(this.hostComponent.form.controls.firstName, 'setValue').and.callThrough();
        const eve = { config: { code: 'firstName' }, path: 'test', leafState: '' };
        this.hostComponent.ngOnInit();
        pageService.logError(eve);
        expect((this.hostComponent as any).selectedOptions).toEqual('');
        expect(this.hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();
      });
    });
    
});