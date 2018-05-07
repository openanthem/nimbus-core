'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { AccordionMain } from './accordion.component';
import { AccordionModule } from 'primeng/primeng';
import { CardDetailsComponent } from '../card/card-details.component';
import { Link } from '../link.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { StaticText } from '../content/static-content.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { WebContentSvc } from '../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';

class MockWebContentSvc {
    findLabelContent(param) {
        const test = {
            text: 'testing'
        };
        return test;
    }
}

describe('AccordionMain', () => {
  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        declarations: [
          AccordionMain,
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
          SelectItemPipe
        ],
        imports: [
          AccordionModule,
          FormsModule,
          DropdownModule,
          HttpModule,
          HttpClientModule
        ],
        providers: [
            {provide: WebContentSvc, useClass: MockWebContentSvc},
            PageService,
            CustomHttpClient,
            LoaderService,
            ConfigService,
            LoggerService
        ]
      }).compileComponents();
    })
  );

  it('should create the AccordionMain', async(() => {
      const fixture = TestBed.createComponent(AccordionMain);
      const app = fixture.debugElement.componentInstance;
      expect(app).toBeTruthy();
    }));

  it('get multiple() should return the this.element.config.uiStyles.attributes.multiple value', async(
      () => {
        const fixture = TestBed.createComponent(AccordionMain);
        const app = fixture.debugElement.componentInstance;
        app.element = {
          config: {
            uiStyles: {
              attributes: {
                multiple: false
              }
            }
          }
        };
        expect(app.multiple).toEqual(false);
      }
    ));

  it('closeAll should clear the index array', async(() => {
      const fixture = TestBed.createComponent(AccordionMain);
      const app = fixture.debugElement.componentInstance;
      app.accordion = { tabs: true };
      app.index = [1, 2, 3];
      app.closeAll();
      expect(app.index).toEqual([-1]);
    }));

  it('closeAll should not clear the index array', async(() => {
      const fixture = TestBed.createComponent(AccordionMain);
      const app = fixture.debugElement.componentInstance;
      app.accordion = { tabs: false };
      app.index = [1, 2, 3];
      app.closeAll();
      expect(app.index).toEqual([1, 2, 3]);
    }));

    it('openAll() should update index array', async(() => {
        const fixture = TestBed.createComponent(AccordionMain);
        const app = fixture.debugElement.componentInstance;
        app.index = [];
        app.accordion = {
            tabs: [1, 2, 3]
        };
        app.openAll();
        expect(app.index.length).toEqual(3);
      }));

      it('openAll() should not update index array', async(() => {
        const fixture = TestBed.createComponent(AccordionMain);
        const app = fixture.debugElement.componentInstance;
        app.index = [];
        app.accordion = { };
        app.openAll();
        expect(app.index.length).toEqual(0);
      }));

});
