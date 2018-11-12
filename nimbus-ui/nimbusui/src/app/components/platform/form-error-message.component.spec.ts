import { By } from '@angular/platform-browser';
/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, ValidatorFn, Validators, FormGroup, FormControl } from '@angular/forms';
import { DropdownModule, GrowlModule, MessagesModule, DialogModule, AccordionModule, 
    DataTableModule, FileUploadModule, PickListModule, ListboxModule, CheckboxModule, 
    RadioButtonModule, CalendarModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';

import { Form } from './form.component';
import { FrmGroupCmp } from './form-group.component';
import { Accordion } from '../platform/content/accordion.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Button } from '../platform/form/elements/button.component';
import { FormElement } from './form-element.component';
import { MessageComponent } from './message/message.component';
import { DataTable } from './grid/table.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { DateControl } from '../platform/form/elements/date.component';
import { TextArea } from '../platform/form/elements/textarea.component';
import { Signature } from '../platform/form/elements/signature.component'
import { InputText } from '../platform/form/elements/textbox.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { Header } from './content/header.component';
import { Section } from './section.component';
import { ActionDropdown } from '../platform/form/elements/action-dropdown.component';
import { TooltipComponent } from '../platform/tooltip/tooltip.component';
import { SelectItemPipe } from '../../pipes/select-item.pipe';
import { Menu } from '../platform/menu.component';
import { Link } from '../platform/link.component';
import { StaticText } from '../platform/content/static-content.component';
import { CardDetailsComponent } from '../platform/card/card-details.component';
import { CardDetailsGrid } from '../platform/card/card-details-grid.component';
import { ActionLink } from '../platform/form/elements/action-dropdown.component';
import { CardDetailsFieldComponent } from '../platform/card/card-details-field.component';
import { InPlaceEditorComponent } from '../platform/form/elements/inplace-editor.component';
import { DateTimeFormatPipe } from '../../pipes/date.pipe';
import { PageService } from '../../services/page.service';
import { CustomHttpClient } from '../../services/httpclient.service';
import { LoaderService } from '../../services/loader.service';
import { ConfigService } from '../../services/config.service';
import { LoggerService } from '../../services/logger.service';
import { FormElementsService } from './form-builder.service';
import { Subject } from 'rxjs';
import { Model } from '../../shared/param-state';
import { SessionStoreService, CUSTOM_STORAGE } from '../../services/session.store';
import { AppInitService } from '../../services/app.init.service';
import { HeaderCheckBox } from '../platform/form/elements/header-checkbox.component';
import { SvgComponent } from './svg/svg.component';
import { Image } from './image.component';
import { TreeGrid } from './tree-grid/tree-grid.component';
import { InputSwitch } from './form/elements/input-switch.component';
import { FormGridFiller } from './form/form-grid-filler.component';
import { ValueStylesDirective } from '../../directives/value-styles.directive';
import { InputLabel } from './form/elements/input-label.component';
import { Label } from './content/label.component';
import { CardDetailsFieldGroupComponent } from './card/card-details-field-group.component';
import { InputLegend } from './form/elements/input-legend.component';
import { setup, TestContext } from './../../setup.spec';
import { Param } from './../../shared/param-state';
import { FormErrorMessage } from './form-error-message.component';
/**
 * \@author Sandeep.Mantha
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
var payload: string;

describe("form error message component", () => {
    payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';
    let param: Param = JSON.parse(payload);

    setup();

    beforeEach(async function(this: TestContext<FormErrorMessage>) {
        let declarations = [ Form,
            FrmGroupCmp,
            Accordion,
            ButtonGroup,
            Button,
            FormElement,
            MessageComponent,
            DataTable,
            FileUploadComponent,
            OrderablePickList,
            MultiselectCard,
            MultiSelectListBox,
            CheckBox,
            CheckBoxGroup,
            RadioButton,
            ComboBox,
            Calendar,
            DateControl,
            TextArea,
            Signature,
            InputText,
            Paragraph,
            Header,
            Section,
            ActionDropdown,
            TooltipComponent,
            SelectItemPipe,
            Menu,
            Link,
            StaticText,
            CardDetailsComponent,
            CardDetailsGrid,
            ActionLink,
            CardDetailsFieldComponent,
            InPlaceEditorComponent,
            DateTimeFormatPipe,
            HeaderCheckBox,
            SvgComponent,
            Image,
            TreeGrid,
            InputSwitch,
            FormGridFiller,
            ValueStylesDirective,
            InputLabel,
            FormErrorMessage,
            Label,
            CardDetailsFieldGroupComponent,
            InputLegend ];
        let imports = [   FormsModule, ReactiveFormsModule,
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
            TableModule,
            KeyFilterModule,
            HttpModule,
            HttpClientTestingModule,
            StorageServiceModule,
            AngularSvgIconModule,
            ToastModule,
            InputSwitchModule, 
            TreeTableModule ];

        this.create(FormErrorMessage, declarations);
        let fg= new FormGroup({});
        var checks: ValidatorFn[] = [];
        checks.push(Validators.required);
        param.type.model.params[0].visible = true;
        fg.addControl(param.type.model.params[0].config.code, new FormControl(param.type.model.params[0].leafState,checks));
        this.hostComponent.form = fg;
        this.hostComponent.element = param;
        
    });


    it("check if message is displayed", function(this: TestContext<FormErrorMessage>) {
        this.fixture.detectChanges();
        let messageDom = this.fixture.debugElement.query(By.css('div'));
        expect(this.hostComponent).toBeTruthy();
        expect(messageDom.nativeElement.innerHTML).toEqual(' Remaining 0 of 1 ');
             
    });

    it("check if message is re-evaluated on form value change", function(this: TestContext<FormErrorMessage>) {
        this.fixture.detectChanges();
        this.hostComponent.form.controls[param.type.model.params[0].config.code].setValue("");
        this.fixture.detectChanges();
        let messageDom = this.fixture.debugElement.query(By.css('div'));
        expect(this.hostComponent).toBeTruthy();
        expect(messageDom.nativeElement.innerHTML).toEqual(' Remaining 1 of 1 ');
             
    });
});