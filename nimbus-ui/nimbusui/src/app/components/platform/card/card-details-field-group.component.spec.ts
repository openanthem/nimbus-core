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

import { CardDetailsFieldComponent } from './card-details-field.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { Param } from '../../../shared/param-state';
import { ServiceConstants } from '../../../services/service.constants';
import { PageService } from '../../../services/page.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { AppInitService } from '../../../services/app.init.service';
import { cardDetailsFieldGroupElement, newCardDetailsFieldGroupElement } from 'mockdata';
import { Paragraph } from '../content/paragraph.component';

let fixture,hostComponent;

const declarations = [
  CardDetailsFieldGroupComponent,
  CardDetailsFieldComponent,
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  DateTimeFormatPipe,
  TooltipComponent,
  SelectItemPipe,
  DisplayValueDirective,
  InputLabel,
  Paragraph
];
const imports = [
    FormsModule, 
    DropdownModule, 
    HttpClientModule, 
    HttpModule,
    StorageServiceModule
  ];
  const providers = [
      { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
      { provide: 'JSNLOG', useValue: JL },
      { provide: LocationStrategy, useClass: HashLocationStrategy },
      Location,
      AppInitService,
      SessionStoreService,
      CustomHttpClient,
      WebContentSvc,
      PageService,
      LoaderService,
      ConfigService,
      LoggerService
    ];

describe('CardDetailsFieldGroupComponent', () => {
  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
   fixture = TestBed.createComponent(CardDetailsFieldGroupComponent);
   hostComponent = fixture.debugElement.componentInstance;
   hostComponent.element = cardDetailsFieldGroupElement as Param;;
  });

  it('should create the CardDetailsFieldGroupComponent',  async(() => {
    expect(hostComponent).toBeTruthy(); 
  }));

  it('Label should be created on providing the element.labelconfig display the value provided',async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'));
    expect(labelEle.name).toEqual('nm-input-label');
    expect(labelEle.nativeElement.innerText.toString().trim()).toEqual('Case ID');
  }));

  it('Label should not be created on if element.labelconfig is empty',async(() => {
    ServiceConstants.LOCALE_LANGUAGE = 'en-US';
    hostComponent.element.type.model.params[0].labels = [];
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const labelEle = debugElement.query(By.css('nm-input-label'))
    expect(labelEle.nativeElement.innerText.toString()).toEqual('');
  }));

  it('nm-card-details-field should be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias === FieldValue',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(cardDetailsFieldEle.name).toEqual('nm-card-details-field');
  }));

  it('nm-card-details-field should not be created if element?.type?.model?.params[0].config?.uiStyles?.attributes?.alias !== FieldValue',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.alias = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const cardDetailsFieldEle = debugElement.query(By.css('nm-card-details-field'));
    expect(cardDetailsFieldEle).toBeFalsy();
  }));

  it('another nm-card-details-field should be added on updating the param',async(() => {
    hostComponent.element.type.model.params[0].config.uiStyles.attributes.alias = 'FieldValue';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const allCardDetailsFieldEles = debugElement.queryAll(By.css('nm-card-details-field'));
    expect(allCardDetailsFieldEles.length).toEqual(1);
    hostComponent.element.type.model.params.push(newCardDetailsFieldGroupElement);
    fixture.detectChanges();
    const newAllCardDetailsFieldEles = debugElement.queryAll(By.css('nm-card-details-field'));
    expect(newAllCardDetailsFieldEles.length).toEqual(2);
  }));

});
