'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, MessagesModule, InputSwitchModule, TreeTableModule  } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Table } from 'primeng/table';
import * as moment from 'moment';
import { ElementRef, NgZone } from '@angular/core';
import { DomHandler } from 'primeng/components/dom/domhandler';
import { ObjectUtils } from 'primeng/components/utils/objectutils';
import { TableService } from 'primeng/components/table/table';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

import { DataTable } from './table.component';
import { Section } from '../section.component';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { ComboBox } from '../../platform/form/elements/combobox.component';
import { InputText } from '../form/elements/textbox.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Button } from '../form/elements/button.component';
import { Menu } from '../menu.component';
import { Link } from '../link.component';
import { Form } from '../form.component';
import { StaticText } from '../content/static-content.component';
import { Paragraph } from '../content/paragraph.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { MessageComponent } from '../message/message.component';
import { ActionLink } from '../form/elements/action-dropdown.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { FrmGroupCmp } from '../form-group.component';
import { Accordion } from '../content/accordion.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { FormElement } from '../form-element.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { TextArea } from '../form/elements/textarea.component';
import { FileUploadComponent } from '../fileupload/file-upload.component';
import { OrderablePickList } from '../form/elements/picklist.component';
import { MultiselectCard } from '../form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../form/elements/multi-select-listbox.component';
import { CheckBox } from '../form/elements/checkbox.component';
import { CheckBoxGroup } from '../form/elements/checkbox-group.component';
import { RadioButton } from '../form/elements/radio.component';
import { Calendar } from '../form/elements/calendar.component';
import { DateControl } from '../form/elements/date.component';
import { Signature } from '../form/elements/signature.component';
import { Header } from '../content/header.component';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { GridService } from '../../../services/grid.service';
import { Param } from '../../../shared/param-state';
import { Subject } from 'rxjs';
import { ParamConfig, ConfigType, UiStyle, UiAttribute } from '../../../shared/param-config';
import { ParamUtils } from '../../../shared/param-utils';
import { ViewComponent } from '../../../shared/param-annotations.enum';
import { HeaderCheckBox } from '../form/elements/header-checkbox.component';
import { SvgComponent } from '../svg/svg.component';
import { Image } from '../image.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputSwitch } from '../../platform/form/elements/input-switch.component';
import { TreeGrid } from '../../platform/tree-grid/tree-grid.component';
import { Label } from '../../platform/content/label.component';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { CardDetailsFieldGroupComponent } from '../../platform/card/card-details-field-group.component';
import { FormGridFiller } from '../../platform/form/form-grid-filler.component';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { InputLegend } from '../../platform/form/elements/input-legend.component';

let fixture, app, configService, pageService, elementRef, ngZone, objectUtils, domHandler, tableService;

class MockPageService {
    gridValueUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;

    constructor() {
        this.gridValueUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
    }
    postOnChange(a, b, c) { }
    processEvent(a, s, d, f, g) { }
    logError(a) {
        this.gridValueUpdate$.next(a);
    }
    notifyErrorEvent(a) {
        this.validationUpdate$.next(a);
    }
}

class MockElementRef {
    nativeElement = {
        querySelector: () => {
            return {
                scrollIntoView: () => {}};
        }
    };
}

describe('DataTable', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          DataTable,
          Section,
          ActionDropdown,
          TooltipComponent,
          ComboBox,
          InputText,
          ButtonGroup,
          Button,
          Menu,
          Link,
          Form,
          StaticText,
          Paragraph,
          CardDetailsComponent,
          CardDetailsGrid,
          MessageComponent,
          ActionLink,
          SelectItemPipe,
          FrmGroupCmp,
          Accordion,
          CardDetailsFieldComponent,
          FormElement,
          DateTimeFormatPipe,
          InPlaceEditorComponent,
          TextArea,
          FileUploadComponent,
          OrderablePickList,
          MultiselectCard,
          MultiSelectListBox,
          CheckBox,
          CheckBoxGroup,
          RadioButton,
          Calendar,
          DateControl,
          Signature,
          Header,
          HeaderCheckBox,
          SvgComponent,
          Image,
          DisplayValueDirective,
          InputSwitch,
          TreeGrid,
          Label,
          InputLabel,
          CardDetailsFieldGroupComponent,
          FormGridFiller,
          InputLegend
       ],
       imports: [
           DialogModule,
           FormsModule,
           DropdownModule,
           DataTableModule,
           AccordionModule,
           ReactiveFormsModule,
           GrowlModule,
           MessagesModule,
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
           AngularSvgIconModule,
           ToastModule,
           InputSwitchModule,
           TreeTableModule,
           StorageServiceModule
       ],
       providers: [
           {provide: PageService, useClass: MockPageService},
           {provide: ElementRef, useClass: MockElementRef},
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           CustomHttpClient,
           LoaderService,
           ConfigService,
           LoggerService,
           GridService,
           ObjectUtils,
           DomHandler,
           TableService,
           SessionStoreService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(DataTable);
    app = fixture.debugElement.componentInstance;
    configService = TestBed.get(ConfigService);
    pageService = TestBed.get(PageService);
    elementRef = TestBed.get(ElementRef);
    objectUtils = TestBed.get(ObjectUtils);
    domHandler = TestBed.get(DomHandler);
    tableService = TestBed.get(TableService);
  }));

    it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

    it('app._value should update the app.value', async(() => {
      app._value = 'test';
      expect(app.value).toEqual('test');
    }));

    it('app.value should update the app.value', async(() => {
      app.value = 'test';
      expect(app.value).toEqual('test');
    }));

    it('writeValue() should call cd.markForCheck()', async(() => {
      app.cd = { markForCheck: () => {} };
      spyOn(app.cd, 'markForCheck').and.callThrough();
      app.writeValue({});
      expect(app.cd.markForCheck).toHaveBeenCalled();
    }));

    it('writeValue() should call cd.markForCheck() on undefined argument', async(() => {
      app.cd = { markForCheck: () => {} };
      spyOn(app.cd, 'markForCheck').and.callThrough();
      app.writeValue(undefined);
      expect(app.cd.markForCheck).toHaveBeenCalled();
    }));

    it('registerOnChange() should update the onChange property', async(() => {
      const test = () => {};
      app.registerOnChange(test);
      expect(app.onChange).toEqual(test);
    }));

    it('registerOnTouched() should update the onTouched property', async(() => {
      const test = () => {};
      app.registerOnTouched(test);
      expect(app.onTouched).toEqual(test);
    }));

    it('ngOnInit() should update the hasFilters, and rowExpanderKey properties', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { rowSelection: true } } };
      app.element.gridList = [1];
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      app.rowExpanderKey = 'test';
      app.dt = { filterConstraints: { between: '' } };
      app.between = 'test';
      app.ngOnInit();
      expect(app.hasFilters).toBeFalsy();
      expect(app.rowExpanderKey).toEqual('test');
    }));

    it('ngAfterViewInit() should  call pageService.processEvent()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(pageService, 'processEvent').and.callThrough();
      app.ngAfterViewInit();
      expect(pageService.processEvent).toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should not call pageService.processEvent()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(pageService, 'processEvent').and.callThrough();
      app.ngAfterViewInit();
      expect(pageService.processEvent).not.toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should  not call pageService.processEvent()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: false } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(pageService, 'processEvent').and.callThrough();
      app.ngAfterViewInit();
      expect(pageService.processEvent).not.toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should call dt.filter()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
      app.params = [{ code: '', uiStyles: { attributes: { filterValue: 'a', filterMode: '' } } }];
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app.dt, 'filter').and.returnValue('');
      app.ngAfterViewInit();
      expect(app.dt.filter).toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should not call dt.filter()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
      app.params = [{ code: '', uiStyles: { attributes: { filterValue: '', filterMode: '' } } }];
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app.dt, 'filter').and.returnValue('');
      app.ngAfterViewInit();
      expect(app.dt.filter).not.toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should update the value, totalRecords properties and call updatePageDetailsState()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
      app.element.path = 'test';
      const eve = { path: 'test', gridList: [], page: { totalElements: 'telements', first: true } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app, 'updatePageDetailsState').and.callThrough();
      app.ngAfterViewInit();
      pageService.logError(eve);
      expect(app.value).toEqual([]);
      expect(app.totalRecords).toEqual('telements');
      expect(app.updatePageDetailsState).toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should not call updatePageDetailsState()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
      app.element.path = 'test';
      const eve = { path: '1test', gridList: 'tGrid', page: { totalElements: 'telements', first: true } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app, 'updatePageDetailsState').and.callThrough();
      app.ngAfterViewInit();
      pageService.logError(eve);
      expect(app.updatePageDetailsState).not.toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should call updatePageDetailsState() and update the dt.first', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: false } } };
      app.element.path = 'test';
      const eve = { path: 'test', gridList: 'tGrid', page: { totalElements: 'telements', first: true } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app, 'updatePageDetailsState').and.callThrough();
      app.ngAfterViewInit();
      pageService.logError(eve);
      expect(app.updatePageDetailsState).toHaveBeenCalled();
      expect(app.dt.first).toEqual(0);
    }));

    it('ngAfterViewInit() should not call updatePageDetailsState() based on eve.page.first', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
      app.element.path = 'test';
      const eve = { path: 'test', gridList: 'tGrid', page: { totalElements: 'telements', first: false } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app, 'updatePageDetailsState').and.callThrough();
      app.ngAfterViewInit();
      pageService.logError(eve);
      expect(app.updatePageDetailsState).not.toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should call form.controls.t.enable()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
      app.form = { controls: { t: { enable: () => {}, disable: () => {} } } };
      app.element.path = '/test';
      const eve = { enabled: true, path: '/test', config: { code: 't' } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app.form.controls.t, 'enable').and.callThrough();
      app.ngAfterViewInit();
      pageService.notifyErrorEvent(eve);
      expect(app.form.controls.t.enable).toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should call form.controls.t.disable()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
      app.form = { controls: { t: { enable: () => {}, disable: () => {} } } };
      app.element.path = '/test';
      const eve = { enabled: false, path: '/test', config: { code: 't' } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app.form.controls.t, 'disable').and.callThrough();
      app.ngAfterViewInit();
      pageService.notifyErrorEvent(eve);
      expect(app.form.controls.t.disable).toHaveBeenCalled();
    }));

    it('ngAfterViewInit() should not call form.controls.t.disable()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
      app.form = { controls: { t: { enable: () => {}, disable: () => {} } } };
      app.element.path = '/test';
      const eve = { enabled: false, path: '/1test', config: { code: 't' } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(app.form.controls.t, 'disable').and.callThrough();
      app.ngAfterViewInit();
      pageService.notifyErrorEvent(eve);
      expect(app.form.controls.t.disable).not.toHaveBeenCalled();
    }));

    it('isRowExpanderHidden() should return true', async(() => {
      app.rowExpanderKey = '';
      expect(app.isRowExpanderHidden('')).toBeTruthy();
    }));

    it('isRowExpanderHidden() should return true based on argument', async(() => {
      app.rowExpanderKey = 't';
      const data = { t: true };
      expect(app.isRowExpanderHidden(data)).toBeTruthy();
    }));

    it('isRowExpanderHidden() should return false', async(() => {
      app.rowExpanderKey = 't';
      const data = { t: false };
      expect(app.isRowExpanderHidden(data)).toBeFalsy();
    }));

    it('getCellDisplayValue() should call dtFormat.transform()', async(() => {
      const col = new ParamConfig(configService);
      col.code = 't';
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.datePattern = 'Date';
      const rowData = { t: true };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(true);
      spyOn(app.dtFormat, 'transform').and.returnValue('test');
      app.getCellDisplayValue(rowData, col);
      expect(app.dtFormat.transform).toHaveBeenCalled();
    }));

    it('getCellDisplayValue() should not call dtFormat.transform() and return true', async(() => {
      const col = new ParamConfig(configService);
      col.code = 't';
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.datePattern = 'Date';
      const rowData = { t: true };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
      spyOn(app.dtFormat, 'transform').and.returnValue('test');
      const res = app.getCellDisplayValue(rowData, col);
      expect(app.dtFormat.transform).not.toHaveBeenCalled();
      expect(res).toBeTruthy();
    }));

    it('getCellDisplayValue() should not call dtFormat.transform() and return col.uiStyles.attributes.placeholder', async(() => {
      const col = new ParamConfig(configService);
      col.code = 't';
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.datePattern = 'Date';
      col.uiStyles.attributes.placeholder = 'test';
      const rowData = { t: null };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
      spyOn(app.dtFormat, 'transform').and.returnValue('test');
      const res = app.getCellDisplayValue(rowData, col);
      expect(app.dtFormat.transform).not.toHaveBeenCalled();
      expect(res).toEqual('test');
    }));

    it('showColumn() should return true', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'test';
      expect(app.showColumn(col)).toBeTruthy();
    }));

    it('showColumn() should return true for link', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'Link';
      expect(app.showColumn(col)).toBeTruthy();
    }));

    it('showColumn() should return true for button', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'Button';
      expect(app.showColumn(col)).toBeTruthy();
    }));

    it('showColumn() should return true for LinkMenu', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(app.showColumn(col)).toBeTruthy();
    }));

    it('showColumn() should return false for gridRowBody', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'GridRowBody';
      expect(app.showColumn(col)).toBeFalsy();
    }));

    it('showColumn() should return false', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = true;
      col.uiStyles.attributes.alias = 'test';
      expect(app.showColumn(col)).toBeFalsy();
    }));

    it('showHeader() should return true', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'GridColumn';
      expect(app.showHeader(col)).toBeTruthy();
    }));

    it('showHeader() should return false', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'Button';
      expect(app.showHeader(col)).toBeFalsy();
    }));

    it('showValue(col) should return true', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'test';
      expect(app.showValue(col)).toBeFalsy();
    }));

    it('showValue(col) should return false if no uiStyle is given', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      expect(app.showValue(col)).toBeFalsy();
    })); 

    it('showValue(col) should return true for GridColumn', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = ViewComponent.gridcolumn.toString();
      expect(app.showValue(col)).toBeTruthy();
    }));

    it('showValue(col) should return false for GridColumn with showAsLink attribute', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'GridColumn';
      col.uiStyles.attributes.showAsLink = true;
      expect(app.showValue(col)).toBeFalsy();
    }));

    it('showValue(col) should return false for link', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Link';
      expect(app.showValue(col)).toBeFalsy();
    }));

    it('showValue(col) should return false for button', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Button';
      expect(app.showValue(col)).toBeFalsy();
    }));

    it('showValue(col) should return false for link Menu', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(app.showValue(col)).toBeFalsy();
    }));

    it('showValue(col) should return false for grid row body', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'GridRowBody';
      expect(app.showValue(col)).toBeFalsy();
    }));

    it('showUiStyleInColumn(col) should return true for link', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Link';
      expect(app.showUiStyleInColumn(col)).toBeTruthy();
    }));

    it('showUiStyleInColumn(col) should return true for button', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Button';
      expect(app.showUiStyleInColumn(col)).toBeTruthy();
    }));

    it('showUiStyleInColumn(col) should return true for linkMenu', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(app.showUiStyleInColumn(col)).toBeTruthy();
    }));

    it('showUiStyleInColumn(col) should return false', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'TextBox';
      expect(app.showUiStyleInColumn(col)).toBeFalsy();
    }));

    it('showUiStyleInColumn(col) should return false', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Link1';
      expect(app.showUiStyleInColumn(col)).toBeFalsy();
    }));

    it('showLinkMenu(col) should return true', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(app.showLinkMenu(col)).toBeTruthy();
    }));

    it('showLinkMenu(col) should return false', async(() => {
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = '1LinkMenu';
      expect(app.showLinkMenu(col)).toBeFalsy();
    }));

    it('getViewParam() should return true', async(() => {
      const col = { code: '' };
      app.element = new Param(configService);
      app.element.collectionParams = { find: () => {
          return true;
        } };
      expect(app.getViewParam()).toBeTruthy();
    }));
    
    it('isClickedOnDropDown() should return true', async(() => {
      const dArray = [{ elementRef: { nativeElement: { contains: () => {
                return true;
              } } } }];
      expect(app.isClickedOnDropDown(dArray)).toBeTruthy();
    }));

    it('isClickedOnDropDown() should return false', async(() => {
      const dArray = [{ elementRef: { nativeElement: { contains: () => {
                return false;
              } } } }];
      expect(app.isClickedOnDropDown(dArray)).toBeFalsy();
    }));

    it('isActive() should return true', async(() => {
      app.filterState = { test: 123 };
      expect(app.isActive('test')).toBeTruthy();
    }));

    it('isActive()should return false', async(() => {
      app.filterState = { test: 123 };
      expect(app.isActive('test1')).toBeFalsy();
    }));

    it('getRowPath() should return path', async(() => {
      const col = new ParamConfig(configService);
      col.code = '123';
      app.element = new Param(configService);
      app.element.path = '/test';
      const item = { elemId: 456 };
      expect(app.getRowPath(col, item)).toEqual('/test/456/123');
    }));

    it('processOnClick() should call pageService.processEvent()', async(() => {
      const col = new ParamConfig(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.b = '';
      col.uiStyles.attributes.method = '';
      app.element = new Param(configService);
      app.element.path = '';
      spyOn(pageService, 'processEvent').and.returnValue('');
      app.processOnClick(col, { elemId: '' });
      expect(pageService.processEvent).toHaveBeenCalled();
    }));

    it('processOnClick() should call pageService.processEvent() based on getAllURLParams()', async(() => {
      const col = new ParamConfig(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.b = '';
      col.uiStyles.attributes.method = '';
      app.element = new Param(configService);
      app.element.path = '';
      spyOn(pageService, 'processEvent').and.returnValue('');
      spyOn(app, 'getAllURLParams').and.returnValue(['test']);
      app.processOnClick(col, { elemId: '', es: 't' });
      expect(pageService.processEvent).toHaveBeenCalled();
    }));

    it('processOnClick() should call pageService.processEvent() based on getAllURLParams() and argument', async(() => {
      const col = new ParamConfig(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.b = '';
      col.uiStyles.attributes.method = '';
      app.element = new Param(configService);
      app.element.path = '';
      spyOn(pageService, 'processEvent').and.returnValue('');
      spyOn(app, 'getAllURLParams').and.returnValue(['test']);
      app.processOnClick(col, { elemId: '' });
      expect(pageService.processEvent).toHaveBeenCalled();
    }));

    it('toggleFilter() should update the showFilters property', async(() => {
      app.showFilters = true;
      app.toggleFilter('');
      expect(app.showFilters).toBeFalsy();
    }));

    it('postGridData() should call the pageService.processEvent()', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { postButtonTargetPath: true, postButtonUrl: '/test' } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      app.selectedRows = [{ elemId: 123 }];
      spyOn(pageService, 'processEvent').and.returnValue('');
      app.postGridData({});
      expect(pageService.processEvent).toHaveBeenCalled();
    }));

    it('resetMultiSelection should update the selectedRows property', async(() => {
      app.resetMultiSelection();
      expect(app.selectedRows).toEqual([]);
    }));

    it('customSort() should not sort value array for number type', async(() => {
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.value = [{ test: null, index: 1 }, { test: null, index: 2 }];
      const value = app.value;
      app.customSort(eve);
      expect(app.value).toEqual(value);
    }));

    it('customSort() should sort value array for number type, if first code is null', async(() => {
      const eve = { order: -2, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.value = [{ test: null, index: 4 }, { test: 1, index: 3 }];
      app.customSort(eve);
      expect(app.value[0].index).toEqual(3);
    }));

    it('customSort() should sort value array for number type, if second code is null', async(() => {
      const eve = { order: 1, field: { order: 1, code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.value = [{ index: 2, test: 1 }, { index: 1, test: null }];
      app.customSort(eve);
      expect(app.value[0].index).toEqual(1);
    }));

    it('customSort() should sort value array for number type, if second code less than first code', async(() => {
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.value = [{ index: 5, test: 1 }, { index: 1, test: 0 }];
      app.customSort(eve);
      expect(app.value[0].index).toEqual(1);
    }));

    it('customSort() should sort value array for number type, if first code less than second code', async(() => {
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.value = [{ test: 1, index: 4 }, { test: 2, index: 1 }];
      app.customSort(eve);
      expect(app.value[0].index).toEqual(1);
    }));

    it('customSort() should sort value array for number type, if both codes are equal', async(() => {
      const eve = { field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.value = [{ index: 1, test: 1 }, { index: 2, test: 1 }];
      const value = app.value;
      app.customSort(eve);
      expect(app.value).toEqual(value);
    }));

    it('customSort() should not sort value array for Date type', async(() => {
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsDate').and.returnValue(true);
      app.value = [{ test: null }, { test: null }];
      const value = app.value;
      app.customSort(eve);
      expect(app.value).toEqual(value);
    }));

    it('customSort() should sort value array for Date type, if first code is null', async(() => {
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      spyOn(app, 'isSortAsDate').and.returnValue(true);
      app.value = [{ test: null }, { test: 1 }];
      app.customSort(eve);
      expect(app.value[0].test).toEqual(1);
    }));

    it('customSort() should not sort value array', async(() => {
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      app.value = [{ test: null }, { test: null }];
      const value = app.value;
      app.customSort(eve);
      expect(app.value).toEqual(value);
    }));

    it('customSort() should sort value array, if first code is null', async(() => {
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      app.value = [{ test: null }, { test: 11 }];
      app.customSort(eve);
      expect(app.value[0].test).toEqual(11);
    }));

    it('customSort() should sort value array, if second code is null', async(() => {
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      app.value = [{ test: 111 }, { test: null }];
      app.customSort(eve);
      expect(app.value[0].test).toEqual(null);
    }));

    it('customSort() should sort value array, if first code less than second code', async(() => {
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      app.value = [{ test: { localeCompare: () => {
              return 1;
            } } }, { test: 222 }];
      app.customSort(eve);
      expect(app.value[0].test).toEqual(222);
    }));

    it('isSortAsNumber() should return true for int, NUMBER argument', async(() => {
      const res = app.isSortAsNumber('int', 'NUMBER');
      expect(res).toBeTruthy();
    }));

    it('isSortAsNumber() should return true for int, null argument', async(() => {
      const res = app.isSortAsNumber('int', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsNumber() should return true for integer, null argument', async(() => {
      const res = app.isSortAsNumber('integer', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsNumber() should return true for long, null argument', async(() => {
      const res = app.isSortAsNumber('long', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsNumber() should return true for double, null argument', async(() => {
      const res = app.isSortAsNumber('double', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsDate() should return true for int, DATE argument', async(() => {
      const res = app.isSortAsDate('int', 'DATE');
      expect(res).toBeTruthy();
    }));

    it('isSortAsDate() should return true for int, null argument', async(() => {
      const res = app.isSortAsDate('date', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsDate() should return true for localdate, null argument', async(() => {
      const res = app.isSortAsDate('localdate', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsDate() should return true for localdatetime, null argument', async(() => {
      const res = app.isSortAsDate('localdatetime', null);
      expect(res).toBeTruthy();
    }));

    it('isSortAsDate() should return true for zonedatetime, null argument', async(() => {
      const res = app.isSortAsDate('zoneddatetime', null);
      expect(res).toBeTruthy();
    }));

    it('between() should return false', async(() => {
      expect(app.between('day', 1)).toBeFalsy();
    }));

    it('dateFilter() should call updatePageDetailsState() and dt.filter()', async(() => {
      const e = new Date();
      const dt = new Table(elementRef, domHandler, objectUtils, null, tableService);
      const datePattern = 'MMDDYYYY';
      spyOn(app, 'updatePageDetailsState').and.callThrough();
      spyOn(dt, 'filter').and.callThrough();
      app.dateFilter(e, dt, '', datePattern);
      expect(app.updatePageDetailsState).toHaveBeenCalled();
      expect(dt.filter).toHaveBeenCalled();
    }));

    it('inputFilter() shlould call dt.filter()', async(() => {
      const dt = { filter: () => {} };
      spyOn(dt, 'filter').and.returnValue(true);
      app.inputFilter({ target: { value: 1 } }, dt, 't', 't');
      setTimeout(() => {
        expect(dt.filter).toHaveBeenCalled();
      }, 600);
    }));

    it('inputFilter() shlould call dt.filter() based onb the filterTimeout property', async(() => {
      const dt = { filter: () => {} };
      spyOn(dt, 'filter').and.returnValue(true);
      app.filterTimeout = true;
      app.inputFilter({ target: { value: 1 } }, dt, 't', 't');
      setTimeout(() => {
        expect(dt.filter).toHaveBeenCalled();
      }, 600);
    }));

    it('clearFilter() shlould call dt.filter()', async(() => {
      const dt = { filter: () => {} };
      spyOn(dt, 'filter').and.returnValue('');
      app.clearFilter({ value: 1 }, dt, 'as', 1);
      expect(dt.filter).toHaveBeenCalled();
    }));

    it('clearAll() should calldt.reset() and update the filterState property', async(() => {
      app.dt = { reset: () => {} };
      spyOn(app.dt, 'reset').and.returnValue('');
      app.clearAll();
      expect(app.dt.reset).toHaveBeenCalled();
      expect(app.filterState).toEqual([]);
    }));

    it('paginate() should update the rowEnd property', async(() => {
      const eve = { first: 12, rows: 2 };
      app.totalRecords = 15;
      app.paginate(eve);
      expect(app.rowEnd).toEqual(14);
    }));

    it('paginate() should update the rowEnd property based on the totalRecords value', async(() => {
      const eve = { first: 12, rows: 2 };
      app.totalRecords = 13;
      app.paginate(eve);
      expect(app.rowEnd).toEqual(13);
    }));

    it('updatePageDetailsState() should update the rowStart property', async(() => {
      app.totalRecords = 1;
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 3 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      app.updatePageDetailsState();
      expect(app.rowStart).toEqual(1);
    }));

    it('updatePageDetailsState() should update the rowStart property as 0', async(() => {
      app.totalRecords = 0;
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 3 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      app.updatePageDetailsState();
      expect(app.rowStart).toEqual(0);
    }));

    it('filterCallBack() should call the updatePageDetailsState()', async(() => {
      const eve = { filteredValue: [1] };
      spyOn(app, 'updatePageDetailsState').and.returnValue('');
      app.filterCallBack(eve);
      expect(app.updatePageDetailsState).toHaveBeenCalled();
      expect(app.totalRecords).toEqual(1);
    }));

    it('toggleOpen() should call the mouseEventSubscription.unsubscribe()', async(() => {
      const eve = { isOpen: true, state: 'openPanel' };
      app.mouseEventSubscription = { closed: false, unsubscribe: () => {} };
      app.cd = { detectChanges: () => {} };
      spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
      app.toggleOpen(eve);
      expect(app.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
    }));

    it('toggleOpen() should not call the mouseEventSubscription.unsubscribe()', async(() => {
      const eve = { isOpen: true, state: 'openPanel' };
      app.mouseEventSubscription = { closed: true, unsubscribe: () => {} };
      app.cd = { detectChanges: () => {} };
      spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
      app.toggleOpen(eve);
      expect(app.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
    }));

    it('toggleOpen() should not call the mouseEventSubscription.unsubscribe() based on eve.state', async(() => {
      const eve = { isOpen: true, state: '1openPanel' };
      app.mouseEventSubscription = { closed: true, unsubscribe: () => {} };
      app.cd = { detectChanges: () => {} };
      spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
      app.toggleOpen(eve);
      expect(app.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
    }));

    it('toggleOpen() should not call the mouseEventSubscription.unsubscribe() based on dropDowns property', async(() => {
      app.dropDowns = { toArray: () => {
          return [{ isOpen: true, selectedItem: false, state: '' }];
        } };
      const eve = { isOpen: true, state: '1openPanel' };
      app.mouseEventSubscription = { closed: false, unsubscribe: () => {} };
      app.cd = { detectChanges: () => {} };
      spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
      app.toggleOpen(eve);
      expect(app.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
    }));

    it('export() should call dt.exportCSV()', async(() => {
      const tDate = new Date('December 17, 2017');
      app.dt = { filteredValue: [{ a: tDate }], value: '', exportCSV: () => {} };
      app.params = [{ code: 'a', type: { name: 'Date' }, uiStyles: { attributes: {datePattern: ''} } }];
      spyOn(app.dt, 'exportCSV').and.callThrough();
      app.export();
      expect(app.dt.exportCSV).toHaveBeenCalled();
    }));

    it('export() should call dt.exportCSV() even without dt.filterValue.a', async(() => {
      const tDate = new Date('December 17, 2017');
      app.dt = { filteredValue: [{ a: '' }], value: '', exportCSV: () => {} };
      app.params = [{ code: 'a', type: { name: 'Date' } }];
      spyOn(app.dt, 'exportCSV').and.callThrough();
      app.export();
      expect(app.dt.exportCSV).toHaveBeenCalled();
    }));

    it('export() should call dt.exportCSV() even without dt.filterValue', async(() => {
      const tDate = new Date('December 17, 2017');
      app.dt = { filteredValue: [{}], value: '', exportCSV: () => {} };
      app.params = [{ code: 'a', type: null }];
      spyOn(app.dt, 'exportCSV').and.callThrough();
      app.export();
      expect(app.dt.exportCSV).toHaveBeenCalled();
    }));

    it('ngOnDestroy() should call the mouseEventSubscription.unsubscribe and cd.detach()', async(() => {
      app.mouseEventSubscription = { unsubscribe: () => {} };
      app.cd = { detach: () => {} };
      spyOn(app.cd, 'detach').and.callThrough();
      spyOn(app.mouseEventSubscription, 'unsubscribe').and.callThrough();
      app.ngOnDestroy();
      expect(app.cd.detach).toHaveBeenCalled();
      expect(app.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
    }));

    it('ngOnDestroy() should call the cd.detach()', async(() => {
      app.cd = { detach: () => {} };
      spyOn(app.cd, 'detach').and.callThrough();
      app.ngOnDestroy();
      expect(app.cd.detach).toHaveBeenCalled();
    }));

    it('loadDataLazy() call getQueryString() with 12, DESC', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const eve = { first: 12, sortField: { code: '' }, sortOrder: 22, filters: [{ value: '' }] };
      spyOn(app, 'getQueryString').and.callThrough();
      app.loadDataLazy(eve);
      expect(app.getQueryString).toHaveBeenCalled();
      expect(app.getQueryString).toHaveBeenCalledWith(12, ',DESC');
    }));

    it('loadDataLazy() call getQueryString() with 0, DESC', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const eve = { first: 0, sortField: { code: '' }, sortOrder: 22, filters: [{ value: '' }] };
      spyOn(app, 'getQueryString').and.callThrough();
      app.loadDataLazy(eve);
      expect(app.getQueryString).toHaveBeenCalled();
      expect(app.getQueryString).toHaveBeenCalledWith(0, ',DESC');
    }));

    it('loadDataLazy() call getQueryString() with 0, ASC', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const eve = { first: 0, sortField: { code: '' }, sortOrder: 1, filters: [{ value: '' }] };
      spyOn(app, 'getQueryString').and.callThrough();
      app.loadDataLazy(eve);
      expect(app.getQueryString).toHaveBeenCalled();
      expect(app.getQueryString).toHaveBeenCalledWith(0, ',ASC');
    }));

    it('loadDataLazy() call getQueryString() with 0, undefined', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const eve = { first: 0, sortField: false, sortOrder: 1, filters: [{ value: '' }] };
      spyOn(app, 'getQueryString').and.callThrough();
      app.loadDataLazy(eve);
      expect(app.getQueryString).toHaveBeenCalled();
      expect(app.getQueryString).toHaveBeenCalledWith(0, undefined);
    }));

    it('loadDataLazy() call getQueryString() with 0, ASC, if eve.filters is not avilable', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const eve = { first: 0, sortField: { code: '' }, sortOrder: 1 };
      spyOn(app, 'getQueryString').and.callThrough();
      app.loadDataLazy(eve);
      expect(app.getQueryString).toHaveBeenCalled();
      expect(app.getQueryString).toHaveBeenCalledWith(0, ',ASC');
    }));

    it('getQueryString(1, ASC) should return &sortBy=ASC&pageSize=1&page=1', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const res = app.getQueryString(1, 'ASC');
      expect(res).toEqual('&sortBy=ASC&pageSize=1&page=1');
    }));

    it('getQueryString(1, null) should return &pageSize=1&page=1', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const res = app.getQueryString(1, null);
      expect(res).toEqual('&pageSize=1&page=1');
    }));

    it('getQueryString(undefined, null) should return empty string', async(() => {
      app.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      const res = app.getQueryString(undefined, null);
      expect(res).toEqual('');
    }));

    it('getPattern() should return numPattern value', async(() => {
      spyOn(app, 'isSortAsNumber').and.returnValue(true);
      app.numPattern = 'test';
      expect(app.getPattern('')).toEqual('test');
    }));

    it('getPattern() should return defaultPattern value', async(() => {
      spyOn(app, 'isSortAsNumber').and.returnValue(false);
      app.defaultPattern = 'test';
      expect(app.getPattern('')).toEqual('test');
    })); 
});