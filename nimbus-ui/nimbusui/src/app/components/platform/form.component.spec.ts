'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule, GrowlModule, MessagesModule, DialogModule, AccordionModule, DataTableModule, FileUploadModule, PickListModule, ListboxModule, CheckboxModule, RadioButtonModule, CalendarModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SimpleChanges } from '@angular/core';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { Form } from './form.component';
import { FrmGroupCmp } from './form-group.component';
import { AccordionGroup } from '../platform/accordion-group.component';
import { Accordion } from '../platform/accordion.component';
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
import { DateControl } from '../platform/form/elements/date.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { Signature } from '../platform/form/elements/signature.component'
import { InputText } from '../platform/form/elements/textbox.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { Header } from './content/header.component';
import { Section } from './section.component';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { AccordionMain } from '../platform/content/accordion.component';
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

let fixture, app, formElementsService, pageService, configService;

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

describe('Form', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          Form,
          FrmGroupCmp,
          AccordionGroup,
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
          DateControl,
          TextArea,
          Signature,
          InputText,
          Paragraph,
          Header,
          Section,
          ActionDropdown,
          TooltipComponent,
          SelectItemPipe,
          AccordionMain,
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
          Image
       ],
       imports: [
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
           AngularSvgIconModule
       ],
       providers: [
        {provide: FormElementsService, useClass: MockFormElementsService},
        {provide: PageService, useClass: MockPageService},
        { provide: 'JSNLOG', useValue: JL },
        { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
        {provide: LoggerService, useClass: MockLoggerService},
         CustomHttpClient,
         LoaderService,
         ConfigService,
         AppInitService
        ]
    }).compileComponents();
    fixture = TestBed.createComponent(Form);
    app = fixture.debugElement.componentInstance;
    formElementsService = TestBed.get(FormElementsService);
    pageService = TestBed.get(PageService);
    configService = TestBed.get(ConfigService);
  }));

    it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

    it('toggle() should update the opened property', async(() => {
      app.opened = true;
      app.toggle();
      expect(app.opened).toBeFalsy();
    }));

    it('groupFormElements() should update groups property', async(() => {
      const model = { params: [{ config: { uiStyles: { attributes: { alias: 'Accordion' } } } }] };
      app.groupFormElements(model);
      expect(app.groups.includes(model.params[0])).toBeTruthy();
    }));

    it('groupFormElements() should update formElements property', async(() => {
      const model = { params: [{ config: { uiStyles: { attributes: { alias: 'tset' } } } }] };
      app.groupFormElements(model);
      expect(app.formElements.includes(model.params[0])).toBeTruthy();
    }));

    it('groupFormElements() should not update formElements property', async(() => {
      const model = { params: [{ config: { uiStyles: null } }] };
      app.groupFormElements(model);
      expect(app.formElements.includes(model.params[0])).toBeFalsy();
    }));

    it('groupFormElements() should call groupFormElements() two times', async(() => {
      const model = { params: [{ type: { model: 'test' }, config: { uiStyles: null } }] };
      spyOn(app, 'groupFormElements').and.callThrough();
      app.groupFormElements(model);
      expect(app.groupFormElements).toHaveBeenCalledTimes(2);
    }));

    it('groupFormElements() should not call groupFormElements() two times', async(() => {
      const model = { params: null };
      spyOn(app, 'groupFormElements').and.callThrough();
      app.groupFormElements(model);
      expect(app.groupFormElements).not.toHaveBeenCalledTimes(2);
    }));

    it('ngOnChanges() should call buildFormElements()', async(() => {
      const changes = { model: { currentValue: 'test' } };
      app.element = { config: '' };
      spyOn(app, 'buildFormElements').and.callThrough();
      app.ngOnChanges(changes);
      expect(app.buildFormElements).toHaveBeenCalled();
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is sixColumn', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'sixColumn' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual('col-lg-2 col-md-4 col-sm-12');
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is fourColumn', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'fourColumn' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual('col-lg-3 col-md-6 col-sm-12');
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is threeColumn', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'threeColumn' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual('col-lg-4 col-md-6 col-sm-12');
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is twoColumn', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'twoColumn' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual('col-sm-12 col-md-6');
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is oneColumn', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'oneColumn' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual('col-sm-12');
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is inline', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'inline' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual('d-inline-block mr-3');
    }));

    it('ngOnInit() should update the elementCss property if element.config.uiStyles.attributes.cssClass is questionGroup', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'questionGroup' } } } };
      app.ngOnInit();
      expect(app.elementCss).toEqual(' questionGroup');
    }));

    it('ngOnInit() should update the elementCss property based on element.config.uiStyles.attributes.cssClass', async(() => {
      app.element = { config: { uiStyles: { attributes: { cssClass: 'test' } } } };
      spyOn(app, 'buildFormElements').and.callThrough();
      app.ngOnInit();
      expect(app.elementCss).toEqual('test');
      expect(app.buildFormElements).toHaveBeenCalled();
    }));

    it('buildFormElements() should update the groupFormElements()', async(() => {
      const model = {};
      app.element = { config: '' };
      spyOn(app, 'groupFormElements').and.callThrough();
      app.buildFormElements(model);
      expect(app.groupFormElements).toHaveBeenCalled();
    }));

    it('buildFormElements() should subscribe to pageService.eventUpdate$', async(() => {
      const model = {};
      app.element = { config: '', path: 'test' };
      const eve = { leafState: { a: 'a' }, path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
      app.form = { patchValue: () => {} };
      spyOn(pageService.eventUpdate$, 'subscribe').and.callThrough();
      app.buildFormElements(model);
      pageService.logError(eve);
      expect(pageService.eventUpdate$.subscribe).toHaveBeenCalled();
    }));

    it('buildFormElements() should call form.patchValue() and update form', async(() => {
      const model = {};
      app.element = { config: '', path: 'test' };
      const eve = { leafState: { a: 'a' }, path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
      app.buildFormElements(model);
      pageService.logError(eve);
      expect(app.form).toBeTruthy();
    }));

    it('buildFormElements() should not call form.patchValue() and no form changes', async(() => {
      const model = {};
      app.element = { config: '', path: 'test' };
      const eve = { path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
      app.form = { patchValue: () => {} };
      app.buildFormElements(model);
      pageService.logError(eve);
      expect(app.form).toBeTruthy();
    }));

    it('buildFormElements() should not call form.patchValue() and no form changes', async(() => {
      const model = {};
      app.element = { config: '', path: 'test' };
      const eve = { path: '1test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
      app.form = { patchValue: () => {} };
      app.buildFormElements(model);
      pageService.logError(eve);
      expect(app.form).toBeTruthy();
    }));

    it('buildFormElements() should call form.patchValue() and update form', async(() => {
      const model = {};
      app.element = { config: '', path: 'test' };
      const eve = { leafState: 'test', path: 'test', config: { uiStyles: { attributes: { alias: 'Form' } } } };
      app.form = { patchValue: () => {} };
      spyOn(app, 'hasNull').and.returnValue(false);
      app.buildFormElements(model);
      pageService.logError(eve);
      expect(app.form).toBeTruthy();
    }));

    it('hasNull() should return true', async(() => {
      const target = { a: null };
      expect(app.hasNull(target)).toBeTruthy();
    }));

    it('hasNull() should return false', async(() => {
      const target = { a: '123' };
      expect(app.hasNull(target)).toBeFalsy();
    }));

    it('partialUpdate() should call form.patchValue()', async(() => {
      const obj = {};
      app.form = { patchValue: () => {} };
      spyOn(app.form, 'patchValue').and.returnValue('');
      app.partialUpdate(obj);
      expect(app.form.patchValue).toHaveBeenCalled();
    }));

});