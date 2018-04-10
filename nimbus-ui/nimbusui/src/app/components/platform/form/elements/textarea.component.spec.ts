'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { TextArea } from './textarea.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';

describe('TextArea', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        TextArea,
        TooltipComponent
       ],
       imports: [
        FormsModule,
        HttpClientModule,
        HttpModule
       ],
       providers: [
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(TextArea);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});