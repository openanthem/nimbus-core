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
// import { Button } from '../platform/form/elements/button.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Header } from '../platform/content/header.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { TextArea } from '../platform/form/elements/textarea.component';
// import { DateControl } from '../platform/form/elements/date.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
// import { InfiniteScrollGrid } from '../platform/grid/grid.component';
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
  // DateControl,
  Calendar,
  ComboBox,
  RadioButton,
  CheckBoxGroup,
  CheckBox,
  MultiSelectListBox,
  MultiselectCard,
  OrderablePickList,
  FileUploadComponent,
  // InfiniteScrollGrid,
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
    hostComponent.element = nmElementInputParam as Param;
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

  it('Span should be created if the element.visible==true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle).toBeTruthy();
  }));

  it('Span should get the css class from getCssClass()', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const spanEle = debugElement.query(By.css('span'));
    expect(spanEle.nativeElement.classList[0]).toEqual('testing-span-cssClass');
  }));

  it('nm-input-legend inside should be created', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLegendEle = debugElement.query(By.css('nm-input-legend'));
    expect(inputLegendEle).toBeTruthy();
  }));

  it('fieldset should be created if element?.config?.uiStyles?.attributes?.alias == FormElementGroup', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const fieldSetEle = debugElement.query(By.css('fieldset'));
    expect(fieldSetEle).toBeTruthy();
  }));

  it('fieldset should not be created if element?.config?.uiStyles?.attributes?.alias !== FormElementGroup', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const fieldSetEle = debugElement.query(By.css('fieldset'));
    expect(fieldSetEle).toBeFalsy();
  }));

  it('Nested nm-frm-grp should be created based on the element.type.model.params', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeTruthy();
  }));

  it('Nested nm-frm-grp should not be created based on the element.type.model.params', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
    hostComponent.element.type.model.params = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGrpEle = debugElement.query(By.css('nm-frm-grp'));
    expect(frmGrpEle).toBeFalsy();
  }));

  it('nm-element should be created based on the condition !element.type?.model?.params?.length', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
    });
    hostComponent.element = nmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
    expect(nmElementEle).toBeTruthy();
  }));

  it('nm-element should be created based on the condition element.config?.type?.collection', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
    });
    nmElementInputParam.type['model'] = { params: [1] }
    nmElementInputParam.config.type.collection = true;
    hostComponent.element = nmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeTruthy();
    expect(nmElementEle).toBeTruthy();
  }));

  it('nm-element should not be created based on the condition element.config?.type?.collection or !element.type?.model?.params?.length', async(() => {
    nmElementInputParam.config.type.collection = false;
    hostComponent.element = nmElementInputParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.query(By.css('nm-element'));
    const nmInputEle = debugElement.query(By.css('nm-input'));
    expect(nmInputEle).toBeFalsy();
    expect(nmElementEle).toBeFalsy();
  }));

  it('nm-element should be created if element?.config?.uiStyles?.attributes?.alias == Picklist', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl()
    });
    hostComponent.element = nmPickListParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmElementEle = debugElement.queryAll(By.css('nm-element'));
    const picklictEle = debugElement.query(By.css('nm-pickList'));
    expect(nmElementEle).toBeTruthy();
    expect(picklictEle).toBeTruthy();
  }));

  it('nm-element should not be created if element?.config?.uiStyles?.attributes?.alias !== Picklist', async(() => {
    hostComponent.form = new FormGroup({
      question123: new FormControl(),
      selected: new FormControl()
    });
    nmPickListParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = nmPickListParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const picklictEle = debugElement.query(By.css('nm-pickList'));
    expect(picklictEle).toBeFalsy();
  }));

  it('nm-button should be created if element.config?.uiStyles?.attributes?.alias == Button', async(() => {
    hostComponent.element = nmButtonParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmBtnEle = debugElement.query(By.css('nm-button'));
    expect(nmBtnEle).toBeTruthy();
  }));

  it('nm-button should not be created if element.config?.uiStyles?.attributes?.alias !== Button', async(() => {
    nmButtonParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = nmButtonParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const nmBtnEle = debugElement.query(By.css('nm-button'));
    expect(nmBtnEle).toBeFalsy();
  }));

  it('nm-form-grid-filler should be created if element?.config?.uiStyles?.attributes?.alias == FormGridFiller', async(() => {
    hostComponent.element = nmFormGridFiller as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGridFillerEle = debugElement.query(By.css('nm-form-grid-filler'));
    expect(frmGridFillerEle).toBeTruthy();
  }));

  it('nm-form-grid-filler should not be created if element?.config?.uiStyles?.attributes?.alias !== FormGridFiller', async(() => {
    nmFormGridFiller.config.uiStyles.attributes.alias = '';
    hostComponent.element = nmFormGridFiller as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const frmGridFillerEle = debugElement.query(By.css('nm-form-grid-filler'));
    expect(frmGridFillerEle).toBeFalsy();
  }));

  it('nm-link should be created if element.config?.uiStyles?.attributes?.alias == Link', async(() => {
    hostComponent.element = nmLinkParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeTruthy()
  }));

  it('nm-link should not be created if element.config?.uiStyles?.attributes?.alias !== Link', async(() => {
    hostComponent.element = nmLinkParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = ''
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const linkEle = debugElement.query(By.css('nm-link'));
    expect(linkEle).toBeFalsy()
  }));

  it('nm-paragraph should be created if element.config?.uiStyles?.attributes?.alias == Paragraph', async(() => {
    hostComponent.element = nmParagraphParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeTruthy();
  }));

  it('nm-paragraph should not be created if element.config?.uiStyles?.attributes?.alias !== Paragraph', async(() => {
    nmParagraphParam.config.uiStyles.attributes.alias = '';
    hostComponent.element = nmParagraphParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const paragraphEle = debugElement.query(By.css('nm-paragraph'));
    expect(paragraphEle).toBeFalsy();
  }));

  it('nm-header should be created if element.config?.uiStyles?.attributes?.alias == Header', async(() => {
    hostComponent.element = nmHeaderParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const headerEle = debugElement.query(By.css('nm-header'));
    expect(headerEle).toBeTruthy();
  }));

  it('nm-header should not be created if element.config?.uiStyles?.attributes?.alias !== Header', async(() => {
    hostComponent.element = nmHeaderParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const headerEle = debugElement.query(By.css('nm-header'));
    expect(headerEle).toBeFalsy();
  }));

  it('nm-button-group should be created if element.config?.uiStyles?.attributes?.alias == ButtonGroup', async(() => {
    hostComponent.element = nmButtonGroupParam as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const btnGrpEle = debugElement.query(By.css('nm-button-group'));
    expect(btnGrpEle).toBeTruthy()
  }));

  it('nm-button-group should not be created if element.config?.uiStyles?.attributes?.alias !== ButtonGroup', async(() => {
    hostComponent.element = nmButtonGroupParam as Param;
    hostComponent.element.config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const btnGrpEle = debugElement.query(By.css('nm-button-group'));
    expect(btnGrpEle).toBeFalsy()
  }));

  it('Span should be hidden if the element.visible!==false', async(() => {
    hostComponent.element = nestedFrmGrpEle as Param;
    hostComponent.element.visible = false;
    fixture.detectChanges();
    const spanEle: any = document.getElementsByClassName('testing-span-cssClass');
    expect(spanEle[0].attributes[2].name).toEqual('hidden');
  }));

});

const nmElementInputParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "57258",
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
              "cssClass": "testing-css-class",
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
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "57258",
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
};

const nmButtonParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "57265",
      "code": "showHistory",
      "validations": null,
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.Button",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "b": "$execute",
              "method": "GET",
              "formReset": true,
              "type": "button",
              "title": "",
              "url": "",
              "printPath": "",
              "cssClass": "text-sm-right",
              "payload": "",
              "alias": "Button",
              "style": "PLAIN",
              "imgSrc": "",
              "imgType": "FA"
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "string"
      },
      "uiNatures": [
          {
              "name": "ViewConfig.Hints",
              "attributes": {
                  "hidden": false,
                  "readOnly": false,
                  "submitButton": true,
                  "showName": true,
                  "pageSize": 25,
                  "browserBack": false,
                  "showAsLink": false,
                  "value": "Right"
              }
          }
      ]
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "57265",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/showHistory",
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
};

const nmParagraphParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "57261",
      "code": "headerCallSection",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.Paragraph",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "cssClass": "font-weight-bold",
              "alias": "Paragraph"
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
  "configId": "57261",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/headerCallSection",
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
};

const nmButtonGroupParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "58336",
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
              "cssClass": "123text-sm-center",
              "alias": "ButtonGroup"
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "VPOwnerInfo.VSPets.VBG1",
          "model": {
              "paramConfigIds": [
                  "58338",
                  "58339"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "58336",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/vbg1",
  "type": {
      "model": {
          "params": [
              {
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "58338",
                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/vbg1/cancel",
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
                  "configId": "58339",
                  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/vbg1/ok",
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
};

const nmLinkParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "59337",
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
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "59337",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/create",
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
};

const nmHeaderParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "60522",
      "code": "addOwnerHeader1",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.Header",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "size": "H3",
              "cssClass": "",
              "alias": "Header"
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
  "configId": "60522",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/addOwnerHeader1",
  "type": {
      "nested": false,
      "name": "string",
      "collection": false
  },
  "message": [],
  "values": [],
  "labels": [],
  "elemLabels": {}
};

const nmPickListParam: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "60775",
      "code": "category",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.PickList",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "showSourceControls": false,
              "help": "",
              "cssClass": "",
              "dataEntryField": true,
              "labelClass": "anthem-label",
              "alias": "PickList",
              "targetHeader": "Selected Category",
              "showTargetControls": false,
              "sourceHeader": "Available Category",
              "cols": ""
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "VPAddEditPet.PicklistType",
          "model": {
              "paramConfigIds": [
                  "60777"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "60775",
  "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category",
  "type": {
      "model": {
          "params": [
              {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "60777",
                    "code": "selected",
                    "uiNatures": [],
                    "uiStyles": {
                        "isLink": false,
                        "isHidden": false,
                        "name": "ViewConfig.PickListSelected",
                        "attributes": {
                            "hidden": false,
                            "readOnly": false,
                            "submitButton": true,
                            "showName": true,
                            "pageSize": 25,
                            "browserBack": false,
                            "showAsLink": false,
                            "alias": "PickListSelected",
                            "postEventOnChange": true
                        }
                    },
                    "type": {
                        "collection": false,
                        "nested": false,
                        "name": "array-string"
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
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "60777",
                  "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category/selected",
                  "type": {},
                  "message": [],
                  "values": [
                      {
                          "code": "Bobtail",
                          "label": "American Bobtail"
                      },
                      {
                          "code": "Curl",
                          "label": "American Curl"
                      },
                      {
                          "code": "White",
                          "label": "American SnowWhite"
                      },
                      {
                          "code": "Sporting",
                          "label": "Sporting Group"
                      },
                      {
                          "code": "Hound",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Service",
                          "label": "Service Group"
                      },
                      {
                          "code": "Hound2",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound3",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound4",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound5",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound6",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound7",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound8",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound9",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound10",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound11",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound12",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound13",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound14",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound15",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound16",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound17",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound18",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound19",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound20",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound21",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound22",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound23",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound24",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound25",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound26",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound27",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound28",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound29",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound30",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound31",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound32",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound33",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound34",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound35",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound36",
                          "label": "Hound Group"
                      },
                      {
                          "code": "Hound37",
                          "label": "Hound Group"
                      }
                  ],
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
          "text": "Category"
      }
  ],
  "elemLabels": {}
};

const nmFormGridFiller: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "61632",
      "code": "formgridfiller",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FormGridFiller",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "cssClass": "",
              "alias": "FormGridFiller",
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
  "configId": "61632",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/formgridfiller",
  "type": {
      "nested": false,
      "name": "string",
      "collection": false
  },
  "message": [],
  "values": [],
  "labels": [],
  "elemLabels": {}
};

const nestedFrmGrpEle: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "62036",
      "code": "section12",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FormElementGroup",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "cssClass": "testing-span-cssClass",
              "alias": "FormElementGroup",
              "cols": "1"
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "PetCareAssessment.Section12",
          "model": {
              "paramConfigIds": [
                  "62038",
                  "62039",
                  "62040",
                  "62041"
              ]
          }
      }
  },
  "enabled": false,
  "visible": false,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "62036",
  "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12",
  "type": {
      "model": {
          "params": [
              {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "62038",
                    "code": "question13",
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
                  "enabled": false,
                  "visible": false,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "62038",
                  "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/question13",
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
                          "text": "Question 13"
                      }
                  ],
                  "elemLabels": {}
              },
              {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "62039",
                    "code": "question14",
                    "uiNatures": [],
                    "uiStyles": {
                        "isLink": false,
                        "isHidden": false,
                        "name": "ViewConfig.Calendar",
                        "attributes": {
                            "hidden": false,
                            "readOnly": false,
                            "submitButton": true,
                            "showName": true,
                            "pageSize": 25,
                            "browserBack": false,
                            "showAsLink": false,
                            "readonlyInput": false,
                            "dataEntryField": true,
                            "labelClass": "anthem-label",
                            "yearRange": "1910:2050",
                            "showTime": false,
                            "controlId": "",
                            "yearNavigator": false,
                            "type": "calendar",
                            "monthNavigator": false,
                            "postEventOnChange": false,
                            "hourFormat": "12",
                            "help": "",
                            "timeOnly": false,
                            "cssClass": "",
                            "alias": "Calendar",
                            "cols": ""
                        }
                    },
                    "type": {
                        "collection": false,
                        "nested": false,
                        "name": "LocalDate"
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
                  "enabled": false,
                  "visible": false,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "62039",
                  "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/question14",
                  "type": {},
                  "leafState": null,
                  "message": [],
                  "values": [],
                  "labels": [
                      {
                          "locale": "en-US",
                          "text": "Calendar with date only"
                      }
                  ],
                  "elemLabels": {}
              },
              {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "62040",
                    "code": "question15",
                    "validations": null,
                    "uiNatures": [],
                    "uiStyles": {
                        "isLink": false,
                        "isHidden": false,
                        "name": "ViewConfig.Radio",
                        "attributes": {
                            "hidden": false,
                            "readOnly": false,
                            "submitButton": true,
                            "showName": true,
                            "pageSize": 25,
                            "browserBack": false,
                            "showAsLink": false,
                            "help": "",
                            "cssClass": "questionGroup form-inline",
                            "dataEntryField": true,
                            "labelClass": "anthem-label",
                            "level": "0",
                            "alias": "Radio",
                            "controlId": "",
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
                  "enabled": false,
                  "visible": false,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "62040",
                  "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/question15",
                  "type": {
                      "nested": false,
                      "name": "string",
                      "collection": false
                  },
                  "message": [],
                  "values": [
                      {
                          "code": "Yes",
                          "label": "Yes"
                      },
                      {
                          "code": "No",
                          "label": "No"
                      },
                      {
                          "code": "MayBe",
                          "label": "MayBe"
                      }
                  ],
                  "labels": [
                      {
                          "locale": "en-US",
                          "text": "Radio with inline syles"
                      }
                  ],
                  "elemLabels": {}
              },
              {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "62041",
                    "code": "section16",
                    "validations": null,
                    "uiNatures": [],
                    "uiStyles": {
                        "isLink": false,
                        "isHidden": false,
                        "name": "ViewConfig.FormElementGroup",
                        "attributes": {
                            "hidden": false,
                            "readOnly": false,
                            "submitButton": true,
                            "showName": true,
                            "pageSize": 25,
                            "browserBack": false,
                            "showAsLink": false,
                            "cssClass": "",
                            "alias": "FormElementGroup",
                            "cols": "1"
                        }
                    },
                    "type": {
                        "collection": false,
                        "nested": true,
                        "name": "PetCareAssessment.Section16",
                        "model": {
                            "paramConfigIds": [
                                "62043",
                                "62044",
                                "62045"
                            ]
                        }
                    }
                },
                  "enabled": false,
                  "visible": false,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "62041",
                  "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/section16",
                  "type": {
                      "model": {
                          "params": [

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
          "text": "Section 12"
      }
  ],
  "elemLabels": {}
};