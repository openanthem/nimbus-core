'use strict';
import { TestBed, async, fakeAsync, tick } from '@angular/core/testing';
import { FormsModule, Validators } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FormGroup, ValidatorFn, FormControl } from '@angular/forms';

import { InputText } from './textbox.component';
import { TooltipComponent } from '../../tooltip/tooltip.component'
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { Param } from '../../../../shared/param-state';
import { ControlSubscribers } from './../../../../services/control-subscribers.service';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

let app, fixture, param: Param, payload, controlService;

describe('InputText', () => {
  payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';
  param = JSON.parse(payload);
  // let app, fixture;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        InputText,
        TooltipComponent,
        InputLabel
       ],
       imports: [
        FormsModule,
        HttpClientTestingModule,
        HttpModule,
        StorageServiceModule
       ],
       providers: [
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        { provide: 'JSNLOG', useValue: JL },
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        Location,
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        LoggerService,
        SessionStoreService,
        AppInitService,
        ControlSubscribers
       ]
    }).compileComponents();
     fixture = TestBed.createComponent(InputText);
     app = fixture.debugElement.componentInstance;
     controlService = TestBed.get(ControlSubscribers);
     let fg= new FormGroup({});
     let checks: ValidatorFn[] = [];
     checks.push(Validators.required);
     fg.addControl(param.config.code, new FormControl(param.leafState,checks));
     app.form = fg;
     app.element = param;
  }));

  it('form control value with default leafstate', async(() => {
    fixture.detectChanges();
    expect(app.form.controls['firstName'].value).toBe('testData');
    expect(app).toBeTruthy();
  }));

  it('control validity', async(() => {
    app.form.controls['firstName'].setValue('');
    fixture.detectChanges();
    expect(app.form.controls['firstName'].valid).toBeFalsy();
  }));

  it('post on focus out', async(() => {
    spyOn(app, 'emitValueChangedEvent').and.callThrough();
    fixture.detectChanges();
    const textBox = fixture.debugElement.children[0].nativeElement;
    textBox.value = 'abcd123';
    textBox.dispatchEvent(new Event('input'));
    textBox.dispatchEvent(new Event('focusout'));
    fixture.detectChanges();
    expect(app.emitValueChangedEvent).toHaveBeenCalled();
  }));
});