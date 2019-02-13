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

'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, FormGroup, ValidatorFn, Validators, FormControl } from '@angular/forms';
import { DropdownModule, GrowlModule, MessagesModule, DialogModule, AccordionModule, 
    DataTableModule, FileUploadModule, PickListModule, ListboxModule, CheckboxModule, 
    RadioButtonModule, CalendarModule, InputSwitchModule, TreeTableModule, InputMaskModule, TabViewModule, EditorModule, ChartModule } from 'primeng/primeng';

import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SimpleChanges } from '@angular/core';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { Form } from './form.component';
import { FrmGroupCmp } from './form-group.component';
import { Accordion } from '../platform/content/accordion.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { FormElement } from './form-element.component';
import { MessageComponent } from './message/message.component';
import { DataTable } from './grid/table.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { Signature } from '../platform/form/elements/signature.component'
import { InputText } from '../platform/form/elements/textbox.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { Header } from './content/header.component';
import { Section } from './section.component';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { Menu } from '../platform/menu.component';
import { Link } from '../platform/link.component';
import { StaticText } from '../platform/content/static-content.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { LoggerService } from '../../services/logger.service';
import { FormElementsService } from './form-builder.service';
import { Subject } from 'rxjs';
import { Model } from '../../shared/param-state';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { AppInitService } from '../../services/app.init.service';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { SvgComponent } from './svg/svg.component';
import { Image } from './image.component';
import { TreeGrid } from './tree-grid/tree-grid.component';
import { InputSwitch } from './form/elements/input-switch.component';
import { FormGridFiller } from './form/form-grid-filler.component';
import { DisplayValueDirective } from '../../directives/display-value.directive';
import { InputLabel } from './form/elements/input-label.component';
import { Label } from './content/label.component';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';
import { InputLegend } from './form/elements/input-legend.component';
import { FormErrorMessage } from './form-error-message.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';
import { Param } from '../../shared/param-state';
import { PrintDirective } from '../../directives/print.directive';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../services/service.constants';
import { PrintService } from '../../services/print.service';
import { GridService } from '../../services/grid.service';
import { formElement, formModel, textboxnotnullmodel, textboxnotnullelement } from 'mockdata';
import { InputMaskComp } from './form/elements/input-mask.component';
import { RichText } from './form/elements/rich-text.component';
import { Tab } from './content/tab.component';
import { NmMessageService } from './../../services/toastmessage.service';
import { NmChart } from './charts/chart.component';
import { TableHeader } from './grid/table-header.component';

class MockLoggerService {
  debug() { }
  info() { }
  error() { }
}

@Component({
  template: '<div></div>',
  selector: 'nm-button'
})
class Button {

  @Input() element: any;
  @Input() payload: string;
  @Input() form: any;
  @Input() actionTray?: boolean;

  @Output() buttonClickEvent = new EventEmitter();

  @Output() elementChange = new EventEmitter();
  private imagesPath: string;
  private btnClass: string;
  private disabled: boolean;
  files: any;
  differ: any;
  componentTypes;
}

class MockFormElementsService {
    toFormGroup(a, b) {
        return '';
    }
}

class MockPageService {
    eventUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;
    gridValueUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
        this.gridValueUpdate$ = new Subject();
    }

    logError(a) {
        this.eventUpdate$.next(a);
    }

    processEvent() {}
}

const declarations = [
  Form,
  FrmGroupCmp,
  Accordion,
  ButtonGroup,
  Button,
  FormElement,
  MessageComponent,
  DataTable,
  TableHeader,
  FileUploadComponent,
  OrderablePickList,
  MultiselectCard,
  MultiSelectListBox,
  CheckBox,
  CheckBoxGroup,
  RadioButton,
  ComboBox,
  Calendar,
  TextArea,
  Signature,
  InputText,
  Paragraph,
  Header,
  Section,
  ActionDropdown,
  TooltipComponent,
  SelectItemPipe,
  Menu,
  Link,
  StaticText,
  CardDetailsComponent,
  CardDetailsGrid,
  ActionLink,
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  DateTimeFormatPipe,
  HeaderCheckBox,
  SvgComponent,
  Image,
  TreeGrid,
  InputSwitch,
  FormGridFiller,
  DisplayValueDirective,
  InputLabel,
  Label,
  CardDetailsFieldGroupComponent,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  InputMaskComp,
  Tab,
  NmChart,
  RichText
];
const imports = [
   FormsModule, 
   ReactiveFormsModule,
   GrowlModule,
   MessagesModule,
   DialogModule,
   ReactiveFormsModule,
   AccordionModule,
   DataTableModule,
   DropdownModule,
   FileUploadModule,
   PickListModule,
   ListboxModule,
   CheckboxModule,
   RadioButtonModule,
   CalendarModule,
   TableModule,
   KeyFilterModule,
   HttpModule,
   HttpClientTestingModule,
   StorageServiceModule,
   AngularSvgIconModule,
   ToastModule,
   InputSwitchModule, 
   TreeTableModule,
   BrowserAnimationsModule,
   InputMaskModule,
   TabViewModule,
   ChartModule,
   EditorModule
];
const providers = [
  FormElementsService,
PageService,
{ provide: 'JSNLOG', useValue: JL },
{ provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
{provide: LoggerService, useClass: MockLoggerService},
{ provide: LocationStrategy, useClass: HashLocationStrategy },
Location,
 CustomHttpClient,
 LoaderService,
 ConfigService,
 AppInitService,
 PrintService,
 GridService,
 NmMessageService,
 SessionStoreService
];

let fixture, hostComponent, pageService, formElementsService, configService;

describe('Form', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach( () => {
    fixture = TestBed.createComponent(Form);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.form = new FormGroup({
      calls: new FormControl(),
      medications1: new FormControl(),
      firstName: new FormControl(),
      question1notnull: new FormControl()
   });
    hostComponent.element = formElement as Param;
    hostComponent.model = formModel as Model;
    formElementsService = TestBed.get(FormElementsService);
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
  });

  it('should create the Form',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('nm-counter-message should be created if the showmessages attribute is configured as true',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmCounterMessageEle = debugElement.query(By.css('nm-counter-message'));
    expect(nmCounterMessageEle.attributes['ng-reflect-form']).toBeTruthy();
    expect(nmCounterMessageEle.attributes['ng-reflect-form'].length).not.toEqual(0);
    expect(nmCounterMessageEle.attributes['ng-reflect-form']).not.toEqual(null);
    expect(nmCounterMessageEle).toBeTruthy();
  }));

  it('nm-counter-message should not be created if the showmessages attribute is configured as false',async(() => {
    hostComponent.element.config.uiStyles.attributes.showMessages = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmCounterMessageEle = debugElement.query(By.css('nm-counter-message'));
    expect(nmCounterMessageEle).toBeFalsy();
  }));

  it('nm-label should be created if the label is configured',async(() => {
    hostComponent.position = 1;
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;    
    const nmLabelEle = debugElement.queryAll(By.css('nm-label'));
    expect(nmLabelEle.length).toEqual(2);
  }));

  it('nm-label should not be created if the label is not configured',async(() => {
    const newElement = Object.assign({}, formElement);
    newElement.labels =[];
    hostComponent.element = newElement as Param;
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmLabelEle = debugElement.queryAll(By.css('form nm-label'));    
    expect(nmLabelEle.length === 1).toBeTruthy();
  }));

  it('nm-accordion should be created if the accordion is configured',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const accordionEle = debugElement.query(By.css('nm-accordion'));
    expect(accordionEle.attributes['ng-reflect-form']).toBeTruthy();
    expect(accordionEle.attributes['ng-reflect-form'].length).not.toEqual(0);
    expect(accordionEle.attributes['ng-reflect-form']).not.toEqual(null);
    expect(accordionEle).toBeTruthy();
  }));

  it('nm-accordion should not be created if the accordion is not configured',async(() => {    
    hostComponent.model.params[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const accordionEle = debugElement.query(By.css('nm-accordion'));
    expect(accordionEle).toBeFalsy();
  }));

  it('nm-frm-grp should be created if the elementgroup is configured',async(() => {
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.queryAll(By.css('form nm-frm-grp'));    
    expect(frmGrpEle).toBeTruthy();
    expect(frmGrpEle[5].attributes['ng-reflect-position']).toEqual('2');
    expect(frmGrpEle[5].attributes['ng-reflect-form']).toBeTruthy();
    expect(frmGrpEle[5].attributes['ng-reflect-form'].length).not.toEqual(0);
    expect(frmGrpEle[5].attributes['ng-reflect-form']).not.toEqual(null);
    expect(frmGrpEle.length).toEqual(7);
  }));

  it('nm-frm-grp should not be created if the elementgroup is not configured',async(() => {
    const newModel = Object.assign({}, formModel); 
    newModel.params[2].type.model.params[4].config.uiStyles.attributes.alias = '';
    hostComponent.model = newModel as Model;
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.queryAll(By.css('form nm-frm-grp'));
    expect(frmGrpEle.length).toEqual(7);
    for (let i = 0; i < frmGrpEle.length; i++) {
      expect(frmGrpEle[i].attributes['ng-reflect-position']).not.toEqual('2');
    }
  }));

  it('nm-frm-grp should be created if the element is configured',async(() => {
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.queryAll(By.css('form nm-frm-grp'));
    expect(frmGrpEle[0].attributes['ng-reflect-form']).toBeTruthy();
    expect(frmGrpEle[0].attributes['ng-reflect-form'].length).not.toEqual(0);
    expect(frmGrpEle[0].attributes['ng-reflect-form']).not.toEqual(null);
    expect(frmGrpEle).toBeTruthy();    
    expect(frmGrpEle[0].attributes['ng-reflect-position']).toEqual('1');
    expect(frmGrpEle.length).toEqual(7);
  }));

  it('nm-frm-grp should not be created if the element is not configured',async(() => {
    const newModel = Object.assign({}, formModel);
    delete newModel.params[0];
    hostComponent.model = newModel as Model;
    hostComponent.position = 1;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.queryAll(By.css('form nm-frm-grp'));
    expect(frmGrpEle.length).toEqual(6);
  }));

  it('On updating the textbox value the form should be valid', async(() => {
    const newElement = Object.assign({}, formElement);
    const newModel = Object.assign({}, formModel);
    newElement.type.model.params[2].config.type.model.paramConfigIds = ["537", "538", "539", "540", "541", "543", "544"];
    newElement.type.model.params[2].type.model.params.push(textboxnotnullelement);
    newModel.params[2].config.type.model.paramConfigIds = ["537", "538", "539", "540", "541", "543", "544"];
    newModel.params[2].type.model.params.push(textboxnotnullmodel);
    hostComponent.element = newElement as Param;
    hostComponent.model = newModel as Model;
    fixture.detectChanges();
    const formValue = hostComponent.form.value;
    expect(formValue).toEqual({"medications1": "", "question1notnull": ""});
    expect(hostComponent.form.valid).toBeFalsy();
    const textBox = fixture.debugElement.query(By.css('.form-control.text-input')).nativeElement;
    textBox.value = 'test-436';
    textBox.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    expect(hostComponent.form.valid).toBeTruthy();
    expect(hostComponent.form.value).toEqual({"medications1": "", "question1notnull": "test-436"});
  }));

    it('toggle() should update the opened property',  async(() => {
    hostComponent.opened = true;
    hostComponent.toggle();
    expect(hostComponent.opened).toBeFalsy();
  }));

  it('groupFormElements() should update formElements property',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'tset';
      const model: any = { params: [hostComponent.element] };
      hostComponent.groupFormElements(model, true);
      expect(hostComponent.formElements.includes(model.params[0])).toBeTruthy();
    });
  });

  it('groupFormElements() should not call groupFormElements() two times',  async(() => {
    const model: any = { params: null };
    spyOn(hostComponent, 'groupFormElements').and.callThrough();
    hostComponent.groupFormElements(model, false);
    expect(hostComponent.groupFormElements).not.toHaveBeenCalledTimes(2);
  }));

  it('ngOnChanges() should call buildFormElements()',  async(() => {
    const changes: any = { model: { currentValue: 'test' } };
    const element: any = { config: '' };
    hostComponent.element = element;
    spyOn(hostComponent, 'buildFormElements').and.callThrough();
    hostComponent.ngOnChanges(changes);
    expect(hostComponent.buildFormElements).toHaveBeenCalled();
  }));

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is sixColumn',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'sixColumn';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('sixColumn');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is fourColumn',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'fourColumn';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('fourColumn');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is threeColumn',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'threeColumn';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('threeColumn');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is twoColumn',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'twoColumn';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('twoColumn');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is oneColumn',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'oneColumn';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('oneColumn');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is inline',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'inline';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('d-block d-md-inline-block mr-3');
    });
  });
 
  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is questionGroup',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'questionGroup';
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('questionGroup');
    });
  });

  it('ngOnInit() should update the elementCss property based on element.config.uiStyles.attributes.cssClass',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test';
      spyOn(hostComponent, 'buildFormElements').and.callThrough();
      hostComponent.ngOnInit();
      expect(hostComponent.elementCss).toEqual('test');
      expect(hostComponent.buildFormElements).toHaveBeenCalled();
    });
  });

  it('buildFormElements() should update the groupFormElements()',  async(() => {
    const model: any = {};
    const element: any = { config: '' };
    hostComponent.element = element;
    spyOn(hostComponent, 'groupFormElements').and.callThrough();
    hostComponent.buildFormElements(model);
    expect(hostComponent.groupFormElements).toHaveBeenCalled();
  }));

  it('buildFormElements() should subscribe to pageService.eventUpdate$',  async(() => {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    hostComponent.element = element;
    const eve = { leafState: { a: 'a' }, path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    spyOn(pageService.eventUpdate$, 'subscribe').and.callThrough();
    hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(pageService.eventUpdate$.subscribe).toHaveBeenCalled();
  }));

  it('buildFormElements() should call form.patchValue() and update form',  async(() => {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    hostComponent.element = element;
    const eve = { leafState: { a: 'a' }, path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(hostComponent.form).toBeTruthy();
  }));

  it('buildFormElements() should not call form.patchValue() and no form changes',  async(() => {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    hostComponent.element = element;
    const eve = { path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(hostComponent.form).toBeTruthy();
  }));

  it('buildFormElements() should not call form.patchValue() and no form changes',  async(() => {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    hostComponent.element = element;
    const eve = { path: '1test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(hostComponent.form).toBeTruthy();
  }));

  it('buildFormElements() should call form.patchValue() and update form',  async(() => {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    hostComponent.element = element;
    const eve = { leafState: 'test', path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    spyOn(hostComponent, 'hasNull').and.returnValue(false);
    hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(hostComponent.form).toBeTruthy();
  }));

  it('hasNull() should return true',  async(() => {
    const target = { a: null };
    expect(hostComponent.hasNull(target)).toBeTruthy();
  }));

  it('hasNull() should return false',  async(() => {
    const target = { a: '123' };
    expect(hostComponent.hasNull(target)).toBeFalsy();
  }));

  it('partialUpdate() should call form.patchValue()',  async(() => {
    const obj = {};
    spyOn(hostComponent.form, 'patchValue').and.returnValue('');
    hostComponent.partialUpdate(obj);
    expect(hostComponent.form.patchValue).toHaveBeenCalled();
  }));

  it('groupFormElements() should call groupFormElements() two times',  async(() => {
    const model1: any = 'test';
    hostComponent.element.type.model = model1;
    hostComponent.element.config.uiStyles = null;
    const model: any = { params: [hostComponent.element] };
    spyOn(hostComponent, 'groupFormElements').and.callThrough();
    hostComponent.groupFormElements(model, true);
    expect(hostComponent.groupFormElements).toHaveBeenCalledTimes(2);
  }));

  it('groupFormElements() should not update formElements property',  async(() => {
    hostComponent.element.config.uiStyles = null;
    const model: any = { params: [hostComponent.element] };
    hostComponent.formModel = [];
    hostComponent.groupFormElements(model, false);
    expect(hostComponent.formElements.includes(model.params[0])).toBeFalsy();
  }));

});

