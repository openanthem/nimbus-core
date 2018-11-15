'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, FormGroup, ValidatorFn, Validators, FormControl } from '@angular/forms';
import { DropdownModule, GrowlModule, MessagesModule, DialogModule, AccordionModule, 
    DataTableModule, FileUploadModule, PickListModule, ListboxModule, CheckboxModule, 
    RadioButtonModule, CalendarModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SimpleChanges } from '@angular/core';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';

import { Form } from './form.component';
import { FrmGroupCmp } from './form-group.component';
import { Accordion } from '../platform/content/accordion.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Button } from '../platform/form/elements/button.component';
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
// import { DateControl } from '../platform/form/elements/date.component';
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

let formElementsService, pageService, configService, param: Param;;

class MockLoggerService {
  debug() { }
  info() { }
  error() { }
}

class MockFormElementsService {
    toFormGroup(a, b) {
        return '';
    }
}

class MockPageService {
    eventUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
    }

    logError(a) {
        this.eventUpdate$.next(a);
    }
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
  FileUploadComponent,
  OrderablePickList,
  MultiselectCard,
  MultiSelectListBox,
  CheckBox,
  CheckBoxGroup,
  RadioButton,
  ComboBox,
  Calendar,
  // DateControl,
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
  PrintDirective
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
   TreeTableModule
];
const providers = [
{provide: FormElementsService, useClass: MockFormElementsService},
{provide: PageService, useClass: MockPageService},
{ provide: 'JSNLOG', useValue: JL },
{ provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
{provide: LoggerService, useClass: MockLoggerService},
 CustomHttpClient,
 LoaderService,
 ConfigService,
 AppInitService
];

describe('Form', () => {

  configureTestSuite();
  setup(Form, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<Form>){
      const fg = new FormGroup({});
      const checks: ValidatorFn[] = [];
      checks.push(Validators.required);
      fg.addControl(param.config.code, new FormControl(param.leafState,checks));
      this.hostComponent.form = fg;
      this.hostComponent.element = param;
      formElementsService = TestBed.get(FormElementsService);
      pageService = TestBed.get(PageService);
      configService = TestBed.get(ConfigService);
  });

  it('should create the Form', function(this: TestContext<Form>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('toggle() should update the opened property', function(this: TestContext<Form>) {
    this.hostComponent.opened = true;
    this.hostComponent.toggle();
    expect(this.hostComponent.opened).toBeFalsy();
  });

  it('groupFormElements() should update formElements property', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.alias = 'tset';
      const model: any = { params: [this.hostComponent.element] };
      this.hostComponent.groupFormElements(model, true);
      expect(this.hostComponent.formElements.includes(model.params[0])).toBeTruthy();
    });
  });

  it('groupFormElements() should not call groupFormElements() two times', function(this: TestContext<Form>) {
    const model: any = { params: null };
    spyOn(this.hostComponent, 'groupFormElements').and.callThrough();
    this.hostComponent.groupFormElements(model, false);
    expect(this.hostComponent.groupFormElements).not.toHaveBeenCalledTimes(2);
  });

  it('ngOnChanges() should call buildFormElements()', function(this: TestContext<Form>) {
    const changes: any = { model: { currentValue: 'test' } };
    const element: any = { config: '' };
    this.hostComponent.element = element;
    spyOn(this.hostComponent, 'buildFormElements').and.callThrough();
    this.hostComponent.ngOnChanges(changes);
    expect(this.hostComponent.buildFormElements).toHaveBeenCalled();
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is sixColumn', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'sixColumn';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('col-lg-2 col-md-4 col-sm-12');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is fourColumn', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'fourColumn';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('col-lg-3 col-md-6 col-sm-12');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is threeColumn', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'threeColumn';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('col-lg-4 col-md-6 col-sm-12');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is twoColumn', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'twoColumn';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('col-sm-12 col-md-6');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is oneColumn', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'oneColumn';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('col-sm-12');
    });
  });

  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is inline', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'inline';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('d-block d-md-inline-block mr-3');
    });
  });
 
  it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is questionGroup', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'questionGroup';
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual(' questionGroup');
    });
  });

  it('ngOnInit() should update the elementCss property based on element.config.uiStyles.attributes.cssClass', function(this: TestContext<Form>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.cssClass = 'test';
      spyOn(this.hostComponent, 'buildFormElements').and.callThrough();
      this.hostComponent.ngOnInit();
      expect(this.hostComponent.elementCss).toEqual('test');
      expect(this.hostComponent.buildFormElements).toHaveBeenCalled();
    });
  });

  it('buildFormElements() should update the groupFormElements()', function(this: TestContext<Form>) {
    const model: any = {};
    const element: any = { config: '' };
    this.hostComponent.element = element;
    spyOn(this.hostComponent, 'groupFormElements').and.callThrough();
    this.hostComponent.buildFormElements(model);
    expect(this.hostComponent.groupFormElements).toHaveBeenCalled();
  });

  it('buildFormElements() should subscribe to pageService.eventUpdate$', function(this: TestContext<Form>) {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    this.hostComponent.element = element;
    const eve = { leafState: { a: 'a' }, path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    spyOn(pageService.eventUpdate$, 'subscribe').and.callThrough();
    this.hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(pageService.eventUpdate$.subscribe).toHaveBeenCalled();
  });

  it('buildFormElements() should call form.patchValue() and update form', function(this: TestContext<Form>) {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    this.hostComponent.element = element;
    const eve = { leafState: { a: 'a' }, path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    this.hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(this.hostComponent.form).toBeTruthy();
  });

  it('buildFormElements() should not call form.patchValue() and no form changes', function(this: TestContext<Form>) {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    this.hostComponent.element = element;
    const eve = { path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    this.hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(this.hostComponent.form).toBeTruthy();
  });

  it('buildFormElements() should not call form.patchValue() and no form changes', function(this: TestContext<Form>) {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    this.hostComponent.element = element;
    const eve = { path: '1test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    this.hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(this.hostComponent.form).toBeTruthy();
  });

  it('buildFormElements() should call form.patchValue() and update form', function(this: TestContext<Form>) {
    const model: any = {};
    const element: any = { config: '', path: 'test' };
    this.hostComponent.element = element;
    const eve = { leafState: 'test', path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
    spyOn(this.hostComponent, 'hasNull').and.returnValue(false);
    this.hostComponent.buildFormElements(model);
    pageService.logError(eve);
    expect(this.hostComponent.form).toBeTruthy();
  });

  it('hasNull() should return true', function(this: TestContext<Form>) {
    const target = { a: null };
    expect(this.hostComponent.hasNull(target)).toBeTruthy();
  });

  it('hasNull() should return false', function(this: TestContext<Form>) {
    const target = { a: '123' };
    expect(this.hostComponent.hasNull(target)).toBeFalsy();
  });

  it('partialUpdate() should call form.patchValue()', function(this: TestContext<Form>) {
    const obj = {};
    spyOn(this.hostComponent.form, 'patchValue').and.returnValue('');
    this.hostComponent.partialUpdate(obj);
    expect(this.hostComponent.form.patchValue).toHaveBeenCalled();
  });

  it('groupFormElements() should call groupFormElements() two times', function(this: TestContext<Form>) {
    const model1: any = 'test';
    this.hostComponent.element.type.model = model1;
    this.hostComponent.element.config.uiStyles = null;
    const model: any = { params: [this.hostComponent.element] };
    spyOn(this.hostComponent, 'groupFormElements').and.callThrough();
    this.hostComponent.groupFormElements(model, true);
    expect(this.hostComponent.groupFormElements).toHaveBeenCalledTimes(2);
  });

  it('groupFormElements() should not update formElements property', function(this: TestContext<Form>) {
    this.hostComponent.element.config.uiStyles = null;
    const model: any = { params: [this.hostComponent.element] };
    this.hostComponent.formModel = [];
    this.hostComponent.groupFormElements(model, false);
    expect(this.hostComponent.formElements.includes(model.params[0])).toBeFalsy();
  });

});