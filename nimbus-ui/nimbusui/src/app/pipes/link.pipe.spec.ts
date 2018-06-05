import { LinkPipe } from './link.pipe';

describe('LinkPipe', () => {
    const pipe = new LinkPipe();

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('pipe should return updated anchor tag with http', () => {
      expect(pipe.transform('http://test.com', '/path/test')).toEqual('<a href="http://test.com" target="_blank">http://test.com</a>');
  });

  it('pipe should return updated anchor tag with www', () => {
    expect(pipe.transform('www.test.com', '/path/test')).toEqual('<a href="http://www.test.com" target="_blank">www.test.com</a>');
  });

  it('pipe should return updated anchor tag with mailto: in href attribute', () => {
    expect(pipe.transform('test@gmail.com', '/path/test')).toEqual('<a href="mailto:test@gmail.com">test@gmail.com</a>');
  });

});