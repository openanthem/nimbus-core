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
import { cardDetailsFieldInputLabel, cardDetailsFieldInputLabelNoDate, cardDetailsFieldParam, cardDetailsFieldNmDisplayValueParam } from 'mockdata';

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
    hostComponent.element = cardDetailsFieldParam;
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
    const iLabel = cardDetailsFieldInputLabel;
    hostComponent.element = iLabel;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle.name).toEqual('nm-input-label');
  }));

  it('inputLabel should not be created when element.config.uiStyles.attributes.showName', async(() => {
    const iLabel = cardDetailsFieldInputLabel;
    hostComponent.element = iLabel;
    hostComponent.element.config.uiStyles.attributes.showName = false;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle).toBeFalsy();
  }));

  it('inputLabel should be created with out date', async(() => {
    const iLabel = cardDetailsFieldInputLabelNoDate;
    hostComponent.element = iLabel;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const inputLabelEle = debugElement.query(By.css('nm-input-label'));
    expect(inputLabelEle.name).toEqual('nm-input-label');
  }));

  it('inputLabel should not be created with out date', async(() => {
    const iLabel = cardDetailsFieldInputLabelNoDate;
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

  it('nmDisplayValue should add leafstate and config.code as class', async(() => {
    hostComponent.element = cardDetailsFieldNmDisplayValueParam;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const divNmDisplayValueDivEle = debugElement.query(By.css('.textWrapBreakWord'));    
    expect(divNmDisplayValueDivEle.nativeElement.classList[1]).toEqual(hostComponent.element.leafState);
    expect(divNmDisplayValueDivEle.nativeElement.classList[2]).toEqual(hostComponent.element.config.code);
  }));

});

