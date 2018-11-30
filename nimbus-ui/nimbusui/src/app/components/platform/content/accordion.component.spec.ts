'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
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

let pageService, param, webContentSvc, configService;

class MockWebContentSvc {
  findLabelContent(param1) {
    const test = {
      text: 'testing'
    };
    return test;
  }
}

class MockPageService {
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
  TreeTableModule
];
const providers = [
  { provide: WebContentSvc, useClass: MockWebContentSvc },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  { provide: PageService, useClass: MockPageService },
  CustomHttpClient,
  LoaderService,
  ConfigService,
  LoggerService,
  SessionStoreService,
  AppInitService,
  Location
];
let fixture, hostComponent;
describe('Accordion', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(Accordion);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
    pageService = TestBed.get(PageService);
    webContentSvc = TestBed.get(WebContentSvc);
    configService = TestBed.get(ConfigService);
  });

  it('should create the Accordion', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Expand All and Collapse All should be created if element.config?.uiStyles?.attributes?.showExpandAll is valid',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Expand All and Collapse All should not be created if element.config?.uiStyles?.attributes?.showExpandAll is invalid',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Onclick of expand all button the openAll() should be called',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Onclick of Collapse all button the closeAll() should be called',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('p-accordion should be created if element.visible is true',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('p-accordion should not be created if element.visible is false',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('p-accordionTab should be created if element.type.model.params[0].visible is true',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('p-accordionTab should be created if element.type.model.params[0].visible is false',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Label in header should be created on providing the element.type.model.params[0].labelConfig display the value provided',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Label in header should not be created on if element.type.model.params[0].labelconfig is empty',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If element.type.model.params[0].type.model.params[i].leafState or element.type.model.params[0].type.model.params[i].config.uiStyles.attributes.info is valid then It should be displayed in pheader',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If element.type.model.params[0].type.model.params[i].leafState or element.type.model.params[0].type.model.params[i].config.uiStyles.attributes.info is invalid then It should not be displayed in pheader',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If element.type.model.params[0].type.model.params[i].leafState or element.type.model.params[0].type.model.params[i].config.uiStyles.attributes.imgSrc is valid then image should be displayed in pheader',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If element.type.model.params[0].type.model.params[i].leafState or element.type.model.params[0].type.model.params[i].config.uiStyles.attributes.imgSrc is invalid then image should not be displayed in pheader',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-counter-message in pheader should be created if element.config?.uiStyles?.attributes?.showMessages is true',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-counter-message in pheader should not be created if element.config?.uiStyles?.attributes?.showMessages is false',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Edit Button should be created if element.type.model.params[0]?.config?.uiStyles?.attributes?.editable is true',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Edit Button should not be created if element.type.model.params[0]?.config?.uiStyles?.attributes?.editable is false',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Onclick of edit button processOnClick() should be called',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If element.type.model.params[0].type.model.params[i] and form is defined then form group should be created',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If element.type.model.params[0].type.model.params[i] and form is undefined then form group should not be created',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias == ButtonGroup then button group should be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias !== ButtonGroup then button group should not be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias == Link then Link should be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias !== Link then Link should not be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias == grid then table should be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias !== grid then table should not be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias == cardDetail then cardDetail should be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias !== cardDetail then cardDetail should not be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias == cardDetailsGrid then cardDetailsGrid should be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias !== cardDetailsGrid then cardDetailsGrid should not be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias == form then form should be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('If form is undefined and element.type.model.params[0].type.model.params[I].alias !== form then form should not be created ',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  // it('get multiple() should return the this.element.config.uiStyles.attributes.multiple value', async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.multiple = false;
  //     expect(hostComponent.multiple).toEqual(false);
  //   });
  // }));

  // it('closeAll should clear the index array', async(() => {
  //   hostComponent.accordion = new Accordion(webContentSvc, pageService);
  //   hostComponent.accordion['tabs'] = true;
  //   hostComponent.index = [1, 2, 3];
  //   hostComponent.closeAll();
  //   expect(hostComponent.index).toEqual([-1]);
  // }));

  // it('closeAll should not clear the index array', async(() => {
  //   hostComponent.accordion = new Accordion(webContentSvc, pageService);
  //   hostComponent.accordion['tabs'] = false;
  //   hostComponent.index = [1, 2, 3];
  //   hostComponent.closeAll();
  //   expect(hostComponent.index).toEqual([1, 2, 3]);
  // }));

  // it('openAll() should update index array', async(() => {
  //   hostComponent.accordion = new Accordion(webContentSvc, pageService);
  //   hostComponent.accordion['tabs'] = [1, 2, 3];
  //   hostComponent.index = [];
  //   hostComponent.openAll();
  //   expect(hostComponent.index.length).toEqual(3);
  // }));

  // it('openAll() should not update index array', async(() => {
  //   hostComponent.accordion = new Accordion(webContentSvc, pageService);
  //   hostComponent.index = [];
  //   hostComponent.openAll();
  //   expect(hostComponent.index.length).toEqual(0);
  // }));

  // it('processOnClick() should call processEvent()', async(() => {
  //   spyOn(pageService, 'processEvent').and.callThrough();
  //   const test = new Param(configService);
  //   hostComponent.processOnClick(test);
  //   expect(pageService.processEvent).toHaveBeenCalled();
  // }));

  // it('getImageSrc() should return imgSrc', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { info: 'test' } } } }] } } };
  //   expect(hostComponent.getImageSrc(tab)).toEqual('test');
  // }));

  // it('getImageSrc() should return undefined', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'Image', visible: false, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }] } } };
  //   expect(hostComponent.getImageSrc(tab)).toBeFalsy();
  // }));

  // it('getImageSrc() should return leafState', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: 'test', config: { uiStyles: { attributes: { imgSrc: '' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }] } } };
  //   expect(hostComponent.getImageSrc(tab)).toEqual('test');
  // }));

  // it('getImageType() should return type, getTitle() should return title and getcssClass() should return cssClass', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test', type: 'testingType', title: 'testingTitle', cssClass: 'testingCssClass' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { info: 'test' } } } }] } } };
  //   expect(hostComponent.getImageType(tab)).toEqual('testingType');
  //   expect(hostComponent.getTitle(tab)).toEqual('testingTitle');
  //   expect(hostComponent.getcssClass(tab)).toEqual('testingCssClass');
  // }));

  // it('ngOnInit() should call updatePositionWithNoLabel()', async(() => {
  //   hostComponent.updatePositionWithNoLabel = () => {};
  //   spyOn(hostComponent, 'updatePositionWithNoLabel').and.callThrough();
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.updatePositionWithNoLabel).toHaveBeenCalled();    
  // }));

  // it('getInfoText() should return undefined', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'TabInfo', visible: false, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
  //   expect(hostComponent.getInfoText(tab)).toEqual(undefined);
  // }));

  // it('getInfoText() should return tab.type.model.params[0].config.uiStyles.attributes.info', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'TabInfo', visible: true, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
  //   expect(hostComponent.getInfoText(tab)).toEqual('info');
  // }));

  // it('getInfoText() should return tab.type.model.params[0].leafState', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'TabInfo', leafState: 'leafState', visible: true, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
  //   expect(hostComponent.getInfoText(tab)).toEqual('leafState');
  // }));

  // it('getTabInfoClass() should return tab.type.model.params[0].config.uiStyles.attributes.cssClass', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'TabInfo', config: { uiStyles: { attributes: { cssClass: 'cssClass' } } } }] } } };
  //   expect(hostComponent.getTabInfoClass(tab)).toEqual('cssClass');
  // }));

  // it('getTabInfoClass() should return nm-accordion-headertext', async(() => {
  //   const tab = { type: { model: { params: [{ alias: 'TabInfo', config: { uiStyles: { attributes: {} } } }] } } };
  //   expect(hostComponent.getTabInfoClass(tab)).toEqual('nm-accordion-headertext');
  // }));

});
