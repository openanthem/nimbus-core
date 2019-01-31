'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
  FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
  ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule, InputMaskModule } from 'primeng/primeng';
  import { TableModule } from 'primeng/table';
  import { KeyFilterModule } from 'primeng/keyfilter';
  import { ToastModule } from 'primeng/toast';
  import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren, ChangeDetectorRef } from '@angular/core';
  import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { Accordion } from './accordion.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { Link } from '../link.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { StaticText } from '../content/static-content.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { WebContentSvc } from '../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';
import { Image } from '../image.component';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FrmGroupCmp } from '../form-group.component';
import { SvgComponent } from '../svg/svg.component';
import { FormElement } from '../form-element.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Signature } from '../form/elements/signature.component';
import { Calendar } from '../form/elements/calendar.component';
import { RadioButton } from '../form/elements/radio.component';
import { CheckBoxGroup } from '../form/elements/checkbox-group.component';
import { CheckBox } from '../form/elements/checkbox.component';
import { MultiSelectListBox } from '../form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../form/elements/multi-select-card.component';
import { OrderablePickList } from '../form/elements/picklist.component';
import { FileUploadComponent } from '../fileupload/file-upload.component';
import { DataTable } from '../grid/table.component';
import { MessageComponent } from '../message/message.component';
import { Header } from '../content/header.component';
import { Paragraph } from '../content/paragraph.component';
import { HeaderCheckBox } from '../form/elements/header-checkbox.component';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { Section } from '../section.component';
import { ActionLink } from '../form/elements/action-dropdown.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { Menu } from '../menu.component';
import { Form } from '../form.component';
import { Label } from './label.component';
import { CardDetailsFieldGroupComponent } from '../card/card-details-field-group.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../form/elements/input-label.component';
import { TreeGrid } from '../tree-grid/tree-grid.component';
import { FormGridFiller } from '../form/form-grid-filler.component';
import { InputSwitch } from '../form/elements/input-switch.component';
import { InputLegend } from '../form/elements/input-legend.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { FormErrorMessage } from '../form-error-message.component';
import { Param } from '../../../shared/param-state';
import { PrintDirective } from '../../../directives/print.directive';
import { Subject } from 'rxjs';
import { By } from '@angular/platform-browser';
import { GridService } from '../../../services/grid.service';
import { PrintService } from '../../../services/print.service';
import { accordionElementWithForm, accordionElementWithNoForm } from 'mockdata';
import { InputMaskComp } from './../form/elements/input-mask.component';

import { RichText } from '../form/elements/rich-text.component';
import { NmChart } from './../charts/chart.component';
import { ChartModule } from 'primeng/chart';
import { EditorModule } from 'primeng/editor';
import { TableHeader } from './../grid/table-header.component';
let pageService, webContentSvc, configService;

class MockWebContentSvc {
  findLabelContent(param1) {
    const test = {
      text: 'testing'
    };
    return test;
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
  processEvent(a, b, c, d) { 
   }
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

class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

const declarations = [
  Accordion,
  CardDetailsComponent,
  Link,
  CardDetailsFieldComponent,
  StaticText,
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  DateTimeFormatPipe,
  TooltipComponent,
  SelectItemPipe,
  Image,
  FrmGroupCmp,
  SvgComponent,
  FormElement,
  Button,
  ButtonGroup,
  Signature,
  Calendar,
  RadioButton,
  CheckBoxGroup,
  CheckBox,
  MultiSelectListBox,
  MultiselectCard,
  OrderablePickList,
  FileUploadComponent,
  DataTable,
  TableHeader,
  MessageComponent,
  Header,
  Paragraph,
  HeaderCheckBox,
  ActionDropdown,
  Section,
  ActionLink,
  CardDetailsGrid,
  Menu,
  Form,
  Label,
  CardDetailsFieldGroupComponent,
  DisplayValueDirective,
  InputLabel,
  TreeGrid,
  FormGridFiller,
  InputSwitch,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  InputMaskComp,
  NmChart,
  RichText
];
const imports = [
  FormsModule,
  HttpModule,
  HttpClientModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ReactiveFormsModule,
  DataTableModule, 
  SharedModule, 
  OverlayPanelModule, 
  PickListModule, 
  ChartModule,
  DragDropModule, 
  CalendarModule, 
  FileUploadModule, 
  ListboxModule, 
  DialogModule, 
  CheckboxModule, 
  DropdownModule, 
  RadioButtonModule, 
  ProgressBarModule, 
  ProgressSpinnerModule, 
  AccordionModule, 
  GrowlModule,
  TableModule,
  KeyFilterModule,
  ToastModule,
  InputSwitchModule, 
  TreeTableModule,
  BrowserAnimationsModule,
  InputMaskModule,
  EditorModule
];
const providers = [
  { provide: WebContentSvc, useClass: MockWebContentSvc },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  { provide: PageService, useClass: MockPageService },
  { provide: LoggerService, useClass: MockLoggerService },
  CustomHttpClient,
  LoaderService,
  ConfigService,
  SessionStoreService,
  AppInitService,
  Location,
  GridService,
  PrintService,
  ChangeDetectorRef
];
let fixture, hostComponent, changeDetectorRef;
describe('Accordion', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Accordion);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = accordionElementWithForm as Param;
    pageService = TestBed.get(PageService);
    webContentSvc = TestBed.get(WebContentSvc);
    configService = TestBed.get(ConfigService);
    changeDetectorRef = TestBed.get(ChangeDetectorRef);
  });

  it('should create the Accordion', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Expand All and Collapse All should be created if showExpandAll attribute configured',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const expandAllndCollapseEle = debugElement.queryAll(By.css('.btn.btn-expand'));    
    expect(expandAllndCollapseEle[0].nativeElement.innerText.toString()).toEqual('Expand All');    
    expect(expandAllndCollapseEle[1].nativeElement.innerText.toString()).toEqual('Collapse All');    
  }));

  it('Expand All and Collapse All should not be created if showExpandAll attribute is not configured',async(() => {
    hostComponent.element.config.uiStyles.attributes.showExpandAll = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const expandAllndCollapseEle = debugElement.queryAll(By.css('.btn.btn-expand'));   
    expect(expandAllndCollapseEle.length).toEqual(0);    
  }));

  it('Onclick of expand all button the openAll() should be called',async(() => {
    hostComponent.element.config.uiStyles.attributes.showExpandAll = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const expandAllndCollapseEle = debugElement.queryAll(By.css('.btn.btn-expand'));    
    spyOn(hostComponent, 'openAll').and.callThrough();
    expandAllndCollapseEle[0].nativeElement.click();
    expect(hostComponent.openAll).toHaveBeenCalled();
  }));

  it('Onclick of Collapse all button the closeAll() should be called',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const expandAllndCollapseEle = debugElement.queryAll(By.css('.btn.btn-expand'));    
    spyOn(hostComponent, 'closeAll').and.callThrough();
    expandAllndCollapseEle[1].nativeElement.click();
    expect(hostComponent.closeAll).toHaveBeenCalled();
  }));

  it('p-accordion should not be created if element.visible is false',async(() => {
      hostComponent.element.visible = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pAccordionEle = debugElement.query(By.css('p-accordion'));
    expect(pAccordionEle).toBeFalsy();
  }));

  it('p-accordion should be created if element.visible is true',async(() => {
    hostComponent.element.visible = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pAccordionEle = debugElement.query(By.css('p-accordion'));
    expect(pAccordionEle).toBeTruthy();
  }));

  it('p-accordionTab should be created if element.type.model.params[0].visible is true',async(() => {
    hostComponent.element.type.model.params[0].visible = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pAccordionTab = debugElement.query(By.css('p-accordionTab'));
    expect(pAccordionTab).toBeTruthy();
  }));

  it('Label in header should be created on configuring @Label',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmLabelEle = debugElement.query(By.css('nm-label'));
    expect(nmLabelEle).toBeTruthy();
  }));

  it('If element.type.model.params[0].type.model.params[i].leafState or element.type.model.params[0].type.model.params[i].config.uiStyles.attributes.info is valid then It should be displayed in pheader',async(() => {
      hostComponent.element.type.model.params[0].config.uiStyles.attributes.selected = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pHeaderEle = debugElement.query(By.css('p-header'));
    const infoTextEle = debugElement.query(By.css('.nm-accordion-headertext'));
    expect(pHeaderEle).toBeTruthy();
    expect(infoTextEle.nativeElement.innerText.toString()).toEqual('testing p-header');
  }));

  it('If element.type.model.params[0].type.model.params[i].leafState or element.type.model.params[0].type.model.params[i].config.uiStyles.attributes.info is invalid then It should not be displayed in pheader',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.selected = true;
    hostComponent.element.type.model.params[0].type.model.params[0].leafState = null;
    hostComponent.element.type.model.params[0].type.model.params[0].config.uiStyles.attributes.info = null;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const infoTextEle = debugElement.query(By.css('.nm-accordion-headertext'));
    expect(infoTextEle).toBeFalsy();
  }));

  it('If leafState or imgSrc is configured then image should be displayed in pheader',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.selected = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmImageEle = debugElement.query(By.css('nm-image'));
    expect(nmImageEle).toBeTruthy();
  }));

  it('If leafState or imgSrc is not configured then image should not be displayed in pheader',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.selected = true;
    hostComponent.element.type.model.params[0].type.model.params[1].leafState = null;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmImageEle = debugElement.query(By.css('nm-image'));
    expect(nmImageEle).toBeFalsy();
  }));

  it('Edit Button should be created if editable is configured',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const editButtonEle = debugElement.query(By.css('.btn.btn-plain'));    
    expect(editButtonEle).toBeTruthy();
    expect(editButtonEle.nativeElement.innerText.toString()).toEqual('Edit');
  }));

  it('Onclick of edit button processOnClick() should be called',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const editButtonEle = debugElement.query(By.css('.btn.btn-plain'));    
    spyOn(hostComponent, 'processOnClick').and.callThrough();
    editButtonEle.nativeElement.click();
    expect(hostComponent.processOnClick).toHaveBeenCalled();
    expect(hostComponent.processOnClick).toHaveBeenCalledWith(accordionElementWithForm.type.model.params[0]);
  }));

  it('If element.type.model.params[0].type.model.params[i] and form is defined then form group should be created',async(() => {
    hostComponent.form = new FormGroup({
        question123: new FormControl(),
        txt1: new FormControl()
     });
    hostComponent.ngOnDestroy = () => {}
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeTruthy();
  }));

  it('If form is undefined and @ButtonGroup configured then button group should be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = debugElement.query(By.css('nm-button-group'));
    expect(buttonGroupEle).toBeTruthy();
  }));

  it('If form is undefined and @ButtonGroup is not configured then button group should not be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[5].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = debugElement.query(By.css('nm-button-group'));
    expect(buttonGroupEle).toBeFalsy();
  }));

  it('If form is undefined and @Link is configured then Link should be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeTruthy()
  }));

  it('If form is undefined and @Link is not configured then Link should not be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[6].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy()
  }));

  it('If form is undefined and @Grid is configured then table should be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const gridEle = debugElement.query(By.css('nm-table'));
    expect(gridEle).toBeTruthy()
  }));

  it('If form is undefined and @Grid is not configured then table should not be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[7].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const gridEle = debugElement.query(By.css('nm-table'));
    expect(gridEle).toBeFalsy()
  }));

  it('If form is undefined and @CardDetail is configured then cardDetail should be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailEle = debugElement.query(By.css('nm-card-details'));
    expect(cardDetailEle).toBeTruthy();
  }));

  it('If form is undefined and @CardDetail is not configured then cardDetail should not be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[8].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailEle = debugElement.query(By.css('nm-card-details'));
    expect(cardDetailEle).toBeFalsy();
  }));

  it('If form is undefined and @CardDetailsGrid then cardDetailsGrid should be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsGridEle = debugElement.query(By.css('nm-card-details-grid'));
    expect(cardDetailsGridEle).toBeTruthy();
  }));

  it('If form is undefined and @CardDetailsGrid is not configured then cardDetailsGrid should not be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[9].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsGridEle = debugElement.query(By.css('nm-card-details-grid'));
    expect(cardDetailsGridEle).toBeFalsy();
  }));

  it('If form is undefined and form is configured in nested level then form should be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const formEle = debugElement.query(By.css('nm-form'));
    expect(formEle).toBeTruthy();
  }));

  it('If form is undefined and form is not configured in nested level also then form should not be created ',async(() => {
    hostComponent.element = accordionElementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[10].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const formEle = debugElement.query(By.css('nm-form'));
    expect(formEle).toBeFalsy();
  }));

  it('get multiple() should return the this.element.config.uiStyles.attributes.multiple value', async(() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.multiple = false;
      expect(hostComponent.multiple).toEqual(false);
    });
  }));

  it('closeAll should clear the index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService, changeDetectorRef);
    hostComponent.accordion['tabs'] = true;
    hostComponent.index = [1, 2, 3];
    hostComponent.closeAll();
    expect(hostComponent.index).toEqual([-1]);
  }));

  it('closeAll should not clear the index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService, changeDetectorRef);
    hostComponent.accordion['tabs'] = false;
    hostComponent.index = [1, 2, 3];
    hostComponent.closeAll();
    expect(hostComponent.index).toEqual([1, 2, 3]);
  }));

  it('openAll() should update index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService, changeDetectorRef);
    hostComponent.accordion['tabs'] = [1, 2, 3];
    hostComponent.index = [];
    hostComponent.openAll();
    expect(hostComponent.index.length).toEqual(3);
  }));

  it('openAll() should not update index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService, changeDetectorRef);
    hostComponent.index = [];
    hostComponent.openAll();
    expect(hostComponent.index.length).toEqual(0);
  }));

  it('processOnClick() should call processEvent()', async(() => {
    spyOn(pageService, 'processEvent').and.callThrough();
    const test = new Param(configService);
    hostComponent.processOnClick(test);
    expect(pageService.processEvent).toHaveBeenCalled();
    expect(pageService.processEvent).toHaveBeenCalledWith(undefined, '$execute', null, 'POST');
  }));

  it('getImageSrc() should return imgSrc', async(() => {
    const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { info: 'test' } } } }] } } };
    expect(hostComponent.getImageSrc(tab)).toEqual('test');
  }));

  it('getImageSrc() should return undefined', async(() => {
    const tab = { type: { model: { params: [{ alias: 'Image', visible: false, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }] } } };
    expect(hostComponent.getImageSrc(tab)).toBeFalsy();
  }));

  it('getImageSrc() should return leafState', async(() => {
    const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: 'test', config: { uiStyles: { attributes: { imgSrc: '' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }] } } };
    expect(hostComponent.getImageSrc(tab)).toEqual('test');
  }));

  it('getImageType() should return type, getTitle() should return title and getcssClass() should return cssClass', async(() => {
    const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test', type: 'testingType', title: 'testingTitle', cssClass: 'testingCssClass' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { info: 'test' } } } }] } } };
    expect(hostComponent.getImageType(tab)).toEqual('testingType');
    expect(hostComponent.getTitle(tab)).toEqual('testingTitle');
    expect(hostComponent.getcssClass(tab)).toEqual('testingCssClass');
  }));

  it('ngOnInit() should call updatePositionWithNoLabel()', async(() => {
    hostComponent.updatePositionWithNoLabel = () => {};
    spyOn(hostComponent, 'updatePositionWithNoLabel').and.callThrough();
    hostComponent.ngOnInit();
    expect(hostComponent.updatePositionWithNoLabel).toHaveBeenCalled();    
  }));

  it('getInfoText() should return undefined', async(() => {
    const tab = { type: { model: { params: [{ alias: 'TabInfo', visible: false, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
    expect(hostComponent.getInfoText(tab)).toEqual(undefined);
  }));

  it('getInfoText() should return tab.type.model.params[0].config.uiStyles.attributes.info', async(() => {
    const tab = { type: { model: { params: [{ alias: 'TabInfo', visible: true, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
    expect(hostComponent.getInfoText(tab)).toEqual('info');
  }));

  it('getInfoText() should return tab.type.model.params[0].leafState', async(() => {
    const tab = { type: { model: { params: [{ alias: 'TabInfo', leafState: 'leafState', visible: true, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
    expect(hostComponent.getInfoText(tab)).toEqual('leafState');
  }));

  it('getTabInfoClass() should return tab.type.model.params[0].config.uiStyles.attributes.cssClass', async(() => {
    const tab = { type: { model: { params: [{ alias: 'TabInfo', config: { uiStyles: { attributes: { cssClass: 'cssClass' } } } }] } } };
    expect(hostComponent.getTabInfoClass(tab)).toEqual('cssClass');
  }));

  it('getTabInfoClass() should return nm-accordion-headertext', async(() => {
    const tab = { type: { model: { params: [{ alias: 'TabInfo', config: { uiStyles: { attributes: {} } } }] } } };
    expect(hostComponent.getTabInfoClass(tab)).toEqual('nm-accordion-headertext');
  }));


it('Edit Button should not be created if editable attribute is configured as false',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.editable = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const editButtonEle = debugElement.query(By.css('.btn.btn-plain'));    
    expect(editButtonEle).toBeFalsy();
}));

it('If nested param object and form is undefined then form group should not be created',async(() => {
    hostComponent.form = null;
    hostComponent.element.type.model.params[0].type.model.params = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeFalsy();
  }));

  it('nm-counter-message in pheader should be created if showMessages attribute is configured as true',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.selected = true;
    hostComponent.element.config.uiStyles.attributes.showMessages = true;
    hostComponent.form = new FormGroup({
        question123: new FormControl(),
        txt1: new FormControl()
     });
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const counterMessageEle = debugElement.query(By.css('nm-counter-message'));
    expect(counterMessageEle).toBeTruthy();
  }));

  it('nm-counter-message in pheader should not be created if showMessages attribute is configured as false',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.selected = true;
    hostComponent.element.config.uiStyles.attributes.showMessages = false;
    hostComponent.form = new FormGroup({
        question123: new FormControl(),
        txt1: new FormControl()
     });
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const counterMessageEle = debugElement.query(By.css('nm-counter-message'));
    expect(counterMessageEle).toBeFalsy();
  }));

  it('Label in header should not be created on if element.type.model.params[] is empty',async(() => {
    hostComponent.element.type.model.params = [];
  fixture.detectChanges();
  const debugElement = fixture.debugElement;
  const nmLabelEle = debugElement.query(By.css('nm-label'));
  expect(nmLabelEle).toBeFalsy();
    }));

});
