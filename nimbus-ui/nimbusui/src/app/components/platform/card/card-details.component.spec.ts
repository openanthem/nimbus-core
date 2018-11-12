'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { AngularSvgIconModule } from 'angular-svg-icon';

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
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { Paragraph } from '../content/paragraph.component';
import { ButtonGroup } from '../../platform/form/elements/button-group.component';
import { Label } from '../content/label.component';
import { ValueStylesDirective } from '../../../directives/value-styles.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { Button } from '../../platform/form/elements/button.component';
import { Image } from '../../platform/image.component';
import { SvgComponent } from '../../platform/svg/svg.component';

class MockPageService {
    processEvent() {    }
}

describe('CardDetailsComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
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
        CardDetailsFieldGroupComponent,
        Paragraph,
        ButtonGroup,
        Label,
        ValueStylesDirective,
        InputLabel,
        Button,
        Image,
        SvgComponent
    ],
    imports: [
        FormsModule,
        DropdownModule,
        HttpModule,
        HttpClientModule,
        AngularSvgIconModule
    ],
    providers: [
        { provide: PageService, useClass: MockPageService },
        CustomHttpClient,
        LoaderService,
        ConfigService
    ]
    }).compileComponents();
  }));

  it('should create the CardDetailsComponent', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('toggle() should updated opened property', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    app.opened = true;
    app.toggle();
    expect(app.opened).toEqual(false);
  }));

it('processOnClick() should call pageSvc.processEvent', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    spyOn(app.pageSvc, 'processEvent').and.callThrough();
    app.element = {
        path: '/a'
    };
    app.processOnClick();
    expect(app.pageSvc.processEvent).toHaveBeenCalled();
  }));

it('getAllURLParams should return null matching the regexp', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.getAllURLParams('/webhp?hl=en')).toEqual(null);
  }));

  it('getAllURLParams should return string matching the regexp', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.getAllURLParams('{ /webhp?hl=en}')).toEqual(['{ /webhp?hl=en}']);
  }));

  it('toggleState() should update isHidden and _state properties', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    app.state = 'closedPanel';
    app.isHidden = true;
    app.toggleState();
    expect(app.isHidden).toBeFalsy();
    expect(app._state).toEqual('openPanel');
  }));

  it('toggleState() should update _state property', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    app.state = 'openPanel';
    app.toggleState();
    expect(app._state).toEqual('closedPanel');
  }));

  it('animationDone() should update the isHidden property', async(() => {
    const fixture = TestBed.createComponent(CardDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    app.state = 'closedPanel';
    app.animationDone('a');
    expect(app.isHidden).toBeTruthy();
  }));

});