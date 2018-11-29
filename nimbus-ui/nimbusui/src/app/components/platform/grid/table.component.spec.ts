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
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { DataTable } from './table.component';
import { Section } from '../section.component';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { ComboBox } from '../../platform/form/elements/combobox.component';
import { InputText } from '../form/elements/textbox.component';
import { ButtonGroup } from '../form/elements/button-group.component';
// import { Button } from '../form/elements/button.component';
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
// import { DateControl } from '../form/elements/date.component';
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
import { PrintDirective } from '../../../directives/print.directive';

let configService, pageService, elementRef, ngZone, objectUtils, domHandler, tableService, cd, param, webContentSvc;

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
  // DateControl,
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
  FormErrorMessage,
  PrintDirective
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
let fixture, hostComponent;
describe('DataTable', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     
  let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(DataTable);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    const fg = new FormGroup({});
    fg.addControl(param.config.code, new FormControl(param.leafState, checks));
    hostComponent.form = fg;
    configService = TestBed.get(ConfigService);
    pageService = TestBed.get(PageService);
    elementRef = TestBed.get(ElementRef);
    objectUtils = TestBed.get(ObjectUtils);
    domHandler = TestBed.get(DomHandler);
    tableService = TestBed.get(TableService);
    cd = TestBed.get(ChangeDetectorRef);
    webContentSvc = TestBed.get(WebContentSvc);
});


    it('should create the DataTable', ()=> {
      expect(hostComponent).toBeTruthy();  
   });

    it('hostComponent._value should update the hostComponent.value', ()=> {
      hostComponent._value = ['test'];
      expect(hostComponent.value).toEqual(['test']);
   });

    it('hostComponent.value should update the hostComponent.value', ()=> {
      hostComponent.value = ['test'];
      expect(hostComponent.value).toEqual(['test']);
   });

    it('writeValue() should call cd.markForCheck()', ()=> {
      const spy = spyOn((hostComponent as any).cd, 'markForCheck').and.callThrough();
      hostComponent.writeValue({});
      expect(spy).toHaveBeenCalled();
   });

    it('should create the DataTable', ()=> {
      const spy = spyOn((hostComponent as any).cd, 'markForCheck').and.callThrough();
      hostComponent.writeValue(undefined);
      expect(spy).toHaveBeenCalled();
   });

    it('registerOnChange() should update the onChange property', ()=> {
      const test = () => {};
      hostComponent.registerOnChange(test);
      expect(hostComponent.onChange).toEqual(test);
   });

    it('registerOnTouched() should update the onTouched property', ()=> {
      const test = () => {};
      hostComponent.registerOnTouched(test);
      expect(hostComponent.onTouched).toEqual(test);
   });

    // it('ngOnInit() should update the hasFilters, and rowExpanderKey properties', () => {
    //   fixture.whenStable().then(() => {
    //     const eleConfig = { code: '', uiStyles: { attributes: { rowSelection: true } } };
    //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
    //     hostComponent.rowExpanderKey = 'test';
    //     spyOn(hostComponent, 'between').and.returnValue('test');
    //     hostComponent.ngOnInit();
    //     expect(hostComponent.hasFilters).toBeFalsy();
    //     expect(hostComponent.rowExpanderKey).toEqual('test');
    //   });
    // });

  //   it('ngAfterViewInit() should call pageService.processEvent()', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
  //       hostComponent.element.config.uiStyles.attributes.lazyLoad = false;
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       spyOn(pageService, 'processEvent').and.callThrough();
  //       hostComponent.ngAfterViewInit();
  //       expect(pageService.processEvent).toHaveBeenCalled();
  //     });
  //   });

  //   it('ngAfterViewInit() should not call pageService.processEvent() if lazyLoad is true', () => {
  //     fixture.whenStable().then(() => {
  //       hostComponent.element.config.uiStyles.attributes.lazyLoad = true;
  //       spyOn(pageService, 'processEvent').and.callThrough();
  //       hostComponent.ngAfterViewInit();
  //       expect(pageService.processEvent).not.toHaveBeenCalled();
  //     });
  //   });

  //   it('ngAfterViewInit() should not call pageService.processEvent() if onLoad is false', () => {
  //     fixture.whenStable().then(() => {
  //       hostComponent.element.config.uiStyles.attributes.onLoad = false;
  //       spyOn(pageService, 'processEvent').and.callThrough();
  //       hostComponent.ngAfterViewInit();
  //       expect(pageService.processEvent).not.toHaveBeenCalled();
  //     });
  //   });

  //   it('ngAfterViewInit() should call dt.filter()', ()=> {
  //     hostComponent.element = new Param(configService);
  //     const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
  //     hostComponent.params = [new ParamConfig(configService)];
  //     hostComponent.params[0].code = '';
  //     hostComponent.params[0].uiStyles = new UiStyle();
  //     hostComponent.params[0].uiStyles.attributes = new UiAttribute();
  //     hostComponent.params[0].uiStyles.attributes.filterValue = 'a';
  //     hostComponent.params[0].uiStyles.attributes.filterMode = '';
  //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //     spyOn(hostComponent.dt, 'filter').and.returnValue('');
  //     hostComponent.ngAfterViewInit();
  //     expect(hostComponent.dt.filter).toHaveBeenCalled();
  //  });

  //   it('ngAfterViewInit() should not call dt.filter()', () => {
  //     const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true } } };
  //     hostComponent.params = [new ParamConfig(configService)];
  //     hostComponent.params[0].code = '';
  //     hostComponent.params[0].uiStyles = new UiStyle();
  //     hostComponent.params[0].uiStyles.attributes = new UiAttribute();
  //     hostComponent.params[0].uiStyles.attributes.filterValue = '';
  //     hostComponent.params[0].uiStyles.attributes.filterMode = '';
  //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //     spyOn(hostComponent.dt, 'filter').and.returnValue('');
  //     hostComponent.ngAfterViewInit();
  //     expect(hostComponent.dt.filter).not.toHaveBeenCalled();
  //   });

  //   it('ngAfterViewInit() should update the value, totalRecords properties and call updatePageDetailsState()', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
  //       hostComponent.element.path = 'test';
  //       const eve = { gridData: { leafState: [] }, path: 'test', gridList: [], page: { totalElements: 100, first: true } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       spyOn(hostComponent, 'updatePageDetailsState').and.callThrough();
  //       hostComponent.element.config.uiStyles.attributes.lazyLoad = true;
  //       hostComponent.ngAfterViewInit();
  //       pageService.logError(eve);
  //       expect(hostComponent.value).toEqual([]);
  //       expect(hostComponent.totalRecords).toEqual(100);
  //       expect(hostComponent.updatePageDetailsState).toHaveBeenCalled();
  //     });
  //   });

  //   it('ngAfterViewInit() should not call updatePageDetailsState()', ()=> {
  //     const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
  //     hostComponent.element.path = 'test';
  //     const eve = { path: '1test', gridList: 'tGrid', page: { totalElements: 'telements', first: true } };
  //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //     spyOn(hostComponent, 'updatePageDetailsState').and.callThrough();
  //     hostComponent.ngAfterViewInit();
  //     pageService.logError(eve);
  //     expect(hostComponent.updatePageDetailsState).not.toHaveBeenCalled();
  //  });

  //   it('ngAfterViewInit() should call updatePageDetailsState() and update the dt.first', ()=> {
  //     const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: false } } };
  //     hostComponent.element.path = 'test';
  //     const eve = { gridData: {leafState: ''}, path: 'test', gridList: 'tGrid', page: { totalElements: 'telements', first: true } };
  //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //     spyOn(hostComponent, 'updatePageDetailsState').and.callThrough();
  //     hostComponent.ngAfterViewInit();
  //     pageService.logError(eve);
  //     expect(hostComponent.updatePageDetailsState).toHaveBeenCalled();
  //     expect(hostComponent.dt.first).toEqual(0);
  //  });

  //   it('ngAfterViewInit() should not call updatePageDetailsState() based on eve.page.first', ()=> {
  //     hostComponent.element = new Param(configService);
  //     const eleConfig = { code: '', uiStyles: { attributes: { onLoad: true, lazyLoad: true } } };
  //     hostComponent.element.path = 'test';
  //     const eve = { gridData: {leafState: ''}, path: 'test', gridList: 'tGrid', page: { totalElements: 'telements', first: false } };
  //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //     spyOn(hostComponent, 'updatePageDetailsState').and.callThrough();
  //     hostComponent.ngAfterViewInit();
  //     pageService.logError(eve);
  //     expect(hostComponent.updatePageDetailsState).not.toHaveBeenCalled();
  //  });

  //   it('ngAfterViewInit() should call form.controls.t.enable()', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
  //       hostComponent.element.path = '/test';
  //       const eve = { enabled: true, path: '/test', config: { code: 'firstName' } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       spyOn(hostComponent.form.controls.firstName, 'enable').and.callThrough();
  //       hostComponent.ngAfterViewInit();
  //       pageService.notifyErrorEvent(eve);
  //       expect(hostComponent.form.controls.firstName.enable).toHaveBeenCalled();
  //     });
  //   });

  //   it('ngAfterViewInit() should call form.controls.t.disable()', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
  //       hostComponent.element.path = '/test';
  //       const eve = { enabled: false, path: '/test', config: { code: 'firstName' } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       spyOn(hostComponent.form.controls.firstName, 'disable').and.callThrough();
  //       hostComponent.ngAfterViewInit();
  //       pageService.notifyErrorEvent(eve);
  //       expect(hostComponent.form.controls.firstName.disable).toHaveBeenCalled();
  //     });
  //   });

  //   it('ngAfterViewInit() should not call form.controls.t.disable()', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: 't', uiStyles: { attributes: { onLoad: false } } };
  //       hostComponent.element.path = '/test';
  //       const eve = { enabled: false, path: '/1test', config: { code: 'firstName' } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       spyOn(hostComponent.form.controls.firstName, 'disable').and.callThrough();
  //       hostComponent.ngAfterViewInit();
  //       pageService.notifyErrorEvent(eve);
  //       expect(hostComponent.form.controls.firstName.disable).not.toHaveBeenCalled();
  //     });
  //   });

  //   it('isRowExpanderHidden() should return true', ()=> {
  //     hostComponent.rowExpanderKey = '';
  //     expect(hostComponent.isRowExpanderHidden('')).toBeTruthy();
  //  });

  //   it('isRowExpanderHidden() should return true based on argument', ()=> {
  //     hostComponent.rowExpanderKey = 't';
  //     const testdata = { t: true };
  //     expect(hostComponent.isRowExpanderHidden(testdata)).toBeTruthy();
  //  });

  //   it('isRowExpanderHidden() should return false', ()=> {
  //     hostComponent.rowExpanderKey = 't';
  //     const testdata = { t: false };
  //     expect(hostComponent.isRowExpanderHidden(testdata)).toBeFalsy();
  //  });

  //   it('getCellDisplayValue() should call dtFormat.transform()', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.code = 't';
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.datePattern = 'Date';
  //     const rowData = { t: true };
  //     spyOn(ParamUtils, 'isKnownDateType').and.returnValue(true);
  //     const spy = spyOn((hostComponent as any).dtFormat, 'transform').and.returnValue('test');
  //     hostComponent.getCellDisplayValue(rowData, col);
  //     expect(spy).toHaveBeenCalled();
  //  });

  //   it('getCellDisplayValue() should not call dtFormat.transform() and return true', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.code = 't';
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.datePattern = 'Date';
  //     const rowData = { t: true };
  //     spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
  //     const spy = spyOn((hostComponent as any).dtFormat, 'transform').and.returnValue('test');
  //     const res = hostComponent.getCellDisplayValue(rowData, col);
  //     expect(spy).not.toHaveBeenCalled();
  //     expect(res).toBeTruthy();
  //  });


  //   it('getCellDisplayValue() should not call dtFormat.transform() and return col.uiStyles.attributes.placeholder', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.code = 't';
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.datePattern = 'Date';
  //     col.uiStyles.attributes.placeholder = 'test';
  //     const rowData = { t: null };
  //     spyOn(ParamUtils, 'isKnownDateType').and.returnValue(false);
  //     const spy = spyOn((hostComponent as any).dtFormat, 'transform').and.returnValue('test');
  //     const res = hostComponent.getCellDisplayValue(rowData, col);
  //     expect(spy).not.toHaveBeenCalled();
  //     expect(res).toEqual('test');
  //  });

  //   it('showColumn() should return true', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'test';
  //     expect(hostComponent.showColumn(col)).toBeTruthy();
  //  });

  //   it('showColumn() should return true for link', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'Link';
  //     expect(hostComponent.showColumn(col)).toBeTruthy();
  //  });

  //   it('showColumn() should return true for button', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'Button';
  //     expect(hostComponent.showColumn(col)).toBeTruthy();
  //  });

  //   it('showColumn() should return true for LinkMenu', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'LinkMenu';
  //     expect(hostComponent.showColumn(col)).toBeTruthy();
  //  });

  //   it('showColumn() should return false for gridRowBody', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'GridRowBody';
  //     expect(hostComponent.showColumn(col)).toBeFalsy();
  //  });

  //   it('showColumn() should return false', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = true;
  //     col.uiStyles.attributes.alias = 'test';
  //     expect(hostComponent.showColumn(col)).toBeFalsy();
  //  });

  //   it('showHeader() should return true', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'GridColumn';
  //     expect(hostComponent.showHeader(col)).toBeTruthy();
  //  });

  //   it('showHeader() should return false', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.hidden = false;
  //     col.uiStyles.attributes.alias = 'Button';
  //     expect(hostComponent.showHeader(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return true', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'test';
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return false if no uiStyle is given', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return true for GridColumn', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = ViewComponent.gridcolumn.toString();
  //     expect(hostComponent.showValue(col)).toBeTruthy();
  //  });

  //   it('showValue(col) should return false for GridColumn with showAsLink attribute', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'GridColumn';
  //     col.uiStyles.attributes.showAsLink = true;
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return false for link', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'Link';
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return false for button', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'Button';
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return false for link Menu', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'LinkMenu';
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showValue(col) should return false for grid row body', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.type.nested = false;
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'GridRowBody';
  //     expect(hostComponent.showValue(col)).toBeFalsy();
  //  });

  //   it('showUiStyleInColumn(col) should return true for link', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'Link';
  //     expect(hostComponent.showUiStyleInColumn(col)).toBeTruthy();
  //  });

  //   it('showUiStyleInColumn(col) should return true for button', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'Button';
  //     expect(hostComponent.showUiStyleInColumn(col)).toBeTruthy();
  //  });

  //   it('showUiStyleInColumn(col) should return true for linkMenu', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'LinkMenu';
  //     expect(hostComponent.showUiStyleInColumn(col)).toBeTruthy();
  //  });

  //   it('showUiStyleInColumn(col) should return false', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'TextBox';
  //     expect(hostComponent.showUiStyleInColumn(col)).toBeFalsy();
  //  });

  //   it('showUiStyleInColumn(col) should return false', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'Link1';
  //     expect(hostComponent.showUiStyleInColumn(col)).toBeFalsy();
  //  });

  //   it('showLinkMenu(col) should return truee', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = 'LinkMenu';
  //     expect(hostComponent.showLinkMenu(col)).toBeTruthy();
  //  });

  //   it('showLinkMenu(col) should return false', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.type = new ConfigType(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.alias = '1LinkMenu';
  //     expect(hostComponent.showLinkMenu(col)).toBeFalsy();
  //  });

  //   it('getViewParam() should return element.collectionParams object', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.code = '2';
  //     expect(hostComponent.getViewParam(col, 1).path).toEqual('/test/1/2');
  //  });

  //   it('isClickedOnDropDown() should return true', ()=> {
  //   const dArray = new ActionDropdown(webContentSvc, pageService, elementRef);
  //   dArray['elementRef'].nativeElement.contains = () => {return true};
  //     expect(hostComponent.isClickedOnDropDown([dArray], 'te')).toBeTruthy();
  //  });

  //   it('isClickedOnDropDown() should return false', ()=> {
  //     const dArray = new ActionDropdown(webContentSvc, pageService, elementRef);
  //     dArray['elementRef'].nativeElement.contains = () => {return false};
  //       expect(hostComponent.isClickedOnDropDown([dArray], 'te')).toBeFalsy();
  //  });

  //   it('isActive() should return true', ()=> {
  //     hostComponent.filterState = [123];
  //     expect(hostComponent.isActive(0)).toBeTruthy();
  //  });

  //   it('isActive()should return false', ()=> {
  //     hostComponent.filterState = [123];
  //     expect(hostComponent.isActive(1)).toBeFalsy();
  //  });

  //   it('getRowPath() should return path', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.code = '123';
  //     hostComponent.element.path = '/test';
  //     const item = { elemId: 456 };
  //     expect(hostComponent.getRowPath(col, item)).toEqual('/test/456/123');
  //  });

  //   it('processOnClick() should call pageService.processEvent()', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.b = '';
  //     col.uiStyles.attributes.method = '';
  //     hostComponent.element.path = '';
  //     spyOn(pageService, 'processEvent').and.returnValue('');
  //     hostComponent.processOnClick(col, { elemId: '' });
  //     expect(pageService.processEvent).toHaveBeenCalled();
  //  });

  //   it('processOnClick() should call pageService.processEvent() based on getAllURLParams()', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.b = '';
  //     col.uiStyles.attributes.method = '';
  //     hostComponent.element.path = '';
  //     spyOn(pageService, 'processEvent').and.returnValue('');
  //     spyOn(hostComponent, 'getAllURLParams').and.returnValue(['test']);
  //     hostComponent.processOnClick(col, { elemId: '', es: 't' });
  //     expect(pageService.processEvent).toHaveBeenCalled();
  //  });

  //   it('processOnClick() should call pageService.processEvent() based on getAllURLParams() and argument', ()=> {
  //     const col = new ParamConfig(configService);
  //     col.uiStyles = new UiStyle();
  //     col.uiStyles.attributes = new UiAttribute();
  //     col.uiStyles.attributes.b = '';
  //     col.uiStyles.attributes.method = '';
  //     hostComponent.element.path = '';
  //     spyOn(pageService, 'processEvent').and.returnValue('');
  //     spyOn(hostComponent, 'getAllURLParams').and.returnValue(['test']);
  //     hostComponent.processOnClick(col, { elemId: '' });
  //     expect(pageService.processEvent).toHaveBeenCalled();
  //  });

  //   it('toggleFilter() should update the showFilters property', ()=> {
  //     hostComponent.showFilters = true;
  //     hostComponent.toggleFilter('');
  //     expect(hostComponent.showFilters).toBeFalsy();
  //  });

  //   it('postGridData() should call the pageService.processEvent()', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { postButtonTargetPath: true, postButtonUrl: '/test' } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       hostComponent.selectedRows = [{ elemId: 123 }];
  //       spyOn(pageService, 'processEvent').and.returnValue('');
  //       hostComponent.postGridData({});
  //       expect(pageService.processEvent).toHaveBeenCalled();
  //     });
  //   });

  //   it('resetMultiSelection should update the selectedRows property', ()=> {
  //     hostComponent.resetMultiSelection();
  //     expect(hostComponent.selectedRows).toEqual([]);
  //  });

  //   it('customSort() should not sort value array for number type', ()=> {
  //     const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: null, index: 1 }, { test: null, index: 2 }];
  //     const value = hostComponent.value;
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value).toEqual(value);
  //  });

  //   it('customSort() should sort value array for number type, if first code is null', ()=> {
  //     const eve = { order: -2, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: null, index: 4 }, { test: 1, index: 3 }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].index).toEqual(3);
  //  });

  //   it('customSort() should sort value array for number type, if second code is null', ()=> {
  //     const eve = { order: 1, field: { order: 1, code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ index: 2, test: 1 }, { index: 1, test: null }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].index).toEqual(1);
  //  });

  //   it('customSort() should sort value array for number type, if second code less than first code', ()=> {
  //     const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ index: 5, test: 1 }, { index: 1, test: 0 }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].index).toEqual(1);
  //  });

  //   it('customSort() should sort value array for number type, if first code less than second code', ()=> {
  //     const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: 1, index: 4 }, { test: 2, index: 1 }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].index).toEqual(1);
  //  });

  //   it('customSort() should sort value array for number type, if both codes are equal', ()=> {
  //     const eve = { field: { code: 'test', uiStyles: { attributes: { sortAs: 'NUMBER' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ index: 1, test: 1 }, { index: 2, test: 1 }];
  //     const value = hostComponent.value;
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value).toEqual(value);
  //  });

  //   it('customSort() should not sort value array for Date type', ()=> {
  //     const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'DATE' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: null }, { test: null }];
  //     const value = hostComponent.value;
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value).toEqual(value);
  //  });

  //   it('customSort() should sort value array for Date type, if first code is null', ()=> {
  //     const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 'DATE' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: null }, { test: 1 }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].test).toEqual(1);
  //  });

  //   it('customSort() should not sort value arra', ()=> {
  //     const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: null }, { test: null }];
  //     const value = hostComponent.value;
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value).toEqual(value);
  //  });

  //   it('customSort() should sort value array, if first code is null', ()=> {
  //     const eve = { order: -1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: null }, { test: 11 }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].test).toEqual(11);
  //  });

  //   it('customSort() should sort value array, if second code is null', ()=> {
  //     const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: 111 }, { test: null }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].test).toEqual(null);
  //  });

  //   it('customSort() should sort value array, if second code less than first code', ()=> {
  //     const eve = { order: 1, field: { code: 'test', uiStyles: { attributes: { sortAs: 's' } }, type: { name: 't' } } };
  //     hostComponent.value = [{ test: { x: 2,
  //       localeCompare: () => {
  //             return 1;
  //           } } }, { test: { x: 1,
  //             localeCompare: () => {
  //             return -1;
  //           } } }];
  //     hostComponent.customSort(eve);
  //     expect(hostComponent.value[0].test.x).toEqual(1);
  //  });

  //   it('isSortAsNumber() should return true for int, NUMBER argument', ()=> {
  //     const res = (hostComponent as any).isSortAsNumber('int', 'NUMBER');
  //     expect(res).toBeTruthy();
  //  });

  //   it('isSortAsNumber() should return true for int, null argument', ()=> {
  //     const res = (hostComponent as any).isSortAsNumber('int', null);
  //     expect(res).toBeTruthy();
  //  });

  //   it('isSortAsNumber() should return true for integer, null argument', ()=> {
  //     const res = (hostComponent as any).isSortAsNumber('integer', null);
  //     expect(res).toBeTruthy();
  //  });

  //   it('isSortAsNumber() should return true for long, null argument', ()=> {
  //     const res = (hostComponent as any).isSortAsNumber('long', null);
  //     expect(res).toBeTruthy();
  //  });

  //   it('isSortAsNumber() should return true for double, null argument', ()=> {
  //     const res = (hostComponent as any).isSortAsNumber('double', null);
  //     expect(res).toBeTruthy();
  //  });

  //   it('isSortAsDate() should return true for int, DATE argument', ()=> {
  //     const res = (hostComponent as any).isSortAsNumber('int', 'DATE');
  //     expect(res).toBeTruthy();
  //  });

  //   it('between() should return false', ()=> {
  //     expect(hostComponent.between('day', 1)).toBeFalsy();
  //  });

  //   it('dateFilter() should call updatePageDetailsState() and dt.filter()', ()=> {
  //     const e = new Date();
  //     const dt = new Table(elementRef, domHandler, objectUtils, null, tableService);
  //     const datePattern = 'MMDDYYYY';
  //     spyOn(hostComponent, 'updatePageDetailsState').and.callThrough();
  //     spyOn(dt, 'filter').and.callThrough();
  //     hostComponent.dateFilter(e, dt, '', datePattern);
  //     expect(hostComponent.updatePageDetailsState).toHaveBeenCalled();
  //     expect(dt.filter).toHaveBeenCalled();
  //  });

  //   it('inputFilter() shlould call dt.filter()', ()=> {
  //     const dt:any = { filter: () => {} };
  //     spyOn(dt, 'filter').and.returnValue(true);
  //     hostComponent.inputFilter({ target: { value: 1 } }, dt, 't', 't');
  //     setTimeout(() => {
  //       expect(dt.filter).toHaveBeenCalled();
  //     }, 600);
  //  });

  //   it('inputFilter() shlould call dt.filter() based onb the filterTimeout property', ()=> {
  //     const dt:any = { filter: () => {} };
  //     spyOn(dt, 'filter').and.returnValue(true);
  //     hostComponent.filterTimeout = true;
  //     hostComponent.inputFilter({ target: { value: 1 } }, dt, 't', 't');
  //     setTimeout(() => {
  //       expect(dt.filter).toHaveBeenCalled();
  //     }, 600);
  //  });

  //   it('clearFilter() shlould call dt.filter()', ()=> {
  //     const dt:any = { filter: () => {} };
  //     spyOn(dt, 'filter').and.returnValue('');
  //     hostComponent.clearFilter({ value: 1 }, dt, 'as', 1);
  //     expect(dt.filter).toHaveBeenCalled();
  //  });

  //   it('clearAll() should calldt.reset() and update the filterState property', ()=> {
  //     spyOn(hostComponent.dt, 'reset').and.returnValue('');
  //     hostComponent.clearAll();
  //     expect(hostComponent.dt.reset).toHaveBeenCalled();
  //     expect(hostComponent.filterState).toEqual([]);
  //  });

  //   it('paginate() should update the rowEnd property', ()=> {
  //     const eve = { first: 12, rows: 2 };
  //     hostComponent.totalRecords = 15;
  //     hostComponent.paginate(eve);
  //     expect(hostComponent.rowEnd).toEqual(14);
  //  });

  //   it('paginate() should update the rowEnd property based on the totalRecords value', ()=> {
  //     const eve = { first: 12, rows: 2 };
  //     hostComponent.totalRecords = 13;
  //     hostComponent.paginate(eve);
  //     expect(hostComponent.rowEnd).toEqual(13);
  //  });

  //   it('updatePageDetailsState() should update the rowStart property', () => {
  //     fixture.whenStable().then(() => {
  //       hostComponent.totalRecords = 1;
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 3 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       hostComponent.updatePageDetailsState();
  //       expect(hostComponent.rowStart).toEqual(1);
  //     });
  //   });

  //   it('updatePageDetailsState() should update the rowStart property as 0', ()=> {
  //     hostComponent.totalRecords = 0;
  //     hostComponent.element = new Param(configService);
  //     const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 3 } } };
  //     spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //     hostComponent.updatePageDetailsState();
  //     expect(hostComponent.rowStart).toEqual(0);
  //  });

  //   it('filterCallBack() should call the updatePageDetailsState()', ()=> {
  //     const eve = { filteredValue: [1] };
  //     spyOn(hostComponent, 'updatePageDetailsState').and.returnValue('');
  //     hostComponent.filterCallBack(eve);
  //     expect(hostComponent.updatePageDetailsState).toHaveBeenCalled();
  //     expect(hostComponent.totalRecords).toEqual(1);
  //  });

  //   it('toggleOpen() should call the mouseEventSubscription.unsubscribe()', ()=> {
  //     const eve = { isOpen: true, state: 'openPanel' };
  //     const obj = {x: 10};
  //     const test = observableOf(obj);
  //     hostComponent.mouseEventSubscription =  test.subscribe();
  //     spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
  //     hostComponent.mouseEventSubscription.closed = false;
  //     hostComponent.toggleOpen(eve);
  //     expect(hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
  //  });

  //   it('toggleOpen() should not call the mouseEventSubscription.unsubscribe()', ()=> {
  //     const eve = { isOpen: true, state: 'openPanel' };
  //     const obj = {x: 10};
  //     const test = observableOf(obj);
  //     hostComponent.mouseEventSubscription =  test.subscribe();
  //     hostComponent.mouseEventSubscription.closed = true;
  //     spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
  //     hostComponent.toggleOpen(eve);
  //     expect(hostComponent.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
  //  });

  //   it('toggleOpen() should not call the mouseEventSubscription.unsubscribe() based on eve.state', ()=> {
  //     const eve = { isOpen: true, state: '1openPanel' };
  //     const obj = {x: 10};
  //     const test = observableOf(obj);
  //     hostComponent.mouseEventSubscription =  test.subscribe();
  //     hostComponent.mouseEventSubscription.closed = true;
  //     spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
  //     hostComponent.toggleOpen(eve);
  //     expect(hostComponent.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
  //  });

  //   it('toggleOpen() should not call the mouseEventSubscription.unsubscribe() based on dropDowns property', ()=> {
  //     (hostComponent as any).dropDowns = { toArray: () => {
  //       return [{ isOpen: true, selectedItem: false, state: '' }];
  //     } };
  //   const eve = { isOpen: true, state: '1openPanel' };
  //   const obj = {x: 10};
  //   const test = observableOf(obj);
  //   hostComponent.mouseEventSubscription =  test.subscribe();
  //   hostComponent.mouseEventSubscription.closed = false;
  //   spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
  //   hostComponent.toggleOpen(eve);
  //   expect(hostComponent.mouseEventSubscription.unsubscribe).not.toHaveBeenCalled();
  //  });

  //   it('export() should call dt.exportCSV()', ()=> {
  //     const tDate = new Date('December 17, 2017');
  //     (hostComponent as any).dt = { filteredValue: [{ a: tDate }], value: [], exportCSV: () => {} };
  //     hostComponent.params = [new ParamConfig(configService)];
  //     hostComponent.params[0].code = 'a';
  //     hostComponent.params[0].type = new ConfigType(configService);
  //     hostComponent.params[0].type.name = 'Date'
  //     hostComponent.params[0].uiStyles = new UiStyle();
  //     hostComponent.params[0].uiStyles.attributes = new UiAttribute();
  //     hostComponent.params[0].uiStyles.attributes.datePattern = '';
  //           spyOn(hostComponent.dt, 'exportCSV').and.callThrough();
  //     hostComponent.export();
  //     expect(hostComponent.dt.exportCSV).toHaveBeenCalled();
  //  });

  //   it('export() should call dt.exportCSV() even without dt.filterValue.a', ()=> {
  //     const tDate = new Date('December 17, 2017');
  //     (hostComponent as any).dt = { filteredValue: [{ a: '' }], value: [], exportCSV: () => {} };
  //     hostComponent.params = [new ParamConfig(configService)];
  //     hostComponent.params[0].code = 'a';
  //     hostComponent.params[0].type = new ConfigType(configService);
  //     hostComponent.params[0].type.name = 'Date'
  //     spyOn(hostComponent.dt, 'exportCSV').and.callThrough();
  //     hostComponent.export();
  //     expect(hostComponent.dt.exportCSV).toHaveBeenCalled();
  //  });

  //   it('export() should call dt.exportCSV() even without dt.filterValue', ()=> {
  //     const tDate = new Date('December 17, 2017');
  //     (hostComponent as any).dt = { filteredValue: [{}], value: [], exportCSV: () => {} };
  //     hostComponent.params = [new ParamConfig(configService)];
  //     hostComponent.params[0].code = 'a';
  //     hostComponent.params[0].type = null;
  //     spyOn(hostComponent.dt, 'exportCSV').and.callThrough();
  //     hostComponent.export();
  //     expect(hostComponent.dt.exportCSV).toHaveBeenCalled();
  //  });

  //   it('ngOnDestroy() should call the mouseEventSubscription.unsubscribe and cd.detach()', ()=> {
  //     (hostComponent as any).mouseEventSubscription = { unsubscribe: () => {} };
  //     const spy = spyOn((hostComponent as any).cd, 'detach').and.callThrough();
  //     spyOn(hostComponent.mouseEventSubscription, 'unsubscribe').and.callThrough();
  //     hostComponent.ngOnDestroy();
  //     expect(spy).toHaveBeenCalled();
  //     expect(hostComponent.mouseEventSubscription.unsubscribe).toHaveBeenCalled();
  //  });

  //   it('ngOnDestroy() should call the cd.detach()', ()=> {
  //     const spy = spyOn((hostComponent as any).cd, 'detach').and.returnValue('');
  //     hostComponent.ngOnDestroy();
  //     expect(spy).toHaveBeenCalled();
  //  });

  //   it('loadDataLazy() call getQueryString() with 12, DESC', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const eve = { first: 12, sortField: { code: '' }, sortOrder: 22, filters: [{ value: '' }] };
  //       spyOn(hostComponent, 'getQueryString').and.callThrough();
  //       hostComponent.loadDataLazy(eve);
  //       expect(hostComponent.getQueryString).toHaveBeenCalled();
  //       expect(hostComponent.getQueryString).toHaveBeenCalledWith(12, ',DESC');
  //     });
  //   });

  //   it('loadDataLazy() call getQueryString() with 0, DESC', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const eve = { first: 0, sortField: { code: '' }, sortOrder: 22, filters: [{ value: '' }] };
  //       spyOn(hostComponent, 'getQueryString').and.callThrough();
  //       hostComponent.loadDataLazy(eve);
  //       expect(hostComponent.getQueryString).toHaveBeenCalled();
  //       expect(hostComponent.getQueryString).toHaveBeenCalledWith(0, ',DESC');
  //     });
  //   });

  //   it('loadDataLazy() call getQueryString() with 0, ASC', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const eve = { first: 0, sortField: { code: '' }, sortOrder: 1, filters: [{ value: '' }] };
  //       spyOn(hostComponent, 'getQueryString').and.callThrough();
  //       hostComponent.loadDataLazy(eve);
  //       expect(hostComponent.getQueryString).toHaveBeenCalled();
  //       expect(hostComponent.getQueryString).toHaveBeenCalledWith(0, ',ASC');
  //     });
  //   });

  //   it('loadDataLazy() call getQueryString() with 0, undefined', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const eve = { first: 0, sortField: false, sortOrder: 1, filters: [{ value: '' }] };
  //       spyOn(hostComponent, 'getQueryString').and.callThrough();
  //       hostComponent.loadDataLazy(eve);
  //       expect(hostComponent.getQueryString).toHaveBeenCalled();
  //       expect(hostComponent.getQueryString).toHaveBeenCalledWith(0, undefined);
  //     });
  //   });

  //   it('loadDataLazy() call getQueryString() with 0, ASC, if eve.filters is not avilable', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const eve = { first: 0, sortField: { code: '' }, sortOrder: 1 };
  //       spyOn(hostComponent, 'getQueryString').and.callThrough();
  //       hostComponent.loadDataLazy(eve);
  //       expect(hostComponent.getQueryString).toHaveBeenCalled();
  //       expect(hostComponent.getQueryString).toHaveBeenCalledWith(0, ',ASC');
  //     });
  //   });

  //   it('getQueryString(1, ASC) should return &sortBy=ASC&pageSize=1&page=1', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const res = hostComponent.getQueryString(1, 'ASC');
  //       expect(res).toEqual('&sortBy=ASC&pageSize=1&page=1');
  //     });
  //   });

  //   it('getQueryString(1, null) should return &pageSize=1&page=1', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const res = hostComponent.getQueryString(1, null);
  //       expect(res).toEqual('&pageSize=1&page=1');
  //     });
  //   });

  //   it('getQueryString(undefined, null) should return empty string', () => {
  //     fixture.whenStable().then(() => {
  //       const eleConfig = { code: '', uiStyles: { attributes: { pageSize: 1 } } };
  //       spyOn(configService, 'getViewConfigById').and.returnValue(eleConfig);
  //       const res = hostComponent.getQueryString(undefined, null);
  //       expect(res).toEqual('');
  //     });
  //   });

  //   it('getPattern() should return numPattern value', ()=> {
  //     spyOn((hostComponent as any), 'isSortAsNumber').and.returnValue(true);
  //     (hostComponent as any).numPattern = 'test';
  //     expect(hostComponent.getPattern('')).toEqual('test');
  //  });

  //   it('getPattern() should return defaultPattern value', ()=> {
  //     spyOn((hostComponent as any), 'isSortAsNumber').and.returnValue(false);
  //     (hostComponent as any).defaultPattern = 'test';
  //     expect(hostComponent.getPattern('')).toEqual('test');
  //  });
});