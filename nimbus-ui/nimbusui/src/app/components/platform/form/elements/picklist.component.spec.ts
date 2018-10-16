'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUpload, FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule  } from 'primeng/primeng';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { of as observableOf,  Observable } from 'rxjs';
import { Subject } from 'rxjs';
import { EventEmitter } from '@angular/core';

import { OrderablePickList } from './picklist.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { InputLabel } from './input-label.component';
import { TooltipComponent } from '../../tooltip/tooltip.component';
import { GenericDomain } from '../../../../model/generic-domain.model';
import { ValidationUtils } from './../../validators/ValidationUtils';

let fixture, app, pageService;

class MockPageService {
    eventUpdate$: Subject<any>;
    validationUpdate$: Subject<any>;

    constructor() {
        this.eventUpdate$ = new Subject();
        this.validationUpdate$ = new Subject();
    }

    logError(a) {
        this.eventUpdate$.next(a);
    }

    buildBaseURL(a) {
        this.validationUpdate$.next(a);
    }

    postOnChange(a, b, c) {

    }
}

describe('OrderablePickList', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          OrderablePickList,
          InputLabel,
          TooltipComponent
       ],
       imports: [
           PickListModule,
           HttpModule,
           HttpClientTestingModule,
           StorageServiceModule
       ],
       providers: [
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           { provide: 'JSNLOG', useValue: JL },
           { provide: PageService, useClass: MockPageService },
           CustomHttpClient,
           LoaderService,
           ConfigService,
           LoggerService,
           AppInitService,
           SessionStoreService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(OrderablePickList);
    app = fixture.debugElement.componentInstance;
    pageService = TestBed.get(PageService);
  }));

    it('should create the app', async(() => {
      expect(app).toBeTruthy();
    }));

    it('setState() should update the frmInp.element.leafState', async(() => {
      const frmInp = { element: { leafState: '' } };
      app.setState('t', frmInp);
      expect(frmInp.element.leafState).toEqual('t');
    }));

    it('value property should be updated from set value()', async(() => {
      app.value = 'test';
      expect(app.value).toEqual('test');
    }));

    it('dragStart() should update the draggedItm property', async(() => {
      app.element = { enabled: true };
      app.dragStart('', 't');
      expect(app.draggedItm).toEqual('t');
    }));

    it('findIndexInList() should return 0', async(() => {
      const list = [{ code: 123 }];
      const item = { code: 123 };
      expect(app.findIndexInList(item, list)).toEqual(0);
    }));

    it('registerOnChange() should update the onChange property', async(() => {
      const onChange = () => {};
      app.registerOnChange(onChange);
      expect(app.onChange).toEqual(onChange);
    }));

    it('registerOnTouched() should update the onTouched property', async(() => {
      const onTouched = () => {};
      app.registerOnTouched(onTouched);
      expect(app.onTouched).toEqual(onTouched);
    }));

    it('setDisabledState() should update the disabled property', async(() => {
      app.setDisabledState(false);
      expect(app.disabled).toEqual(false);
    }));

    it('dragEnd() should update the pickListControl.target', async(() => {
      app.draggedItm = true;
      app.targetList = [];
      app.pickListControl = { source: ['a', 'b'], target: [] };
      spyOn(app, 'findIndexInList').and.returnValue(1);
      app.dragEnd('');
      expect(app.pickListControl.target).toEqual([true]);
    }));

    it('dragEnd() should update the pickListControl.source', async(() => {
      app.draggedItm = true;
      app.targetList = [{ code: 'c' }];
      app.pickListControl = { source: ['a', 'b'], target: [] };
      app.findIndexInList = (a, b) => {
        if (b === app.pickListControl.source) {
          return -1;
        }
        return 1;
      };
      app.dragEnd('');
      expect(app.pickListControl.source).toEqual(['a', 'b', true]);
    }));

    it('emitValueChangedEvent() should update the controlValueChanged eventemitter', async(() => {
      app.controlValueChanged = { emit: () => {} };
      app.form = { controls: { a: { valid: true } } };
      app.element = { config: { code: 'a' } };
      spyOn(app.controlValueChanged, 'emit').and.callThrough();
      app.emitValueChangedEvent();
      expect(app.controlValueChanged.emit).toHaveBeenCalled();
    }));

    it('emitValueChangedEvent() should not update the controlValueChanged eventemitter', async(() => {
      app.controlValueChanged = { emit: () => {} };
      app.form = { controls: { a: { valid: false } } };
      app.element = { config: { code: 'a' } };
      spyOn(app.controlValueChanged, 'emit').and.callThrough();
      app.emitValueChangedEvent();
      expect(app.controlValueChanged.emit).not.toHaveBeenCalled();
    }));

    it('updateListValues() should call updateData', async(() => {
      app.updateData = () => {};
      app.emitValueChangedEvent = () => {};
      app.element = { leafState: [{ code: '2' }] };
      spyOn(app, 'updateData').and.callThrough();
      app.updateListValues('');
      expect(app.updateData).toHaveBeenCalled();
    }));

    it('updateListValues should update element.leafState', async(() => {
      app.emitValueChangedEvent = () => {};
      app.updateData = () => {};
      app.element = { leafState: false };
      app.value = true;
      app.updateListValues('');
      expect(app.element.leafState).toBeTruthy();
    }));

    it('getDesc() should return selectedvalues.label', async(() => {
      app.selectedvalues = [{ code: 'test', label: 'tlabel' }];
      expect(app.getDesc('test')).toEqual('tlabel');
    }));

    it('getDesc() should return selectedvalues.code', async(() => {
      app.selectedvalues = [{ code: 'test', label: undefined }];
      expect(app.getDesc('test')).toEqual('test');
    }));

    it('refreshSourceList() should update targetList', async(() => {
      app.element = { leafState: ['test'] };
      app.parent = { values: [{ code: 't' }] };
      app.refreshSourceList();
      expect(app.targetList).toEqual(['test']);
    }));

    it('updateData() should update value to null', async(() => {
      app.targetList = [];
      app.updateData();
      expect(app.value).toEqual(null);
    }));

    it('updateData() should update value to array based on targetList', async(() => {
      app.targetList = [{ code: 'test' }, { a: 'b' }];
      app.updateData();
      expect(app.value).toBeTruthy(['test', { a: 'b' }]);
    }));

    it('updateParentValue() should return GenericDomain based on element.config.code and targetList', async(() => {
      app.targetList = [{ code: 'test' }, { a: 'b' }];
      app.element = { config: { code: 'abc' } };
      const selectedOptions = [];
      selectedOptions.push(app.targetList[0].code);
      selectedOptions.push(app.targetList[1]);
      const item: GenericDomain = new GenericDomain();
      item.addAttribute(app.element.config.code, selectedOptions);
      expect(app.updateParentValue('')).not.toEqual(undefined);
      expect(app.updateParentValue('')).toEqual(item);
    }));

    it('ngOnInit() should call refreshSourceList() two times', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      app.form = null;
      app.refreshSourceList = () => {};
      app.element = { leafState: 'test' };
      app.parent = { path: '/test', labels: '', config: { code: '' } };
      spyOn(app, 'refreshSourceList').and.callThrough();
      app.ngOnInit();
      pageService.logError({ path: '/test' });
      expect(app.refreshSourceList).toHaveBeenCalledTimes(2);
    }));

    it('ngOnInit() should call refreshSourceList() two times and update targetList', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      app.form = null;
      app.refreshSourceList = () => {};
      app.element = { leafState: null };
      app.parent = { path: '/test', labels: '', config: { code: '' } };
      spyOn(app, 'refreshSourceList').and.callThrough();
      app.ngOnInit();
      pageService.logError({ path: '/test' });
      expect(app.refreshSourceList).toHaveBeenCalledTimes(2);
      expect(app.targetList).toEqual([]);
    }));

    it('ngOnInit() should call refreshSourceList() two times and update targetList, requiredCss', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      app.form = { controls: { a: 'a1', b: { valueChanges: testSubject } } };
      app.refreshSourceList = () => {};
      app.element = { leafState: null, activeValidationGroups: ['abc'], config: { code: 'b' } };
      app.parent = { path: '/test', labels: '', config: { code: 'a' } };

      spyOn(app, 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      app.ngOnInit();
      pageService.logError({ path: '/test' });
      expect(app.refreshSourceList).toHaveBeenCalledTimes(2);
      expect(app.targetList).toEqual([]);
      expect(app.requiredCss).toEqual('true');
    }));

    it('ngOnInit() should call refreshSourceList(), setState(), form.controls.a.setValue and update targetList, requiredCss', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      app.form = { controls: { a: { setValue: (a, b) => {} }, b: { valueChanges: testSubject } } };
      app.refreshSourceList = () => {};
      app.element = { leafState: null, activeValidationGroups: ['abc'], config: { code: 'b' } };
      app.parent = { path: '/test', labels: '', config: { code: 'a' } };
      app.setState = (a, b) => {};
      app.updateParentValue = a => {
        return a;
      };
      spyOn(app, 'setState').and.callThrough();
      spyOn(app.form.controls.a, 'setValue').and.callThrough();
      spyOn(app, 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      app.ngOnInit();
      testSubject.next('');
      pageService.logError({ path: '/test' });
      expect(app.refreshSourceList).toHaveBeenCalledTimes(2);
      expect(app.targetList).toEqual([]);
      expect(app.requiredCss).toEqual('true');
      expect(app.setState).toHaveBeenCalled();
      expect(app.form.controls.a.setValue).toHaveBeenCalled();
    }));

    it('ngOnInit() should call refreshSourceList(), form.controls.b.setValue() with element.path and update targetList', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      app.form = { controls: { a: 'a1', b: { valueChanges: testSubject, setValue: a => {}, reset: () => {} } } };
      app.refreshSourceList = () => {};
      app.element = { leafState: null, activeValidationGroups: [], config: { code: 'b' }, path: 't' };
      app.parent = { path: 'test', labels: '', config: { code: 'a' } };

      spyOn(app, 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      spyOn(app.form.controls.b, 'setValue').and.callThrough();
      app.ngOnInit();
      pageService.logError({ path: 'test', leafState: 't' });
      expect(app.refreshSourceList).toHaveBeenCalledTimes(2);
      expect(app.targetList).toEqual([]);
      expect(app.form.controls.b.setValue).toHaveBeenCalled();
      expect(app.form.controls.b.setValue).toHaveBeenCalledWith('t');
    }));

    it('ngOnInit() should call refreshSourceList(), form.controls.b.reset and update targetList', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      app.form = { controls: { a: 'a1', b: { valueChanges: testSubject, setValue: a => {}, reset: () => {} } } };
      app.refreshSourceList = () => {};
      app.element = { leafState: null, activeValidationGroups: [], config: { code: 'b' }, path: 't' };
      app.parent = { path: 'test', labels: '', config: { code: 'a' } };

      spyOn(app, 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      spyOn(app.form.controls.b, 'reset').and.callThrough();
      app.ngOnInit();
      pageService.logError({ path: 'test', leafState: null });
      expect(app.refreshSourceList).toHaveBeenCalledTimes(2);
      expect(app.targetList).toEqual([]);
      expect(app.form.controls.b.reset).toHaveBeenCalled();
    }));

    it('ngOnInit() should call refreshSourceList, ValidationUtils.rebindValidations, ValidationUtils.assessControlValidation and update targetList', async(() => {
      app.loadLabelConfigFromConfigs = () => {};
      spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
      const testSubject = new Subject();
      app.form = { controls: { a: 'a1', b: { valueChanges: testSubject, setValue: a => {}, reset: () => {} } } };
      app.refreshSourceList = () => {};
      app.element = { leafState: null, activeValidationGroups: [], config: { code: 'b' }, path: 'test' };
      app.parent = { path: 'test', labels: '', config: { code: 'a' } };

      spyOn(app, 'refreshSourceList').and.callThrough();
      spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
      spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('true');
      spyOn(app.form.controls.b, 'reset').and.callThrough();
      app.ngOnInit();
      pageService.buildBaseURL({
        path: 'test',
        leafState: null,
        activeValidationGroups: ['a']
      });
      expect(app.refreshSourceList).toHaveBeenCalled();
      expect(app.targetList).toEqual([]);
      expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
      expect(ValidationUtils.assessControlValidation).toHaveBeenCalled();
    }));

});