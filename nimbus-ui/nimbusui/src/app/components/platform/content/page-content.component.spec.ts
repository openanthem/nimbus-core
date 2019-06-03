/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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

import { HttpClientModule } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { async, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  ParamMap,
  Params,
  Route,
  UrlSegment
} from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {
  SESSION_STORAGE,
  StorageServiceModule
} from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { fieldValueParam } from 'mockdata';
import { configureTestSuite } from 'ng-bullet';
import { ChartModule } from 'primeng/chart';
import { KeyFilterModule } from 'primeng/keyfilter';
import {
  AccordionModule,
  AutoCompleteModule,
  CalendarModule,
  CheckboxModule,
  DataTableModule,
  DialogModule,
  DropdownModule,
  EditorModule,
  FileUploadModule,
  GrowlModule,
  InputMaskModule,
  InputSwitchModule,
  ListboxModule,
  PickListModule,
  RadioButtonModule,
  TabViewModule,
  TooltipModule,
  TreeTableModule
} from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { Observable, of as observableOf, Subject } from 'rxjs';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { PrintDirective } from '../../../directives/print.directive';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { AppInitService } from '../../../services/app.init.service';
import { ConfigService } from '../../../services/config.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { LoggerService } from '../../../services/logger.service';
import { PageService } from '../../../services/page.service';
import { PrintService } from '../../../services/print.service';
import {
  CUSTOM_STORAGE,
  SessionStoreService
} from '../../../services/session.store';
import { setup } from '../../../setup.spec';
import { CardDetailsFieldGroupComponent } from '../card/card-details-field-group.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { FileUploadComponent } from '../fileupload/file-upload.component';
import { FormElement } from '../form-element.component';
import { FormErrorMessage } from '../form-error-message.component';
import { FrmGroupCmp } from '../form-group.component';
import { Form } from '../form.component';
import {
  ActionDropdown,
  ActionLink
} from '../form/elements/action-dropdown.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Calendar } from '../form/elements/calendar.component';
import { CheckBoxGroup } from '../form/elements/checkbox-group.component';
import { CheckBox } from '../form/elements/checkbox.component';
import { ComboBox } from '../form/elements/combobox.component';
import { HeaderCheckBox } from '../form/elements/header-checkbox.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputLabel } from '../form/elements/input-label.component';
import { InputLegend } from '../form/elements/input-legend.component';
import { InputSwitch } from '../form/elements/input-switch.component';
import { MultiselectCard } from '../form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../form/elements/multi-select-listbox.component';
import { OrderablePickList } from '../form/elements/picklist.component';
import { RadioButton } from '../form/elements/radio.component';
import { Signature } from '../form/elements/signature.component';
import { TextArea } from '../form/elements/textarea.component';
import { InputText } from '../form/elements/textbox.component';
import { FormGridFiller } from '../form/form-grid-filler.component';
import { DataTable } from '../grid/table.component';
import { Image } from '../image.component';
import { Link } from '../link.component';
import { Menu } from '../menu.component';
import { MessageComponent } from '../message/message.component';
import { Modal } from '../modal/modal.component';
import { Section } from '../section.component';
import { SvgComponent } from '../svg/svg.component';
import { Tile } from '../tile.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { TreeGrid } from '../tree-grid/tree-grid.component';
import { NmMessageService } from './../../../services/toastmessage.service';
import { NmChart } from './../charts/chart.component';
import { NmAutocomplete } from './../form/elements/autocomplete.component';
import { InputMaskComp } from './../form/elements/input-mask.component';
import { RichText } from './../form/elements/rich-text.component';
import { TableHeader } from './../grid/table-header.component';
import { Accordion } from './accordion.component';
import { Header } from './header.component';
import { Label } from './label.component';
import { PageContent } from './page-content.component';
import { Paragraph } from './paragraph.component';
import { StaticText } from './static-content.component';
import { Tab } from './tab.component';

let logger, pageService, param, printService;

export class MockActivatedRoute implements ActivatedRoute {
  snapshot: ActivatedRouteSnapshot;
  url: Observable<UrlSegment[]>;
  params: Observable<Params>;
  queryParams: Observable<Params>;
  fragment: Observable<string>;
  outlet: string;
  component: any;
  routeConfig: Route;
  root: ActivatedRoute;
  parent: ActivatedRoute;
  firstChild: ActivatedRoute;
  children: ActivatedRoute[];
  pathFromRoot: ActivatedRoute[];
  data = observableOf({
    page: {
      type: {
        model: {
          params: [
            {
              config: {
                uiStyles: {
                  attributes: {
                    alias: 'Tile'
                  }
                }
              }
            }
          ]
        }
      }
    }
  });
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
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
  debug() {}
  info() {}
  error() {}
}

class MockPageService {
  errorMessageUpdate$: Subject<any>;

  constructor() {
    this.errorMessageUpdate$ = new Subject();
  }

  logError(a) {
    this.errorMessageUpdate$.next(a);
  }
}

class MockPrintService {
  printClickUpdate$: Subject<any>;

  constructor() {
    this.printClickUpdate$ = new Subject();
  }
}

const declarations = [
  PageContent,
  Tile,
  MessageComponent,
  Modal,
  Section,
  Header,
  TooltipComponent,
  ComboBox,
  InputText,
  ButtonGroup,
  Accordion,
  Menu,
  Link,
  Form,
  StaticText,
  Button,
  Paragraph,
  CardDetailsComponent,
  CardDetailsGrid,
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
  DataTable,
  TableHeader,
  HeaderCheckBox,
  SvgComponent,
  Image,
  Label,
  InputSwitch,
  TreeGrid,
  InputLabel,
  CardDetailsFieldGroupComponent,
  DisplayValueDirective,
  FormGridFiller,
  InputLegend,
  FormErrorMessage,
  PrintDirective,
  InputMaskComp,
  NmAutocomplete,
  Tab,
  NmChart,
  RichText
];
const imports = [
  GrowlModule,
  DialogModule,
  FormsModule,
  TooltipModule,
  DropdownModule,
  DataTableModule,
  AccordionModule,
  ReactiveFormsModule,
  FileUploadModule,
  PickListModule,
  ListboxModule,
  CheckboxModule,
  RadioButtonModule,
  CalendarModule,
  RouterTestingModule,
  HttpClientModule,
  HttpModule,
  TableModule,
  KeyFilterModule,
  StorageServiceModule,
  AngularSvgIconModule,
  ToastModule,
  InputSwitchModule,
  TreeTableModule,
  InputMaskModule,
  TabViewModule,
  AutoCompleteModule,
  ChartModule,
  EditorModule
];
const providers = [
  { provide: ActivatedRoute, useClass: MockActivatedRoute },
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
  { provide: 'JSNLOG', useValue: JL },
  { provide: LoggerService, useClass: MockLoggerService },
  { provide: PageService, useClass: MockPageService },
  { provide: PrintService, useClass: MockPrintService },
  AppInitService,
  NmMessageService,
  SessionStoreService,
  CustomHttpClient,
  LoaderService,
  ConfigService
];
let fixture, hostComponent;
describe('PageContent', () => {
  configureTestSuite(() => {
    setup(declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PageContent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    logger = TestBed.get(LoggerService);
    pageService = TestBed.get(PageService);
    printService = TestBed.get(PrintService);
  });

  it('should create the Header', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  // it('ngOnInit() should update the tilesList[]',  async(() => {
  //   spyOn(logger, 'debug').and.callThrough();
  //   const spy = spyOn((hostComponent as any), 'loadLabelConfig').and.callThrough();
  //   hostComponent.ngOnInit();
  //   expect(logger.debug).toHaveBeenCalled();
  //   expect(spy).toHaveBeenCalled();
  // }));

  // it('ngAfterViewInit() should update the errMsgArray[]',  async(() => {
  //   hostComponent.ngAfterViewInit();
  //   pageService.logError({message: 'test'});
  //   expect(hostComponent.errMsgArray).toEqual([{severity: 'error', summary: 'Error Message', detail: 'test', life: 10000}]);
  // }));
});
