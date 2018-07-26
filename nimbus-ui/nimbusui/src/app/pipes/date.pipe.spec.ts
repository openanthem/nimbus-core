import { DateTimeFormatPipe } from './date.pipe';
import { TestBed, async } from '@angular/core/testing';
import { Param } from '../shared/param-state';

describe('DateTimeFormatPipe', () => {

  it('create an DateTimeFormatPipe instance', () => {
    const pipe = new DateTimeFormatPipe('en');
    expect(pipe).toBeTruthy();
  });

  it('should return date in localDate format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', '', 'LocalDate');
    expect(result).toEqual('12/02/2017');
  });

  it('should return date in default format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', '', '');
    expect(result).toEqual('12/02/2017 12:00 AM');
  });

  it('should return date in default format on sending invalid date format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', '', 'test');
    expect(result).toEqual('12/02/2017 12:00 AM');
  });

  it('should return date in specific format', () => {
    const pipe = new DateTimeFormatPipe('en');
    const result = pipe.transform('12/2/2017', 'dd/MM', 'test');
    expect(result).toEqual('02/12');
  });

});
