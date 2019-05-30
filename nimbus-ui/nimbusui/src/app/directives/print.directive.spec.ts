/**
 * @license
 * Copyright 2016-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { ElementRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { printDirectiveElement, printServiceSubject } from 'mockdata';
import { Subject, Subscription } from 'rxjs';
import { LoggerService } from '../services/logger.service';
import { PrintService } from '../services/print.service';
import { WindowRefService } from './../services/window-ref.service';
import { PrintDirective } from './print.directive';

class MockElementRef {
  nativeElement = {
    style: {
      backgroundColor: ''
    }
  };
}

class MockLoggerService {
  debug() {}
  info() {}
  error() {}
}

class MockWindowRefService {
  window: any = {
    open: () => {
      return {
        document: {
          body: {
            innerHTML: ''
          },
          head: {
            appendChild: () => {}
          }
        }
      };
    },
    document: {
      getElementsByTagName: () => {}
    }
  };
}

class MockPrintService {
  public printClickUpdate$: Subject<any>;

  constructor() {
    this.printClickUpdate$ = new Subject();
  }

  emitPrintEvent(a) {
    this.printClickUpdate$.next(a);
  }
}

let directive, elementRef, printService, loggerService, windowRefService;

describe('PrintDirective', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrintDirective],
      providers: [
        { provide: ElementRef, useClass: MockElementRef },
        { provide: LoggerService, useClass: MockLoggerService },
        { provide: PrintService, useClass: MockPrintService },
        { provide: WindowRefService, useClass: MockWindowRefService }
      ]
    }).compileComponents();
    elementRef = TestBed.get(ElementRef);
    printService = TestBed.get(PrintService);
    loggerService = TestBed.get(LoggerService);
    windowRefService = TestBed.get(WindowRefService);
    directive = new PrintDirective(elementRef, loggerService, printService);
    directive.element = printDirectiveElement;
  });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
    const test: any = '123';
    directive.getPrintableContent(test);
  });

  it('applyStylesToWindow() should call the printWindow.document.head.appendChild() for links and style', () => {
    const event = { printConfig: { useAppStyles: true, stylesheet: 'test' } };
    const printWindow = {
      document: {
        body: { innerHTML: 'test' },
        head: { appendChild: () => {} }
      }
    };
    const links = ['test'];
    links['item'] = () => {
      return {
        cloneNode: () => {
          return { href: '' };
        },
        rel: 'stylesheet',
        href: '/test'
      };
    };
    spyOn(window.document, 'getElementsByTagName').and.returnValue(links);
    spyOn(printWindow.document.head, 'appendChild').and.callThrough();
    directive.applyStylesToWindow(event, printWindow);
    expect(printWindow.document.head.appendChild).toHaveBeenCalledTimes(3);
  });

  it('getPrintableContent() should call logger.debug() and return innerHTML', () => {
    directive.nativeElement = {
      parentElement: 'testParentElement'
    };
    const event = {
      uiEvent: {
        srcElement: {
          closest: () => {
            return { innerHTML: 'testInnerHTML' };
          }
        }
      }
    };
    directive.isPage = true;
    directive.contentSelector = true;
    spyOn(loggerService, 'debug').and.callThrough();
    const res = directive.getPrintableContent(event);
    expect(loggerService.debug).toHaveBeenCalledWith(
      'Print feature is looking for parent via selector: "true"'
    );
    expect(res).toEqual('testInnerHTML');
  });

  it('getPrintableContent() should call logger.debug() and throw an error', () => {
    directive.nativeElement = {
      parentElement: 'testParentElement'
    };
    const event = {
      uiEvent: {
        srcElement: {
          closest: () => {
            return '';
          }
        }
      }
    };
    directive.isPage = true;
    directive.contentSelector = true;
    spyOn(loggerService, 'debug').and.callThrough();
    expect(() => {
      directive.getPrintableContent(event);
    }).toThrow();
    expect(loggerService.debug).toHaveBeenCalledWith(
      'Print feature is looking for parent via selector: "true"'
    );
  });

  it('execute() should call window.open(), applyStylesToWindow() and doPrintActions()', () => {
    const event = { printConfig: { useDelay: '' } };
    const printWindow = { document: { body: { innerHTML: 'test' } } };
    spyOn(window, 'open').and.returnValue(printWindow);
    spyOn(directive, 'applyStylesToWindow').and.returnValue('');
    spyOn(directive, 'doPrintActions').and.returnValue('');
    directive.execute(event);
    expect(window.open).toHaveBeenCalledWith('', '_blank');
    expect(directive.applyStylesToWindow).toHaveBeenCalledWith(
      event,
      printWindow
    );
    expect(directive.doPrintActions).toHaveBeenCalledWith(event, printWindow);
  });

  it('execute() should call window.open(), applyStylesToWindow() and printWindow.setTimeout()', () => {
    const event = { printConfig: { useDelay: 10 } };
    const printWindow = {
      document: { body: { innerHTML: 'test' } },
      setTimeout: () => {}
    };
    spyOn(window, 'open').and.returnValue(printWindow);
    spyOn(directive, 'applyStylesToWindow').and.returnValue('');
    spyOn(printWindow, 'setTimeout').and.returnValue('');
    directive.execute(event);
    expect(window.open).toHaveBeenCalledWith('', '_blank');
    expect(directive.applyStylesToWindow).toHaveBeenCalledWith(
      event,
      printWindow
    );
    expect(printWindow.setTimeout).toHaveBeenCalled();
  });

  it('ngAfterViewInit() should call loggerservice.debug() and execute()', () => {
    spyOn(loggerService, 'debug').and.callThrough();
    spyOn(directive, 'execute').and.returnValue('');
    directive.ngAfterViewInit();
    printService.emitPrintEvent(printServiceSubject);
    expect(loggerService.debug).toHaveBeenCalledWith(
      'Stylesheets found. useDelay will be set to true with delay of 300'
    );
    expect(directive.execute).toHaveBeenCalledWith(printServiceSubject);
  });

  it('doPrintActions() should call the window.print() and window.close()', () => {
    spyOn(window, 'print').and.returnValue('');
    spyOn(window, 'close').and.returnValue('');
    const event = {
      printConfig: {
        autoPrint: true,
        closeAfterPrint: true
      }
    };
    directive.doPrintActions(event, window);
    expect(window.print).toHaveBeenCalled();
    expect(window.close).toHaveBeenCalled();
  });

  it('ngOnDestroy() should unsubscribe the subscription', () => {
    directive.subscription = new Subscription();
    spyOn(directive.subscription, 'unsubscribe').and.returnValue('');
    directive.ngOnDestroy();
    expect(directive.subscription.unsubscribe).toHaveBeenCalled();
  });
});
