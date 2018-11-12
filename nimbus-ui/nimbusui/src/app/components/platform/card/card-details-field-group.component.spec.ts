'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { CardDetailsFieldComponent } from './card-details-field.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { ValueStylesDirective } from '../../../directives/value-styles.directive';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { CardDetailsFieldGroupComponent } from './card-details-field-group.component';
import { WebContentSvc } from '../../../services/content-management.service';

let fixture, app;

describe('CardDetailsFieldGroupComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        CardDetailsFieldGroupComponent,
        CardDetailsFieldComponent,
        InPlaceEditorComponent,
        InputText,
        TextArea,
        ComboBox,
        DateTimeFormatPipe,
        TooltipComponent,
        SelectItemPipe,
        ValueStylesDirective,
        InputLabel
      ],
      imports: [
          FormsModule, 
          DropdownModule, 
          HttpClientModule, 
          HttpModule
        ],
      providers: [
          CustomHttpClient,
          WebContentSvc
        ]
    }).compileComponents();
    fixture = TestBed.createComponent(CardDetailsFieldGroupComponent);
    app = fixture.debugElement.componentInstance;
  }));

    it('should create the app', async(() => {
        expect(app).toBeTruthy();
    }));

    it('getComponentClass() should return array [testClass, col-sm-12]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '1', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual(['testClass', 'col-sm-12']);
    }));

    it('getComponentClass() should return array [testClass, col-sm-6]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '2', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual(['testClass', 'col-sm-6']);
    }));

    it('getComponentClass() should return array [testClass, col-sm-4]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '3', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual(['testClass', 'col-sm-4']);
    }));

    it('getComponentClass() should return array [testClass, col-sm-3]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '4', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual(['testClass', 'col-sm-3']);
    }));

    it('getComponentClass() should return array [testClass, col-sm-2]', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '6', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual(['testClass', 'col-sm-2']);
    }));

    it('getComponentClass() should return array [testClass, col-sm-3] when cols is empty', async(() => {
      app.element = { config: { uiStyles: { attributes: { cols: '', cssClass: 'testClass' } } } };
      expect(app.getComponentClass()).toEqual(['testClass', 'col-sm-3']);
    }));

});
