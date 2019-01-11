import { TableHeader } from './../grid/table-header.component';
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
  ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
  import { TableModule } from 'primeng/table';
  import { KeyFilterModule } from 'primeng/keyfilter';
  import { ToastModule } from 'primeng/toast';
  import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';
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
// import { Button } from '../form/elements/button.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Signature } from '../form/elements/signature.component';
// import { DateControl } from '../form/elements/date.component';
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
import * as data from '../../../payload.json';
import { FormErrorMessage } from '../form-error-message.component';
import { Param } from '../../../shared/param-state';
import { PrintDirective } from '../../../directives/print.directive';
import { Subject } from 'rxjs';
import { By } from '@angular/platform-browser';
import { GridService } from '../../../services/grid.service';
import { PrintService } from '../../../services/print.service';

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
  processEvent(a, b, c, d) {  }
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
  // DateControl,
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
  PrintDirective
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
  BrowserAnimationsModule
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
  PrintService
];
let fixture, hostComponent;
describe('Accordion', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Accordion);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = elementWithForm as Param;
    pageService = TestBed.get(PageService);
    webContentSvc = TestBed.get(WebContentSvc);
    configService = TestBed.get(ConfigService);
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

  it('p-accordionTab should be created if element.type.model.params[0].visible is false',async(() => {
      hostComponent.element.type.model.params[0].visible = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const pAccordionTab = debugElement.query(By.css('p-accordionTab'));
    expect(pAccordionTab).toBeFalsy();
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
    expect(hostComponent.processOnClick).toHaveBeenCalledWith(elementWithForm.type.model.params[0]);
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
    hostComponent.element = elementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = debugElement.query(By.css('nm-button-group'));
    expect(buttonGroupEle).toBeTruthy();
  }));

  it('If form is undefined and @ButtonGroup is not configured then button group should not be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[5].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonGroupEle = debugElement.query(By.css('nm-button-group'));
    expect(buttonGroupEle).toBeFalsy();
  }));

  it('If form is undefined and @Link is configured then Link should be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeTruthy()
  }));

  it('If form is undefined and @Link is not configured then Link should not be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[6].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy()
  }));

  it('If form is undefined and @Grid is configured then table should be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const gridEle = debugElement.query(By.css('nm-table'));
    expect(gridEle).toBeTruthy()
  }));

  it('If form is undefined and @Grid is not configured then table should not be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[7].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const gridEle = debugElement.query(By.css('nm-table'));
    expect(gridEle).toBeFalsy()
  }));

  it('If form is undefined and @CardDetail is configured then cardDetail should be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailEle = debugElement.query(By.css('nm-card-details'));
    expect(cardDetailEle).toBeTruthy();
  }));

  it('If form is undefined and @CardDetail is not configured then cardDetail should not be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[8].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailEle = debugElement.query(By.css('nm-card-details'));
    expect(cardDetailEle).toBeFalsy();
  }));

  it('If form is undefined and @CardDetailsGrid then cardDetailsGrid should be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsGridEle = debugElement.query(By.css('nm-card-details-grid'));
    expect(cardDetailsGridEle).toBeTruthy();
  }));

  it('If form is undefined and @CardDetailsGrid is not configured then cardDetailsGrid should not be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    hostComponent.element.type.model.params[0].type.model.params[9].alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsGridEle = debugElement.query(By.css('nm-card-details-grid'));
    expect(cardDetailsGridEle).toBeFalsy();
  }));

  it('If form is undefined and form is configured in nested level then form should be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const formEle = debugElement.query(By.css('nm-form'));
    expect(formEle).toBeTruthy();
  }));

  it('If form is undefined and form is not configured in nested level also then form should not be created ',async(() => {
    hostComponent.element = elementWithNoForm as Param;
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
    hostComponent.accordion = new Accordion(webContentSvc, pageService);
    hostComponent.accordion['tabs'] = true;
    hostComponent.index = [1, 2, 3];
    hostComponent.closeAll();
    expect(hostComponent.index).toEqual([-1]);
  }));

  it('closeAll should not clear the index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService);
    hostComponent.accordion['tabs'] = false;
    hostComponent.index = [1, 2, 3];
    hostComponent.closeAll();
    expect(hostComponent.index).toEqual([1, 2, 3]);
  }));

  it('openAll() should update index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService);
    hostComponent.accordion['tabs'] = [1, 2, 3];
    hostComponent.index = [];
    hostComponent.openAll();
    expect(hostComponent.index.length).toEqual(3);
  }));

  it('openAll() should not update index array', async(() => {
    hostComponent.accordion = new Accordion(webContentSvc, pageService);
    hostComponent.index = [];
    hostComponent.openAll();
    expect(hostComponent.index.length).toEqual(0);
  }));

  it('processOnClick() should call processEvent()', async(() => {
    spyOn(pageService, 'processEvent').and.callThrough();
    const test = new Param(configService);
    hostComponent.processOnClick(test);
    expect(pageService.processEvent).toHaveBeenCalled();
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


const elementWithForm: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "48264",
      "code": "patientName1",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.Accordion",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "activeIndex": "0",
              "cssClass": "panel-default",
              "showMessages": false,
              "showExpandAll": true,
              "multiple": false,
              "alias": "Accordion"
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "VPOwnerInfo.VSPets.PetAssessment_Name1",
          "model": {
              "paramConfigIds": [
                  "48266"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "48264",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1",
  "type": {
      "model": {
          "params": [
              {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "48266",
                    "code": "petName_Accordion_tab1",
                    "validations": null,
                    "uiNatures": [],
                    "uiStyles": {
                        "isLink": false,
                        "isHidden": false,
                        "name": "ViewConfig.AccordionTab",
                        "attributes": {
                            "hidden": false,
                            "readOnly": false,
                            "submitButton": true,
                            "showName": true,
                            "pageSize": 25,
                            "browserBack": false,
                            "showAsLink": false,
                            "cssClass": "panel-default",
                            "editable": true,
                            "alias": "AccordionTab",
                            "selected": false,
                            "info": "testing info in pheader"
                        }
                    },
                    "type": {
                        "collection": false,
                        "nested": true,
                        "name": "VPOwnerInfo.VSPets.PetAssessment_Name_Tab1",
                        "model": {
                            "paramConfigIds": [
                                "48268",
                                "48269",
                                "48270",
                                "48271",
                                "48272",
                                "48273",
                                "48277",
                                "48282",
                                "48312",
                                "48330",
                                "48336"
                            ]
                        }
                    }
                },
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "48266",
                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1",
                  "type": {
                      "model": {
                          "params": [
                              {
                                  "config": {
                                    "active": false,
                                    "required": false,
                                    "id": "48268",
                                    "code": "testingTab",
                                    "validations": null,
                                    "uiNatures": [],
                                    "uiStyles": {
                                        "isLink": false,
                                        "isHidden": false,
                                        "name": "ViewConfig.TabInfo",
                                        "attributes": {
                                            "hidden": false,
                                            "readOnly": false,
                                            "submitButton": true,
                                            "showName": true,
                                            "pageSize": 25,
                                            "browserBack": false,
                                            "showAsLink": false,
                                            "cssClass": "",
                                            "alias": "TabInfo",
                                            "info": "testing tab info label"
                                        }
                                    },
                                    "type": {
                                        "collection": false,
                                        "nested": false,
                                        "name": "string"
                                    }
                                },
                                "alias": "TabInfo",
                                  "enabled": true,
                                  "visible": true,
                                  "activeValidationGroups": [],
                                  "collectionParams": [],
                                  "configId": "48268",
                                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/testingTab",
                                  "type": {
                                      "nested": false,
                                      "name": "string",
                                      "collection": false
                                  },
                                  "message": [],
                                  "values": [],
                                  "labels": [
                                      {
                                          "locale": "en-US",
                                          "text": "testing tabinfo..."
                                      }
                                  ],
                                  "elemLabels": {},
                                  "leafState": 'testing p-header'
                              },
                              {
                                  "config": {
                                    "active": false,
                                    "required": false,
                                    "id": "48269",
                                    "code": "question123",
                                    "uiNatures": [],
                                    "uiStyles": {
                                        "isLink": false,
                                        "isHidden": false,
                                        "name": "ViewConfig.TextBox",
                                        "attributes": {
                                            "hidden": false,
                                            "readOnly": false,
                                            "submitButton": true,
                                            "showName": true,
                                            "pageSize": 25,
                                            "browserBack": false,
                                            "showAsLink": false,
                                            "help": "",
                                            "cssClass": "",
                                            "dataEntryField": true,
                                            "labelClass": "anthem-label",
                                            "alias": "TextBox",
                                            "controlId": "",
                                            "type": "text",
                                            "postEventOnChange": false,
                                            "cols": ""
                                        }
                                    },
                                    "type": {
                                        "collection": false,
                                        "nested": false,
                                        "name": "string"
                                    },
                                    "validation": {
                                        "constraints": [
                                            {
                                                "name": "NotNull",
                                                "attribute": {
                                                    "message": "",
                                                    "groups": []
                                                }
                                            }
                                        ]
                                    }
                                },
                                "alias": "Image",
                                "leafState": "testing nm-image",
                                  "enabled": true,
                                  "visible": true,
                                  "activeValidationGroups": [],
                                  "collectionParams": [],
                                  "configId": "48269",
                                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/question123",
                                  "type": {
                                      "nested": false,
                                      "name": "string",
                                      "collection": false
                                  },
                                  "message": [],
                                  "values": [],
                                  "labels": [
                                      {
                                          "locale": "en-US",
                                          "text": "Question 1"
                                      }
                                  ],
                                  "elemLabels": {}
                              },
                              {
                                  "config": {
                                    "active": false,
                                    "required": false,
                                    "id": "48270",
                                    "code": "txt1",
                                    "validations": null,
                                    "uiNatures": [],
                                    "uiStyles": {
                                        "isLink": false,
                                        "isHidden": false,
                                        "name": "ViewConfig.TextBox",
                                        "attributes": {
                                            "hidden": false,
                                            "readOnly": false,
                                            "submitButton": true,
                                            "showName": true,
                                            "pageSize": 25,
                                            "browserBack": false,
                                            "showAsLink": false,
                                            "help": "",
                                            "cssClass": "",
                                            "dataEntryField": true,
                                            "labelClass": "anthem-label",
                                            "alias": "TextBox",
                                            "controlId": "",
                                            "type": "text",
                                            "postEventOnChange": false,
                                            "cols": ""
                                        }
                                    },
                                    "type": {
                                        "collection": false,
                                        "nested": false,
                                        "name": "string"
                                    }
                                },
                                  "enabled": true,
                                  "visible": true,
                                  "activeValidationGroups": [],
                                  "collectionParams": [],
                                  "configId": "48270",
                                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/txt1",
                                  "type": {
                                      "nested": false,
                                      "name": "string",
                                      "collection": false
                                  },
                                  "message": [],
                                  "values": [],
                                  "labels": [],
                                  "elemLabels": {}
                              }
                          ]
                      }
                  },
                  "message": [],
                  "values": [],
                  "labels": [
                      {
                          "locale": "en-US",
                          "text": "testing accordion..193"
                      }
                  ],
                  "elemLabels": {}
              }
          ]
      }
  },
  "message": [],
  "values": [],
  "labels": [],
  "elemLabels": {}
};


const accordionForm: any = {
    "_updateTreeValidity": () => {},
    "_registerOnCollectionChange": () => {},
    "get": () => {},
    "validator": null,
    "asyncValidator": null,
    "pristine": true,
    "touched": false,
    "_onDisabledChange": [],
    "controls": {
        "question123": {
            "asyncValidator": null,
            "pristine": true,
            "touched": false,
            "_onDisabledChange": [],
            "_onChange": [],
            "_pendingValue": "",
            "value": "",
            "status": "INVALID",
            "errors": {
                "required": true
            },
            "valueChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            },
            "statusChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            }
        },
        "txt1": {
            "validator": null,
            "asyncValidator": null,
            "pristine": true,
            "touched": false,
            "_onDisabledChange": [],
            "_onChange": [],
            "_pendingValue": "",
            "value": "",
            "status": "VALID",
            "errors": null,
            "valueChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            },
            "statusChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            }
        },
        "petForm_1a1": {
            "validator": null,
            "asyncValidator": null,
            "pristine": true,
            "touched": false,
            "_onDisabledChange": [],
            "_onChange": [],
            "_pendingValue": "",
            "value": "",
            "status": "VALID",
            "errors": null,
            "valueChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            },
            "statusChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            }
        },
        "owners1": {
            "validator": null,
            "asyncValidator": null,
            "pristine": true,
            "touched": false,
            "_onDisabledChange": [],
            "_onChange": [],
            "_pendingValue": [
                {
                    "id": 52,
                    "firstName": "test",
                    "lastName": "1",
                    "shouldUseNickname": false,
                    "ownerCity": "",
                    "telephone": "1231231231",
                    "vlmCaseItemLinks": {},
                    "expandedRowContent": {
                        "pets": []
                    },
                    "elemId": "0"
                },
                {
                    "id": 51,
                    "firstName": "test",
                    "lastName": "1",
                    "shouldUseNickname": false,
                    "ownerCity": "",
                    "telephone": "1231231231",
                    "vlmCaseItemLinks": {},
                    "expandedRowContent": {
                        "pets": []
                    },
                    "elemId": "1"
                },
                {
                    "id": 53,
                    "firstName": "test",
                    "lastName": "123",
                    "shouldUseNickname": false,
                    "ownerCity": "",
                    "telephone": "1231231231",
                    "vlmCaseItemLinks": {},
                    "expandedRowContent": {
                        "pets": []
                    },
                    "elemId": "2"
                }
            ],
            "value": [
                {
                    "id": 52,
                    "firstName": "test",
                    "lastName": "1",
                    "shouldUseNickname": false,
                    "ownerCity": "",
                    "telephone": "1231231231",
                    "vlmCaseItemLinks": {},
                    "expandedRowContent": {
                        "pets": []
                    },
                    "elemId": "0"
                },
                {
                    "id": 51,
                    "firstName": "test",
                    "lastName": "1",
                    "shouldUseNickname": false,
                    "ownerCity": "",
                    "telephone": "1231231231",
                    "vlmCaseItemLinks": {},
                    "expandedRowContent": {
                        "pets": []
                    },
                    "elemId": "1"
                },
                {
                    "id": 53,
                    "firstName": "test",
                    "lastName": "123",
                    "shouldUseNickname": false,
                    "ownerCity": "",
                    "telephone": "1231231231",
                    "vlmCaseItemLinks": {},
                    "expandedRowContent": {
                        "pets": []
                    },
                    "elemId": "2"
                }
            ],
            "status": "VALID",
            "errors": null,
            "valueChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            },
            "statusChanges": {
                "_isScalar": false,
                "observers": [],
                "closed": false,
                "isStopped": false,
                "hasError": false,
                "thrownError": null,
                "__isAsync": false
            }
        }
    },
    "valueChanges": {
        "_isScalar": false,
        "observers": [],
        "closed": false,
        "isStopped": false,
        "hasError": false,
        "thrownError": null,
        "__isAsync": false
    },
    "statusChanges": {
        "_isScalar": false,
        "observers": [],
        "closed": false,
        "isStopped": false,
        "hasError": false,
        "thrownError": null,
        "__isAsync": false
    },
    "status": "INVALID",
    "value": {
        "question123": "",
        "txt1": "",
        "petForm_1a1": "",
        "owners1": [
            {
                "id": 52,
                "firstName": "test",
                "lastName": "1",
                "shouldUseNickname": false,
                "ownerCity": "",
                "telephone": "1231231231",
                "vlmCaseItemLinks": {},
                "expandedRowContent": {
                    "pets": []
                },
                "elemId": "0"
            },
            {
                "id": 51,
                "firstName": "test",
                "lastName": "1",
                "shouldUseNickname": false,
                "ownerCity": "",
                "telephone": "1231231231",
                "vlmCaseItemLinks": {},
                "expandedRowContent": {
                    "pets": []
                },
                "elemId": "1"
            },
            {
                "id": 53,
                "firstName": "test",
                "lastName": "123",
                "shouldUseNickname": false,
                "ownerCity": "",
                "telephone": "1231231231",
                "vlmCaseItemLinks": {},
                "expandedRowContent": {
                    "pets": []
                },
                "elemId": "2"
            }
        ]
    },
    "errors": null
};

const elementWithNoForm: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "49992",
        "code": "patientName1",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Accordion",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "activeIndex": "0",
                "cssClass": "panel-default",
                "showMessages": false,
                "showExpandAll": true,
                "multiple": false,
                "alias": "Accordion"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VPOwnerInfo.VSPets.PetAssessment_Name1",
            "model": {
                "paramConfigIds": [
                    "49994"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "49992",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                        "active": false,
                        "required": false,
                        "id": "49994",
                        "code": "petName_Accordion_tab1",
                        "validations": null,
                        "uiNatures": [],
                        "uiStyles": {
                            "isLink": false,
                            "isHidden": false,
                            "name": "ViewConfig.AccordionTab",
                            "attributes": {
                                "hidden": false,
                                "readOnly": false,
                                "submitButton": true,
                                "showName": true,
                                "pageSize": 25,
                                "browserBack": false,
                                "showAsLink": false,
                                "cssClass": "panel-default",
                                "editable": true,
                                "alias": "AccordionTab",
                                "selected": false
                            }
                        },
                        "type": {
                            "collection": false,
                            "nested": true,
                            "name": "VPOwnerInfo.VSPets.PetAssessment_Name_Tab1",
                            "model": {
                                "paramConfigIds": [
                                    "49996",
                                    "49997",
                                    "49998",
                                    "49999",
                                    "50000",
                                    "50001",
                                    "50005",
                                    "50010",
                                    "50040",
                                    "50058",
                                    "50064"
                                ]
                            }
                        }
                    },
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "49994",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "49996",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/testingTab",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "testing tabinfo..."
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "49997",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/question123",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Question 1"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "49998",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/txt1",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "49999",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/petForm_1a1",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "First Name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "50000",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/headerCallSection1",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Hello!! Welcome to Pet Clinic. This application is the reference implementation for Nimbus Framework."
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "50001",
                                        "code": "vbg1",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": false,
                                            "isHidden": false,
                                            "name": "ViewConfig.ButtonGroup",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "cssClass": "text-sm-center",
                                                "alias": "ButtonGroup"
                                            }
                                        },
                                        "type": {
                                            "collection": false,
                                            "nested": true,
                                            "name": "VPOwnerInfo.VSPets.VBG1",
                                            "model": {
                                                "paramConfigIds": [
                                                    "50003",
                                                    "50004"
                                                ]
                                            }
                                        }
                                    },
                                    "alias": "ButtonGroup",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "50001",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50003",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1/cancel",
                                                    "type": {
                                                        "nested": false,
                                                        "name": "string",
                                                        "collection": false
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [
                                                        {
                                                            "locale": "en-US",
                                                            "text": "Cancel"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50004",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1/ok",
                                                    "type": {
                                                        "nested": false,
                                                        "name": "string",
                                                        "collection": false
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [
                                                        {
                                                            "locale": "en-US",
                                                            "text": "Ok"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                }
                                            ]
                                        }
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "50005",
                                        "code": "create",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": true,
                                            "isHidden": false,
                                            "name": "ViewConfig.Link",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "b": "$executeAnd$nav",
                                                "cssClass": "",
                                                "method": "GET",
                                                "altText": "",
                                                "rel": "",
                                                "alias": "Link",
                                                "value": "DEFAULT",
                                                "imgSrc": "",
                                                "url": "",
                                                "target": ""
                                            }
                                        },
                                        "type": {
                                            "collection": false,
                                            "nested": false,
                                            "name": "string"
                                        }
                                    },
                                    "alias": "Link",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "50005",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/create",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Add Pet"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "50010",
                                        "code": "owners1",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": false,
                                            "isHidden": false,
                                            "name": "ViewConfig.Grid",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 5,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "rowSelection": false,
                                                "postButtonUrl": "",
                                                "pagination": true,
                                                "dataEntryField": true,
                                                "postButtonTargetPath": "",
                                                "postButtonUri": "",
                                                "expandableRows": true,
                                                "showHeader": true,
                                                "postEventOnChange": false,
                                                "lazyLoad": false,
                                                "url": "",
                                                "dataKey": "id",
                                                "cssClass": "",
                                                "clearAllFilters": false,
                                                "postButtonLabel": "",
                                                "alias": "Grid",
                                                "onLoad": true,
                                                "postButtonAlias": "",
                                                "isTransient": false,
                                                "postButton": false,
                                                "export": false
                                            }
                                        },
                                        "type": {
                                            "collection": true,
                                            "nested": true,
                                            "name": "ArrayList",
                                            "collectionType": "list",
                                            "model": {
                                                "paramConfigIds": []
                                            },
                                            "elementConfig": {
                                                "id": "50013",
                                                "type": {
                                                    "collection": false,
                                                    "nested": true,
                                                    "name": "OwnerLineItem",
                                                    "model": {
                                                        "paramConfigIds": [
                                                            "50015",
                                                            "50016",
                                                            "50017",
                                                            "50018",
                                                            "50019",
                                                            "50020",
                                                            "50021",
                                                            "50022",
                                                            "50023",
                                                            "50027"
                                                        ]
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    "alias": "Grid",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [
                                        {
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "50023",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50025",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/edit",
                                                            "type": {
                                                                "nested": false,
                                                                "name": "string",
                                                                "collection": false
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Edit"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        },
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50026",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/ownerInfo",
                                                            "type": {
                                                                "nested": false,
                                                                "name": "string",
                                                                "collection": false
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Owner Info"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        }
                                                    ]
                                                }
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [],
                                            "elemLabels": {}
                                        },
                                        {
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "50027",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50033",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent/pets",
                                                            "type": {
                                                                "model": {
                                                                    "params": []
                                                                }
                                                            },
                                                            "page": {
                                                                "last": true,
                                                                "totalPages": 1,
                                                                "totalElements": 0,
                                                                "size": 0,
                                                                "number": 0,
                                                                "numberOfElements": 0,
                                                                "first": true
                                                            },
                                                            "gridData": {
                                                                "collectionParams": []
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Pets"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        }
                                                    ]
                                                }
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [],
                                            "elemLabels": {}
                                        },
                                        {
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "50023",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50025",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/edit",
                                                            "type": {
                                                                "nested": false,
                                                                "name": "string",
                                                                "collection": false
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Edit"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        },
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50026",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/ownerInfo",
                                                            "type": {
                                                                "nested": false,
                                                                "name": "string",
                                                                "collection": false
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Owner Info"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        }
                                                    ]
                                                }
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [],
                                            "elemLabels": {}
                                        },
                                        {
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "50027",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50033",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent/pets",
                                                            "type": {
                                                                "model": {
                                                                    "params": []
                                                                }
                                                            },
                                                            "page": {
                                                                "last": true,
                                                                "totalPages": 1,
                                                                "totalElements": 0,
                                                                "size": 0,
                                                                "number": 0,
                                                                "numberOfElements": 0,
                                                                "first": true
                                                            },
                                                            "gridData": {
                                                                "collectionParams": []
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Pets"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        }
                                                    ]
                                                }
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [],
                                            "elemLabels": {}
                                        },
                                        {
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "50023",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50025",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/edit",
                                                            "type": {
                                                                "nested": false,
                                                                "name": "string",
                                                                "collection": false
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Edit"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        },
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50026",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/ownerInfo",
                                                            "type": {
                                                                "nested": false,
                                                                "name": "string",
                                                                "collection": false
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Owner Info"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        }
                                                    ]
                                                }
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [],
                                            "elemLabels": {}
                                        },
                                        {
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "50027",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "50033",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent/pets",
                                                            "type": {
                                                                "model": {
                                                                    "params": []
                                                                }
                                                            },
                                                            "page": {
                                                                "last": true,
                                                                "totalPages": 1,
                                                                "totalElements": 0,
                                                                "size": 0,
                                                                "number": 0,
                                                                "numberOfElements": 0,
                                                                "first": true
                                                            },
                                                            "gridData": {
                                                                "collectionParams": []
                                                            },
                                                            "message": [],
                                                            "values": [],
                                                            "labels": [
                                                                {
                                                                    "locale": "en-US",
                                                                    "text": "Pets"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        }
                                                    ]
                                                }
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [],
                                            "elemLabels": {}
                                        }
                                    ],
                                    "configId": "50010",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50013",
                                                    "collectionElem": true,
                                                    "elemId": "0",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50015",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/id",
                                                                    "type": {},
                                                                    "leafState": 52,
                                                                    "previousLeafState": 52,
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50016",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/firstName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "test",
                                                                    "previousLeafState": "test",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "First Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50017",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/lastName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1",
                                                                    "previousLeafState": "1",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Last Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50018",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/nickname",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Nickname"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50019",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/shouldUseNickname",
                                                                    "type": {},
                                                                    "leafState": false,
                                                                    "previousLeafState": false,
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50020",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/status",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Status"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50021",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/ownerCity",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "",
                                                                    "previousLeafState": "",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Owner City"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50022",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/telephone",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1231231231",
                                                                    "previousLeafState": "1231231231",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Telephone"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50023",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50025",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/edit",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Edit"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50026",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/ownerInfo",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Owner Info"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50027",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50033",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent/pets",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": []
                                                                                        }
                                                                                    },
                                                                                    "page": {
                                                                                        "last": true,
                                                                                        "totalPages": 1,
                                                                                        "totalElements": 0,
                                                                                        "size": 0,
                                                                                        "number": 0,
                                                                                        "numberOfElements": 0,
                                                                                        "first": true
                                                                                    },
                                                                                    "gridData": {
                                                                                        "collectionParams": []
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Pets"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "leafState": {
                                                        "id": 52,
                                                        "firstName": "test",
                                                        "lastName": "1",
                                                        "shouldUseNickname": false,
                                                        "ownerCity": "",
                                                        "telephone": "1231231231",
                                                        "vlmCaseItemLinks": {},
                                                        "expandedRowContent": {
                                                            "pets": []
                                                        },
                                                        "elemId": "0"
                                                    },
                                                    "previousLeafState": {
                                                        "id": 52,
                                                        "firstName": "test",
                                                        "lastName": "1",
                                                        "shouldUseNickname": false,
                                                        "ownerCity": "",
                                                        "telephone": "1231231231",
                                                        "vlmCaseItemLinks": {},
                                                        "expandedRowContent": {
                                                            "pets": []
                                                        },
                                                        "elemId": "0"
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50013",
                                                    "collectionElem": true,
                                                    "elemId": "1",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50015",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/id",
                                                                    "type": {},
                                                                    "leafState": 51,
                                                                    "previousLeafState": 51,
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50016",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/firstName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "test",
                                                                    "previousLeafState": "test",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "First Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50017",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/lastName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1",
                                                                    "previousLeafState": "1",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Last Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50018",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/nickname",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Nickname"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50019",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/shouldUseNickname",
                                                                    "type": {},
                                                                    "leafState": false,
                                                                    "previousLeafState": false,
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50020",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/status",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Status"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50021",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/ownerCity",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "",
                                                                    "previousLeafState": "",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Owner City"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50022",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/telephone",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1231231231",
                                                                    "previousLeafState": "1231231231",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Telephone"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50023",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50025",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/edit",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Edit"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50026",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/ownerInfo",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Owner Info"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50027",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50033",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent/pets",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": []
                                                                                        }
                                                                                    },
                                                                                    "page": {
                                                                                        "last": true,
                                                                                        "totalPages": 1,
                                                                                        "totalElements": 0,
                                                                                        "size": 0,
                                                                                        "number": 0,
                                                                                        "numberOfElements": 0,
                                                                                        "first": true
                                                                                    },
                                                                                    "gridData": {
                                                                                        "collectionParams": []
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Pets"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "leafState": {
                                                        "id": 51,
                                                        "firstName": "test",
                                                        "lastName": "1",
                                                        "shouldUseNickname": false,
                                                        "ownerCity": "",
                                                        "telephone": "1231231231",
                                                        "vlmCaseItemLinks": {},
                                                        "expandedRowContent": {
                                                            "pets": []
                                                        },
                                                        "elemId": "1"
                                                    },
                                                    "previousLeafState": {
                                                        "id": 51,
                                                        "firstName": "test",
                                                        "lastName": "1",
                                                        "shouldUseNickname": false,
                                                        "ownerCity": "",
                                                        "telephone": "1231231231",
                                                        "vlmCaseItemLinks": {},
                                                        "expandedRowContent": {
                                                            "pets": []
                                                        },
                                                        "elemId": "1"
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50013",
                                                    "collectionElem": true,
                                                    "elemId": "2",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50015",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/id",
                                                                    "type": {},
                                                                    "leafState": 53,
                                                                    "previousLeafState": 53,
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50016",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/firstName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "test",
                                                                    "previousLeafState": "test",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "First Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50017",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/lastName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "123",
                                                                    "previousLeafState": "123",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Last Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50018",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/nickname",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Nickname"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50019",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/shouldUseNickname",
                                                                    "type": {},
                                                                    "leafState": false,
                                                                    "previousLeafState": false,
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50020",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/status",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Status"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50021",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/ownerCity",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "",
                                                                    "previousLeafState": "",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Owner City"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50022",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/telephone",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1231231231",
                                                                    "previousLeafState": "1231231231",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Telephone"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50023",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50025",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/edit",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Edit"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50026",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/ownerInfo",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Owner Info"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50027",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50033",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent/pets",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": []
                                                                                        }
                                                                                    },
                                                                                    "page": {
                                                                                        "last": true,
                                                                                        "totalPages": 1,
                                                                                        "totalElements": 0,
                                                                                        "size": 0,
                                                                                        "number": 0,
                                                                                        "numberOfElements": 0,
                                                                                        "first": true
                                                                                    },
                                                                                    "gridData": {
                                                                                        "collectionParams": []
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Pets"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "leafState": {
                                                        "id": 53,
                                                        "firstName": "test",
                                                        "lastName": "123",
                                                        "shouldUseNickname": false,
                                                        "ownerCity": "",
                                                        "telephone": "1231231231",
                                                        "vlmCaseItemLinks": {},
                                                        "expandedRowContent": {
                                                            "pets": []
                                                        },
                                                        "elemId": "2"
                                                    },
                                                    "previousLeafState": {
                                                        "id": 53,
                                                        "firstName": "test",
                                                        "lastName": "123",
                                                        "shouldUseNickname": false,
                                                        "ownerCity": "",
                                                        "telephone": "1231231231",
                                                        "vlmCaseItemLinks": {},
                                                        "expandedRowContent": {
                                                            "pets": []
                                                        },
                                                        "elemId": "2"
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                }
                                            ]
                                        }
                                    },
                                    "page": {
                                        "last": true,
                                        "totalPages": 1,
                                        "totalElements": 3,
                                        "size": 0,
                                        "number": 0,
                                        "numberOfElements": 3,
                                        "first": true
                                    },
                                    "gridData": {
                                        "collectionParams": [],
                                        "leafState": [
                                            {
                                                "id": 52,
                                                "firstName": "test",
                                                "lastName": "1",
                                                "shouldUseNickname": false,
                                                "ownerCity": "",
                                                "telephone": "1231231231",
                                                "vlmCaseItemLinks": {},
                                                "expandedRowContent": {
                                                    "pets": []
                                                },
                                                "elemId": "0"
                                            },
                                            {
                                                "id": 51,
                                                "firstName": "test",
                                                "lastName": "1",
                                                "shouldUseNickname": false,
                                                "ownerCity": "",
                                                "telephone": "1231231231",
                                                "vlmCaseItemLinks": {},
                                                "expandedRowContent": {
                                                    "pets": []
                                                },
                                                "elemId": "1"
                                            },
                                            {
                                                "id": 53,
                                                "firstName": "test",
                                                "lastName": "123",
                                                "shouldUseNickname": false,
                                                "ownerCity": "",
                                                "telephone": "1231231231",
                                                "vlmCaseItemLinks": {},
                                                "expandedRowContent": {
                                                    "pets": []
                                                },
                                                "elemId": "2"
                                            }
                                        ],
                                        "stateMap": [
                                            {
                                                "id": {},
                                                "firstName": {},
                                                "lastName": {},
                                                "nickname": {},
                                                "shouldUseNickname": {},
                                                "status": {},
                                                "ownerCity": {},
                                                "telephone": {},
                                                "vlmCaseItemLinks": {},
                                                "expandedRowContent": {}
                                            },
                                            {
                                                "id": {},
                                                "firstName": {},
                                                "lastName": {},
                                                "nickname": {},
                                                "shouldUseNickname": {},
                                                "status": {},
                                                "ownerCity": {},
                                                "telephone": {},
                                                "vlmCaseItemLinks": {},
                                                "expandedRowContent": {}
                                            },
                                            {
                                                "id": {},
                                                "firstName": {},
                                                "lastName": {},
                                                "nickname": {},
                                                "shouldUseNickname": {},
                                                "status": {},
                                                "ownerCity": {},
                                                "telephone": {},
                                                "vlmCaseItemLinks": {},
                                                "expandedRowContent": {}
                                            }
                                        ]
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owners"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "50040",
                                        "code": "vcdOwnerInfo1",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": false,
                                            "isHidden": false,
                                            "name": "ViewConfig.CardDetail",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "border": false,
                                                "cssClass": "contentBox right-gutter bg-alternate mt-0",
                                                "draggable": false,
                                                "expandable": false,
                                                "editable": false,
                                                "modelPath": "",
                                                "alias": "CardDetail",
                                                "imgSrc": ""
                                            }
                                        },
                                        "type": {
                                            "collection": false,
                                            "nested": true,
                                            "name": "VPOwnerInfo.VCDOwnerInfo",
                                            "model": {
                                                "paramConfigIds": [
                                                    "50042"
                                                ]
                                            }
                                        }
                                    },
                                    "alias": "CardDetail",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "50040",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50042",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50044",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/firstName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "test",
                                                                    "previousLeafState": "test",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "First Name---127..."
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50045",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/lastName",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1",
                                                                    "previousLeafState": "1",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Last Name"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50046",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/divider2",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50047",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50049",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/address",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Address"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50050",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/city",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "City"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50051",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/state",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "State"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50052",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/zip",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "",
                                                                                    "previousLeafState": "",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Zip"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Address Group"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50053",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/telephone",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "leafState": "1231231231",
                                                                    "previousLeafState": "1231231231",
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Telephone"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                }
                                            ]
                                        }
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "testing card details label 108-"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "50058",
                                        "code": "vcdgConcerns1",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": false,
                                            "isHidden": false,
                                            "name": "ViewConfig.CardDetailsGrid",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "editUrl": "",
                                                "cssClass": "",
                                                "draggable": false,
                                                "alias": "CardDetailsGrid",
                                                "onLoad": true
                                            }
                                        },
                                        "type": {
                                            "collection": true,
                                            "nested": true,
                                            "name": "ArrayList",
                                            "collectionType": "list",
                                            "model": {
                                                "paramConfigIds": []
                                            },
                                            "elementConfig": {
                                                "id": "50061",
                                                "type": {
                                                    "collection": false,
                                                    "nested": true,
                                                    "name": "VPOwnerInfo.VSOwnerInfo",
                                                    "model": {
                                                        "paramConfigIds": [
                                                            "50063"
                                                        ]
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    "alias": "CardDetailsGrid",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "50058",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50061",
                                                    "collectionElem": true,
                                                    "elemId": "0",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50063",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50042",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": [
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50044",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/firstName",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "test",
                                                                                                    "previousLeafState": "test",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "First Name---127..."
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50045",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/lastName",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "1",
                                                                                                    "previousLeafState": "1",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Last Name"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50046",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/divider2",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50047",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                                    "type": {
                                                                                                        "model": {
                                                                                                            "params": [
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50049",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "Address"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50050",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "City"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50051",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "State"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50052",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "Zip"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                }
                                                                                                            ]
                                                                                                        }
                                                                                                    },
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Address Group"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50053",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/telephone",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "1231231231",
                                                                                                    "previousLeafState": "1231231231",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Telephone"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                }
                                                                                            ]
                                                                                        }
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "testing card details label 108-"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50061",
                                                    "collectionElem": true,
                                                    "elemId": "1",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50063",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50042",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": [
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50044",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/firstName",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "test",
                                                                                                    "previousLeafState": "test",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "First Name---127..."
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50045",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/lastName",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "1",
                                                                                                    "previousLeafState": "1",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Last Name"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50046",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/divider2",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50047",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                                    "type": {
                                                                                                        "model": {
                                                                                                            "params": [
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50049",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "Address"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50050",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "City"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50051",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "State"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50052",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "Zip"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                }
                                                                                                            ]
                                                                                                        }
                                                                                                    },
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Address Group"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50053",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/telephone",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "1231231231",
                                                                                                    "previousLeafState": "1231231231",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Telephone"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                }
                                                                                            ]
                                                                                        }
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "testing card details label 108-"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50061",
                                                    "collectionElem": true,
                                                    "elemId": "2",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50063",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50042",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": [
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50044",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/firstName",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "test",
                                                                                                    "previousLeafState": "test",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "First Name---127..."
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50045",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/lastName",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "123",
                                                                                                    "previousLeafState": "123",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Last Name"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50046",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/divider2",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50047",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                                    "type": {
                                                                                                        "model": {
                                                                                                            "params": [
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50049",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "Address"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50050",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "City"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50051",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "State"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                },
                                                                                                                {
                                                                                                                    "enabled": true,
                                                                                                                    "visible": true,
                                                                                                                    "activeValidationGroups": [],
                                                                                                                    "collectionParams": [],
                                                                                                                    "configId": "50052",
                                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                                                    "type": {
                                                                                                                        "nested": false,
                                                                                                                        "name": "string",
                                                                                                                        "collection": false
                                                                                                                    },
                                                                                                                    "leafState": "",
                                                                                                                    "previousLeafState": "",
                                                                                                                    "message": [],
                                                                                                                    "values": [],
                                                                                                                    "labels": [
                                                                                                                        {
                                                                                                                            "locale": "en-US",
                                                                                                                            "text": "Zip"
                                                                                                                        }
                                                                                                                    ],
                                                                                                                    "elemLabels": {}
                                                                                                                }
                                                                                                            ]
                                                                                                        }
                                                                                                    },
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Address Group"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "enabled": true,
                                                                                                    "visible": true,
                                                                                                    "activeValidationGroups": [],
                                                                                                    "collectionParams": [],
                                                                                                    "configId": "50053",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/telephone",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "1231231231",
                                                                                                    "previousLeafState": "1231231231",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Telephone"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                }
                                                                                            ]
                                                                                        }
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "testing card details label 108-"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                }
                                            ]
                                        }
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "testing grid label-181"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "50064",
                                        "code": "vfForm1",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": false,
                                            "isHidden": false,
                                            "name": "ViewConfig.Form",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "submitUrl": "",
                                                "b": "",
                                                "cssClass": "",
                                                "showMessages": false,
                                                "alias": "Form",
                                                "navLink": "",
                                                "manualValidation": false
                                            }
                                        },
                                        "type": {
                                            "collection": false,
                                            "nested": true,
                                            "name": "VPOwnerInfo.VFForm",
                                            "model": {
                                                "paramConfigIds": [
                                                    "50066",
                                                    "50067"
                                                ]
                                            }
                                        }
                                    },
                                    "alias": "Form",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "50064",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50066",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/headerCallSection",
                                                    "type": {
                                                        "nested": false,
                                                        "name": "string",
                                                        "collection": false
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [
                                                        {
                                                            "locale": "en-US",
                                                            "text": "Hello test 1 !! Welcome to PugsAndPaws. Below is your call history."
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "50067",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50069",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/gridVisibility",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50070",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/showHistory",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Show Call History"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": false,
                                                                    "visible": false,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50071",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/hideHistory",
                                                                    "type": {
                                                                        "nested": false,
                                                                        "name": "string",
                                                                        "collection": false
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [
                                                                        {
                                                                            "locale": "en-US",
                                                                            "text": "Hide Call History"
                                                                        }
                                                                    ],
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": false,
                                                                    "visible": false,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "50072",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/gridWrapper",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": false,
                                                                                    "visible": false,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "50074",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/gridWrapper/calls",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": []
                                                                                        }
                                                                                    },
                                                                                    "page": {
                                                                                        "last": true,
                                                                                        "totalPages": 1,
                                                                                        "totalElements": 0,
                                                                                        "size": 0,
                                                                                        "number": 0,
                                                                                        "numberOfElements": 0,
                                                                                        "first": true
                                                                                    },
                                                                                    "gridData": {
                                                                                        "collectionParams": []
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Call History"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
                                                                    "labels": [],
                                                                    "elemLabels": {}
                                                                }
                                                            ]
                                                        }
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                }
                                            ]
                                        }
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [],
                                    "elemLabels": {}
                                }
                            ]
                        }
                    },
                    "message": [],
                    "values": [],
                    "labels": [
                        {
                            "locale": "en-US",
                            "text": "testing accordion..193"
                        }
                    ],
                    "elemLabels": {}
                }
            ]
        }
    },
    "message": [],
    "values": [],
    "labels": [],
    "elemLabels": {}
};