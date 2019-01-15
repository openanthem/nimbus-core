'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FileUploadModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Subject } from 'rxjs';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

import { FileUploadComponent } from './file-upload.component';
import { FileService } from './../../../services/file.service';
import { WebContentSvc } from './../../../services/content-management.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoggerService } from './../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './../../../services/session.store';
import { AppInitService } from './../../../services/app.init.service';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { fieldValueParam } from 'mockdata';

let fileservice, param;

class MockFileService {
    public addFile$: Subject<any>;
    public errorEmitter$: Subject<any>;
    
    metaData: any;

    constructor() {
        this.addFile$ = new Subject<any>();
        this.errorEmitter$ = new Subject<any>();
    }

    uploadFile(a) {
        this.addFile$.next({test: 123});
    }
}

const declarations = [ FileUploadComponent ];
const imports = [
   FileUploadModule,
   HttpClientModule,
   HttpModule,
   StorageServiceModule
];
const providers = [
   {provide: FileService, useClass: MockFileService},
   { provide: 'JSNLOG', useValue: JL },
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   WebContentSvc,
   CustomHttpClient,
   LoggerService,
   AppInitService
];
let fixture, hostComponent;
describe('FileUploadComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FileUploadComponent);
    hostComponent = fixture.debugElement.componentInstance;
    hostComponent.element = fieldValueParam;
    fileservice = TestBed.get(FileService);
  });

  it('should create the FileUploadComponent', async(() => {
      expect(hostComponent).toBeTruthy();
  }));

  it('get _value should return value', async(() => {
    hostComponent.value = 123;
    expect(hostComponent.value).toEqual(123);
  }));

  it('registerOnTouched should update onTouched property', async(() => {
    hostComponent.registerOnTouched(123);
    expect(hostComponent.onTouched).toEqual(123);
  }));

  it('registerOnChange should update onChange property', async(() => {
    hostComponent.registerOnChange(456);
    expect(hostComponent.onChange).toEqual(456);
  }));

  // it('ngOnInit() should update fileservice.metaData', () => {
  //   fixture.whenStable().then(() => {
  //     hostComponent.element.config.uiStyles.attributes.metaData = 'test';
  //     hostComponent.ngOnInit();
  //     expect(fileservice.metaData).toEqual('test');
  //   });
  // });

  it('hasfile() should return 0', async(() => {
    const file: File = { name: 'testt', size: 321 } as File;
    hostComponent.selectedFiles = [file];
    hostComponent.hasFile(file);
    expect(hostComponent.hasFile(file)).toEqual(0);
  }));

  it('removeFiles() should update value and selectedFiles properties', async(() => {
    const eve = { file: '' };
    spyOn(hostComponent, 'hasFile').and.returnValue('-1');
    const file: File = { name: 'testt', size: 321 } as File;
    hostComponent.selectedFiles = [file];
    hostComponent.removeFiles(eve);
    expect(hostComponent.value).toEqual(hostComponent.selectedFiles);
  }));

  it('removeFiles() should update value property', async(() => {
    const eve = { file: '' };
    spyOn(hostComponent, 'hasFile').and.returnValue('0');
    const file: File = { name: 'testt', size: 321 } as File;
    hostComponent.selectedFiles = [file];
    hostComponent.removeFiles(eve);
    expect(hostComponent.selectedFiles).toEqual([]);
  }));

  it('addFiles() should update selectedFiles as []', () => {
    fixture.whenStable().then(() => {
      const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
      hostComponent.element.config.uiStyles.attributes.url = '/test.com';
      hostComponent.multipleFiles = false;
      spyOn(hostComponent, 'hasFile').and.returnValue('-1');
      spyOn(hostComponent.pfu, 'isFileTypeValid').and.returnValue(false);
      hostComponent.addFiles(eve);
      expect(hostComponent.selectedFiles).toEqual([]);
    });
  });

  it('addFiles() should update selectedFiles[]', () => {
    fixture.whenStable().then(() => {
      const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
      hostComponent.element.config.uiStyles.attributes.url = '/test.com';
      hostComponent.multipleFiles = true;
      hostComponent.selectedFiles = [];
      spyOn(hostComponent, 'hasFile').and.returnValue('-1');
      spyOn(hostComponent.pfu, 'isFileTypeValid').and.returnValue(true);
      hostComponent.addFiles(eve);
      const res: any = { postUrl: '/test.com' };
      expect(hostComponent.selectedFiles).toEqual([res]);
    });
  });

  it('addFiles() should not call fileservice.uploadFile', () => {
    fixture.whenStable().then(() => {
      const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
      hostComponent.element.config.uiStyles.attributes.url = '/test.com';
      spyOn(hostComponent, 'hasFile').and.returnValue('1');
      spyOn(fileservice, 'uploadFile').and.callThrough();
      hostComponent.addFiles(eve);
      expect(fileservice.uploadFile).not.toHaveBeenCalled();
    });
  });

});