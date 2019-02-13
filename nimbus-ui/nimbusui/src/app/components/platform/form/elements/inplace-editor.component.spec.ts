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


import { Param } from './../../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { Subject } from 'rxjs';

import { InPlaceEditorComponent } from './inplace-editor.component';
import { PageService } from './../../../../services/page.service';
import { InputText } from './textbox.component';
import { TextArea } from './textarea.component';
import { ComboBox } from './combobox.component';
import { TooltipComponent } from '../../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../../pipes/select-item.pipe';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Values } from '../../../../shared/param-state';
import { inplaceEditorElement } from 'mockdata';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';

let pageService;

class MockLoggerService {
    error(a) { }
    info(a) { }
    debug(a) { }
}

class MockPageService {
    eventUpdate$ : Subject<any>

    constructor() {
        this.eventUpdate$ = new Subject();
    }
    logError(a) {
        this.eventUpdate$.next(a);
    }
    postOnChange(a, b, c) {    }
}

const declarations = [
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  TooltipComponent,
  SelectItemPipe,
  InputLabel
];
const imports = [
   FormsModule,
   ReactiveFormsModule,
   DropdownModule,
   HttpModule,
   HttpClientTestingModule,
   StorageServiceModule
];
const providers = [
   { provide: 'JSNLOG', useValue: JL },
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   { provide: LoggerService, useClass: MockLoggerService },
   { provide: PageService, useClass: MockPageService },
   CustomHttpClient,
   LoaderService,
   ConfigService,
   SessionStoreService,
   AppInitService
];
let fixture, hostComponent;
describe('InPlaceEditorComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InPlaceEditorComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = inplaceEditorElement as Param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InPlaceEditorComponent', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Label should be created if label is configured',async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle.name).toEqual('nm-input-label');
  }));

  it('nm-input-label should not be created if the label is not configured', async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle).toBeFalsy();
  }));

  it('anchor element should be created if displayValue is not UNASSIGNVALUE', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const anchorEle = debugElement.query(By.css('.form-control-static.editTrigger'));
    expect(anchorEle).toBeTruthy();
    const spanEles = debugElement.queryAll(By.css('span'));
    expect(spanEles[0].nativeElement.innerText).toEqual(hostComponent.element.leafState);
    expect(hostComponent.displayValue).toEqual(hostComponent.element.leafState);
  }));

  it('anchor element should be created if displayValue is UNASSIGNVALUE', async(() => {
    hostComponent.element.leafState = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const anchorEle = debugElement.query(By.css('.form-control-static.editTrigger'));
    expect(anchorEle).toBeTruthy();
    const spanEles = debugElement.queryAll(By.css('span'));
    expect(spanEles[0].nativeElement.innerText).toEqual('Unassigned');
    expect(hostComponent.displayValue).toEqual('Unassigned');    
  }));

  it('anchor element should not be created if the element.enabled is false', async(() => {
    hostComponent.element.enabled = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const anchorEle = debugElement.query(By.css('.form-control-static.editTrigger'));
    expect(anchorEle).toBeFalsy();
  }));

  it('if the element.enabled is false then div element should be created if displayValue is UNASSIGNVALUE', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const divEles = debugElement.queryAll(By.css('div'));    
    expect(divEles[1].childNodes[1].nativeElement.innerText).toEqual('Unassigned');    
  }));

  it('if the element.enabled is false then div element should be created if displayValue is not UNASSIGNVALUE', async(() => {
    hostComponent.element.leafState = 'testleafstate'
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const divEles = debugElement.queryAll(By.css('div'));
    expect(divEles[1].childNodes[2].nativeElement.innerText).toEqual(hostComponent.element.leafState);
    expect(hostComponent.displayValue).toEqual(hostComponent.element.leafState);
  }));

  it('submit button should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEles = debugElement.queryAll(By.css('button'));
    expect(buttonEles[0].attributes.title).toEqual('press enter to submit');    
  }));

  it('on click of submit button should call onSubmit()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEles = debugElement.queryAll(By.css('button'));
    spyOn(hostComponent, 'onSubmit').and.callThrough();
    buttonEles[0].nativeElement.click();
    expect(hostComponent.onSubmit).toHaveBeenCalled();
  }));

  it('cancel button should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEles = debugElement.queryAll(By.css('button'));
    expect(buttonEles[1].attributes.title).toEqual('press escape to cancel');    
  }));

  it('on click of cancel button should call cancel()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    spyOn(hostComponent, 'cancel').and.callThrough();
    const buttonEles = debugElement.queryAll(By.css('button'));
    buttonEles[1].nativeElement.click();
    expect(hostComponent.cancel).toHaveBeenCalled();
  }));

  it('set value() should update the displayValue', async(() => {
    hostComponent.value = 'firstName';
    hostComponent.value = '';
    expect(hostComponent.displayValue).toEqual('Unassigned');
  }));

    it('setDisplayValue() should update the displayValue from element.values[]', async(() => {
      const value = { code: 'test', label: 'l' } as Values;
      hostComponent.element.values = [value];
      hostComponent.setDisplayValue('test');
      expect(hostComponent.displayValue).toEqual('l');
    }));

    it('setDisplayValue() should update the displayValue property as Unassigned', async(() => {
      const value = { code: 'test', label: 'l' } as Values;
      hostComponent.element.values = [value];
      hostComponent.setDisplayValue(null);
      expect(hostComponent.displayValue).toEqual('Unassigned');
    }));

    it('setDisplayValue() should update the displayValue as element.leafState', async(() => {
      const value = { code: 'test', label: null } as Values;
      hostComponent.element.values = [value];
      hostComponent.element.leafState = 'ls';
      hostComponent.setDisplayValue('test');
      expect(hostComponent.displayValue).toEqual('ls');
    }));

    it('ngOnInit() should call the setDisplayValue() 3 times', () => {
      fixture.whenStable().then(() => {
        hostComponent.setDisplayValue = () => {};
        (hostComponent as any).generateComponent = () => {};
        hostComponent.element.leafState = 'l';
        hostComponent.element.config.uiStyles.attributes.inplaceEditType = '';
        hostComponent.element.config.code = '123';
        const eve = { leafState: '', config: { code: '123' } };
        spyOn(hostComponent, 'setDisplayValue').and.callThrough();
        hostComponent.ngOnInit();
        pageService.logError(eve);
        expect(hostComponent.setDisplayValue).toHaveBeenCalledTimes(3);
      });
    });
    
    it('generateComponent() should update the inputInstance.element and setInPlaceEditContext', async(() => {
      const inputInstance = { element: '', setInPlaceEditContext: a => {} };
      spyOn((hostComponent as any), 'getComponentType').and.returnValue('');
      spyOn((hostComponent as any), 'createInputInstance').and.returnValue(inputInstance);
      spyOn(inputInstance, 'setInPlaceEditContext').and.callThrough();
      (hostComponent as any).generateComponent('');
      const res: any = hostComponent.element;
      expect(inputInstance.element).toEqual(res);
      expect(inputInstance.setInPlaceEditContext).toHaveBeenCalled();
    }));

    it('getComponentType() should throw an error', async(() => {
      (hostComponent as any).components = { a: 'test' };
      expect(() => {
        (hostComponent as any).getComponentType('b');
      }).toThrow();
    }));

    it('getComponentType() should return component name', async(() => {
      (hostComponent as any).components = { a: 'test' };
      expect((hostComponent as any).getComponentType('a')).toEqual('test');
    }));

    it('get type() should update from element.config.uiStyles.attributes.inplaceEditType', () => {
      fixture.whenStable().then(() => {
        hostComponent.element.config.uiStyles.attributes.inplaceEditType = 'test';
        expect(hostComponent.type).toEqual('test');
      });
    });

    it('cancel() should update the value and editClass', async(() => {
      hostComponent.element.values = [];
      (hostComponent as any).preValue = 'test';
      hostComponent.cancel();
      expect(hostComponent.value).toEqual('test');
      expect(hostComponent.editClass).toEqual('');
    }));

    it('onSubmit() should update the editClass and call pageService.postOnChange()', () => {
      hostComponent.element.leafState = '';
      hostComponent.element.values = [];
      hostComponent.element.path = '';
      spyOn(pageService, 'postOnChange').and.callThrough();
      hostComponent.onSubmit();
      expect(hostComponent.editClass).toEqual('');
      expect(pageService.postOnChange).toHaveBeenCalled();
    });

    it('enableEdit() should update the preValue and editClass', async(() => {
      hostComponent.element.leafState = '';
      hostComponent.element.values = [];
      hostComponent.value = 'test';
      hostComponent.enableEdit();
      expect((hostComponent as any).preValue).toEqual('test');
      expect(hostComponent.editClass).toEqual('editOn');
    }));

    it('ngOnChanges() should call the setDisplayValue()', async(() => {
      hostComponent.element.leafState = '';
      hostComponent.element.values = [];
      const changes: any = { element: true };
      spyOn(hostComponent, 'setDisplayValue').and.returnValue('');
      hostComponent.ngOnChanges(changes);
      expect(hostComponent.setDisplayValue).toHaveBeenCalled();
    }));
});

