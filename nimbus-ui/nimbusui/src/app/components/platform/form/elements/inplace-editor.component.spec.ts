'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { Subject } from 'rxjs';

import { InPlaceEditorComponent } from './inplace-editor.component';
import { PageService } from './../../../../services/page.service';
import { InputText } from './textbox.component';
import { TextArea } from './textarea.component';
import { ComboBox } from './combobox.component';
import { TooltipComponent } from '../../tooltip/tooltip.component';
import { SelectItemPipe } from '../../../../pipes/select-item.pipe';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import * as data from '../../../../payload.json';
import { Values } from '../../../../shared/param-state';

let pageService, param;

class MockLoggerService {
    error(a) { }
    info(a) { }
    debug(a) { }
}

class MockPageService {
    eventUpdate$ : Subject<any>

    constructor() {
        this.eventUpdate$ = new Subject();
    }
    logError(a) {
        this.eventUpdate$.next(a);
    }
    postOnChange(a, b, c) {    }
}

const declarations = [
  InPlaceEditorComponent,
  InputText,
  TextArea,
  ComboBox,
  TooltipComponent,
  SelectItemPipe,
  InputLabel
];
const imports = [
   FormsModule,
   ReactiveFormsModule,
   DropdownModule,
   HttpModule,
   HttpClientTestingModule,
   StorageServiceModule
];
const providers = [
   { provide: 'JSNLOG', useValue: JL },
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   { provide: LoggerService, useClass: MockLoggerService },
   { provide: PageService, useClass: MockPageService },
   CustomHttpClient,
   LoaderService,
   ConfigService,
   SessionStoreService,
   AppInitService
];

describe('InPlaceEditorComponent', () => {

  configureTestSuite();
  setup(InPlaceEditorComponent, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<InPlaceEditorComponent>){
    this.hostComponent.element = param;
    pageService = TestBed.get(PageService);
  });

  it('should create the InPlaceEditorComponent', async function(this: TestContext<InPlaceEditorComponent>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('set value() should update the displayValue', async function(this: TestContext<InPlaceEditorComponent>) {
    this.hostComponent.value = 'firstName';
    this.hostComponent.value = '';
    expect(this.hostComponent.displayValue).toEqual('Unassigned');
  });

    it('setDisplayValue() should update the displayValue from element.values[]', async function(this: TestContext<InPlaceEditorComponent>) {
      const value = { code: 'test', label: 'l' } as Values;
      this.hostComponent.element.values = [value];
      this.hostComponent.setDisplayValue('test');
      expect(this.hostComponent.displayValue).toEqual('l');
    });

    it('setDisplayValue() should update the displayValue property as Unassigned', async function(this: TestContext<InPlaceEditorComponent>) {
      const value = { code: 'test', label: 'l' } as Values;
      this.hostComponent.element.values = [value];
      this.hostComponent.setDisplayValue(null);
      expect(this.hostComponent.displayValue).toEqual('Unassigned');
    });

    it('setDisplayValue() should update the displayValue as element.leafState', async function(this: TestContext<InPlaceEditorComponent>) {
      const value = { code: 'test', label: null } as Values;
      this.hostComponent.element.values = [value];
      this.hostComponent.element.leafState = 'ls';
      this.hostComponent.setDisplayValue('test');
      expect(this.hostComponent.displayValue).toEqual('ls');
    });

    it('ngOnInit() should call the setDisplayValue() 3 times', async function(this: TestContext<InPlaceEditorComponent>) {
      this.fixture.whenStable().then(() => {
        this.hostComponent.setDisplayValue = () => {};
        (this.hostComponent as any).generateComponent = () => {};
        this.hostComponent.element.leafState = 'l';
        this.hostComponent.element.config.uiStyles.attributes.inplaceEditType = '';
        this.hostComponent.element.config.code = '123';
        const eve = { leafState: '', config: { code: '123' } };
        spyOn(this.hostComponent, 'setDisplayValue').and.callThrough();
        this.hostComponent.ngOnInit();
        pageService.logError(eve);
        expect(this.hostComponent.setDisplayValue).toHaveBeenCalledTimes(3);
      });
    });
    
    it('generateComponent() should update the inputInstance.element and setInPlaceEditContext', async function(this: TestContext<InPlaceEditorComponent>) {
      const inputInstance = { element: '', setInPlaceEditContext: a => {} };
      spyOn((this.hostComponent as any), 'getComponentType').and.returnValue('');
      spyOn((this.hostComponent as any), 'createInputInstance').and.returnValue(inputInstance);
      spyOn(inputInstance, 'setInPlaceEditContext').and.callThrough();
      (this.hostComponent as any).generateComponent('');
      const res: any = this.hostComponent.element;
      expect(inputInstance.element).toEqual(res);
      expect(inputInstance.setInPlaceEditContext).toHaveBeenCalled();
    });

    it('getComponentType() should throw an error', async function(this: TestContext<InPlaceEditorComponent>) {
      (this.hostComponent as any).components = { a: 'test' };
      expect(() => {
        (this.hostComponent as any).getComponentType('b');
      }).toThrow();
    });

    it('getComponentType() should return component name', async function(this: TestContext<InPlaceEditorComponent>) {
      (this.hostComponent as any).components = { a: 'test' };
      expect((this.hostComponent as any).getComponentType('a')).toEqual('test');
    });

    it('get type() should update from element.config.uiStyles.attributes.inplaceEditType', async function(this: TestContext<InPlaceEditorComponent>) {
      this.fixture.whenStable().then(() => {
        this.hostComponent.element.config.uiStyles.attributes.inplaceEditType = 'test';
        expect(this.hostComponent.type).toEqual('test');
      });
    });

    it('cancel() should update the value and editClass', async function(this: TestContext<InPlaceEditorComponent>) {
      this.hostComponent.element.values = [];
      (this.hostComponent as any).preValue = 'test';
      this.hostComponent.cancel();
      expect(this.hostComponent.value).toEqual('test');
      expect(this.hostComponent.editClass).toEqual('');
    });

    it('onSubmit() should update the editClass and call pageService.postOnChange()', async function(this: TestContext<InPlaceEditorComponent>) {
      this.hostComponent.element.leafState = '';
      this.hostComponent.element.values = [];
      this.hostComponent.element.path = '';
      spyOn(pageService, 'postOnChange').and.callThrough();
      this.hostComponent.onSubmit();
      expect(this.hostComponent.editClass).toEqual('');
      expect(pageService.postOnChange).toHaveBeenCalled();
    });

    it('enableEdit() should update the preValue and editClass', async function(this: TestContext<InPlaceEditorComponent>) {
      this.hostComponent.element.leafState = '';
      this.hostComponent.element.values = [];
      this.hostComponent.value = 'test';
      this.hostComponent.enableEdit();
      expect((this.hostComponent as any).preValue).toEqual('test');
      expect(this.hostComponent.editClass).toEqual('editOn');
    });

    it('ngOnChanges() should call the setDisplayValue()', async function(this: TestContext<InPlaceEditorComponent>) {
      this.hostComponent.element.leafState = '';
      this.hostComponent.element.values = [];
      const changes: any = { element: true };
      spyOn(this.hostComponent, 'setDisplayValue').and.returnValue('');
      this.hostComponent.ngOnChanges(changes);
      expect(this.hostComponent.setDisplayValue).toHaveBeenCalled();
    });
});