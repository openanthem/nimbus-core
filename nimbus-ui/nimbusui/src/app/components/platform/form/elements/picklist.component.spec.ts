import { Param } from './../../../../shared/param-state';
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
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { Validators, FormControl, ValidatorFn, FormGroup } from '@angular/forms';
import { pickListElement, pickListParent } from 'mockdata';
import { By } from '@angular/platform-browser';
import { ServiceConstants } from '../../../../services/service.constants';

let pageService;

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

const declarations = [
  OrderablePickList,
  InputLabel,
  TooltipComponent
];
const imports = [
   PickListModule,
   HttpModule,
   HttpClientTestingModule,
   StorageServiceModule
];
const providers = [
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   { provide: 'JSNLOG', useValue: JL },
   { provide: PageService, useClass: MockPageService },
   CustomHttpClient,
   LoaderService,
   ConfigService,
   LoggerService,
   AppInitService,
   SessionStoreService
];

let fixture, hostComponent;

describe('OrderablePickList', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderablePickList);
    hostComponent = fixture.debugElement.componentInstance;
    const fg = new FormGroup({});
    const checks: ValidatorFn[] = [];
    checks.push(Validators.required);
    fg.addControl(pickListElement.config.code, new FormControl(pickListElement.leafState, checks));
    hostComponent.form = fg;
    hostComponent.element = pickListElement as Param;
    hostComponent.parent = pickListParent as Param;
    pageService = TestBed.get(PageService);
  });

    it('nm-input-label should be created if the label is configured', async(() => {
        ServiceConstants.LOCALE_LANGUAGE = 'en-US';
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const labelEle = debugElement.query(By.css('nm-input-label'));
        expect(labelEle).toBeTruthy();
    }));

    it('p-picklist should be created', async(() => {
        ServiceConstants.LOCALE_LANGUAGE = 'en-US';
        fixture.detectChanges();
        const debugElement = fixture.debugElement;
        const pickListEle = debugElement.query(By.css('p-picklist'));
        expect(pickListEle).toBeTruthy();
    }));

    it('ngOnInit() should call refreshSourceList(), form.controls.b.reset and update targetList', () => {
        fixture.whenStable().then(() => {
            (hostComponent as any).loadLabelConfigFromConfigs = () => { };
            spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
            (hostComponent as any).refreshSourceList = () => { };
            hostComponent.element.leafState = null;
            hostComponent.element.activeValidationGroups = [];
            hostComponent.element.path = 't';
            const parent: any = { values: [], path: 'test', labels: '', config: { code: 'a' } };
            hostComponent.parent = parent;
            const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
            spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
            spyOn(hostComponent.form.controls.firstName, 'reset').and.callThrough();
            hostComponent.ngOnInit();
            pageService.logError({ path: 'test', leafState: null });
            expect(spy).toHaveBeenCalledTimes(2);
            expect(hostComponent.targetList).toEqual([]);
            expect(hostComponent.form.controls.firstName.reset).toHaveBeenCalled();
        });
    });

    it('ngOnInit() should call refreshSourceList, ValidationUtils.rebindValidations, ValidationUtils.assessControlValidation and update targetList', async(() => {
        (hostComponent as any).loadLabelConfigFromConfigs = () => { };
        spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
        (hostComponent as any).refreshSourceList = () => { };
        hostComponent.element.leafState = null;
        hostComponent.element.activeValidationGroups = [];
        hostComponent.element.path = 'test'
        const parent: any = { values: [], path: 'test', labels: '', config: { code: 'a' } };
        hostComponent.parent = parent;
        const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
        spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
        spyOn(ValidationUtils, 'assessControlValidation').and.returnValue('true');
        hostComponent.ngOnInit();
        pageService.buildBaseURL({
            path: 'test',
            leafState: null,
            activeValidationGroups: ['a']
        });
        expect(spy).toHaveBeenCalled();
        expect(hostComponent.targetList).toEqual([]);
        expect(ValidationUtils.rebindValidations).toHaveBeenCalled();
        expect(ValidationUtils.assessControlValidation).toHaveBeenCalled();
    }));

    it('should create the OrderablePickList', async(() => {
        expect(hostComponent).toBeTruthy();
    }));

    it('setState() should update the frmInp.element.leafState', async(() => {
        const frmInp = { element: { leafState: '' } };
        hostComponent.setState('t', frmInp);
        expect(frmInp.element.leafState).toEqual('t');
    }));

    it('value property should be updated from set value()', async(() => {
        hostComponent.value = 'test';
        expect(hostComponent.value).toEqual('test');
    }));


    it('dragStart() should update the draggedItm property', async(() => {
        hostComponent.element.enabled = true;
        hostComponent.dragStart('', 't');
        expect((hostComponent as any).draggedItm).toEqual('t');
    }));

    it('findIndexInList() should return 0', async(() => {
        const list: any = [{ code: 123 }];
        const item: any = { code: 123 };
        expect(hostComponent.findIndexInList(item, list)).toEqual(0);
    }));

    it('registerOnChange() should update the onChange property', async(() => {
        const onChange = () => { };
        hostComponent.registerOnChange(onChange);
        expect(hostComponent.onChange).toEqual(onChange);
    }));

    it('registerOnTouched() should update the onTouched property', async(() => {
        const onTouched = () => { };
        hostComponent.registerOnTouched(onTouched);
        expect(hostComponent.onTouched).toEqual(onTouched);
    }));

    it('setDisabledState() should update the disabled property', async(() => {
        hostComponent.setDisabledState(false);
        expect(hostComponent.disabled).toEqual(false);
    }));

    it('dragEnd() should update the pickListControl.target', async(() => {
        (hostComponent as any).draggedItm = true;
        hostComponent.targetList = [];
        const picklist: any = { source: ['a', 'b'], target: [] };
        hostComponent.pickListControl = picklist;
        spyOn(hostComponent, 'findIndexInList').and.returnValue(1);
        hostComponent.dragEnd('');
        expect(hostComponent.pickListControl.target).toEqual([true]);
    }));

    it('dragEnd() should update the pickListControl.source', async(() => {
        (hostComponent as any).draggedItm = true;
        hostComponent.targetList = [{ code: 'c' }];
        const picklist: any = { source: ['a', 'b'], target: [] };
        hostComponent.pickListControl = picklist;
        hostComponent.findIndexInList = (a, b) => {
            if (b === hostComponent.pickListControl.source) {
                return -1;
            }
            return 1;
        };
        hostComponent.dragEnd('');
        expect(hostComponent.pickListControl.source).toEqual(['a', 'b', true]);
    }));

    it('emitValueChangedEvent() should update the controlValueChanged eventemitter', () => {
        fixture.whenStable().then(() => {
            const controlValueChanged: any = { emit: () => { } };
            hostComponent.controlValueChanged = controlValueChanged;
            spyOn(hostComponent.controlValueChanged, 'emit').and.callThrough();
            hostComponent.form.controls[this.hostElement.element.config.code].setValue('s');
            hostComponent.emitValueChangedEvent();
            expect(hostComponent.controlValueChanged.emit).toHaveBeenCalled();
        });
    });

    it('emitValueChangedEvent() should not update the controlValueChanged eventemitter', () => {
        fixture.whenStable().then(() => {
            const controlValueChanged: any = { emit: () => { } };
            hostComponent.controlValueChanged = controlValueChanged;
            hostComponent.form.controls['firstName'].setErrors({ 'incorrect': true });
            spyOn(hostComponent.controlValueChanged, 'emit').and.callThrough();
            hostComponent.emitValueChangedEvent();
            expect(hostComponent.controlValueChanged.emit).not.toHaveBeenCalled();
        });
    });

    it('updateListValues() should call updateData', async(() => {
        (hostComponent as any).updateData = () => { };
        hostComponent.emitValueChangedEvent = () => { };
        hostComponent.element.leafState = [{ code: '2' }];
        const spy = spyOn((hostComponent as any), 'updateData').and.callThrough();
        hostComponent.updateListValues('');
        expect(spy).toHaveBeenCalled();
    }));

    it('updateListValues should update element.leafState', async(() => {
        hostComponent.emitValueChangedEvent = () => { };
        (hostComponent as any).updateData = () => { };
        hostComponent.element.leafState = false;
        hostComponent.value = true;
        hostComponent.updateListValues('');
        expect(hostComponent.element.leafState).toBeTruthy();
    }));

    it('getDesc() should return selectedvalues.label', async(() => {
        const selectedvalues: any = [{ code: 'test', label: 'tlabel' }];
        hostComponent.selectedvalues = selectedvalues;
        expect(hostComponent.getDesc('test')).toEqual('tlabel');
    }));

    it('getDesc() should return selectedvalues.code', async(() => {
        const selectedvalues: any = [{ code: 'test', label: undefined }];
        hostComponent.selectedvalues = selectedvalues;
        expect(hostComponent.getDesc('test')).toEqual('test');
    }));

    it('refreshSourceList() should update targetList', async(() => {
        hostComponent.element.leafState = ['test'];
        const parent: any = { values: [{ code: 't' }] };
        hostComponent.parent = parent;
        (hostComponent as any).refreshSourceList();
        expect(hostComponent.targetList).toEqual(['test']);
    }));

    it('updateData() should update value to null', async(() => {
        hostComponent.targetList = [];
        (hostComponent as any).updateData();
        expect(hostComponent.value).toEqual(null);
    }));

    it('updateData() should update value to array based on targetList', async(() => {
        hostComponent.targetList = [{ code: 'test' }, { a: 'b' }];
        (hostComponent as any).updateData();
        expect(hostComponent.value).toBeTruthy(['test', { a: 'b' }]);
    }));

    it('updateParentValue() should return GenericDomain based on element.config.code and targetList', async(() => {
        hostComponent.targetList = [{ code: 'test' }, { a: 'b' }];
        const selectedOptions = [];
        selectedOptions.push(hostComponent.targetList[0].code);
        selectedOptions.push(hostComponent.targetList[1]);
        const item: GenericDomain = new GenericDomain();
        item.addAttribute(hostComponent.element.config.code, selectedOptions);
        expect((hostComponent as any).updateParentValue('')).not.toEqual(undefined);
        expect((hostComponent as any).updateParentValue('')).toEqual(item);
    }));

    it('ngOnInit() should call refreshSourceList() two times', async(() => {
        (hostComponent as any).loadLabelConfigFromConfigs = () => { };
        spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
        hostComponent.form = null;
        (hostComponent as any).refreshSourceList = () => { };
        hostComponent.element.leafState = 'test';
        const parent: any = { path: '/test', labels: '', config: { code: '' } };
        hostComponent.parent = parent;
        const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
        hostComponent.ngOnInit();
        pageService.logError({ path: '/test' });
        expect(spy).toHaveBeenCalledTimes(2);
    }));

    it('ngOnInit() should call refreshSourceList() two times and update targetList', async(() => {
        (hostComponent as any).loadLabelConfigFromConfigs = () => { };
        spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
        hostComponent.form = null;
        (hostComponent as any).refreshSourceList = () => { };
        hostComponent.element.leafState = null;
        const parent: any = { path: '/test', labels: '', config: { code: '' } };
        hostComponent.parent = parent;
        const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
        hostComponent.ngOnInit();
        pageService.logError({ path: '/test' });
        expect(spy).toHaveBeenCalledTimes(2);
        expect(hostComponent.targetList).toEqual([]);
    }));

    it('ngOnInit() should call refreshSourceList() two times and update targetList, requiredCss', () => {
        fixture.whenStable().then(() => {
            (hostComponent as any).loadLabelConfigFromConfigs = () => { };
            spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
            const testSubject = new Subject();
            (hostComponent as any).refreshSourceList = () => { };
            hostComponent.element.leafState = null;
            hostComponent.element.activeValidationGroups = ['abc'];
            const parent: any = { path: '/test', labels: '', config: { code: 'a' } };
            hostComponent.parent = parent;
            const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
            spyOn(ValidationUtils, 'rebindValidations').and.returnValue(true);
            hostComponent.ngOnInit();
            pageService.logError({ path: '/test' });
            expect(spy).toHaveBeenCalledTimes(2);
            expect(hostComponent.targetList).toEqual([]);
            expect(hostComponent.requiredCss).toEqual(true);
        });
    });

    it('ngOnInit() should call refreshSourceList(), setState(), form.controls.a.setValue and update targetList, requiredCss', () => {
        fixture.whenStable().then(() => {
            (hostComponent as any).loadLabelConfigFromConfigs = () => { };
            spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
            (hostComponent as any).refreshSourceList = () => { };
            hostComponent.element.leafState = null;
            hostComponent.element.activeValidationGroups = ['abc'];
            const parent: any = { path: '/test', labels: '', config: { code: 'firstName' } };
            hostComponent.parent = parent;
            hostComponent.setState = (a, b) => { };
            (hostComponent as any).updateParentValue = a => {
                return a;
            };
            spyOn(hostComponent, 'setState').and.callThrough();
            spyOn(hostComponent.form.controls.firstName, 'setValue').and.callThrough();
            const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
            spyOn(ValidationUtils, 'rebindValidations').and.returnValue(true);
            hostComponent.ngOnInit();
            hostComponent.form.get('firstName').setValue('newValue');
            pageService.logError({ path: '/test' });
            expect(spy).toHaveBeenCalledTimes(2);
            expect(hostComponent.targetList).toEqual([]);
            expect(hostComponent.requiredCss).toEqual(true);
            expect(hostComponent.setState).toHaveBeenCalled();
            expect(hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();
        });
    });

    it('ngOnInit() should call refreshSourceList(), form.controls.b.setValue() with element.path and update targetList', () => {
        fixture.whenStable().then(() => {
            (hostComponent as any).loadLabelConfigFromConfigs = () => { };
            spyOn(ValidationUtils, 'applyelementStyle').and.returnValue(true);
            const testSubject = new Subject();
            (hostComponent as any).refreshSourceList = () => { };
            const parent: any = { path: 'test', labels: '', config: { code: 'firstName' } };
            hostComponent.parent = parent;
            const spy = spyOn((hostComponent as any), 'refreshSourceList').and.callThrough();
            spyOn(ValidationUtils, 'rebindValidations').and.returnValue('true');
            spyOn(hostComponent.form.controls.firstName, 'setValue').and.callThrough();
            hostComponent.element.path = 't';
            hostComponent.ngOnInit();
            pageService.logError({ path: 'test', leafState: 't' });
            expect(spy).toHaveBeenCalledTimes(2);
            expect(hostComponent.targetList).toEqual([]);
            expect(hostComponent.form.controls.firstName.setValue).toHaveBeenCalled();
            expect(hostComponent.form.controls.firstName.setValue).toHaveBeenCalledWith('t');
        });
    });

});
