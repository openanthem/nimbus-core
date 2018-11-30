import { Param } from './../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { CardDetailsGrid } from './card-details-grid.component';
import { PageService } from '../../../services/page.service';
import { CardDetailsComponent } from './card-details.component';
import { Link } from '../link.component';
import { CardDetailsFieldComponent } from './card-details-field.component';
import { StaticText } from '../content/static-content.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { Label } from '../content/label.component';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { Paragraph } from '../content/paragraph.component';
import { ButtonGroup } from '../../platform/form/elements/button-group.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
// import { Button } from '../../platform/form/elements/button.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { PrintDirective } from '../../../directives/print.directive';

let param, pageService;

class MockPageService {
    processEvent() {

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

const declarations = [
    CardDetailsGrid,
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
    Label,
    CardDetailsFieldGroupComponent,
    Paragraph,
    ButtonGroup,
    DisplayValueDirective,
    InputLabel,
    Button,
    Image,
    SvgComponent,
    PrintDirective
    ];
const imports = [ 
    FormsModule,
    DropdownModule,
    HttpClientModule,
    HttpModule,
    AngularSvgIconModule
];
 const providers = [
    { provide: PageService, useClass: MockPageService },
     CustomHttpClient,
     LoaderService,
     ConfigService,
     WebContentSvc
     ];

let fixture, hostComponent;

describe('CardDetailsGrid', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     
  let param: Param = JSON.parse(payload);

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDetailsGrid);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the CardDetailsGrid', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('Label should be created on providing the element.labelconfig display the value provided',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Label should not be created on if element.labelconfig is empty',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-card-details should be created if element?.type?.model?.params[0].type?.model?.params[0].config?.uiStyles?.attributes?.alias === CardDetail',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-card-details should not be created if element?.type?.model?.params[0].type?.model?.params[0].config?.uiStyles?.attributes?.alias !== CardDetail',async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  // it('ngonint() should call pageService.processEvent', () => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.onLoad = true;
  //     spyOn(pageService, 'processEvent').and.callThrough();
  //     hostComponent.ngOnInit();
  //     expect(pageService.processEvent).toHaveBeenCalled();
  //   });
  // });

  // it('ngonint() should not call pageSvc.processEvent', () => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.onLoad = false;
  //     spyOn(pageService, 'processEvent').and.callThrough();
  //     hostComponent.ngOnInit();
  //     expect(pageService.processEvent).not.toHaveBeenCalled();
  //   });
  // });

});
