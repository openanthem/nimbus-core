'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, 
    DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule,
    InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';

import { FrmGroupCmp } from './form-group.component';
import { FormElement } from './form-element.component';
import { Button } from '../platform/form/elements/button.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Header } from '../platform/content/header.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { DateControl } from '../platform/form/elements/date.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { InfiniteScrollGrid } from '../platform/grid/grid.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { Section } from '../platform/section.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { MessageComponent } from '../platform/message/message.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { Accordion } from '../platform/content/accordion.component';
import { Menu } from '../platform/menu.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { StaticText } from '../platform/content/static-content.component';
import { Form } from '../platform/form.component';
import { Link } from '../platform/link.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { WebContentSvc } from '../../services/content-management.service';
import { Signature } from '../platform/form/elements/signature.component'
import { DataTable } from './grid/table.component';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { SvgComponent } from './svg/svg.component';
import { Image } from './image.component';
import { TreeGrid } from './tree-grid/tree-grid.component';
import { InputSwitch } from './form/elements/input-switch.component';
import { FormGridFiller } from './form/form-grid-filler.component';
import { DisplayValueDirective } from '../../directives/display-value.directive';
import { InputLabel } from './form/elements/input-label.component';
import { Label } from './content/label.component';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';

let fixture, app;

class MockWebContentSvc {
    findLabelContent(param) {
        const test = {
            text: 'tText',
            helpText: 'tHelpText'
        }
        return test;
    }
}

describe('FrmGroupCmp', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FrmGroupCmp,
        FormElement,
        Button,
        ButtonGroup,
        Header,
        Paragraph,
        InputText,
        TextArea,
        DateControl,
        Calendar,
        ComboBox,
        RadioButton,
        CheckBoxGroup,
        CheckBox,
        MultiSelectListBox,
        MultiselectCard,
        OrderablePickList,
        FileUploadComponent,
        InfiniteScrollGrid,
        TooltipComponent,
        SelectItemPipe,
        ActionDropdown,
        DateTimeFormatPipe,
        Section,
        ActionLink,
        MessageComponent,
        CardDetailsGrid,
        Accordion,
        Menu,
        CardDetailsComponent,
        StaticText,
        Form,
        Link,
        CardDetailsFieldComponent,
        InPlaceEditorComponent,
        Signature,
        DataTable,
        HeaderCheckBox,
        SvgComponent,
        Image,
        TreeGrid,
        InputSwitch,
        FormGridFiller,
        DisplayValueDirective,
        InputLabel,
        Label,
        CardDetailsFieldGroupComponent
       ],
       imports: [
           FormsModule,
           ReactiveFormsModule,
           CalendarModule,
           DropdownModule,
           RadioButtonModule,
           CheckboxModule,
           ListboxModule,
           PickListModule,
           FileUploadModule,
           DataTableModule,
           GrowlModule,
           AccordionModule,
           HttpModule,
           HttpClientModule,
           TableModule,
           KeyFilterModule,
           AngularSvgIconModule,
           ToastModule,
           InputSwitchModule, 
           TreeTableModule
       ],
       providers: [
           { provide: WebContentSvc, useClass: MockWebContentSvc }
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(FrmGroupCmp);
    app = fixture.debugElement.componentInstance;
  }));

    it('should create the FrmGroupCmp', async(() => {
      expect(app).toBeTruthy();
    }));

    it('getCssClass() should return the element.config.uiStyles.attributes.cssClass', async(() => {
      app.element = { config: { uiStyles: { attributes: { alias: 'FormElementGroup', cssClass: 'test' } } } };
      expect(app.getCssClass()).toEqual('test');
    }));

    it('getCssClass() should return elementCss', async(() => {
      app.element = { config: { uiStyles: { attributes: { alias: 'FormElementGroup' } } } };
      app.elementCss = 'test';
      expect(app.getCssClass()).toEqual('test');
    }));

    it('getCssClass() should return empty string', async(() => {
      app.element = { config: { uiStyles: { attributes: { alias: 'FormElementGroup1' } } } };
      expect(app.getCssClass()).toEqual('');
    }));

});