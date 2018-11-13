 'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, MessagesModule, InputSwitchModule, TreeTableModule  } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { FormsModule, ReactiveFormsModule, ValidatorFn, Validators, FormGroup, FormControl } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Table } from 'primeng/table';
import * as moment from 'moment';
import { ElementRef, ChangeDetectorRef, NgZone } from '@angular/core';
import { DomHandler } from 'primeng/components/dom/domhandler';
import { ObjectUtils } from 'primeng/components/utils/objectutils';
import { TableService } from 'primeng/components/table/table';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { fromEvent as observableFromEvent,  Subscription ,  Observable } from 'rxjs';
import { of as observableOf } from 'rxjs';

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
import { Param, GridData } from '../../../shared/param-state';
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
import { FormErrorMessage } from '../form-error-message.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { WebContentSvc } from '../../../services/content-management.service';

let configService, pageService, elementRef, ngZone, objectUtils, domHandler, tableService, cd, param, webContentSvc;

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

class MockChangeDetectorRef {
  markForCheck() { 
    console.log('markForCheck....');
    
   }
}

const declarations = [
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
  InputLegend,
  FormErrorMessage
];
const imports = [
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
];
const providers = [
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
   SessionStoreService,
   WebContentSvc,
   ChangeDetectorRef,
];

describe('DataTable', () => {

  configureTestSuite();
  setup(DataTable, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<DataTable>){
    this.hostComponent.element = param;
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    const fg = new FormGroup({});
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    this.hostComponent.form = fg;
    configService = TestBed.get(ConfigService);
    pageService = TestBed.get(PageService);
    elementRef = TestBed.get(ElementRef);
    objectUtils = TestBed.get(ObjectUtils);
    domHandler = TestBed.get(DomHandler);
    tableService = TestBed.get(TableService);
    cd = TestBed.get(ChangeDetectorRef);
    webContentSvc = TestBed.get(WebContentSvc);
});


    it('should create the DataTable', async function(this: TestContext<DataTable>){
      expect(this.hostComponent).toBeTruthy();  
    });

    it('this.hostComponent._value should update the this.hostComponent.value', async function(this: TestContext<DataTable>){
      this.hostComponent._value = ['test'];
      expect(this.hostComponent.value).toEqual(['test']);
    });

    it('this.hostComponent.value should update the this.hostComponent.value', async function(this: TestContext<DataTable>){
      this.hostComponent.value = ['test'];
      expect(this.hostComponent.value).toEqual(['test']);
    });

    it('writeValue() should call cd.markForCheck()', async function(this: TestContext<DataTable>){
      const spy = spyOn((this.hostComponent as any).cd, 'markForCheck').and.callThrough();
      this.hostComponent.writeValue({});
      expect(spy).toHaveBeenCalled();
    });

    it('should create the DataTable', async function(this: TestContext<DataTable>){
      const spy = spyOn((this.hostComponent as any).cd, 'markForCheck').and.callThrough();
      this.hostComponent.writeValue(undefined);
      expect(spy).toHaveBeenCalled();
    });

    it('registerOnChange() should update the onChange property', async function(this: TestContext<DataTable>){
      const test = () => {};
      this.hostComponent.registerOnChange(test);
      expect(this.hostComponent.onChange).toEqual(test);
    });

    it('registerOnTouched() should update the onTouched property', async function(this: TestContext<DataTable>){
      const test = () => {};
      this.hostComponent.registerOnTouched(test);
      expect(this.hostComponent.onTouched).toEqual(test);
    });

    it('ngOnInit() should update the hasFilters, and rowExpanderKey properties', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { rowSelection: true } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        this.hostComponent.rowExpanderKey = 'test';
        spyOn(this.hostComponent, 'between').and.returnValue('test');
        this.hostComponent.ngOnInit();
        expect(this.hostComponent.hasFilters).toBeFalsy();
        expect(this.hostComponent.rowExpanderKey).toEqual('test');
      });
    });

    it('ngAfterViewInit() should call pageService.processEvent()', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
        this.hostComponent.element.config.uiStyles.attributes.lazyLoad = false;
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.ngAfterViewInit();
        expect(pageService.processEvent).toHaveBeenCalled();
      });
    });

    it('ngAfterViewInit() should not call pageService.processEvent() if lazyLoad is true', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        this.hostComponent.element.config.uiStyles.attributes.lazyLoad = true;
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.ngAfterViewInit();
        expect(pageService.processEvent).not.toHaveBeenCalled();
      });
    });

    it('ngAfterViewInit() should not call pageService.processEvent() if onLoad is false', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        this.hostComponent.element.config.uiStyles.attributes.onLoad = false;
        spyOn(pageService, 'processEvent').and.callThrough();
        this.hostComponent.ngAfterViewInit();
        expect(pageService.processEvent).not.toHaveBeenCalled();
      });
    });

    it('ngAfterViewInit() should call dt.filter()', async function(this: TestContext<DataTable>){
      this.hostComponent.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
      this.hostComponent.params = [new ParamConfig(configService)];
      this.hostComponent.params[0].code = '';
      this.hostComponent.params[0].uiStyles = new UiStyle();
      this.hostComponent.params[0].uiStyles.attributes = new UiAttribute();
      this.hostComponent.params[0].uiStyles.attributes.filterValue = 'a';
      this.hostComponent.params[0].uiStyles.attributes.filterMode = '';
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(this.hostComponent.dt, 'filter').and.returnValue('');
      this.hostComponent.ngAfterViewInit();
      expect(this.hostComponent.dt.filter).toHaveBeenCalled();
    });

    it('ngAfterViewInit() should not call dt.filter()', async function(this: TestContext<DataTable>){
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
      this.hostComponent.params = [new ParamConfig(configService)];
      this.hostComponent.params[0].code = '';
      this.hostComponent.params[0].uiStyles = new UiStyle();
      this.hostComponent.params[0].uiStyles.attributes = new UiAttribute();
      this.hostComponent.params[0].uiStyles.attributes.filterValue = '';
      this.hostComponent.params[0].uiStyles.attributes.filterMode = '';
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(this.hostComponent.dt, 'filter').and.returnValue('');
      this.hostComponent.ngAfterViewInit();
      expect(this.hostComponent.dt.filter).not.toHaveBeenCalled();
    });

    it('ngAfterViewInit() should update the value, totalRecords properties and call updatePageDetailsState()', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
        this.hostComponent.element.path = 'test';
        const eve = { gridData: { leafState: [] }, path: 'test', gridList: [], page: { totalElements: 100, first: true } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        spyOn(this.hostComponent, 'updatePageDetailsState').and.callThrough();
        this.hostComponent.element.config.uiStyles.attributes.lazyLoad = true;
        this.hostComponent.ngAfterViewInit();
        pageService.logError(eve);
        expect(this.hostComponent.value).toEqual([]);
        expect(this.hostComponent.totalRecords).toEqual(100);
        expect(this.hostComponent.updatePageDetailsState).toHaveBeenCalled();
      });
    });

    it('ngAfterViewInit() should not call updatePageDetailsState()', async function(this: TestContext<DataTable>){
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
      this.hostComponent.element.path = 'test';
      const eve = { path: '1test', gridList: 'tGrid', page: { totalElements: 'telements', first: true } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(this.hostComponent, 'updatePageDetailsState').and.callThrough();
      this.hostComponent.ngAfterViewInit();
      pageService.logError(eve);
      expect(this.hostComponent.updatePageDetailsState).not.toHaveBeenCalled();
    });

    it('ngAfterViewInit() should call updatePageDetailsState() and update the dt.first', async function(this: TestContext<DataTable>){
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: false } } };
      this.hostComponent.element.path = 'test';
      const eve = { gridData: {leafState: ''}, path: 'test', gridList: 'tGrid', page: { totalElements: 'telements', first: true } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(this.hostComponent, 'updatePageDetailsState').and.callThrough();
      this.hostComponent.ngAfterViewInit();
      pageService.logError(eve);
      expect(this.hostComponent.updatePageDetailsState).toHaveBeenCalled();
      expect(this.hostComponent.dt.first).toEqual(0);
    });

    it('ngAfterViewInit() should not call updatePageDetailsState() based on eve.page.first', async function(this: TestContext<DataTable>){
      this.hostComponent.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
      this.hostComponent.element.path = 'test';
      const eve = { gridData: {leafState: ''}, path: 'test', gridList: 'tGrid', page: { totalElements: 'telements', first: false } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      spyOn(this.hostComponent, 'updatePageDetailsState').and.callThrough();
      this.hostComponent.ngAfterViewInit();
      pageService.logError(eve);
      expect(this.hostComponent.updatePageDetailsState).not.toHaveBeenCalled();
    });

    it('ngAfterViewInit() should call form.controls.t.enable()', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
        this.hostComponent.element.path = '/test';
        const eve = { enabled: true, path: '/test', config: { code: 'firstName' } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        spyOn(this.hostComponent.form.controls.firstName, 'enable').and.callThrough();
        this.hostComponent.ngAfterViewInit();
        pageService.notifyErrorEvent(eve);
        expect(this.hostComponent.form.controls.firstName.enable).toHaveBeenCalled();
      });
    });

    it('ngAfterViewInit() should call form.controls.t.disable()', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
        this.hostComponent.element.path = '/test';
        const eve = { enabled: false, path: '/test', config: { code: 'firstName' } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        spyOn(this.hostComponent.form.controls.firstName, 'disable').and.callThrough();
        this.hostComponent.ngAfterViewInit();
        pageService.notifyErrorEvent(eve);
        expect(this.hostComponent.form.controls.firstName.disable).toHaveBeenCalled();
      });
    });

    it('ngAfterViewInit() should not call form.controls.t.disable()', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
        this.hostComponent.element.path = '/test';
        const eve = { enabled: false, path: '/1test', config: { code: 'firstName' } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        spyOn(this.hostComponent.form.controls.firstName, 'disable').and.callThrough();
        this.hostComponent.ngAfterViewInit();
        pageService.notifyErrorEvent(eve);
        expect(this.hostComponent.form.controls.firstName.disable).not.toHaveBeenCalled();
      });
    });

    it('isRowExpanderHidden() should return true', async function(this: TestContext<DataTable>){
      this.hostComponent.rowExpanderKey = '';
      expect(this.hostComponent.isRowExpanderHidden('')).toBeTruthy();
    });

    it('isRowExpanderHidden() should return true based on argument', async function(this: TestContext<DataTable>){
      this.hostComponent.rowExpanderKey = 't';
      const testdata = { t: true };
      expect(this.hostComponent.isRowExpanderHidden(testdata)).toBeTruthy();
    });

    it('isRowExpanderHidden() should return false', async function(this: TestContext<DataTable>){
      this.hostComponent.rowExpanderKey = 't';
      const testdata = { t: false };
      expect(this.hostComponent.isRowExpanderHidden(testdata)).toBeFalsy();
    });

    it('getCellDisplayValue() should call dtFormat.transform()', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.code = 't';
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.datePattern = 'Date';
      const rowData = { t: true };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(true);
      const spy = spyOn((this.hostComponent as any).dtFormat, 'transform').and.returnValue('test');
      this.hostComponent.getCellDisplayValue(rowData, col);
      expect(spy).toHaveBeenCalled();
    });

    it('getCellDisplayValue() should not call dtFormat.transform() and return true', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.code = 't';
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.datePattern = 'Date';
      const rowData = { t: true };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
      const spy = spyOn((this.hostComponent as any).dtFormat, 'transform').and.returnValue('test');
      const res = this.hostComponent.getCellDisplayValue(rowData, col);
      expect(spy).not.toHaveBeenCalled();
      expect(res).toBeTruthy();
    });


    it('getCellDisplayValue() should not call dtFormat.transform() and return col.uiStyles.attributes.placeholder', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.code = 't';
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.datePattern = 'Date';
      col.uiStyles.attributes.placeholder = 'test';
      const rowData = { t: null };
      spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
      const spy = spyOn((this.hostComponent as any).dtFormat, 'transform').and.returnValue('test');
      const res = this.hostComponent.getCellDisplayValue(rowData, col);
      expect(spy).not.toHaveBeenCalled();
      expect(res).toEqual('test');
    });

    it('showColumn() should return true', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'test';
      expect(this.hostComponent.showColumn(col)).toBeTruthy();
    });

    it('showColumn() should return true for link', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'Link';
      expect(this.hostComponent.showColumn(col)).toBeTruthy();
    });

    it('showColumn() should return true for button', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'Button';
      expect(this.hostComponent.showColumn(col)).toBeTruthy();
    });

    it('showColumn() should return true for LinkMenu', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(this.hostComponent.showColumn(col)).toBeTruthy();
    });

    it('showColumn() should return false for gridRowBody', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'GridRowBody';
      expect(this.hostComponent.showColumn(col)).toBeFalsy();
    });

    it('showColumn() should return false', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = true;
      col.uiStyles.attributes.alias = 'test';
      expect(this.hostComponent.showColumn(col)).toBeFalsy();
    });

    it('showHeader() should return true', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'GridColumn';
      expect(this.hostComponent.showHeader(col)).toBeTruthy();
    });

    it('showHeader() should return false', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.hidden = false;
      col.uiStyles.attributes.alias = 'Button';
      expect(this.hostComponent.showHeader(col)).toBeFalsy();
    });

    it('showValue(col) should return true', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'test';
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showValue(col) should return false if no uiStyle is given', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showValue(col) should return true for GridColumn', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = ViewComponent.gridcolumn.toString();
      expect(this.hostComponent.showValue(col)).toBeTruthy();
    });

    it('showValue(col) should return false for GridColumn with showAsLink attribute', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'GridColumn';
      col.uiStyles.attributes.showAsLink = true;
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showValue(col) should return false for link', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Link';
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showValue(col) should return false for button', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Button';
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showValue(col) should return false for link Menu', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showValue(col) should return false for grid row body', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.type.nested = false;
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'GridRowBody';
      expect(this.hostComponent.showValue(col)).toBeFalsy();
    });

    it('showUiStyleInColumn(col) should return true for link', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Link';
      expect(this.hostComponent.showUiStyleInColumn(col)).toBeTruthy();
    });

    it('showUiStyleInColumn(col) should return true for button', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Button';
      expect(this.hostComponent.showUiStyleInColumn(col)).toBeTruthy();
    });

    it('showUiStyleInColumn(col) should return true for linkMenu', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(this.hostComponent.showUiStyleInColumn(col)).toBeTruthy();
    });

    it('showUiStyleInColumn(col) should return false', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'TextBox';
      expect(this.hostComponent.showUiStyleInColumn(col)).toBeFalsy();
    });

    it('showUiStyleInColumn(col) should return false', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'Link1';
      expect(this.hostComponent.showUiStyleInColumn(col)).toBeFalsy();
    });

    it('showLinkMenu(col) should return truee', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = 'LinkMenu';
      expect(this.hostComponent.showLinkMenu(col)).toBeTruthy();
    });

    it('showLinkMenu(col) should return false', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.type = new ConfigType(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.alias = '1LinkMenu';
      expect(this.hostComponent.showLinkMenu(col)).toBeFalsy();
    });

    it('getViewParam() should return element.collectionParams object', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.code = '2';
      expect(this.hostComponent.getViewParam(col, 1).path).toEqual('/test/1/2');
    });

    it('isClickedOnDropDown() should return true', async function(this: TestContext<DataTable>){
    const dArray = new ActionDropdown(webContentSvc, pageService, elementRef);
    dArray['elementRef'].nativeElement.contains = () => {return true};
      expect(this.hostComponent.isClickedOnDropDown([dArray], 'te')).toBeTruthy();
    });

    it('isClickedOnDropDown() should return false', async function(this: TestContext<DataTable>){
      const dArray = new ActionDropdown(webContentSvc, pageService, elementRef);
      dArray['elementRef'].nativeElement.contains = () => {return false};
        expect(this.hostComponent.isClickedOnDropDown([dArray], 'te')).toBeFalsy();
    });

    it('isActive() should return true', async function(this: TestContext<DataTable>){
      this.hostComponent.filterState = [123];
      expect(this.hostComponent.isActive(0)).toBeTruthy();
    });

    it('isActive()should return false', async function(this: TestContext<DataTable>){
      this.hostComponent.filterState = [123];
      expect(this.hostComponent.isActive(1)).toBeFalsy();
    });

    it('getRowPath() should return path', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.code = '123';
      this.hostComponent.element.path = '/test';
      const item = { elemId: 456 };
      expect(this.hostComponent.getRowPath(col, item)).toEqual('/test/456/123');
    });

    it('processOnClick() should call pageService.processEvent()', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.b = '';
      col.uiStyles.attributes.method = '';
      this.hostComponent.element.path = '';
      spyOn(pageService, 'processEvent').and.returnValue('');
      this.hostComponent.processOnClick(col, { elemId: '' });
      expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('processOnClick() should call pageService.processEvent() based on getAllURLParams()', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.b = '';
      col.uiStyles.attributes.method = '';
      this.hostComponent.element.path = '';
      spyOn(pageService, 'processEvent').and.returnValue('');
      spyOn(this.hostComponent, 'getAllURLParams').and.returnValue(['test']);
      this.hostComponent.processOnClick(col, { elemId: '', es: 't' });
      expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('processOnClick() should call pageService.processEvent() based on getAllURLParams() and argument', async function(this: TestContext<DataTable>){
      const col = new ParamConfig(configService);
      col.uiStyles = new UiStyle();
      col.uiStyles.attributes = new UiAttribute();
      col.uiStyles.attributes.b = '';
      col.uiStyles.attributes.method = '';
      this.hostComponent.element.path = '';
      spyOn(pageService, 'processEvent').and.returnValue('');
      spyOn(this.hostComponent, 'getAllURLParams').and.returnValue(['test']);
      this.hostComponent.processOnClick(col, { elemId: '' });
      expect(pageService.processEvent).toHaveBeenCalled();
    });

    it('toggleFilter() should update the showFilters property', async function(this: TestContext<DataTable>){
      this.hostComponent.showFilters = true;
      this.hostComponent.toggleFilter('');
      expect(this.hostComponent.showFilters).toBeFalsy();
    });

    it('postGridData() should call the pageService.processEvent()', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { postButtonTargetPath: true, postButtonUrl: '/test' } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        this.hostComponent.selectedRows = [{ elemId: 123 }];
        spyOn(pageService, 'processEvent').and.returnValue('');
        this.hostComponent.postGridData({});
        expect(pageService.processEvent).toHaveBeenCalled();
      });
    });

    it('resetMultiSelection should update the selectedRows property', async function(this: TestContext<DataTable>){
      this.hostComponent.resetMultiSelection();
      expect(this.hostComponent.selectedRows).toEqual([]);
    });

    it('customSort() should not sort value array for number type', async function(this: TestContext<DataTable>){
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: null, index: 1 }, { test: null, index: 2 }];
      const value = this.hostComponent.value;
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value).toEqual(value);
    });

    it('customSort() should sort value array for number type, if first code is null', async function(this: TestContext<DataTable>){
      const eve = { order: -2, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: null, index: 4 }, { test: 1, index: 3 }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].index).toEqual(3);
    });

    it('customSort() should sort value array for number type, if second code is null', async function(this: TestContext<DataTable>){
      const eve = { order: 1, field: { order: 1, code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ index: 2, test: 1 }, { index: 1, test: null }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].index).toEqual(1);
    });

    it('customSort() should sort value array for number type, if second code less than first code', async function(this: TestContext<DataTable>){
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ index: 5, test: 1 }, { index: 1, test: 0 }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].index).toEqual(1);
    });

    it('customSort() should sort value array for number type, if first code less than second code', async function(this: TestContext<DataTable>){
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: 1, index: 4 }, { test: 2, index: 1 }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].index).toEqual(1);
    });

    it('customSort() should sort value array for number type, if both codes are equal', async function(this: TestContext<DataTable>){
      const eve = { field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ index: 1, test: 1 }, { index: 2, test: 1 }];
      const value = this.hostComponent.value;
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value).toEqual(value);
    });

    it('customSort() should not sort value array for Date type', async function(this: TestContext<DataTable>){
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'DATE' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: null }, { test: null }];
      const value = this.hostComponent.value;
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value).toEqual(value);
    });

    it('customSort() should sort value array for Date type, if first code is null', async function(this: TestContext<DataTable>){
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'DATE' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: null }, { test: 1 }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].test).toEqual(1);
    });

    it('customSort() should not sort value arra', async function(this: TestContext<DataTable>){
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: null }, { test: null }];
      const value = this.hostComponent.value;
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value).toEqual(value);
    });

    it('customSort() should sort value array, if first code is null', async function(this: TestContext<DataTable>){
      const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: null }, { test: 11 }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].test).toEqual(11);
    });

    it('customSort() should sort value array, if second code is null', async function(this: TestContext<DataTable>){
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: 111 }, { test: null }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].test).toEqual(null);
    });

    it('customSort() should sort value array, if second code less than first code', async function(this: TestContext<DataTable>){
      const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
      this.hostComponent.value = [{ test: { x: 2,
        localeCompare: () => {
              return 1;
            } } }, { test: { x: 1,
              localeCompare: () => {
              return -1;
            } } }];
      this.hostComponent.customSort(eve);
      expect(this.hostComponent.value[0].test.x).toEqual(1);
    });

    it('isSortAsNumber() should return true for int, NUMBER argument', async function(this: TestContext<DataTable>){
      const res = (this.hostComponent as any).isSortAsNumber('int', 'NUMBER');
      expect(res).toBeTruthy();
    });

    it('isSortAsNumber() should return true for int, null argument', async function(this: TestContext<DataTable>){
      const res = (this.hostComponent as any).isSortAsNumber('int', null);
      expect(res).toBeTruthy();
    });

    it('isSortAsNumber() should return true for integer, null argument', async function(this: TestContext<DataTable>){
      const res = (this.hostComponent as any).isSortAsNumber('integer', null);
      expect(res).toBeTruthy();
    });

    it('isSortAsNumber() should return true for long, null argument', async function(this: TestContext<DataTable>){
      const res = (this.hostComponent as any).isSortAsNumber('long', null);
      expect(res).toBeTruthy();
    });

    it('isSortAsNumber() should return true for double, null argument', async function(this: TestContext<DataTable>){
      const res = (this.hostComponent as any).isSortAsNumber('double', null);
      expect(res).toBeTruthy();
    });

    it('isSortAsDate() should return true for int, DATE argument', async function(this: TestContext<DataTable>){
      const res = (this.hostComponent as any).isSortAsNumber('int', 'DATE');
      expect(res).toBeTruthy();
    });

    it('between() should return false', async function(this: TestContext<DataTable>){
      expect(this.hostComponent.between('day', 1)).toBeFalsy();
    });

    it('dateFilter() should call updatePageDetailsState() and dt.filter()', async function(this: TestContext<DataTable>){
      const e = new Date();
      const dt = new Table(elementRef, domHandler, objectUtils, null, tableService);
      const datePattern = 'MMDDYYYY';
      spyOn(this.hostComponent, 'updatePageDetailsState').and.callThrough();
      spyOn(dt, 'filter').and.callThrough();
      this.hostComponent.dateFilter(e, dt, '', datePattern);
      expect(this.hostComponent.updatePageDetailsState).toHaveBeenCalled();
      expect(dt.filter).toHaveBeenCalled();
    });

    it('inputFilter() shlould call dt.filter()', async function(this: TestContext<DataTable>){
      const dt:any = { filter: () => {} };
      spyOn(dt, 'filter').and.returnValue(true);
      this.hostComponent.inputFilter({ target: { value: 1 } }, dt, 't', 't');
      setTimeout(() => {
        expect(dt.filter).toHaveBeenCalled();
      }, 600);
    });

    it('inputFilter() shlould call dt.filter() based onb the filterTimeout property', async function(this: TestContext<DataTable>){
      const dt:any = { filter: () => {} };
      spyOn(dt, 'filter').and.returnValue(true);
      this.hostComponent.filterTimeout = true;
      this.hostComponent.inputFilter({ target: { value: 1 } }, dt, 't', 't');
      setTimeout(() => {
        expect(dt.filter).toHaveBeenCalled();
      }, 600);
    });

    it('clearFilter() shlould call dt.filter()', async function(this: TestContext<DataTable>){
      const dt:any = { filter: () => {} };
      spyOn(dt, 'filter').and.returnValue('');
      this.hostComponent.clearFilter({ value: 1 }, dt, 'as', 1);
      expect(dt.filter).toHaveBeenCalled();
    });

    it('clearAll() should calldt.reset() and update the filterState property', async function(this: TestContext<DataTable>){
      spyOn(this.hostComponent.dt, 'reset').and.returnValue('');
      this.hostComponent.clearAll();
      expect(this.hostComponent.dt.reset).toHaveBeenCalled();
      expect(this.hostComponent.filterState).toEqual([]);
    });

    it('paginate() should update the rowEnd property', async function(this: TestContext<DataTable>){
      const eve = { first: 12, rows: 2 };
      this.hostComponent.totalRecords = 15;
      this.hostComponent.paginate(eve);
      expect(this.hostComponent.rowEnd).toEqual(14);
    });

    it('paginate() should update the rowEnd property based on the totalRecords value', async function(this: TestContext<DataTable>){
      const eve = { first: 12, rows: 2 };
      this.hostComponent.totalRecords = 13;
      this.hostComponent.paginate(eve);
      expect(this.hostComponent.rowEnd).toEqual(13);
    });

    it('updatePageDetailsState() should update the rowStart property', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        this.hostComponent.totalRecords = 1;
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 3 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        this.hostComponent.updatePageDetailsState();
        expect(this.hostComponent.rowStart).toEqual(1);
      });
    });

    it('updatePageDetailsState() should update the rowStart property as 0', async function(this: TestContext<DataTable>){
      this.hostComponent.totalRecords = 0;
      this.hostComponent.element = new Param(configService);
      const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 3 } } };
      spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
      this.hostComponent.updatePageDetailsState();
      expect(this.hostComponent.rowStart).toEqual(0);
    });

    it('filterCallBack() should call the updatePageDetailsState()', async function(this: TestContext<DataTable>){
      const eve = { filteredValue: [1] };
      spyOn(this.hostComponent, 'updatePageDetailsState').and.returnValue('');
      this.hostComponent.filterCallBack(eve);
      expect(this.hostComponent.updatePageDetailsState).toHaveBeenCalled();
      expect(this.hostComponent.totalRecords).toEqual(1);
    });

    it('toggleOpen() should call the mouseEventSubscription.unsubscribe()', async function(this: TestContext<DataTable>){
      const eve = { isOpen: true, state: 'openPanel' };
      const obj = {x: 10};
      const test = observableOf(obj);
      this.hostComponent.mouseEventSubscription =  test.subscribe();
      spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
      this.hostComponent.mouseEventSubscription.closed = false;
      this.hostComponent.toggleOpen(eve);
      expect(this.hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
    });

    it('toggleOpen() should not call the mouseEventSubscription.unsubscribe()', async function(this: TestContext<DataTable>){
      const eve = { isOpen: true, state: 'openPanel' };
      const obj = {x: 10};
      const test = observableOf(obj);
      this.hostComponent.mouseEventSubscription =  test.subscribe();
      this.hostComponent.mouseEventSubscription.closed = true;
      spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
      this.hostComponent.toggleOpen(eve);
      expect(this.hostComponent.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
    });

    it('toggleOpen() should not call the mouseEventSubscription.unsubscribe() based on eve.state', async function(this: TestContext<DataTable>){
      const eve = { isOpen: true, state: '1openPanel' };
      const obj = {x: 10};
      const test = observableOf(obj);
      this.hostComponent.mouseEventSubscription =  test.subscribe();
      this.hostComponent.mouseEventSubscription.closed = true;
      spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
      this.hostComponent.toggleOpen(eve);
      expect(this.hostComponent.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
    });

    it('toggleOpen() should not call the mouseEventSubscription.unsubscribe() based on dropDowns property', async function(this: TestContext<DataTable>){
      (this.hostComponent as any).dropDowns = { toArray: () => {
        return [{ isOpen: true, selectedItem: false, state: '' }];
      } };
    const eve = { isOpen: true, state: '1openPanel' };
    const obj = {x: 10};
    const test = observableOf(obj);
    this.hostComponent.mouseEventSubscription =  test.subscribe();
    this.hostComponent.mouseEventSubscription.closed = false;
    spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
    this.hostComponent.toggleOpen(eve);
    expect(this.hostComponent.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
    });

    it('export() should call dt.exportCSV()', async function(this: TestContext<DataTable>){
      const tDate = new Date('December 17, 2017');
      (this.hostComponent as any).dt = { filteredValue: [{ a: tDate }], value: [], exportCSV: () => {} };
      this.hostComponent.params = [new ParamConfig(configService)];
      this.hostComponent.params[0].code = 'a';
      this.hostComponent.params[0].type = new ConfigType(configService);
      this.hostComponent.params[0].type.name = 'Date'
      this.hostComponent.params[0].uiStyles = new UiStyle();
      this.hostComponent.params[0].uiStyles.attributes = new UiAttribute();
      this.hostComponent.params[0].uiStyles.attributes.datePattern = '';
            spyOn(this.hostComponent.dt, 'exportCSV').and.callThrough();
      this.hostComponent.export();
      expect(this.hostComponent.dt.exportCSV).toHaveBeenCalled();
    });

    it('export() should call dt.exportCSV() even without dt.filterValue.a', async function(this: TestContext<DataTable>){
      const tDate = new Date('December 17, 2017');
      (this.hostComponent as any).dt = { filteredValue: [{ a: '' }], value: [], exportCSV: () => {} };
      this.hostComponent.params = [new ParamConfig(configService)];
      this.hostComponent.params[0].code = 'a';
      this.hostComponent.params[0].type = new ConfigType(configService);
      this.hostComponent.params[0].type.name = 'Date'
      spyOn(this.hostComponent.dt, 'exportCSV').and.callThrough();
      this.hostComponent.export();
      expect(this.hostComponent.dt.exportCSV).toHaveBeenCalled();
    });

    it('export() should call dt.exportCSV() even without dt.filterValue', async function(this: TestContext<DataTable>){
      const tDate = new Date('December 17, 2017');
      (this.hostComponent as any).dt = { filteredValue: [{}], value: [], exportCSV: () => {} };
      this.hostComponent.params = [new ParamConfig(configService)];
      this.hostComponent.params[0].code = 'a';
      this.hostComponent.params[0].type = null;
      spyOn(this.hostComponent.dt, 'exportCSV').and.callThrough();
      this.hostComponent.export();
      expect(this.hostComponent.dt.exportCSV).toHaveBeenCalled();
    });

    it('ngOnDestroy() should call the mouseEventSubscription.unsubscribe and cd.detach()', async function(this: TestContext<DataTable>){
      (this.hostComponent as any).mouseEventSubscription = { unsubscribe: () => {} };
      const spy = spyOn((this.hostComponent as any).cd, 'detach').and.callThrough();
      spyOn(this.hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
      this.hostComponent.ngOnDestroy();
      expect(spy).toHaveBeenCalled();
      expect(this.hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
    });

    it('ngOnDestroy() should call the cd.detach()', async function(this: TestContext<DataTable>){
      const spy = spyOn((this.hostComponent as any).cd, 'detach').and.returnValue('');
      this.hostComponent.ngOnDestroy();
      expect(spy).toHaveBeenCalled();
    });

    it('loadDataLazy() call getQueryString() with 12, DESC', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const eve = { first: 12, sortField: { code: '' }, sortOrder: 22, filters: [{ value: '' }] };
        spyOn(this.hostComponent, 'getQueryString').and.callThrough();
        this.hostComponent.loadDataLazy(eve);
        expect(this.hostComponent.getQueryString).toHaveBeenCalled();
        expect(this.hostComponent.getQueryString).toHaveBeenCalledWith(12, ',DESC');
      });
    });

    it('loadDataLazy() call getQueryString() with 0, DESC', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const eve = { first: 0, sortField: { code: '' }, sortOrder: 22, filters: [{ value: '' }] };
        spyOn(this.hostComponent, 'getQueryString').and.callThrough();
        this.hostComponent.loadDataLazy(eve);
        expect(this.hostComponent.getQueryString).toHaveBeenCalled();
        expect(this.hostComponent.getQueryString).toHaveBeenCalledWith(0, ',DESC');
      });
    });

    it('loadDataLazy() call getQueryString() with 0, ASC', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const eve = { first: 0, sortField: { code: '' }, sortOrder: 1, filters: [{ value: '' }] };
        spyOn(this.hostComponent, 'getQueryString').and.callThrough();
        this.hostComponent.loadDataLazy(eve);
        expect(this.hostComponent.getQueryString).toHaveBeenCalled();
        expect(this.hostComponent.getQueryString).toHaveBeenCalledWith(0, ',ASC');
      });
    });

    it('loadDataLazy() call getQueryString() with 0, undefined', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const eve = { first: 0, sortField: false, sortOrder: 1, filters: [{ value: '' }] };
        spyOn(this.hostComponent, 'getQueryString').and.callThrough();
        this.hostComponent.loadDataLazy(eve);
        expect(this.hostComponent.getQueryString).toHaveBeenCalled();
        expect(this.hostComponent.getQueryString).toHaveBeenCalledWith(0, undefined);
      });
    });

    it('loadDataLazy() call getQueryString() with 0, ASC, if eve.filters is not avilable', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const eve = { first: 0, sortField: { code: '' }, sortOrder: 1 };
        spyOn(this.hostComponent, 'getQueryString').and.callThrough();
        this.hostComponent.loadDataLazy(eve);
        expect(this.hostComponent.getQueryString).toHaveBeenCalled();
        expect(this.hostComponent.getQueryString).toHaveBeenCalledWith(0, ',ASC');
      });
    });

    it('getQueryString(1, ASC) should return &sortBy=ASC&pageSize=1&page=1', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const res = this.hostComponent.getQueryString(1, 'ASC');
        expect(res).toEqual('&sortBy=ASC&pageSize=1&page=1');
      });
    });

    it('getQueryString(1, null) should return &pageSize=1&page=1', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const res = this.hostComponent.getQueryString(1, null);
        expect(res).toEqual('&pageSize=1&page=1');
      });
    });

    it('getQueryString(undefined, null) should return empty string', async function(this: TestContext<DataTable>){
      this.fixture.whenStable().then(() => {
        const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
        spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
        const res = this.hostComponent.getQueryString(undefined, null);
        expect(res).toEqual('');
      });
    });

    it('getPattern() should return numPattern value', async function(this: TestContext<DataTable>){
      spyOn((this.hostComponent as any), 'isSortAsNumber').and.returnValue(true);
      (this.hostComponent as any).numPattern = 'test';
      expect(this.hostComponent.getPattern('')).toEqual('test');
    });

    it('getPattern() should return defaultPattern value', async function(this: TestContext<DataTable>){
      spyOn((this.hostComponent as any), 'isSortAsNumber').and.returnValue(false);
      (this.hostComponent as any).defaultPattern = 'test';
      expect(this.hostComponent.getPattern('')).toEqual('test');
    });
});