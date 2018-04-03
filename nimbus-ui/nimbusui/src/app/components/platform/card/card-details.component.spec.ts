'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';

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
    ],
    imports: [
        FormsModule,
        DropdownModule,
        HttpModule,
        HttpClientModule
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

});