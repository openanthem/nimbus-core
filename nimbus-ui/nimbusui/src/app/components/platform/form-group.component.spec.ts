'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, 
    DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule,
    InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';

import { FrmGroupCmp } from './form-group.component';
import { FormElement } from './form-element.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Header } from '../platform/content/header.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { Section } from '../platform/section.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { MessageComponent } from '../platform/message/message.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { Accordion } from '../platform/content/accordion.component';
import { Menu } from '../platform/menu.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { StaticText } from '../platform/content/static-content.component';
import { Form } from '../platform/form.component';
import { Link } from '../platform/link.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { WebContentSvc } from '../../services/content-management.service';
import { Signature } from '../platform/form/elements/signature.component'
import { DataTable } from './grid/table.component';
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
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service'
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { LoggerService } from '../../services/logger.service';
import { By } from '@angular/platform-browser';
import { formGroupNmElementInputParam, formGroupNmButtonParam, formGroupnmParagraphParam, formGroupParam, formGroupNmLinkParam, formGroupNmHeaderParam, formGroupNmPickListParam, formGroupNmFormGridFiller, formGroupNestedFrmGrpEle} from 'mockdata';
import { TableHeader } from './grid/table-header.component';

let param: Param;

class MockWebContentSvc {
    findLabelContent(param) {
        const test = {
            text: 'tText',
            helpText: 'tHelpText'
        }
        return test;
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
  FrmGroupCmp,
  FormElement,
  Button,
  ButtonGroup,
  Header,
  Paragraph,
  InputText,
  TextArea,
  Calendar,
  ComboBox,
  RadioButton,
  CheckBoxGroup,
  CheckBox,
  MultiSelectListBox,
  MultiselectCard,
  OrderablePickList,
  FileUploadComponent,
  TooltipComponent,
  SelectItemPipe,
  ActionDropdown,
  DateTimeFormatPipe,
  Section,
  ActionLink,
  MessageComponent,
  CardDetailsGrid,
  Accordion,
  Menu,
  CardDetailsComponent,
  StaticText,
  Form,
  Link,
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  Signature,
  DataTable,
  TableHeader,
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
     CalendarModule,
     DropdownModule,
     RadioButtonModule,
     CheckboxModule,
     ListboxModule,
     PickListModule,
     FileUploadModule,
     DataTableModule,
     GrowlModule,
     AccordionModule,
     HttpModule,
     HttpClientModule,
     TableModule,
     KeyFilterModule,
     AngularSvgIconModule,
     ToastModule,
     InputSwitchModule, 
     TreeTableModule,
     StorageServiceModule
 ];
 const providers = [
     { provide: WebContentSvc, useClass: MockWebContentSvc },
     { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
     {provide: LoggerService, useClass: MockLoggerService},
     { provide: LocationStrategy, useClass: HashLocationStrategy },
     Location,
     SessionStoreService,
     PageService,
     CustomHttpClient,
     LoaderService,
     ConfigService
 ];

 let fixture, hostComponent;
 
describe('FrmGroupCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach( () => {
    fixture = TestBed.createComponent(FrmGroupCmp);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = formGroupNmElementInputParam as Param;
    hostComponent.form = hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl(),
      question13: new FormControl(),
      question14: new FormControl(),
      question15: new FormControl()
    });
  });

  it('should create the FrmGroupCmp', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('getCssClass() should return the element.config.uiStyles.attributes.cssClass', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test';
      expect(hostComponent.getCssClass()).toEqual('test');
    });
  });

  it('getCssClass() should return elementCss', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test1';
      expect(hostComponent.getCssClass()).toEqual('test1');
    });
  });

  it('getCssClass() should return empty string', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup1';
      expect(hostComponent.getCssClass()).toEqual('');
    });
  });

  it('All elements in form group should be created if the element.visible==true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle).toBeTruthy();
  }));

  it('Span should get the css class from getCssClass()', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle.nativeElement.classList[0]).toEqual('testing-span-cssClass');
  }));

  it('nm-input-legend inside should be created', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLegendEle = debugElement.query(By.css('nm-input-legend'));
    expect(inputLegendEle).toBeTruthy();
  }));

  it('fieldset should be created if @FormElementGroup is configured', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const fieldSetEle = debugElement.query(By.css('fieldset'));
    expect(fieldSetEle).toBeTruthy();
  }));

  it('fieldset should not be created if @FormElementGroup is not configured', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const fieldSetEle = debugElement.query(By.css('fieldset'));
    expect(fieldSetEle).toBeFalsy();
  }));

  it('Nested nm-frm-grp should be created based on the element.type.model.params', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeTruthy();
  }));

  it('Nested nm-frm-grp should not be created based on the element.type.model.params', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    hostComponent.element.type.model.params = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeFalsy();
  }));

  it('nm-element should be created based on the nestedparams length', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
    });
    hostComponent.element = formGroupNmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
    expect(nmElementEle).toBeTruthy();
  }));

  it('nm-element should be created based on the collection', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
    });
    formGroupNmElementInputParam.type['model'] = { params: [1] }
    formGroupNmElementInputParam.config.type.collection = true;
    hostComponent.element = formGroupNmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
    expect(nmElementEle).toBeTruthy();
  }));

  it('nm-element should not be created based on the collection or with no nestedparams', async(() => {
    formGroupNmElementInputParam.config.type.collection = false;
    hostComponent.element = formGroupNmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeFalsy();
    expect(nmElementEle).toBeFalsy();
  }));

  it('nm-element should be created if @Picklist is configured', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl()
    });
    hostComponent.element = formGroupNmPickListParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.queryAll(By.css('nm-element'));
    const picklictEle = debugElement.query(By.css('nm-pickList'));
    expect(nmElementEle).toBeTruthy();
    expect(picklictEle).toBeTruthy();
  }));

  it('nm-element should not be created if @Picklist is not configured', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl()
    });
    formGroupNmPickListParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupNmPickListParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const picklictEle = debugElement.query(By.css('nm-pickList'));
    expect(picklictEle).toBeFalsy();
  }));

  it('nm-button should be created if @Button is configured', async(() => {
    hostComponent.element = formGroupNmButtonParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmBtnEle = debugElement.query(By.css('nm-button'));
    expect(nmBtnEle).toBeTruthy();
  }));

  it('nm-button should not be created if @Button is not configured', async(() => {
    formGroupNmButtonParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupNmButtonParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmBtnEle = debugElement.query(By.css('nm-button'));
    expect(nmBtnEle).toBeFalsy();
  }));

  it('nm-form-grid-filler should be created if @FormGridFiller is configured', async(() => {
    hostComponent.element = formGroupNmFormGridFiller as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGridFillerEle = debugElement.query(By.css('nm-form-grid-filler'));
    expect(frmGridFillerEle).toBeTruthy();
  }));

  it('nm-form-grid-filler should not be created if @FormGridFiller is not configured', async(() => {
    formGroupNmFormGridFiller.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupNmFormGridFiller as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGridFillerEle = debugElement.query(By.css('nm-form-grid-filler'));
    expect(frmGridFillerEle).toBeFalsy();
  }));

  it('nm-link should be created if @Link is configured', async(() => {
    hostComponent.element = formGroupNmLinkParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeTruthy()
  }));

  it('nm-link should not be created if @Link is not configured', async(() => {
    hostComponent.element = formGroupNmLinkParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = ''
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy()
  }));

  it('nm-paragraph should be created if @Paragraph is configured', async(() => {
    hostComponent.element = formGroupnmParagraphParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeTruthy();
  }));

  it('nm-paragraph should not be created if @Paragraph is not configured', async(() => {
    formGroupnmParagraphParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = formGroupnmParagraphParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeFalsy();
  }));

  it('nm-header should be created if @Header is configured', async(() => {
    hostComponent.element = formGroupNmHeaderParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const headerEle = debugElement.query(By.css('nm-header'));
    expect(headerEle).toBeTruthy();
  }));

  it('nm-header should not be created if @Header is not configured', async(() => {
    hostComponent.element = formGroupNmHeaderParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const headerEle = debugElement.query(By.css('nm-header'));
    expect(headerEle).toBeFalsy();
  }));

  it('nm-button-group should be created if @ButtonGroup is configured', async(() => {
    hostComponent.element = formGroupParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const btnGrpEle = debugElement.query(By.css('nm-button-group'));
    expect(btnGrpEle).toBeTruthy()
  }));

  it('nm-button-group should not be created if @ButtonGroup is not configured', async(() => {
    hostComponent.element = formGroupParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const btnGrpEle = debugElement.query(By.css('nm-button-group'));
    expect(btnGrpEle).toBeFalsy()
  }));

  it('All elements in form group should be hidden if the element.visible!==false', async(() => {
    hostComponent.element = formGroupNestedFrmGrpEle as Param;
    hostComponent.element.visible = false;
    fixture.detectChanges();
    const spanEle: any = document.getElementsByClassName('testing-span-cssClass');
    expect(spanEle[0].attributes[2].name).toEqual('hidden');
  }));

});

