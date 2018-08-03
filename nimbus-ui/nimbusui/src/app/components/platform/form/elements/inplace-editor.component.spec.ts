'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GrowlModule, AccordionModule, PickListModule, ListboxModule, CalendarModule, DataTableModule, DropdownModule, FileUploadModule, RadioButtonModule, CheckboxModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

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
import { Subject } from 'rxjs';

let fixture, app, pageService;

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

describe('InPlaceEditorComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          InPlaceEditorComponent,
          InputText,
          TextArea,
          ComboBox,
          TooltipComponent,
          SelectItemPipe
       ],
       imports: [
           FormsModule,
           ReactiveFormsModule,
           DropdownModule,
           HttpModule,
           HttpClientTestingModule,
           StorageServiceModule
       ],
       providers: [
           { provide: 'JSNLOG', useValue: JL },
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           { provide: LoggerService, useClass: MockLoggerService },
           { provide: PageService, useClass: MockPageService },
           CustomHttpClient,
           LoaderService,
           ConfigService,
           SessionStoreService,
           AppInitService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(InPlaceEditorComponent);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
  }));

    it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

    it('set value() should update the displayValue', async(() => {
      app.element = { values: [{ code: 'test', label: 'l' }] };
      app.value = 'test';
      app.value = '';
      expect(app.displayValue).toEqual('Unassigned');
    }));

    it('setDisplayValue() should update the displayValue from element.values[]', async(() => {
      app.element = { values: [{ code: 'test', label: 'l' }] };
      app.setDisplayValue('test');
      expect(app.displayValue).toEqual('l');
    }));

    it('setDisplayValue() should update the displayValue property as Unassigned', async(() => {
      app.element = { values: [{ code: 'test', label: 'l' }] };
      app.setDisplayValue(null);
      expect(app.displayValue).toEqual('Unassigned');
    }));

    it('setDisplayValue() should update the displayValue as element.leafState', async(() => {
      app.element = { leafState: 'ls', values: [{ code: 'test', label: null }] };
      app.setDisplayValue('test');
      expect(app.displayValue).toEqual('ls');
    }));

    it('ngOnInit() should call the setDisplayValue() 3 times', async(() => {
      app.setDisplayValue = () => {};
      app.generateComponent = () => {};
      app.element = { leafState: 'l', config: { uiStyles: { attributes: { inplaceEditType: '' } }, code: 123 } };
      const eve = { leafState: '', config: { code: 123 } };
      spyOn(app, 'setDisplayValue').and.callThrough();
      app.ngOnInit();
      pageService.logError(eve);
      expect(app.setDisplayValue).toHaveBeenCalledTimes(3);
    }));

    //   it('9should create the app', async(() => {
    //       const res = app.createInputInstance('Textarea');
    //       console.log('res///79', res);
    //     // expect(app).toBeTruthy();
    //   }));

    it('generateComponent() should update the inputInstance.element and setInPlaceEditContext', async(() => {
      app.element = 'test';
      const inputInstance = { element: '', setInPlaceEditContext: a => {} };
      spyOn(app, 'getComponentType').and.returnValue('');
      spyOn(app, 'createInputInstance').and.returnValue(inputInstance);
      spyOn(inputInstance, 'setInPlaceEditContext').and.callThrough();
      app.generateComponent('');
      expect(inputInstance.element).toEqual('test');
      expect(inputInstance.setInPlaceEditContext).toHaveBeenCalled();
    }));

    it('getComponentType() should throw an error', async(() => {
      app.components = { a: 'test' };
      expect(() => {
        app.getComponentType('b');
      }).toThrow();
    }));

    it('getComponentType() should return component name', async(() => {
      app.components = { a: 'test' };
      expect(app.getComponentType('a')).toEqual('test');
    }));

    it('get type() should update from element.config.uiStyles.attributes.inplaceEditType', async(() => {
      app.element = { values: [], config: { uiStyles: { attributes: { inplaceEditType: 'test' } } } };
      expect(app.type).toEqual('test');
    }));

    it('cancel() should update the value and editClass', async(() => {
      app.element = { values: [] };
      app.preValue = 'test';
      app.cancel();
      expect(app.value).toEqual('test');
      expect(app.editClass).toEqual('');
    }));

    it('onSubmit() should update the editClass and call pageService.postOnChange()', async(() => {
      app.element = { leafState: '', values: [], path: '' };
      spyOn(pageService, 'postOnChange').and.callThrough();
      app.onSubmit();
      expect(app.editClass).toEqual('');
      expect(pageService.postOnChange).toHaveBeenCalled();
    }));

    it('enableEdit() should update the preValue and editClass', async(() => {
      app.element = { leafState: '', values: [] };
      app.value = 'test';
      app.enableEdit();
      expect(app.preValue).toEqual('test');
      expect(app.editClass).toEqual('editOn');
    }));

    it('ngOnChanges() should call the setDisplayValue()', async(() => {
      app.element = { leafState: '', values: [] };
      const changes = { element: true };
      spyOn(app, 'setDisplayValue').and.returnValue('');
      app.ngOnChanges(changes);
      expect(app.setDisplayValue).toHaveBeenCalled();
    }));

});