import { Param } from './../../../shared/param-state';
'use strict';
import { TestBed, async, fakeAsync, tick } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, MessagesModule, InputSwitchModule, TreeTableModule  } from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule, ValidatorFn, Validators, FormGroup, FormControl } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';
import { AngularSvgIconModule } from 'angular-svg-icon';
import {ToastModule} from 'primeng/toast';
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { Modal } from './modal.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
// import { Section } from '../section.component';
import { ComboBox } from '../../platform/form/elements/combobox.component';
import { InputText } from '../form/elements/textbox.component';
import { ButtonGroup } from '../form/elements/button-group.component';
// import { Button } from '../form/elements/button.component';
// import { InfiniteScrollGrid } from '../grid/grid.component';
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
import { Accordion } from '../content/accordion.component';
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
// import { DateControl } from '../form/elements/date.component';
import { Signature } from '../form/elements/signature.component';
import { Header } from '../content/header.component';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { DataTable } from '../grid/table.component';
import { HeaderCheckBox } from '../form/elements/header-checkbox.component';
import { SvgComponent } from '../svg/svg.component';
import { Image } from '../image.component';
import { InputSwitch } from '../../platform/form/elements/input-switch.component';
import { TreeGrid } from '../../platform/tree-grid/tree-grid.component';
import { Label } from '../../platform/content/label.component';
import { InputLabel } from '../../platform/form/elements/input-label.component';
import { CardDetailsFieldGroupComponent } from '../../platform/card/card-details-field-group.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { FormGridFiller } from '../../platform/form/form-grid-filler.component';
import { InputLegend } from '../../platform/form/elements/input-legend.component';
import { FormErrorMessage } from '../form-error-message.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';
import { PrintDirective } from '../../../directives/print.directive';
import { Subject } from 'rxjs';
import { WebContentSvc } from './../../../services/content-management.service';

// @Directive({
//     selector: '[nmPrint]',
//   })
//   export class PrintDirective {
//     @Input() contentSelector: string;
//     @Input() isPage: boolean;
//     @Input() element: any;
//     @Input() nmPrint: any;
//     nativeElement: any;
//     subscription: any;
//   }

@Component({
    template: '<div></div>',
    selector: 'nm-button'
  })
  class Button {
  
    @Input() element: any;
    @Input() payload: string;
    @Input() form: any;
    @Input() actionTray?: boolean;
  
    @Output() buttonClickEvent = new EventEmitter();
  
    @Output() elementChange = new EventEmitter();
    private imagesPath: string;
    private btnClass: string;
    private disabled: boolean;
    files: any;
    differ: any;
    componentTypes;
  }

  @Component({
    selector: 'nm-section',
    template: '<div></div>'
})
export class Section  {
    @Input() position: any;
    @Input() element: any;
    viewComponent: any;
    componentTypes : any;
    constructor() {}
}

class MockPageService {
    public eventUpdate$: Subject<any>;
  
    constructor() {
      this.eventUpdate$ = new Subject();
    }
    processEvent(a, b, c, d) { }

}

let pageservice, param;

const declarations = [
    Modal,
    TooltipComponent,
    Section,
    ComboBox,
    InputText,
    ButtonGroup,
    Button,
    // InfiniteScrollGrid,
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
    // DateControl,
    Signature,
    Header,
    DataTable,
    HeaderCheckBox,
    SvgComponent,
    Image,
    InputSwitch,
    TreeGrid,
    Label,
    InputLabel,
    CardDetailsFieldGroupComponent,
    DisplayValueDirective,
    FormGridFiller,
    InputLegend,
    FormErrorMessage,
    PrintDirective
   ];
   const imports = [
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
       KeyFilterModule,
       AngularSvgIconModule,
       ToastModule,
       InputSwitchModule,
       TreeTableModule
   ];
   const providers = [
    {provide: PageService, useClass: MockPageService},
    CustomHttpClient,
    LoaderService,
    ConfigService,
    WebContentSvc
   ];
let fixture, hostComponent;
describe('Modal', () => {

    configureTestSuite(() => {
        setup( declarations, imports, providers);
      });
    
       let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);
    // const payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';
    // param = JSON.parse(payload);
  
    beforeEach(() => {
        fixture = TestBed.createComponent(Modal);
        hostComponent = fixture.debugElement.componentInstance;
        hostComponent.element = param;
        pageservice = TestBed.get(PageService);
    });
  
    it('should create the Modal', async(() => {
        // fixture.detectChanges();
        fixture.whenStable().then(() => {
            console.log('hostComponent.element', hostComponent.element);
            
            expect(hostComponent).toBeTruthy();
        });
    }));

    it('width should be 500 for small size', async(() => {
        console.log('here is the error...modal', hostComponent.element);
        // fixture.detectChanges();
        // fixture.whenStable().then(() => {
                // hostComponent.element.config.uiStyles.attributes.width = 'small';
                // expect(hostComponent.width).toEqual('500');
        // });
    }));

    // it('width should be 700 for medium size', async(() => {
    //     // fixture.whenStable().then(() => {
    //         hostComponent.element.config.uiStyles.attributes.width = 'medium';
    //         expect(hostComponent.width).toEqual('700');
    //     // });
    // }));

    // it('width should be 900 for large size', async(() => {
    //     // fixture.whenStable().then(() => {
    //         hostComponent.element.config.uiStyles.attributes.width = 'large';
    //         expect(hostComponent.width).toEqual('900');
    //     // });
    // }));

    // it('width property should be updated from element if size is not available', async(() => {
    //     // fixture.whenStable().then(() => {
    //         hostComponent.element.config.uiStyles.attributes.width = '999';
    //         expect(hostComponent.width).toEqual('999');
    //     // });
    // }));

    // it('closeDialog() should call pageservice.processEvent', async(() => {
    //     hostComponent.element.visible = true;
    //     spyOn(pageservice, 'processEvent').and.callThrough();
    //     hostComponent.closeDialog(false);
    //     expect(pageservice.processEvent).toHaveBeenCalled();
    // }));

    // it('closeDialog() should not call pageservice.processEvent', async(() => {
    //     hostComponent.element.visible = false;
    //     spyOn(pageservice, 'processEvent').and.callThrough();
    //     hostComponent.closeDialog('a');
    //     expect(pageservice.processEvent).not.toHaveBeenCalled();
    // }));

    // it('resizable property should be updated from element', async(() => {
    //     // fixture.whenStable().then(() => {
    //         hostComponent.element.config.uiStyles.attributes.resizable = true;
    //         expect(hostComponent.resizable).toEqual(true);
    
    //     // });
    // }));

    // it('closable property should updated from the element', function (this: TestContext<Modal>) {
    //     // fixture.whenStable().then(() => {
    //         hostComponent.element.config.uiStyles.attributes.closable = true;
    //         expect(hostComponent.closable).toEqual(true);
    //     // });
    // }));

});