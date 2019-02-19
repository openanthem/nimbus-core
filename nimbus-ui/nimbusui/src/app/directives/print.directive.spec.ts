import { TestBed, async } from '@angular/core/testing';

import { PrintDirective } from './print.directive';
import { Directive, ElementRef, Input } from '@angular/core';
import { LoggerService } from '../services/logger.service';
import { PrintService } from '../services/print.service';
import { Subject, Subscription } from 'rxjs';
import { WindowRefService } from './../services/window-ref.service';
import { printServiceSubject, printDirectiveElement } from 'mockdata';

class MockElementRef {
    nativeElement = {
        style: {
            backgroundColor: ''
        }
    };
}

class MockLoggerService {
    debug() { }
    info() { }
    error() { }
}

class MockWindowRefService {
    window: any = {
        'open': () => {
            return {
                'document': {
                    'body': {
                        'innerHTML': ''
                    },
                    'head': {
                        'appendChild': () => {}
                    }
                }
            }
        },
        'document': {
            'getElementsByTagName': () => {}
        }
    }    
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
          declarations: [
            PrintDirective
           ],
           providers: [
               {provide: ElementRef, useClass: MockElementRef},
               {provide: LoggerService, useClass: MockLoggerService},
               {provide: PrintService, useClass: MockPrintService},
               {provide: WindowRefService, useClass: MockWindowRefService}
            ]
        }).compileComponents();
        elementRef = TestBed.get(ElementRef);
        printService = TestBed.get(PrintService);
        loggerService = TestBed.get(LoggerService);
        windowRefService = TestBed.get(WindowRefService);
        directive = new PrintDirective(elementRef, loggerService, printService)
        directive.element = printDirectiveElement;
      });

  it('should create an instance', () => {
    expect(directive).toBeTruthy();
    const test: any = '123';
    directive.getPrintableContent(test);
  });

  it('ngAfterViewInit() should call loggerservice.debug() and execute()', () => {
    spyOn(loggerService, 'debug').and.callThrough();
    spyOn(directive, 'execute').and.returnValue('');
    directive.ngAfterViewInit();
    printService.emitPrintEvent(printServiceSubject);
    expect(loggerService.debug).toHaveBeenCalledWith('Stylesheets found. useDelay will be set to true with delay of 300');
    expect(directive.execute).toHaveBeenCalledWith(printServiceSubject);
  });

  it('ngOnDestroy() should unsubscribe the subscription', () => {
    directive.subscription = new Subscription();
    spyOn(directive.subscription, 'unsubscribe').and.returnValue('');
    directive.ngOnDestroy();
    expect(directive.subscription.unsubscribe).toHaveBeenCalled();
  });

});
