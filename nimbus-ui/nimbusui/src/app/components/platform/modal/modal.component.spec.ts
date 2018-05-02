'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, MessagesModule  } from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';

import { Modal } from './modal.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { Section } from '../section.component';
import { ComboBox } from '../../platform/form/elements/combobox.component';
import { InputText } from '../form/elements/textbox.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Button } from '../form/elements/button.component';
import { InfiniteScrollGrid } from '../grid/grid.component';
import { AccordionMain } from '../content/accordion.component';
import { Menu } from '../menu.component';
import { Link } from '../link.component';
import { Form } from '../form.component';
import { StaticText } from '../content/static-content.component';
import { Paragraph } from '../content/paragraph.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { MessageComponent } from '../message/message.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { FrmGroupCmp } from '../form-group.component';
import { Accordion } from '../accordion.component';
import { AccordionGroup } from '../accordion-group.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { ActionLink } from '../form/elements/action-dropdown.component';
import { FormElement } from '../form-element.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { TextArea } from '../form/elements/textarea.component';
import { FileUploadComponent } from '../fileupload/file-upload.component';
import { OrderablePickList } from '../form/elements/picklist.component';
import { MultiselectCard } from '../form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../form/elements/multi-select-listbox.component';
import { CheckBox } from '../form/elements/checkbox.component';
import { CheckBoxGroup } from '../form/elements/checkbox-group.component';
import { RadioButton } from '../form/elements/radio.component';
import { Calendar } from '../form/elements/calendar.component';
import { DateControl } from '../form/elements/date.component';
import { Signature } from '../form/elements/signature.component';
import { Header } from '../content/header.component';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { DataTable } from '../grid/table.component';

class MockPageService {
    processEvent(a, b, c, d) { }
}

let fixture, app, pageservice;

describe('Modal', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Modal,
        TooltipComponent,
        Section,
        ComboBox,
        InputText,
        ButtonGroup,
        Button,
        InfiniteScrollGrid,
        AccordionMain,
        Menu,
        Link,
        Form,
        StaticText,
        Paragraph,
        CardDetailsComponent,
        CardDetailsGrid,
        MessageComponent,
        SelectItemPipe,
        ActionDropdown,
        DateTimeFormatPipe,
        FrmGroupCmp,
        Accordion,
        AccordionGroup,
        CardDetailsFieldComponent,
        ActionLink,
        FormElement,
        InPlaceEditorComponent,
        TextArea,
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
        Header,
        DataTable
       ],
       imports: [
           DialogModule,
           FormsModule,
           DropdownModule,
           DataTableModule,
           AccordionModule,
           ReactiveFormsModule,
           GrowlModule,
           MessagesModule,
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
    fixture = TestBed.createComponent(Modal);
    app = fixture.debugElement.componentInstance;
    pageservice = TestBed.get(PageService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('closable property should updated from the element', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    closable: true
                }
            }
        }
    };
    expect(app.closable).toEqual(true);
  }));

  it('width should be 500 for small size', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    width: 'small'
                }
            }
        }
    };
    expect(app.width).toEqual('500');
  }));

  it('width should be 700 for medium size', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    width: 'medium'
                }
            }
        }
    };
    expect(app.width).toEqual('700');
  }));

  it('width should be 900 for large size', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    width: 'large'
                }
            }
        }
    };
    expect(app.width).toEqual('900');
  }));

  it('width property should be updated from element if size is not available', async(() => {
    app.element = {
        config: {
            uiStyles: {
                attributes: {
                    width: '999'
                }
            }
        }
    };
    expect(app.width).toEqual('999');
  }));

  it('closeDialog() should call pageservice.processEvent', async(() => {
      app.element = {
          visible: true
      };
      spyOn(pageservice, 'processEvent').and.callThrough();
      app.closeDialog('a');
    expect(pageservice.processEvent).toHaveBeenCalled();
  }));

  it('closeDialog() should not call pageservice.processEvent', async(() => {
    app.element = {
        visible: false
    };
    spyOn(pageservice, 'processEvent').and.callThrough();
    app.closeDialog('a');
  expect(pageservice.processEvent).not.toHaveBeenCalled();
}));

  it('resizable property should be updated from element', async(() => {
      app.element = {
          config: {
            uiStyles: {
                attributes: {
                    resizable: true
                }
            }
          }
      };
    expect(app.resizable).toEqual(true);
  }));

});