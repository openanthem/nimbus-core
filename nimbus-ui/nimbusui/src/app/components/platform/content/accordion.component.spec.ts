'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { JL } from 'jsnlog';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
  FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
  ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
  import { TableModule } from 'primeng/table';
  import { KeyFilterModule } from 'primeng/keyfilter';
  import { ToastModule } from 'primeng/toast';

import { Accordion } from './accordion.component';
import { CardDetailsComponent } from '../card/card-details.component';
import { Link } from '../link.component';
import { CardDetailsFieldComponent } from '../card/card-details-field.component';
import { StaticText } from '../content/static-content.component';
import { InPlaceEditorComponent } from '../form/elements/inplace-editor.component';
import { InputText } from '../form/elements/textbox.component';
import { TextArea } from '../form/elements/textarea.component';
import { ComboBox } from '../form/elements/combobox.component';
import { DateTimeFormatPipe } from '../../../pipes/date.pipe';
import { TooltipComponent } from '../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../pipes/select-item.pipe';
import { WebContentSvc } from '../../../services/content-management.service';
import { PageService } from '../../../services/page.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoaderService } from '../../../services/loader.service';
import { ConfigService } from '../../../services/config.service';
import { LoggerService } from '../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../services/session.store';
import { AppInitService } from '../../../services/app.init.service';
import { Image } from '../image.component';
import { Location, LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FrmGroupCmp } from '../form-group.component';
import { SvgComponent } from '../svg/svg.component';
import { FormElement } from '../form-element.component';
import { Button } from '../form/elements/button.component';
import { ButtonGroup } from '../form/elements/button-group.component';
import { Signature } from '../form/elements/signature.component';
import { DateControl } from '../form/elements/date.component';
import { Calendar } from '../form/elements/calendar.component';
import { RadioButton } from '../form/elements/radio.component';
import { CheckBoxGroup } from '../form/elements/checkbox-group.component';
import { CheckBox } from '../form/elements/checkbox.component';
import { MultiSelectListBox } from '../form/elements/multi-select-listbox.component';
import { MultiselectCard } from '../form/elements/multi-select-card.component';
import { OrderablePickList } from '../form/elements/picklist.component';
import { FileUploadComponent } from '../fileupload/file-upload.component';
import { DataTable } from '../grid/table.component';
import { MessageComponent } from '../message/message.component';
import { Header } from '../content/header.component';
import { Paragraph } from '../content/paragraph.component';
import { HeaderCheckBox } from '../form/elements/header-checkbox.component';
import { ActionDropdown } from '../form/elements/action-dropdown.component';
import { Section } from '../section.component';
import { ActionLink } from '../form/elements/action-dropdown.component';
import { CardDetailsGrid } from '../card/card-details-grid.component';
import { Menu } from '../menu.component';
import { Form } from '../form.component';
import { Label } from './label.component';
import { CardDetailsFieldGroupComponent } from '../card/card-details-field-group.component';
import { DisplayValueDirective } from '../../../directives/display-value.directive';
import { InputLabel } from '../form/elements/input-label.component';
import { TreeGrid } from '../tree-grid/tree-grid.component';
import { FormGridFiller } from '../form/form-grid-filler.component';
import { InputSwitch } from '../form/elements/input-switch.component';
import { InputLegend } from '../form/elements/input-legend.component';

let pageService;

class MockWebContentSvc {
  findLabelContent(param) {
    const test = {
      text: 'testing'
    };
    return test;
  }
}

class MockPageService {
  processEvent(a, b, c, d) {  }
}

describe('Accordion', () => {
  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        declarations: [
          Accordion,
          CardDetailsComponent,
          Link,
          CardDetailsFieldComponent,
          StaticText,
          InPlaceEditorComponent,
          InputText,
          TextArea,
          ComboBox,
          DateTimeFormatPipe,
          TooltipComponent,
          SelectItemPipe,
          Image,
          FrmGroupCmp,
          SvgComponent,
          FormElement,
          Button,
          ButtonGroup,
          Signature,
          DateControl,
          Calendar,
          RadioButton,
          CheckBoxGroup,
          CheckBox,
          MultiSelectListBox,
          MultiselectCard,
          OrderablePickList,
          FileUploadComponent,
          DataTable,
          MessageComponent,
          Header,
          Paragraph,
          HeaderCheckBox,
          ActionDropdown,
          Section,
          ActionLink,
          CardDetailsGrid,
          Menu,
          Form,
          Label,
          CardDetailsFieldGroupComponent,
          DisplayValueDirective,
          InputLabel,
          TreeGrid,
          FormGridFiller,
          InputSwitch,
          InputLegend
        ],
        imports: [
          FormsModule,
          HttpModule,
          HttpClientModule,
          StorageServiceModule,
          AngularSvgIconModule,
          ReactiveFormsModule,
          DataTableModule, 
          SharedModule, 
          OverlayPanelModule, 
          PickListModule, 
          DragDropModule, 
          CalendarModule, 
          FileUploadModule, 
          ListboxModule, 
          DialogModule, 
          CheckboxModule, 
          DropdownModule, 
          RadioButtonModule, 
          ProgressBarModule, 
          ProgressSpinnerModule, 
          AccordionModule, 
          GrowlModule,
          TableModule,
          KeyFilterModule,
          ToastModule,
          InputSwitchModule, 
          TreeTableModule
        ],
        providers: [
          { provide: WebContentSvc, useClass: MockWebContentSvc },
          { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
          { provide: 'JSNLOG', useValue: JL },
          { provide: LocationStrategy, useClass: HashLocationStrategy },
          { provide: PageService, useClass: MockPageService },
          // PageService,
          CustomHttpClient,
          LoaderService,
          ConfigService,
          LoggerService,
          SessionStoreService,
          AppInitService,
          Location
        ]
      }).compileComponents();
      pageService = TestBed.get(PageService);
    })
  );

  it('should create the Accordion', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('get multiple() should return the this.element.config.uiStyles.attributes.multiple value', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    app.element = { config: { uiStyles: { attributes: { multiple: false } } } };
    expect(app.multiple).toEqual(false);
  }));

  it('closeAll should clear the index array', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    app.accordion = { tabs: true };
    app.index = [1, 2, 3];
    app.closeAll();
    expect(app.index).toEqual([-1]);
  }));

  it('closeAll should not clear the index array', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    app.accordion = { tabs: false };
    app.index = [1, 2, 3];
    app.closeAll();
    expect(app.index).toEqual([1, 2, 3]);
  }));

  it('openAll() should update index array', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    app.index = [];
    app.accordion = { tabs: [1, 2, 3] };
    app.openAll();
    expect(app.index.length).toEqual(3);
  }));

  it('openAll() should not update index array', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    app.index = [];
    app.accordion = {};
    app.openAll();
    expect(app.index.length).toEqual(0);
  }));

  it('processOnClick() should call processEvent()', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    spyOn(pageService, 'processEvent').and.callThrough();
    app.processOnClick({});
    expect(pageService.processEvent).toHaveBeenCalled();
  }));

  it('getImageSrc() should return imgSrc', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { info: 'test' } } } }] } } };
    expect(app.getImageSrc(tab)).toEqual('test');
  }));

  it('getImageSrc() should return undefined', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'Image', visible: false, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }] } } };
    expect(app.getImageSrc(tab)).toBeFalsy();
  }));

  it('getImageSrc() should return leafState', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: 'test', config: { uiStyles: { attributes: { imgSrc: '' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test' } } } }] } } };
    expect(app.getImageSrc(tab)).toEqual('test');
  }));

  it('getImageType() should return type, getTitle() should return title and getcssClass() should return cssClass', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'Image', visible: true, leafState: false, config: { uiStyles: { attributes: { imgSrc: 'test', type: 'testingType', title: 'testingTitle', cssClass: 'testingCssClass' } } } }, { alias: 'tabInfo123', visible: true, leafState: false, config: { uiStyles: { attributes: { info: 'test' } } } }] } } };
    expect(app.getImageType(tab)).toEqual('testingType');
    expect(app.getTitle(tab)).toEqual('testingTitle');
    expect(app.getcssClass(tab)).toEqual('testingCssClass');
  }));

  it('ngOnInit() should call updatePositionWithNoLabel()', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    app.updatePositionWithNoLabel = () => {};
    spyOn(app, 'updatePositionWithNoLabel').and.callThrough();
    app.ngOnInit();
    expect(app.updatePositionWithNoLabel).toHaveBeenCalled();
  }));

  it('getInfoText() should return undefined', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'TabInfo', visible: false, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
    expect(app.getInfoText(tab)).toEqual(undefined);
  }));

  it('getInfoText() should return tab.type.model.params[0].config.uiStyles.attributes.info', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'TabInfo', visible: true, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
    expect(app.getInfoText(tab)).toEqual('info');
  }));

  it('getInfoText() should return tab.type.model.params[0].leafState', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'TabInfo', leafState: 'leafState', visible: true, config: { uiStyles: { attributes: { info: 'info' } } } }] } } };
    expect(app.getInfoText(tab)).toEqual('leafState');
  }));

  it('getTabInfoClass() should return tab.type.model.params[0].config.uiStyles.attributes.cssClass', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'TabInfo', config: { uiStyles: { attributes: { cssClass: 'cssClass' } } } }] } } };
    expect(app.getTabInfoClass(tab)).toEqual('cssClass');
  }));

  it('getTabInfoClass() should return nm-accordion-headertext', async(() => {
    const fixture = TestBed.createComponent(Accordion);
    const app = fixture.debugElement.componentInstance;
    const tab = { type: { model: { params: [{ alias: 'TabInfo', config: { uiStyles: { attributes: {} } } }] } } };
    expect(app.getTabInfoClass(tab)).toEqual('nm-accordion-headertext');
  }));

});
