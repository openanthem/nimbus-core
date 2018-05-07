'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CalendarModule, RadioButtonModule, CheckboxModule, ListboxModule, FileUploadModule, GrowlModule, DropdownModule, DataTableModule, AccordionModule, PickListModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';

import { Section } from './section.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Button } from '../platform/form/elements/button.component';
import { InfiniteScrollGrid } from '../platform/grid/grid.component';
import { AccordionMain } from '../platform/content/accordion.component';
import { Menu } from '../platform/menu.component';
import { Link } from '../platform/link.component';
import { Form } from '../platform/form.component';
import { StaticText } from '../platform/content/static-content.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { MessageComponent } from '../platform/message/message.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { FrmGroupCmp } from './form-group.component';
import { AccordionGroup } from '../platform/accordion-group.component';
import { Accordion } from '../platform/accordion.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { FormElement } from './form-element.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { TextArea } from '../platform/form/elements/textarea.component';
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
import { Header } from '../platform/content/header.component';
import { WebContentSvc } from '../../services/content-management.service';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { DataTable } from './grid/table.component';

class MockPageService {
    processEvent() {

    }
}


describe('Section', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
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
        TooltipComponent,
        SelectItemPipe,
        ActionDropdown,
        DateTimeFormatPipe,
        FrmGroupCmp,
        AccordionGroup,
        Accordion,
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
        FormsModule,
        DropdownModule,
        DataTableModule,
        AccordionModule,
        ReactiveFormsModule,
        GrowlModule,
        PickListModule,
        FileUploadModule,
        ListboxModule,
        CheckboxModule,
        RadioButtonModule,
        CalendarModule,
        HttpModule,
        HttpClientModule,
        TableModule,
        KeyFilterModule
       ],
       providers: [
        { provide: PageService, useClass: MockPageService },
        WebContentSvc,
        CustomHttpClient,
        LoaderService,
        ConfigService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(Section);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should call pageSvc.processEvent()', async(() => {
    const fixture = TestBed.createComponent(Section);
    const app = fixture.debugElement.componentInstance;
    const service = TestBed.get(PageService);
    app.element = {
        config: {
            initializeComponent: () => {
                return 'true';
            }
        }
    }
    spyOn(service, 'processEvent').and.callThrough();
    app.ngOnInit();
    expect(service.processEvent).toHaveBeenCalled();
  }));

  it('ngOnInit() should not call pageSvc.processEvent()', async(() => {
    const fixture = TestBed.createComponent(Section);
    const app = fixture.debugElement.componentInstance;
    const service = TestBed.get(PageService);
    app.element = { }
    spyOn(service, 'processEvent').and.callThrough();
    app.ngOnInit();
    expect(service.processEvent).not.toHaveBeenCalled();
  }));

});
