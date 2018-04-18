'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/primeng';

import { NavMenuGlobal } from './nav-global-menu.component';
import { Link } from '../link.component';
import { ComboBox } from '../form/elements/combobox.component';
import {KeysPipe} from '../../../pipes/app.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        NavMenuGlobal,
        Link,
        ComboBox,
        KeysPipe,
        TooltipComponent,
        SelectItemPipe
        ],
     imports: [
        FormsModule,
        DropdownModule
     ]
    }).compileComponents();
  }));

  it('should create the NavMenuGlobal', async(() => {
    const fixture = TestBed.createComponent(NavMenuGlobal);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

});

