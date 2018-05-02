'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule, GrowlModule, MessagesModule, DialogModule, AccordionModule, DataTableModule, FileUploadModule, PickListModule, ListboxModule, CheckboxModule, RadioButtonModule, CalendarModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { KeyFilterModule } from 'primeng/keyfilter';

import { Tile } from './tile.component';
import { MessageComponent } from './message/message.component';
import { Header } from './content/header.component';
import { Section } from './section.component';
import { Modal } from './modal/modal.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { StaticText } from '../platform/content/static-content.component';
import { Form } from '../platform/form.component';
import { Link } from '../platform/link.component';
import { Menu } from '../platform/menu.component';
import { AccordionMain } from '../platform/content/accordion.component';
import { InfiniteScrollGrid } from '../platform/grid/grid.component';
import { Button } from '../platform/form/elements/button.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { FrmGroupCmp } from './form-group.component';
import { Accordion } from '../platform/accordion.component';
import { AccordionGroup } from '../platform/accordion-group.component';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { FormElement } from './form-element.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { DateControl } from '../platform/form/elements/date.component';
import { Signature } from '../platform/form/elements/signature.component'
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { DataTable } from './grid/table.component';

let fixture, app, pageService;

class MockPageService {
    processEvent() {

    }
}

describe('Tile', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Tile,
        MessageComponent,
        Header,
        Section,
        Modal,
        CardDetailsGrid,
        CardDetailsComponent,
        Paragraph,
        StaticText,
        Form,
        Link,
        Menu,
        AccordionMain,
        InfiniteScrollGrid,
        Button,
        ButtonGroup,
        InputText,
        ComboBox,
        TooltipComponent,
        CardDetailsFieldComponent,
        FrmGroupCmp,
        Accordion,
        AccordionGroup,
        ActionDropdown,
        DateTimeFormatPipe,
        SelectItemPipe,
        InPlaceEditorComponent,
        TextArea,
        FormElement,
        ActionLink,
        FileUploadComponent,
        OrderablePickList,
        MultiselectCard,
        MultiSelectListBox,
        CheckBox,
        CheckBoxGroup,
        RadioButton,
        Calendar,
        DateControl,
        Signature,
        DataTable
       ],
       imports: [
        FormsModule,
        GrowlModule,
        MessagesModule,
        DialogModule,
        ReactiveFormsModule,
        AccordionModule,
        DataTableModule,
        DropdownModule,
        FileUploadModule,
        PickListModule,
        ListboxModule,
        CheckboxModule,
        RadioButtonModule,
        CalendarModule,
        HttpModule,
        HttpClientTestingModule,
        TableModule,
        KeyFilterModule
       ],
       providers: [
        {provide: PageService, useClass: MockPageService},
        CustomHttpClient,
        LoaderService,
        ConfigService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(Tile);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('based on the xSmall size the styleWd and styleHt should be updated', async(() => {
      app.element = {
          config: {
              initializeComponent: () => {},
              uiStyles: {
                  attributes: {
                      size: 'XSmall'
                  }
              }
          }
      };
      app.ngOnInit();
    expect(app.styleWd).toEqual('card-holder col-lg-3 col-md-6 XsmallCard');
    expect(app.styleHt).toEqual('height-md');
  }));

  it('based on the small size the styleWd and styleHt should be updated', async(() => {
    app.element = {
        config: {
            initializeComponent: () => {},
            uiStyles: {
                attributes: {
                    size: 'Small'
                }
            }
        }
    };
    app.ngOnInit();
  expect(app.styleWd).toEqual('col-xl-6 col-sm-6 smallCard');
  expect(app.styleHt).toEqual('height-md');
  }));

  it('based on the medium size the styleWd and styleHt should be updated', async(() => {
    app.element = {
        config: {
            initializeComponent: () => {},
            uiStyles: {
                attributes: {
                    size: 'Medium'
                }
            }
        }
    };
    app.ngOnInit();
  expect(app.styleWd).toEqual('card-holder col-lg-9 col-md-12 mediumCard');
  expect(app.styleHt).toEqual('height-md');
  }));

  it('based on the colorBox size the styleWd and styleHt should be updated', async(() => {
    app.element = {
        config: {
            initializeComponent: () => {},
            uiStyles: {
                attributes: {
                    size: 'Colorbox'
                }
            }
        }
    };
    app.ngOnInit();
  expect(app.styleWd).toEqual('my-card-holder col-xs-12 colorBox');
  expect(app.styleHt).toEqual('my-card-block ');
  }));

  it('based on the size the styleWd and styleHt should be updated', async(() => {
    app.element = {
        config: {
            initializeComponent: () => {},
            uiStyles: {
                attributes: {
                    size: ''
                }
            }
        }
    };
    app.ngOnInit();
  expect(app.styleWd).toEqual('');
  expect(app.styleHt).toEqual('');
  }));

  it('ngOnInit() should update the styleWd', async(() => {
    app.element = {
        config: {
            initializeComponent: () => {},
            uiStyles: {
                attributes: {
                    size: ''
                }
            }
        }
    };
    app.tileType = 'subcard';
    app.ngOnInit();
    expect(app.styleWd).toEqual(' subcard');
  }));

  it('ngOnInit() should call the pageService.processEvent', async(() => {
    app.element = {
        config: {
            initializeComponent: () => {
                return true;
            },
            uiStyles: {
                attributes: {
                    size: ''
                }
            }
        }
    };
    spyOn(pageService, 'processEvent').and.callThrough();
    app.ngOnInit();
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

});