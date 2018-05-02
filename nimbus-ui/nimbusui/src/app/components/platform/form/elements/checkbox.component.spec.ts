'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { CheckBox } from './checkbox.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';

describe('CheckBox', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        CheckBox,
        TooltipComponent
       ],
       imports: [
        FormsModule,
        HttpClientModule,
        HttpModule
       ],
       providers:[
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        LoggerService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(CheckBox);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});