'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DropdownModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';

import { ComboBox } from './combobox.component';
import { TooltipComponent } from '../../../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../../../pipes/select-item.pipe';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';

describe('ComboBox', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ComboBox,
        TooltipComponent,
        SelectItemPipe
       ],
       imports: [
        DropdownModule,
        FormsModule,
        HttpClientModule,
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

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(ComboBox);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});