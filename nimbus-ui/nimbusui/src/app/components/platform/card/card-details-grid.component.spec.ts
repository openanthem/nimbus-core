'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { AngularSvgIconModule } from 'angular-svg-icon';

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
import { Button } from '../../platform/form/elements/button.component';
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

describe('CardDetailsGrid', () => {

    configureTestSuite();
    setup(CardDetailsGrid, declarations, imports, providers);
    param = (<any>data).payload;

  beforeEach(async function(this: TestContext<CardDetailsGrid>){
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the CardDetailsGrid', async function(this: TestContext<CardDetailsGrid>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngonint() should call pageService.processEvent', async function(this: TestContext<CardDetailsGrid>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.onLoad = true;
      spyOn(pageService, 'processEvent').and.callThrough();
      this.hostComponent.ngOnInit();
      expect(pageService.processEvent).toHaveBeenCalled();
    });
  });

  it('ngonint() should not call pageSvc.processEvent', async function(this: TestContext<CardDetailsFieldComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.onLoad = false;
      spyOn(pageService, 'processEvent').and.callThrough();
      this.hostComponent.ngOnInit();
      expect(pageService.processEvent).not.toHaveBeenCalled();
    });
  });

});
