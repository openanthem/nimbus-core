'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FileUploadModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Subject } from 'rxjs';

import { FileUploadComponent } from './file-upload.component';
import { FileService } from './../../../services/file.service';
import { WebContentSvc } from './../../../services/content-management.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoggerService } from './../../../services/logger.service';

let fixture, app, fileservice;

class MockFileService {
    public addFile$: Subject<any>;

    constructor() {
        this.addFile$ = new Subject<any>();
    }

    uploadFile(a) {
      console.log('uploadfile push is getting called...');
        this.addFile$.next({test: 123});
    }
}

describe('FileUploadComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          FileUploadComponent
       ],
       imports: [
           FileUploadModule,
           HttpClientModule,
           HttpModule
       ],
       providers: [
           {provide: FileService, useClass: MockFileService},
           WebContentSvc,
           CustomHttpClient,
           LoggerService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(FileUploadComponent);
    app = fixture.debugElement.componentInstance;
    fileservice = TestBed.get(FileService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('get _value should return value', async(() => {
    app.value = 123;
    expect(app.value).toEqual(123);
  }));

  it('registerOnTouched should update app.onTouched', async(() => {
    app.registerOnTouched(123);
    expect(app.onTouched).toEqual(123);
  }));

  it('registerOnChange should update app.onChange', async(() => {
    app.registerOnChange(456);
    expect(app.onChange).toEqual(456);
  }));

  it('ngOnInit should update app.selectedFiles', async(() => {
    // app.multipleFiles = true;
    console.log('app.multipleFiles', app.multipleFiles);
    app.ngOnInit();
    fileservice.uploadFile({name: 'test '});
    expect(app.selectedFiles).toEqual([{test: 123}]);
  }));

  it('ngOnInit should update app.selectedFiles with app.multipleFiles flag', async(() => {
    app.multipleFiles = true;
    console.log('app.multipleFiles', app.multipleFiles);
    app.ngOnInit();
    fileservice.uploadFile({name: 'test '});
    expect(app.selectedFiles).toEqual([{test: 123}]);
  }));

  it('hasfile() should return 0', async(() => {
    const file = {
      name: 'testt',
      size: 321
    };
    app.selectedFiles = [{name: 'testt', size: 321}, {}, {}];
    app.hasFile(file);
    expect(app.hasFile(file)).toEqual(0);
  }));

  it('removeFiles() should update app.value and app.selectedFiles', async(() => {
    const eve = {
      file: ''
    };
    spyOn(app, 'hasFile').and.returnValue('-1');
    app.selectedFiles = 'test';
    app.removeFiles(eve);
    expect(app.value).toEqual(app.selectedFiles);
  }));

  it('removeFiles() should update app.value', async(() => {
    const eve = {
      file: ''
    };
    spyOn(app, 'hasFile').and.returnValue('0');
    app.selectedFiles = [1, 2];
    app.removeFiles(eve);
    expect(app.value).toEqual([2]);
  }));


  it('addFiles() should call fileservice.uploadFile', async(() => {
    const eve = {
        originalEvent: {
            dataTransfer: {
                files: [{
                  postUrl: ''
                }]
            }
        }
    };
    app.element = {
      config: {
        uiStyles: {
          attributes: {
            url: '/test.com'
          }
        }
      }
    };
    spyOn(app, 'hasFile').and.returnValue('-1');
    spyOn(fileservice, 'uploadFile').and.callThrough();
    spyOn(app.pfu, 'isFileTypeValid').and.returnValue(true);
    app.addFiles(eve);
    expect(fileservice.uploadFile).toHaveBeenCalled();
  }));

  it('addFiles() should not call fileservice.uploadFile', async(() => {
    const eve = {
        originalEvent: {
            dataTransfer: {
                files: [{
                  postUrl: ''
                }]
            }
        }
    };
    app.element = {
      config: {
        uiStyles: {
          attributes: {
            url: '/test.com'
          }
        }
      }
    };
    spyOn(app, 'hasFile').and.returnValue('1');
    spyOn(fileservice, 'uploadFile').and.callThrough();
    app.addFiles(eve);
    expect(fileservice.uploadFile).not.toHaveBeenCalled();
  }));

});