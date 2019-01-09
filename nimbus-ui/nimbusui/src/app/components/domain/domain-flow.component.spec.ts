import { element } from 'protractor';
import { Message } from './../../shared/message';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing'
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Router, ActivatedRoute, Route, ActivatedRouteSnapshot, UrlSegment, Params, Data, ParamMap, PRIMARY_OUTLET } from '@angular/router';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
  FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
  ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { of as observableOf,  Observable } from 'rxjs';
import { Subject } from 'rxjs';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { FormsModule, ReactiveFormsModule, ValidatorFn, Validators, FormGroup, FormControl } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { DomainFlowCmp } from './domain-flow.component';
import { SubHeaderCmp } from '../platform/sub-header.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { CustomHttpClient } from '../../services/httpclient.service';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { BreadcrumbService } from '../platform/breadcrumb/breadcrumb.service';
import { LayoutService } from '../../services/layout.service';
import { LoggerService } from '../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { AppInitService } from '../../services/app.init.service'
import { ActionTray } from '../platform/actiontray.component';
// import { Button } from '../platform/form/elements/button.component';
import { SvgComponent } from '../platform/svg/svg.component';
// import { Accordion } from '../platform/content/accordion.component';
// import { Modal } from '../platform/modal/modal.component';
import { Image } from '../platform/image.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { FrmGroupCmp } from '../platform/form-group.component';
import { Label } from '../platform/content/label.component';
import { Section } from '../platform/section.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { MenuRouteLink } from '../../directives/routes/route-link.component';
import { Link } from '../platform/link.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { CardDetailsFieldGroupComponent } from '../platform/card/card-details-field-group.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { StaticText } from '../platform/content/static-content.component';
import { ActionDropdown, ActionLink } from '../platform/form/elements/action-dropdown.component';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { FormElement } from '../platform/form-element.component';
import { InputSwitch } from '../platform/form/elements/input-switch.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { MessageComponent } from '../platform/message/message.component';
import { Form } from '../platform/form.component';
import { Menu } from '../platform/menu.component';
import { TreeGrid } from '../platform/tree-grid/tree-grid.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { DisplayValueDirective } from '../../directives/display-value.directive';
import { FormGridFiller } from '../platform/form/form-grid-filler.component';
import { Header } from '../platform/content/header.component';
import { Signature } from '../platform/form/elements/signature.component';
// import { DateControl } from '../platform/form/elements/date.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { InputLabel } from '../platform/form/elements/input-label.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { InputLegend } from '../platform/form/elements/input-legend.component';
import { FormErrorMessage } from '../platform/form-error-message.component';
import { setup, TestContext } from '../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import { PrintDirective } from '../../directives/print.directive';
import { PrintService } from '../../services/print.service';

let layoutservice, pageservice, router, route;

@Component({
    template: '<div></div>',
    selector: 'nm-panelMenu'
})
export class NmPanelMenu {
    @Input() model: any[];
    @Input() style: any;
    @Input() styleClass: string;
    @Input() multiple: boolean = true;  
}

@Component({
  template: '<div></div>',
  selector: 'nm-modal'
})
export class Modal {
  @Input() element: any;
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

@Component({
    template: '<div></div>',
    selector: 'nm-breadcrumb'
})
export class BreadcrumbComponent {
}

@Component({
    template: '<div></div>',
    selector: 'nm-panelMenuSub'
})
export class NmPanelMenuSub {
    @Input() item: any;
    @Input() expanded: boolean;
}

@Component({
  template: '<div></div>',
  selector: 'nm-accordion'
})
export class Accordion {
  @Input() form: FormGroup;
  @Input() elementCss: string;
  @Input() element: any;
  @Input() position: any;
  componentTypes: any;
  viewComponent: any;
  _multiple: boolean;
  index: number[]; 
  @ViewChild('accordion') accordion: any;
}


@Component({
    template: '<div></div>',
    selector: 'nm-table'
})
export class DataTable {
    @Output() onScrollEvent: any = new EventEmitter();
    @Input() params: any;
    @Input() form: any;
    @Input() _value = [];
    @Input() position: any;
    @Input() element: any;
    @ViewChild('dt') dt: any;
    @ViewChild('op') overlayPanel: any;
    @ViewChildren('dropDown') dropDowns: any;
}

class MockLayoutService {
  public layout$: Subject<any>;

  constructor() {
    this.layout$ = new Subject();
  }

  getLayout(a) {    }
  parseLayoutConfig(result) {
    this.layout$.next(result);
  }
}

class MockPageService {
  public config$: Subject<any>;
  public subdomainconfig$: Subject<any>;
  public messageEvent$: Subject<Message[]>
  
  constructor() {
    this.config$ = new Subject();
    this.subdomainconfig$ = new Subject();
    this.messageEvent$ = new Subject();
  }

  logError(res) {
    this.config$.next(res);
  }

}

const mockLayout: any = {
  model: {
    "params": [
        {
            "config": {
                "active": false,
                "required": false,
                "id": "1420",
                "code": "vpHome",
                "validations": null,
                "uiNatures": [],
                "uiStyles": {
                    "isLink": false,
                    "isHidden": false,
                    "name": "ViewConfig.Page",
                    "attributes": {
                        "hidden": false,
                        "readOnly": false,
                        "submitButton": true,
                        "showName": true,
                        "pageSize": 25,
                        "browserBack": false,
                        "showAsLink": false,
                        "defaultPage": false,
                        "route": "",
                        "cssClass": "",
                        "fixLayout": false,
                        "alias": "Page",
                        "imgSrc": ""
                    }
                },
                "type": {
                    "collection": false,
                    "nested": true,
                    "name": "VLHome.VPHome",
                    "model": {
                        "paramConfigIds": [
                            "1422",
                            "1426"
                        ]
                    }
                }
            },
            "alias": "Page",
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "1420",
            "path": "/home/vpHome",
            "type": {
                "model": {
                    "params": [
                        {
                          "config": {
                            "active": false,
                            "required": false,
                            "id": "1422",
                            "code": "vsHomeHeader",
                            "validations": null,
                            "uiNatures": [],
                            "initializeComponent": () => {},
                            "uiStyles": {
                                "isLink": false,
                                "isHidden": false,
                                "name": "ViewConfig.Section",
                                "attributes": {
                                    "hidden": false,
                                    "readOnly": false,
                                    "submitButton": true,
                                    "showName": true,
                                    "pageSize": 25,
                                    "browserBack": false,
                                    "showAsLink": false,
                                    "cssClass": "",
                                    "alias": "Section",
                                    "value": "HEADER",
                                    "imgSrc": "",
                                    "imgType": "FA"
                                }
                            },
                            "type": {
                                "collection": false,
                                "nested": true,
                                "name": "VSHomeHeader",
                                "model": {
                                    "paramConfigIds": [
                                        "1424",
                                        "1425"
                                    ]
                                }
                            }
                        },
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "1422",
                            "path": "/home/vpHome/vsHomeHeader",
                            "type": {
                                "model": {
                                    "params": [
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1424",
                                              "code": "linkHomeLogo",
                                              "validations": null,
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
                                                      "imgSrc": "/images/anthem.png",
                                                      "url": "",
                                                      "target": ""
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
                                                          "value": "Left"
                                                      }
                                                  },
                                                  {
                                                      "name": "ViewConfig.PageHeader",
                                                      "attributes": {
                                                          "hidden": false,
                                                          "readOnly": false,
                                                          "submitButton": true,
                                                          "showName": true,
                                                          "pageSize": 25,
                                                          "browserBack": false,
                                                          "showAsLink": false,
                                                          "value": "LOGO"
                                                      }
                                                  }
                                              ]
                                          },
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "1424",
                                            "path": "/home/vpHome/vsHomeHeader/linkHomeLogo",
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
                                                    "text": "Anthem"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1425",
                                              "code": "title",
                                              "validations": null,
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
                                                      "cssClass": "",
                                                      "alias": "Paragraph"
                                                  }
                                              },
                                              "type": {
                                                  "collection": false,
                                                  "nested": false,
                                                  "name": "string"
                                              },
                                              "uiNatures": [
                                                  {
                                                      "name": "ViewConfig.PageHeader",
                                                      "attributes": {
                                                          "hidden": false,
                                                          "readOnly": false,
                                                          "submitButton": true,
                                                          "showName": true,
                                                          "pageSize": 25,
                                                          "browserBack": false,
                                                          "showAsLink": false,
                                                          "value": "APPTITLE"
                                                      }
                                                  }
                                              ]
                                          },
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "1425",
                                            "path": "/home/vpHome/vsHomeHeader/title",
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
                                                    "text": "Pet Clinic"
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
                            "id": "1426",
                            "code": "vsHomeLeftBar",
                            "validations": null,
                            "uiNatures": [],
                            "uiStyles": {
                                "isLink": false,
                                "isHidden": false,
                                "name": "ViewConfig.MenuPanel",
                                "attributes": {
                                    "hidden": false,
                                    "readOnly": false,
                                    "submitButton": true,
                                    "showName": true,
                                    "pageSize": 25,
                                    "browserBack": false,
                                    "showAsLink": false,
                                    "cssClass": "",
                                    "rel": "",
                                    "alias": "MenuPanel",
                                    "type": "INTERNAL",
                                    "imgSrc": "",
                                    "url": "",
                                    "target": "",
                                    "imgType": "FA"
                                }
                            },
                            "type": {
                                "collection": false,
                                "nested": true,
                                "name": "VSHomeLeftBar",
                                "model": {
                                    "paramConfigIds": [
                                        "1428",
                                        "1429",
                                        "1433",
                                        "1434",
                                        "1435",
                                        "1436",
                                        "1437"
                                    ]
                                }
                            }
                        },
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "1426",
                            "path": "/home/vpHome/vsHomeLeftBar",
                            "type": {
                                "model": {
                                    "params": [
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1428",
                                              "code": "home",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuLink",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuLink",
                                                      "page": "",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "notesIcon",
                                                      "url": "petclinicdashboard/vpDashboard",
                                                      "target": "",
                                                      "imgType": "FA"
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
                                            "configId": "1428",
                                            "path": "/home/vpHome/vsHomeLeftBar/home",
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
                                                    "text": "Home"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1429",
                                              "code": "vmpVisits",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuPanel",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuPanel",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "notesIcon",
                                                      "url": "",
                                                      "target": "",
                                                      "imgType": "SVG"
                                                  }
                                              },
                                              "type": {
                                                  "collection": false,
                                                  "nested": true,
                                                  "name": "VSHomeLeftBar.VMPVisits",
                                                  "model": {
                                                      "paramConfigIds": [
                                                          "1431",
                                                          "1432"
                                                      ]
                                                  }
                                              }
                                          },
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "1429",
                                            "path": "/home/vpHome/vsHomeLeftBar/vmpVisits",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "config": {
                                                              "active": false,
                                                              "required": false,
                                                              "id": "1431",
                                                              "code": "visits",
                                                              "validations": null,
                                                              "uiNatures": [],
                                                              "uiStyles": {
                                                                  "isLink": false,
                                                                  "isHidden": false,
                                                                  "name": "ViewConfig.MenuLink",
                                                                  "attributes": {
                                                                      "hidden": false,
                                                                      "readOnly": false,
                                                                      "submitButton": true,
                                                                      "showName": true,
                                                                      "pageSize": 25,
                                                                      "browserBack": false,
                                                                      "showAsLink": false,
                                                                      "cssClass": "",
                                                                      "rel": "",
                                                                      "alias": "MenuLink",
                                                                      "page": "",
                                                                      "type": "INTERNAL",
                                                                      "imgSrc": "notesIcon",
                                                                      "url": "visitlandingview/vpVisits",
                                                                      "target": "",
                                                                      "imgType": "FA"
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
                                                            "configId": "1431",
                                                            "path": "/home/vpHome/vsHomeLeftBar/vmpVisits/visits",
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
                                                                    "text": "Visits"
                                                                }
                                                            ],
                                                            "elemLabels": {}
                                                        },
                                                        {
                                                            "config": {
                                                              "active": false,
                                                              "required": false,
                                                              "id": "1432",
                                                              "code": "visitsBulkAction",
                                                              "validations": null,
                                                              "uiNatures": [],
                                                              "uiStyles": {
                                                                  "isLink": false,
                                                                  "isHidden": false,
                                                                  "name": "ViewConfig.MenuLink",
                                                                  "attributes": {
                                                                      "hidden": false,
                                                                      "readOnly": false,
                                                                      "submitButton": true,
                                                                      "showName": true,
                                                                      "pageSize": 25,
                                                                      "browserBack": false,
                                                                      "showAsLink": false,
                                                                      "cssClass": "",
                                                                      "rel": "",
                                                                      "alias": "MenuLink",
                                                                      "page": "",
                                                                      "type": "INTERNAL",
                                                                      "imgSrc": "notesIcon",
                                                                      "url": "visitlandingview/vpVisitsBulkAction",
                                                                      "target": "",
                                                                      "imgType": "FA"
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
                                                            "configId": "1432",
                                                            "path": "/home/vpHome/vsHomeLeftBar/vmpVisits/visitsBulkAction",
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
                                                                    "text": "Visits - Bulk Action"
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
                                                    "text": "Visits"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1433",
                                              "code": "vets",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuLink",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuLink",
                                                      "page": "",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "notesIcon",
                                                      "url": "veterinarianview/vpVeterenarians",
                                                      "target": "",
                                                      "imgType": "FA"
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
                                            "configId": "1433",
                                            "path": "/home/vpHome/vsHomeLeftBar/vets",
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
                                                    "text": "Veterinarians"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1434",
                                              "code": "owners",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuLink",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuLink",
                                                      "page": "",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "notesIcon",
                                                      "url": "ownerlandingview/vpOwners",
                                                      "target": "",
                                                      "imgType": "FA"
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
                                            "configId": "1434",
                                            "path": "/home/vpHome/vsHomeLeftBar/owners",
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
                                                    "text": "Owners"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1435",
                                              "code": "pets",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuLink",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuLink",
                                                      "page": "",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "notesIcon",
                                                      "url": "petclinicdashboard/vpAllPets",
                                                      "target": "",
                                                      "imgType": "FA"
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
                                            "configId": "1435",
                                            "path": "/home/vpHome/vsHomeLeftBar/pets",
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
                                                    "text": "Pets"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1436",
                                              "code": "notes",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuLink",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuLink",
                                                      "page": "",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "notesIcon",
                                                      "url": "petclinicdashboard/vpNotes",
                                                      "target": "",
                                                      "imgType": "FA"
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
                                            "configId": "1436",
                                            "path": "/home/vpHome/vsHomeLeftBar/notes",
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
                                                    "text": "Notes"
                                                }
                                            ],
                                            "elemLabels": {}
                                        },
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "1437",
                                              "code": "treegrid",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.MenuLink",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "cssClass": "",
                                                      "rel": "",
                                                      "alias": "MenuLink",
                                                      "page": "",
                                                      "type": "INTERNAL",
                                                      "imgSrc": "",
                                                      "url": "petclinicdashboard/vpTreegridDemo",
                                                      "target": "",
                                                      "imgType": "FA"
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
                                            "configId": "1437",
                                            "path": "/home/vpHome/vsHomeLeftBar/treegrid",
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
                                                    "text": "Treegrid Demo"
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
};

export class MockActivatedRoute implements ActivatedRoute {
    snapshot: ActivatedRouteSnapshot;
    url: Observable<UrlSegment[]>;
    params: Observable<Params>;
    queryParams: Observable<Params>;
    fragment: Observable<string>;
    outlet: string;
    component: any;
    routeConfig: Route;
    root: any = {
        children: [{
            outlet: PRIMARY_OUTLET,
            component: 'test',
            children: [],
            snapshot: {
                params: {
                    pageId: 1
                }
            }
        }]
    };
    parent: ActivatedRoute;
    firstChild: ActivatedRoute;
    children: ActivatedRoute[];
    pathFromRoot: ActivatedRoute[];
    data = observableOf({
            layout: mockLayout
      });
    paramMap: Observable<ParamMap>;
    queryParamMap: Observable<ParamMap>;
  }

  class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

  export class MockActivatedRoute1 implements ActivatedRoute {
    snapshot: ActivatedRouteSnapshot;
    url: Observable<UrlSegment[]>;
    params: Observable<Params>;
    queryParams: Observable<Params>;
    fragment: Observable<string>;
    outlet: string;
    component: any;
    routeConfig: Route;
    root: any = {
        children: [{
            outlet: PRIMARY_OUTLET,
            component: 'test',
            children: [],
            snapshot: {
                params: {
                    pageId: 1
                }
            }
        }]
    };
    parent: ActivatedRoute;
    firstChild: ActivatedRoute;
    children: ActivatedRoute[];
    pathFromRoot: ActivatedRoute[];
    data = observableOf({
      });
    paramMap: Observable<ParamMap>;
    queryParamMap: Observable<ParamMap>;
  }

  class MockRouter {
    events = {
      pipe: () => {},
      filter: () => {
        return observableOf({         })
      }
    };
    navigate() {    }
  }

  const declarations = [
    DomainFlowCmp,
    BreadcrumbComponent,
    SubHeaderCmp,
    DateTimeFormatPipe,
    ActionTray,
    Button,
    SvgComponent,
    Accordion,
    Modal,
    NmPanelMenu,
    Image,
    CardDetailsGrid,
    CardDetailsComponent,
    DataTable,
    ButtonGroup,
    FrmGroupCmp,
    Label,
    Section,
    TooltipComponent,
    NmPanelMenuSub,
    MenuRouteLink,
    Link,
    CardDetailsFieldComponent,
    CardDetailsFieldGroupComponent,
    Paragraph,
    StaticText,
    ActionDropdown,
    HeaderCheckBox,
    FormElement,
    InputSwitch,
    ComboBox,
    MessageComponent,
    Form,
    Menu,
    TreeGrid,
    InputText,
    InPlaceEditorComponent,
    TextArea,
    DisplayValueDirective,
    ActionLink,
    FormGridFiller,
    Header,
    Signature,
    // DateControl,
    Calendar,
    RadioButton,
    CheckBoxGroup,
    CheckBox,
    MultiSelectListBox,
    MultiselectCard,
    FileUploadComponent,
    OrderablePickList,
    InputLabel,
    SelectItemPipe,
    InputLegend,
    FormErrorMessage,
    PrintDirective
 ];
 const imports =  [
     RouterTestingModule,
     HttpClientModule,
     HttpModule,
     StorageServiceModule,
     AngularSvgIconModule,
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
     InputSwitchModule, 
     TreeTableModule,
     TableModule,
     KeyFilterModule,
     FormsModule,
     ReactiveFormsModule,
     ToastModule,
     BrowserAnimationsModule
 ];
 const providers = [
     {provide: LayoutService, useClass: MockLayoutService},
     {provide: ActivatedRoute, useClass: MockActivatedRoute},
     {provide: PageService, useClass: MockPageService},
     {provide: Router, useClass: MockRouter},
     { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
     { provide: 'JSNLOG', useValue: JL },
     {provide: LoggerService, useClass: MockLoggerService},
     CustomHttpClient,
     WebContentSvc,
     LoaderService,
     ConfigService,
     BreadcrumbService,
     SessionStoreService,
     AppInitService,
     PrintService
  ];

let fixture, hostComponent;

describe('DomainFlowCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(DomainFlowCmp);
    hostComponent = fixture.debugElement.componentInstance;
    layoutservice = TestBed.get(LayoutService);
    pageservice = TestBed.get(PageService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
    hostComponent.items = items;
    const document = {
        "getElementById": () => {
            return {"classList": {"remove": () => {}, "add": () => {}}, "scrollTop": 11, "setAttribute": () => {}, "style": {"height": ''}};
        },
        "body": {
            "classList": {
                "remove": () => {},
                "add": () => {}
            }
        }
    };
    spyOn(hostComponent, 'getDocument').and.returnValue(document);
  });

  it('should create the app', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('accordion, button, breadcrump, panelmenu, actiontray and modal should be created', () => {
    hostComponent.actionTray = actionTray;
    hostComponent.modalItems = modalItems;
    hostComponent.accordions = accordions;
    fixture.detectChanges();
    hostComponent.actionTray = actionTray;
    hostComponent.modalItems = modalItems;
    hostComponent.accordions = accordions;
    hostComponent.hasLayout = true;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const breadcrumb  = debugElement.query(By.css('nm-breadcrumb'));
    const accordionEle  = debugElement.query(By.css('nm-accordion'));
    const button  = debugElement.query(By.css('button'));
    const panelMenu = debugElement.query(By.css('nm-panelMenu'));
    const actiontray = debugElement.query(By.css('nm-actiontray'));
    const modal = debugElement.query(By.css('nm-modal'));
    expect(breadcrumb.name).toEqual('nm-breadcrumb');
    expect(button.name).toEqual('button');
    expect(panelMenu.name).toEqual('nm-panelMenu');
    expect(actiontray.name).toEqual('nm-actiontray');    
    expect(accordionEle.name).toEqual('nm-accordion');
    expect(modal.name).toEqual('nm-modal');
  });

  it('accordion should not be created', async(() => {
    hostComponent.accordions = null;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const accordion  = debugElement.query(By.css('nm-accordion'));
    expect(accordion).toBeFalsy();
    }));

  it('modal should not be created', async(() => {
    hostComponent.modalItems = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const modal = debugElement.query(By.css('nm-modal'));
    expect(modal).toBeFalsy();  
  }));

  it('action tray should not be created', async(() => {
    hostComponent.actionTray = null;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const actiontray = debugElement.query(By.css('nm-actiontray'));
    expect(actiontray).toBeFalsy();  
    }));

  it('ngOnInit() should not update main-content', async(() => {
    spyOn(document, 'getElementById').and.callThrough();
    const res = { topBar: { headerMenus: 'theaderMenus' } };
    hostComponent.ngOnInit();
    layoutservice.parseLayoutConfig(res);
    expect(document.getElementById).not.toHaveBeenCalled();
  }));

  it('ngOnInit() should call router.navigate',  async(() => {
    const res = { pageConfig: { config: { code: 321 } } };
    spyOn(router, 'navigate').and.callThrough();
    hostComponent.ngOnInit();
    pageservice.logError(res);
    expect(router.navigate).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call router.navigate',  async(() => {
    const res = {};
    spyOn(router, 'navigate').and.callThrough();
    hostComponent.ngOnInit();
    pageservice.logError(res);
    expect(router.navigate).not.toHaveBeenCalled();
  }));

});

const secondProviders = [
    {provide: LayoutService, useClass: MockLayoutService},
    {provide: ActivatedRoute, useClass: MockActivatedRoute1},
    {provide: PageService, useClass: MockPageService},
    {provide: Router, useClass: MockRouter},
    { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
    { provide: 'JSNLOG', useValue: JL },
    {provide: LoggerService, useClass: MockLoggerService},
    CustomHttpClient,
    WebContentSvc,
    LoaderService,
    ConfigService,
    BreadcrumbService,
    SessionStoreService,
    AppInitService
 ];

describe('DomainFlowCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, secondProviders);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(DomainFlowCmp);
    hostComponent = fixture.debugElement.componentInstance;
    layoutservice = TestBed.get(LayoutService);
    pageservice = TestBed.get(PageService);
    router = TestBed.get(Router);
    route = TestBed.get(ActivatedRoute);
    hostComponent.accordions = accordions;
  });

  it('ngOnInit should not call layoutservice.getLayout()',  async(() => {
    spyOn(hostComponent, 'setLayoutScroll').and.returnValue('');
    spyOn(layoutservice, 'getLayout').and.callThrough();
    spyOn(document, 'getElementById').and.returnValue({
      classList: {
        remove: () => {},
        add: () => {}
      }
    });
    hostComponent.ngOnInit();
    expect(layoutservice.getLayout).not.toHaveBeenCalled();
  }));

});

const accordions = [
  {
      "enabled": true,
      "visible": false,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "14455",
      "path": "/home/vpHome/vmAddNote",
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
];



const items = [
  {
      "label": "Home",
      "path": "/home/vpHome/vsHomeLeftBar/home",
      "page": "",
      "icon": "tasksIcon",
      "imgType": "FA",
      "url": "petclinicdashboard/vpDashboard",
      "type": "INTERNAL",
      "target": "",
      "rel": "",
      "routerLink": "/h/petclinicdashboard/vpDashboard",
      "code": "home",
      "expanded": false
  },
  {
      "label": "Veterinarians",
      "path": "/home/vpHome/vsHomeLeftBar/vets",
      "page": "",
      "icon": "caseHistoryIcon",
      "imgType": "FA",
      "url": "veterinarianview/vpVeterenarians",
      "type": "INTERNAL",
      "target": "",
      "rel": "",
      "routerLink": "/h/veterinarianview/vpVeterenarians",
      "code": "vets",
      "expanded": false
  },
  {
      "label": "Owners",
      "path": "/home/vpHome/vsHomeLeftBar/owners",
      "page": "",
      "icon": "caseHistoryIcon",
      "imgType": "FA",
      "url": "ownerlandingview/vpOwners",
      "type": "INTERNAL",
      "target": "",
      "rel": "",
      "routerLink": "/h/ownerlandingview/vpOwners",
      "code": "owners"
  },
  {
      "label": "Pets",
      "path": "/home/vpHome/vsHomeLeftBar/pets",
      "page": "",
      "icon": "caseHistoryIcon",
      "imgType": "FA",
      "url": "petview/vpAllPets",
      "type": "INTERNAL",
      "target": "",
      "rel": "",
      "routerLink": "/h/petview/vpAllPets",
      "code": "pets",
      "expanded": false
  },
  {
      "label": "Notes",
      "path": "/home/vpHome/vsHomeLeftBar/notes",
      "page": "",
      "icon": "notesIcon",
      "imgType": "FA",
      "url": "petclinicdashboard/vpNotes",
      "type": "INTERNAL",
      "target": "",
      "rel": "",
      "routerLink": "/h/petclinicdashboard/vpNotes",
      "code": "notes",
      "expanded": false
  }
];

const actionTray = {
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "14452",
  "path": "/home/vpHome/vsActionTray",
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
};

const modalItems = [
  {
      "enabled": true,
      "visible": false,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "14455",
      "path": "/home/vpHome/vmAddNote",
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
];


