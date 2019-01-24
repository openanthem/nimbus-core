import { TableHeader } from './grid/table-header.component';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CalendarModule, RadioButtonModule, CheckboxModule, ListboxModule, FileUploadModule, 
  GrowlModule, DropdownModule, DataTableModule, AccordionModule, PickListModule, TreeTableModule, 
  InputSwitchModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { Section } from './section.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Menu } from '../platform/menu.component';
import { Link } from '../platform/link.component';
import { Form } from '../platform/form.component';
import { StaticText } from '../platform/content/static-content.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { MessageComponent } from '../platform/message/message.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { FrmGroupCmp } from './form-group.component';
import { Accordion } from '../platform/content/accordion.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { FormElement } from './form-element.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { Signature } from '../platform/form/elements/signature.component'
import { Header } from '../platform/content/header.component';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { DataTable } from './grid/table.component';
import { LoggerService } from '../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { AppInitService } from '../../services/app.init.service';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { SvgComponent } from './svg/svg.component';
import { Image } from './image.component';
import { InputLabel } from '../platform/form/elements/input-label.component';
import { Label } from '../platform/content/label.component';
import { TreeGrid } from '../platform/tree-grid/tree-grid.component';
import { InputSwitch } from '../platform/form/elements/input-switch.component';
import { CardDetailsFieldGroupComponent } from '../platform/card/card-details-field-group.component';
import { DisplayValueDirective } from '../../directives/display-value.directive';
import { FormGridFiller } from '../platform/form/form-grid-filler.component';
import { InputLegend } from '../platform/form/elements/input-legend.component';
import { FormErrorMessage } from './form-error-message.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { Param } from '../../shared/param-state';
import { PrintDirective } from '../../directives/print.directive';
import { fieldValueParam } from 'mockdata';

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
    processEvent() {

    }
}

class MockLoggerService {
  debug() { }
  info() { }
  error() { }
}

const declarations = [
  Section,
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
  TooltipComponent,
  SelectItemPipe,
  ActionDropdown,
  DateTimeFormatPipe,
  FrmGroupCmp,
  Accordion,
  CardDetailsFieldComponent,
  ActionLink,
  FormElement,
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
  Signature,
  Header,
  DataTable,
  TableHeader,
  HeaderCheckBox,
  SvgComponent,
  Image,
  InputLabel,
  Label,
  TreeGrid,
  InputSwitch,
  CardDetailsFieldGroupComponent,
  DisplayValueDirective,
  FormGridFiller,
  InputLegend,
  FormErrorMessage,
  PrintDirective
 ];
const imports = [
  FormsModule,
  DropdownModule,
  DataTableModule,
  AccordionModule,
  ReactiveFormsModule,
  GrowlModule,
  PickListModule,
  FileUploadModule,
  ListboxModule,
  CheckboxModule,
  RadioButtonModule,
  CalendarModule,
  HttpModule,
  HttpClientModule,
  TableModule,
  KeyFilterModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ToastModule,
  TreeTableModule,
  InputSwitchModule
 ];
const providers = [
  { provide: PageService, useClass: MockPageService },
  { provide: 'JSNLOG', useValue: JL },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  {provide: LoggerService, useClass: MockLoggerService},
  WebContentSvc,
  CustomHttpClient,
  LoaderService,
  ConfigService,
  AppInitService
 ];

 let fixture, hostComponent;

describe('Section', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Section);
    hostComponent = fixture.debugElement.componentInstance;
      hostComponent.element = fieldValueParam;
  });

  it('should create the Section',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('ngOnInit() should call pageSvc.processEvent()',  async(() => {
   const service = TestBed.get(PageService);
    hostComponent.element.config.initializeComponent = () => {return true};
    spyOn(service, 'processEvent').and.callThrough();
    hostComponent.ngOnInit();
    expect(service.processEvent).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call pageSvc.processEvent()',  async(() => {
    const service = TestBed.get(PageService);
    hostComponent.element = { } as Param;
    spyOn(service, 'processEvent').and.callThrough();
    hostComponent.ngOnInit();
    expect(service.processEvent).not.toHaveBeenCalled();
  }));

});
