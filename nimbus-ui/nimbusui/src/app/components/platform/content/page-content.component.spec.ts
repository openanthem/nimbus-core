'use strict';
import { TestBed, async } from '@angular/core/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule  } from 'primeng/primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing'
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { ActivatedRoute, Route, ActivatedRouteSnapshot, UrlSegment, Params, Data, ParamMap } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { TableModule } from 'primeng/table';
import { KeyFilterModule } from 'primeng/keyfilter';

import { PageContent } from './page-content.component';
import { Tile } from '../tile.component';
import { MessageComponent } from '../message/message.component';
import { Modal } from '../modal/modal.component';
import { Section } from '../section.component';
import { Header } from './header.component';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { ComboBox } from '../form/elements/combobox.component';
import { InputText } from '../form/elements/textbox.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { InfiniteScrollGrid } from '../grid/grid.component';
import { AccordionMain } from './accordion.component';
import { Menu } from '../menu.component';
import { Link } from '../link.component';
import { Form } from '../form.component';
import { StaticText } from './static-content.component';
import { Button } from '../form/elements/button.component';
import { Paragraph } from './paragraph.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { FrmGroupCmp } from '../form-group.component';
import { AccordionGroup } from '../accordion-group.component';
import { Accordion } from '../accordion.component';
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
import { WebContentSvc } from './../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { DataTable } from '../grid/table.component';

export class MockActivatedRoute implements ActivatedRoute {
  snapshot: ActivatedRouteSnapshot;
  url: Observable<UrlSegment[]>;
  params: Observable<Params>;
  queryParams: Observable<Params>;
  fragment: Observable<string>;
  outlet: string;
  component: any;
  routeConfig: Route;
  root: ActivatedRoute;
  parent: ActivatedRoute;
  firstChild: ActivatedRoute;
  children: ActivatedRoute[];
  pathFromRoot: ActivatedRoute[];
  data = Observable.of({
    page: {
      type: {
        model: {
          params: [{
            config: {
              uiStyles: {
                attributes: {
                  alias: 'Tile'
                }
              }
            }
          }]
        }
      }
    }
  });
  paramMap: Observable<ParamMap>;
  queryParamMap: Observable<ParamMap>;
}

class MockWebContentSvc {
  findLabelContent(a) {
    return {
      text: 213,
      helpText: 345
    };
  }
}

describe('PageContent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PageContent,
        Tile,
        MessageComponent,
        Modal,
        Section,
        Header,
        TooltipComponent,
        ComboBox,
        InputText,
        ButtonGroup,
        InfiniteScrollGrid,
        AccordionMain,
        Menu,
        Link,
        Form,
        StaticText,
        Button,
        Paragraph,
        CardDetailsComponent,
        CardDetailsGrid,
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
        DataTable
       ],
       imports: [
        GrowlModule,
        DialogModule,
        FormsModule,
        DropdownModule,
        DataTableModule,
        AccordionModule,
        ReactiveFormsModule,
        FileUploadModule,
        PickListModule,
        ListboxModule,
        CheckboxModule,
        RadioButtonModule,
        CalendarModule,
        RouterTestingModule,
        HttpClientModule,
        HttpModule,
        TableModule,
        KeyFilterModule
       ],
       providers: [
        {provide: WebContentSvc, useClass: MockWebContentSvc},
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        PageService,
        CustomHttpClient,
        LoaderService,
        ConfigService,
        LoggerService
       ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(PageContent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

 it('ngOnInit() should update element and titleList', async(() => {
    const fixture = TestBed.createComponent(PageContent);
    const app = fixture.debugElement.componentInstance;
    const activatedRoute = TestBed.get(ActivatedRoute);
    app.ngOnInit();
    const test1 = {
      type: {
        model: {
          params: [{
            config: {
              uiStyles: {
                attributes: {
                  alias: 'Tile'
                }
              }
            }
          }]
        }
      }
    };
    expect(app.element).toEqual(test1);
  }));

});