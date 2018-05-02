'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpModule } from '@angular/http';

import { InputText } from './textbox.component';
import { TooltipComponent } from '../../tooltip/tooltip.component'
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';

describe('InputText', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        InputText,
        TooltipComponent
       ],
       imports: [
        FormsModule,
        HttpClientTestingModule,
        HttpModule
       ],
       providers: [
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        LoggerService
       ]
    }).compileComponents();
  }));

  it('InputText should create the app', async(() => {
    const fixture = TestBed.createComponent(InputText);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});