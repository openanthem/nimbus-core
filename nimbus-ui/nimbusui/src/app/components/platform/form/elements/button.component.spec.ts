import { Param } from './../../../../shared/param-state';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Location } from '@angular/common';
import { RouterTestingModule } from '@angular/router/testing';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { of as observableOf,  Observable } from 'rxjs';

import { Button } from './button.component';
import { PageService } from '../../../../services/page.service';
import { CustomHttpClient } from '../../../../services/httpclient.service';
import { LoaderService } from '../../../../services/loader.service';
import { ConfigService } from '../../../../services/config.service';
import { LoggerService } from '../../../../services/logger.service';
import { FileService } from '../../../../services/file.service';
import { Subject, observable } from 'rxjs';
import { SessionStoreService, CUSTOM_STORAGE } from '../../../../services/session.store';
import { AppInitService } from '../../../../services/app.init.service';
import { SvgComponent } from '../../svg/svg.component';
import { Image } from '../../image.component';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../../setup.spec';
import { UiAttribute } from '../../../../shared/param-config';
import { FormGroup, ValidatorFn, Validators, FormControl } from '@angular/forms';
import { PrintDirective } from '../../../../directives/print.directive';
import { PrintService } from '../../../../services/print.service';
import { EventPropagationDirective } from './event-propagation.directive';
import { By } from '@angular/platform-browser';
import { buttonPrimaryElement, buttonSecondaryElement, buttonDestructiveElement, buttonValidationElement, buttonPrintElement, buttonPlainElement } from 'mockdata';

let location, pageService, fileService;

class MockLocation {
    back() { }
}

class MockPageService {
    validationUpdate$: Subject<any>;
    postResponseProcessing$: Subject<any>;
    constructor() {
        this.validationUpdate$ = new Subject();
        this.postResponseProcessing$ = new Subject();
    }
    processEvent(a, b, c, d) {

    }

    logError(a) {
        this.validationUpdate$.next(a);
    }
}

class MockFileService {
  test = new Subject();
  uploadFile(a, b) {
    this.test.next('');
    return this.test;
  }
}

const declarations = [
  Button,
  SvgComponent,
  Image,
  PrintDirective,
  EventPropagationDirective
];
const imports = [
   HttpModule,
   HttpClientTestingModule,
   RouterTestingModule,
   StorageServiceModule,
   AngularSvgIconModule
];
const providers = [
   {provide: Location, useClass: MockLocation},
   {provide: PageService, useClass: MockPageService},
   { provide: 'JSNLOG', useValue: JL },
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   {provide: FileService, useClass: MockFileService},
   CustomHttpClient,
   LoaderService,
   ConfigService,
   LoggerService,
   AppInitService,
   SessionStoreService,
   PrintService
];
let fixture, hostComponent;
describe('Button', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Button);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.form = new FormGroup({
      search: new FormControl()
   });
    hostComponent.element = buttonPrimaryElement;
    pageService = TestBed.get(PageService);
    location = TestBed.get(Location);
    fileService = TestBed.get(FileService);
  });

  it('should create the Button', async(() => {
    expect(hostComponent).toBeTruthy();
  }));

  it('emitEvent() should update the location', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.browserBack = true;
      spyOn(location, 'back').and.callThrough();
      hostComponent.emitEvent('eve');
      expect(location.back).toHaveBeenCalled();
    });
  });

  it('emitEvent() should emit buttonClickEvent', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.browserBack = null;
      spyOn(hostComponent.buttonClickEvent, 'emit').and.callThrough();
      hostComponent.emitEvent('eve');
      expect(hostComponent.buttonClickEvent.emit).not.toHaveBeenCalled();
    });
  });

  it('ngOnInit() should update disabled property as false', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.enabled = true;
      hostComponent.ngOnInit();
      expect((hostComponent as any).disabled).toBeFalsy();
    });
  });

  it('checkObjectType() should return false', async(() => {
      expect(hostComponent.checkObjectType('test', { test: 'test' })).toBeFalsy();
  }));

  it('checkObjectType() should return true', async(() => {
    expect(hostComponent.checkObjectType('Object', {ttttesttest1 : 'testtest1'})).toBeTruthy();
  }));

  it('getFileParameter() should call checkObjectType()', async(() => {
    const item: any = { a: [1] };
    spyOn(hostComponent, 'checkObjectType').and.returnValue(true);
    const key = hostComponent.getFileParameter(item);
    expect(hostComponent.checkObjectType).toHaveBeenCalled();
    expect(key).toEqual('a');
  }));

  it('getFileParameter() should return null', async(() => {
    const item: any = {};
    spyOn(hostComponent, 'checkObjectType').and.returnValue(true);
    const key = hostComponent.getFileParameter(item);
    expect(key).toBeFalsy();
  }));

  it('getFileParameter() should call checkObjectType() and return null', async(() => {
    const item: any = { a: [1] };
    spyOn(hostComponent, 'checkObjectType').and.returnValue(false);
    const key = hostComponent.getFileParameter(item);
    expect(hostComponent.checkObjectType).toHaveBeenCalled();
    expect(key).toBeFalsy();
  }));

  it('getFileParameter() should call checkObjectType() and return null on empty array', async(() => {
    const item: any = { a: [] };
    spyOn(hostComponent, 'checkObjectType').and.returnValue(true);
    const key = hostComponent.getFileParameter(item);
    expect(hostComponent.checkObjectType).toHaveBeenCalled();
    expect(key).toBeFalsy();
  }));

  it('getFileParameter() should return false if a is not File', async(() => {
    const item: any = { a: [1] };
    hostComponent.checkObjectType = (a, b) => {
      if (a === 'File') {
        return false;
      }
      return true;
    };
    const key = hostComponent.getFileParameter(item);
    expect(key).toBeFalsy();
  }));

  it('onSubmit() should call pageService.processEvent() without form.value.formControl', () => {
    fixture.whenStable().then(() => {
      spyOn(pageService, 'processEvent').and.returnValue('');
      hostComponent.onSubmit();
      expect(pageService.processEvent).toHaveBeenCalled();
    });
  });

  it('onSubmit() should call pageService.processEvent() with form.value.formControl', () => {
    fixture.whenStable().then(() => {
      const files = [{} as File];
      hostComponent.form.value.fileControl = files;
      spyOn(pageService, 'processEvent').and.returnValue('');
      hostComponent.element.config.uiStyles.attributes.formReset = false;
      hostComponent.onSubmit();
      fileService.uploadFile('', '');
      expect(pageService.processEvent).toHaveBeenCalled();
    });
  });

  it('reset() should call form.reset()', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.formReset = true;
      spyOn(hostComponent.form, 'reset').and.callThrough();
      hostComponent.reset();
      expect(hostComponent.form.reset).toHaveBeenCalled();
    });
  });

  it('reset() should not call form.reset()', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.config.uiStyles.attributes.formReset = false;
      spyOn(hostComponent.form, 'reset').and.callThrough();
      hostComponent.reset();
      expect(hostComponent.form.reset).not.toHaveBeenCalled();
    });
  });

  it('getAllURLParams() should return null', async(() => {
    expect(hostComponent.getAllURLParams('www.test.com')).toBeFalsy();
  }));

  it('Button should be created if the imgSrc is not provided', async(() => {
    hostComponent.element = buttonPrimaryElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
  }));

  it('nm-image should not be created if imgSrc is not provided', async(() => {
    hostComponent.element = buttonPrimaryElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const imageEle = debugElement.query(By.css('nm-image'));
    expect(imageEle).toBeFalsy();
  }));

  it('nm-image should be created if imgSrc is provided', async(() => {
    hostComponent.element = buttonPrintElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const imageEle = debugElement.query(By.css('nm-image'));
    expect(imageEle).toBeTruthy();
  }));

  it('On click image button should call emitEvent()', async(() => {
    hostComponent.element = buttonPrintElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'emitEvent').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.emitEvent).toHaveBeenCalled();
    });
  }));

  it('Primary button should be created if style is configured as primary', async(() => {
    hostComponent.element = buttonPrimaryElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
    expect(buttonEle.nativeElement.classList[1].toString()).toEqual('btn-primary');
  }));

  it('On click primary button should call onSubmit()', async(() => {
    hostComponent.element = buttonPrimaryElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'onSubmit').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.onSubmit).toHaveBeenCalled();
    });
  }));

  it('secondary button should be created if style is configured as secondary', async(() => {
    hostComponent.element = buttonSecondaryElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
    expect(buttonEle.nativeElement.classList[1].toString()).toEqual('btn-secondary');
  }));

  it('On click secondary button should call emitEvent()', async(() => {
    hostComponent.element = buttonSecondaryElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'emitEvent').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.emitEvent).toHaveBeenCalled();
    });
  }));

  it('plain button should be created if style is configured as plain', async(() => {
    hostComponent.element = buttonPlainElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
    expect(buttonEle.nativeElement.classList[1].toString()).toEqual('btn-plain');
  }));

  it('On click plain button should call emitEvent()', async(() => {
    hostComponent.element = buttonPlainElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'emitEvent').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.emitEvent).toHaveBeenCalled();
    });
  }));

  it('destructive button should be created if style is configured as destructive', async(() => {
    hostComponent.element = buttonDestructiveElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
    expect(buttonEle.nativeElement.classList[1].toString()).toEqual('btn-delete');
  }));

  it('On click destructive button should call emitEvent()', async(() => {
    hostComponent.element = buttonDestructiveElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'emitEvent').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.emitEvent).toHaveBeenCalled();
    });
  }));

  it('validation button should be created if style is configured as validation', async(() => {
    hostComponent.element = buttonValidationElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();    
    expect(buttonEle.nativeElement.classList[1].toString()).toEqual('btn-primary');
  }));

  it('On click validation button should call emitEvent()', async(() => {
    hostComponent.element = buttonValidationElement as Param;
    hostComponent.form = new FormGroup({
      validationButton: new FormControl()
   });
   hostComponent.form.controls['validationButton'].setErrors({'incorrect': true});
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'emitEvent').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.emitEvent).toHaveBeenCalled();
    });
  }));

  it('print button should be created if style is configured as print', async(() => {
    hostComponent.element = buttonPrintElement as Param;
    hostComponent.element.config.uiStyles.attributes.imgSrc = '';
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    expect(buttonEle).toBeTruthy();
    expect(buttonEle.nativeElement.classList[1].toString()).toEqual('btn-secondary');
  }));

  it('On click print button should call emitEvent()', async(() => {
    hostComponent.element = buttonPrintElement as Param;
    fixture.detectChanges();
    const debugElement = fixture.debugElement;
    const buttonEle = debugElement.query(By.css('button'));
    spyOn(hostComponent, 'emitEvent').and.callThrough();
    buttonEle.nativeElement.click();
    fixture.whenStable().then(() => {
      expect(hostComponent.emitEvent).toHaveBeenCalled();
    });
  }));

  it('ngOnInit() should update disabled property as true based on pageService.validationUpdate$ subject', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.enabled = false;
      const eve = { path: 'test', enabled: false };
      hostComponent.ngOnInit();
      pageService.logError(eve);
      expect((hostComponent as any).disabled).toBeTruthy();
    });
  });

  it('ngOnInit() should update disabled property as false based on pageService.validationUpdate$ subject', () => {
    fixture.whenStable().then(() => {
      hostComponent.element.enabled = true;
      const eve = { path: '1test', enabled: false };
      hostComponent.ngOnInit();
      pageService.logError(eve);
      expect((hostComponent as any).disabled).toBeFalsy();
    });
  });

});
