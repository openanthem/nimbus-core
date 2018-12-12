'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { By } from '@angular/platform-browser';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { PageService } from './../../../services/page.service';
import { CardDetailsFieldComponent } from './card-details-field.component';
// import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Values, Param } from '../../../shared/param-state';
import { SessionStoreService, CUSTOM_STORAGE } from './../../../services/session.store';
import { LoaderService } from './../../../services/loader.service';
import { ConfigService } from './../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';

@Component({
  template: '<div></div>',
  selector: 'inplace-editor'
})
class InPlaceEditorComponent {
  public editClass: string;
  public label: string;
  public UNASSIGNVALUE = 'Unassigned';
  public displayValue = '';
  private _value = '';
  private preValue = '';

  private componentRef: any;
  @ViewChild('container')
  private container: any
  private inputInstance: any;
  @Input() element: any;
}

const declarations = [
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  DateTimeFormatPipe,
  TooltipComponent,
  SelectItemPipe,
  DisplayValueDirective,
  InputLabel
];
const imports = [FormsModule, DropdownModule, HttpClientModule, HttpModule, StorageServiceModule];
const providers = [
  { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE }, 
  { provide: 'JSNLOG', useValue: JL },
  { provide: LocationStrategy, useClass: HashLocationStrategy },
  Location,
  SessionStoreService, 
  CustomHttpClient, 
  PageService,
  LoaderService, 
  ConfigService, 
  LoggerService,
  AppInitService
];
let fixture, hostComponent;

describe('CardDetailsFieldComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsFieldComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
  });

  it('should create the CardDetailsFieldComponent',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

    it('inplaceEditor should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inplaceEditorEle = debugElement.query(By.css('inplace-editor'));
    expect(inplaceEditorEle.name).toEqual('inplace-editor');
  }));

  it('inplaceEditor should not be created', async(() => {
    hostComponent.element.config.uiStyles.attributes.inplaceEdit = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inplaceEditor = debugElement.query(By.css('inplace-editor'));
    expect(inplaceEditor).toBeFalsy();
  }));

  it('inplaceEditor should not be created when imgSrc is available', async(() => {
    hostComponent.element.config.uiStyles.attributes.imgSrc = 't';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inplaceEditorEle = debugElement.query(By.css('inplace-editor'));
    expect(inplaceEditorEle).toBeFalsy();
  }));

  it('inputLabel should be created', async(() => {
    const iLabel = inputLabel;
    hostComponent.element = iLabel;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle.name).toEqual('nm-input-label');
  }));

  it('inputLabel should not be created when element.config.uiStyles.attributes.showName', async(() => {
    const iLabel = inputLabel;
    hostComponent.element = iLabel;
    hostComponent.element.config.uiStyles.attributes.showName = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle).toBeFalsy();
  }));

  it('inputLabel should be created with out date', async(() => {
    const iLabel = inputLabelNoDate;
    hostComponent.element = iLabel;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle.name).toEqual('nm-input-label');
  }));

  it('inputLabel should not be created with out date', async(() => {
    const iLabel = inputLabelNoDate;
    hostComponent.element = iLabel;
    hostComponent.element.config.uiStyles.attributes.showName = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle).toBeFalsy();
  }));

  it('ngOnInit() should update fieldClass property for cols:6',  async(() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cols = '6';
      hostComponent.ngOnInit();
      expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  }));

  it('ngOnInit() should update fieldClass property for cols:4',  async(() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cols = '4';
      hostComponent.ngOnInit();
      expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  }));

  it('ngOnInit() should update fieldClass property for cols:3',  async(() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cols = '3';
      hostComponent.ngOnInit();
      expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  }));

  it('ngOnInit() should update fieldClass property for cols:2',  async(() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cols = '2';
      hostComponent.ngOnInit();
      expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  }));

  it('ngOnInit() should update fieldClass property for cols:1',  async(() => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.cols = '1';
      hostComponent.ngOnInit();
      expect((hostComponent as any).fieldClass).toEqual('col-sm-3');
    });
  }));

  it('value property should be updated with element.leafstate',  async(() => {
    hostComponent.element.leafState = 'test';
    hostComponent.element.values = [];
    hostComponent.value = '';
    expect(hostComponent.value).toEqual('test');
  }));

  it('value property should be updated with element.values.label',  async(() => {
    hostComponent.element.leafState = 'test';
    const testValue = new Values();
    testValue.code = 'test';
    testValue.label = 'tLabel';
    hostComponent.element.values = [testValue];
    hostComponent.value = '';
    expect(hostComponent.value).toEqual('tLabel');
  }));
  it('value property should be updated with element.leafstate based on code',  async(() => {
    hostComponent.element.leafState = 'test';
    const testValue = new Values();
    testValue.code = 'test1';
    testValue.label = 'tLabel';
    hostComponent.element.values = [testValue];
    hostComponent.value = '';
    expect(hostComponent.value).toEqual('test');
  }));

  it('registerOnChange() should update the onChange property',  async(() => {
    const test = () => {
      return true;
    };
    hostComponent.registerOnChange(test);
    expect(hostComponent.onChange).toEqual(test);
  }));

  it('writeValue() shouls call onChange()',  async(() => {
    hostComponent.element.leafState = 'test';
    hostComponent.element.values = [];
    spyOn(hostComponent, 'onChange').and.callThrough();
    hostComponent.writeValue(123);
    hostComponent.writeValue(undefined);
    expect(hostComponent.onChange).toHaveBeenCalled();
  }));
  
  it('registerOnTouched() should update the onTouched property',  async(() => {
    const test = () => {
      return true;
    };
    hostComponent.registerOnTouched(test);
    expect(hostComponent.onTouched).toEqual(test);
  }));

  // it('getComponentClass() should return array [testClass, col-sm-12, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '1';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-12'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-6, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '2';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-6'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-4, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '3';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-4'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '4';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-3'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-2, p-0, clearfix]',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '6';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-2'
  //     ]);
  //   });
  // }));

  // it('getComponentClass() should return array [testClass, col-sm-3, p-0, clearfix] when cols is empty',  async(() => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.cols = '';
  //     hostComponent.element.config.uiStyles.attributes.cssClass = 'testClass';
  //     expect(hostComponent.getComponentClass()).toEqual([
  //       'testClass',
  //       'p-0',
  //       'clearfix',
  //       'col-sm-3'
  //     ]);
  //   });
  // }));

  it('getComponentClass() should return array [mb-3]',  async(() => {
    expect(hostComponent.getComponentClass()).toEqual(['mb-3']);
  }));

  it('value getter() should return _value property value',  async(() => {
    hostComponent.value = 'test';
    expect(hostComponent._value).toEqual('test');
  }));

  it('set value() should update the value property',  async(() => {
    hostComponent.element.leafState = 'test';
    const testValue = new Values();
    testValue.code = 'test1';
    hostComponent.element.values = [testValue];
    hostComponent.value = '';
    expect(hostComponent.value).toEqual('test');
  }));

});




const inplaceEditor = {
  "config": {
      "configSvc": {
          "flowConfigs": {
              "ownerview": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "22248",
                              "path": "/ownerview/vpAddEditOwner",
                              "type": {
                                  "model": {
                                      "params": []
                                  }
                              },
                              "message": [],
                              "values": [],
                              "labels": [
                                  {
                                      "locale": "en-US",
                                      "text": "Add Owner"
                                  }
                              ],
                              "elemLabels": {}
                          }
                      ]
                  },
                  "layout": "home"
              }
          }
      },
      "active": false,
      "required": false,
      "id": "22284",
      "code": "firstName",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FieldValue",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "inplaceEditType": "",
              "cssClass": "",
              "datePattern": "",
              "alias": "FieldValue",
              "applyValueStyles": false,
              "placeholder": "",
              "inplaceEdit": true,
              "type": "Field",
              "cols": "2",
              "imgSrc": ""
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
  "configId": "21196",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/firstName",
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
};


const inputLabel = {
  "config": {
      "configSvc": {
          "flowConfigs": {
              "ownerview": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "23958",
                              "path": "/ownerview/vpAddEditOwner",
                              "type": {
                                  "model": {
                                      "params": []
                                  }
                              },
                              "message": [],
                              "values": [],
                              "labels": [
                                  {
                                      "locale": "en-US",
                                      "text": "Add Owner"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "23987",
                              "path": "/ownerview/vpOwnerInfo",
                              "type": {
                                  "model": {
                                      "params": []
                                  }
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
                  },
                  "layout": "home"
              }
          }
      },
      "active": false,
      "required": false,
      "id": "24003",
      "code": "caseStatusDate",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FieldValue",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "inplaceEditType": "",
              "cssClass": "",
              "datePattern": "MM/dd/yyyy",
              "alias": "FieldValue",
              "applyValueStyles": false,
              "placeholder": "",
              "inplaceEdit": false,
              "type": "Field",
              "cols": "3",
              "imgSrc": ""
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "LocalDateTime"
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "24003",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/caseStatusDate",
  "type": {},
  "leafState": "2018-09-04T20:47:18.000Z",
  "message": [],
  "values": [],
  "labels": [
      {
          "locale": "en-US",
          "text": "Case Status Date"
      }
  ],
  "elemLabels": {}
};


const inputLabelNoDate = {
  "config": {
      "configSvc": {
          "flowConfigs": {
              "ownerview": {
                  "model": {
                      "params": [

                      ]
                  },
                  "layout": "home"
              }
          }
      },
      "active": false,
      "required": false,
      "id": "462",
      "code": "lastName",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FieldValue",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "inplaceEditType": "",
              "cssClass": "",
              "datePattern": "",
              "alias": "FieldValue",
              "applyValueStyles": false,
              "placeholder": "",
              "inplaceEdit": false,
              "type": "Field",
              "cols": "4",
              "imgSrc": ""
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
  "configId": "462",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/lastName",
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
};

const param = {
  "config": {
      "active": false,
      "required": false,
      "id": "1329",
      "code": "firstName",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FieldValue",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "inplaceEditType": "",
              "cssClass": "",
              "datePattern": "",
              "alias": "FieldValue",
              "applyValueStyles": false,
              "placeholder": "",
              "inplaceEdit": true,
              "type": "Field",
              "cols": "2",
              "imgSrc": ""
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
  "configId": "1329",
  "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/firstName",
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
};