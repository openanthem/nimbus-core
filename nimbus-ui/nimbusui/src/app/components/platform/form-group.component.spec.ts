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
import { Component, Input, Output, ViewChild, EventEmitter, ViewChildren } from '@angular/core';

import { FrmGroupCmp } from './form-group.component';
import { FormElement } from './form-element.component';
// import { Button } from '../platform/form/elements/button.component';
import { ButtonGroup } from '../platform/form/elements/button-group.component';
import { Header } from '../platform/content/header.component';
import { Paragraph } from '../platform/content/paragraph.component';
import { InputText } from '../platform/form/elements/textbox.component';
import { TextArea } from '../platform/form/elements/textarea.component';
// import { DateControl } from '../platform/form/elements/date.component';
import { Calendar } from '../platform/form/elements/calendar.component';
import { ComboBox } from '../platform/form/elements/combobox.component';
import { RadioButton } from '../platform/form/elements/radio.component';
import { CheckBoxGroup } from '../platform/form/elements/checkbox-group.component';
import { CheckBox } from '../platform/form/elements/checkbox.component';
import { MultiSelectListBox } from '../platform/form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../platform/form/elements/multi-select-card.component';
import { OrderablePickList } from '../platform/form/elements/picklist.component';
import { FileUploadComponent } from '../platform/fileupload/file-upload.component';
// import { InfiniteScrollGrid } from '../platform/grid/grid.component';
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
import { InputLegend } from './form/elements/input-legend.component';
import { FormErrorMessage } from './form-error-message.component';
import { setup, TestContext } from './../../setup.spec';
import { configureTestSuite } from 'ng-bullet';
import * as data from '../../payload.json';
import { Param } from '../../shared/param-state';
import { PrintDirective } from '../../directives/print.directive';

let param: Param;

class MockWebContentSvc {
    findLabelContent(param) {
        const test = {
            text: 'tText',
            helpText: 'tHelpText'
        }
        return test;
    }
}

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

const declarations = [
  FrmGroupCmp,
  FormElement,
  Button,
  ButtonGroup,
  Header,
  Paragraph,
  InputText,
  TextArea,
  // DateControl,
  Calendar,
  ComboBox,
  RadioButton,
  CheckBoxGroup,
  CheckBox,
  MultiSelectListBox,
  MultiselectCard,
  OrderablePickList,
  FileUploadComponent,
  // InfiniteScrollGrid,
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
  CardDetailsFieldGroupComponent,
  InputLegend,
  FormErrorMessage,
  PrintDirective
 ];
 const imports = [
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
 ];
 const providers = [
     { provide: WebContentSvc, useClass: MockWebContentSvc }
 ];

 let fixture, hostComponent;
 
describe('FrmGroupCmp', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

     let payload = '{\"activeValidationGroups\":[], \"config\":{\"code\":\"firstName\",\"desc\":{\"help\":\"firstName\",\"hint\":\"firstName\",\"label\":\"firstName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":true,\"name\":\"string\",\"collection\":false,\"model\": {"\params\":[{\"activeValidationGroups\":[], \"config\":{\"code\":\"nestedName\",\"desc\":{\"help\":\"nestedName\",\"hint\":\"nestedName\",\"label\":\"nestedName\"},\"validation\":{\"constraints\":[{\"name\":\"NotNull\",\"value\":null,\"attribute\":{\"groups\": []}}]},\"values\":[],\"uiNatures\":[],\"enabled\":true,\"visible\":true,\"uiStyles\":{\"isLink\":false,\"isHidden\":false,\"name\":\"ViewConfig.TextBox\",\"value\":null,\"attributes\":{\"hidden\":false,\"readOnly\":false,\"alias\":\"TextBox\",\"labelClass\":\"anthem-label\",\"type\":\"text\",\"postEventOnChange\":false,\"controlId\":\"\"}},\"postEvent\":false},\"type\":{\"nested\":false,\"name\":\"string\",\"collection\":false},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/nestedName\"}]}},\"leafState\":\"testData\",\"path\":\"/page/memberSearch/memberSearch/memberSearch/firstName\"}';     let param: Param = JSON.parse(payload);

  beforeEach( () => {
    fixture = TestBed.createComponent(FrmGroupCmp);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = param;
  });

  it('should create the FrmGroupCmp',  async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('getCssClass() should return the element.config.uiStyles.attributes.cssClass',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test';
      expect(hostComponent.getCssClass()).toEqual('test');
    });
  });


  it('getCssClass() should return elementCss',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup';
      hostComponent.element.config.uiStyles.attributes.cssClass = 'test1';
      expect(hostComponent.getCssClass()).toEqual('test1');
    });
  });

  it('getCssClass() should return empty string',  () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.alias = 'FormElementGroup1';
      expect(hostComponent.getCssClass()).toEqual('');
    });
  });

  it('Span should be created if the element.visible==true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Span should not be created if the element.visible!==true', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Span should get the css class from getCssClass()', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('fieldset should be created if element?.config?.uiStyles?.attributes?.alias == FormElementGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('fieldset should not be created if element?.config?.uiStyles?.attributes?.alias !== FormElementGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-input-legend inside should be created', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Nested nm-frm-grp should be created based on the element.type.model.params', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('Nested nm-frm-grp should not be created based on the element.type.model.params', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-element should be created based on the condition !element.type?.model?.params?.length', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-element should be created based on the condition element.config?.type?.collection', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-element should not be created based on the condition element.config?.type?.collection or !element.type?.model?.params?.length', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-element should be created if element?.config?.uiStyles?.attributes?.alias == Picklist', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-element should not be created if element?.config?.uiStyles?.attributes?.alias !== Picklist', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-button should be created if element.config?.uiStyles?.attributes?.alias == Button', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-button should not be created if element.config?.uiStyles?.attributes?.alias !== Button', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-form-grid-filler should be created if element?.config?.uiStyles?.attributes?.alias == FormGridFiller', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-form-grid-filler should not be created if element?.config?.uiStyles?.attributes?.alias !== FormGridFiller', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-link should be created if element.config?.uiStyles?.attributes?.alias == Link', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-link should not be created if element.config?.uiStyles?.attributes?.alias !== Link', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-paragraph should be created if element.config?.uiStyles?.attributes?.alias == Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-paragraph should not be created if element.config?.uiStyles?.attributes?.alias !== Paragraph', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-header should be created if element.config?.uiStyles?.attributes?.alias == Header', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-header should not be created if element.config?.uiStyles?.attributes?.alias !== Header', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-button-group should be created if element.config?.uiStyles?.attributes?.alias == ButtonGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

  it('nm-button-group should not be created if element.config?.uiStyles?.attributes?.alias !== ButtonGroup', async(() => {
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
  }));

});